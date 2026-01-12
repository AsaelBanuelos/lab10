package com.example.lab10.service;

import com.example.lab10.model.Note;
import com.example.lab10.model.User;
import com.example.lab10.repository.NoteRepository;
import com.example.lab10.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service layer for note management operations.
 * Handles all business logic for creating, reading, updating, and deleting notes.

 * This service is responsible for:
 * - Managing note ownership (ensuring users can only access their own notes)
 * - Enforcing authorization (checking that current user owns the note)
 * - Communicating with the NoteRepository for database operations
 * - Getting the currently authenticated user
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    /**
     * Constructor that injects the required repository dependencies.
     * Spring automatically provides these when the service is created.
     */
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    // ============================================================
    // HELPER METHOD - Getting the current authenticated user
    // ============================================================

    /**
     * Gets the currently authenticated user from Spring Security context.
     * This method is used to enforce ownership rules - we ensure every operation
     * is performed on notes belonging to the logged-in user.

     * Process:
     * 1. Get the Authentication object from Spring Security's SecurityContext
     * 2. Extract the username/email from the authentication
     * 3. Look up the User from the database using the email
     * 4. Return the User object (which contains the database ID needed for queries)
     */
    private User currentUserOrThrow() {
        // Get the current authentication from Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication exists and has a username
        if (auth == null || auth.getName() == null || auth.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        // Get the username and normalize it (lowercase, trimmed)
        String email = auth.getName().trim().toLowerCase();

        // Look up the user in the database by email
        // Use orElseThrow to throw an exception if the user is not found
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    // ============================================================
    // READ OPERATIONS - Getting notes
    // ============================================================

    /**
     * Retrieves all notes belonging to the current authenticated user.

     * This uses the native SQL query defined in the repository.
     * The query is required by the lab and uses parameter binding for safety.
     */
    public List<Note> findMyNotes() {
        // Get the currently authenticated user
        User me = currentUserOrThrow();

        // Use the native query to fetch notes (this satisfies the lab requirement)
        return noteRepository.findAllMineNative(me.getId());
    }

    /**
     * Retrieves a single note, but only if it belongs to the current user.
     * If the note doesn't exist or doesn't belong to the user, throws 404 error.
     */
    public Note getMineOr404(Integer noteId) {
        // Get the currently authenticated user
        User me = currentUserOrThrow();

        // Query for a note with this ID and owned by the current user
        // If not found, throw 404
        return noteRepository.findByIdAndOwner_Id(noteId, me.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    // ============================================================
    // CREATE OPERATIONS - Creating notes
    // ============================================================

    /**
     * Creates a new note owned by the current authenticated user.

     * Process:
     * 1. Get the current user
     * 2. Create a new Note entity with the title, content, and owner
     * 3. Save to the database
     * 4. Return the saved note (with database-generated ID)
     */
    public Note create(String title, String content) {
        // Get the currently authenticated user
        User me = currentUserOrThrow();

        // Create a new Note with the title, content, and owner set
        Note note = new Note(title, content, me);

        // Save the note to the database
        return noteRepository.save(note);
    }

    // ============================================================
    // UPDATE OPERATIONS - Editing notes
    // ============================================================

    /**
     * Updates an existing note with new title and content.
     * Only works if the note belongs to the current user.
     */
    public Note updateMine(Integer noteId, String title, String content) {
        // Get the note and verify ownership (throws 404 if not owned)
        Note note = getMineOr404(noteId);

        // Update the note's title and content with the new values
        note.setTitle(title);
        note.setContent(content);

        // Save the updated note to the database
        return noteRepository.save(note);
    }

    // ============================================================
    // DELETE OPERATIONS - Removing notes
    // ============================================================

    /**
     * Deletes a note belonging to the current user.
     * Only works if the note belongs to the current user.
     */
    public void deleteMine(Integer noteId) {
        // Get the note and verify ownership (throws 404 if not owned)
        Note note = getMineOr404(noteId);

        // Delete the note from the database
        noteRepository.delete(note);
    }
}
