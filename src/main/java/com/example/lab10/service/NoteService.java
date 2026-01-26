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

/*
 * Service layer for notes.
 *
 * IMPORTANT:
 * This is where user isolation is enforced.
 * If this class is correct, User A can NEVER access User B’s notes.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    /*
     * Injects repositories needed for note and user access.
     */
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    // ============================================================
    // Helper: get the currently logged-in user
    // ============================================================

    /*
     * Gets the currently authenticated user from Spring Security.s
     */
    private User currentUserOrThrow() {

        // Get authentication info from Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // If no authentication exists, the user is not logged in
        if (auth == null || auth.getName() == null || auth.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        // Normalizes email (same logic as login)
        String email = auth.getName().trim().toLowerCase();

        // Load the user from the database
        // If user does not exist, treat as unauthorized
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
                );
    }

    // ============================================================
    // READ operations
    // ============================================================

    /*
     * Returns all notes that belong ONLY to the current user.
     */
    public List<Note> findMyNotes() {

        // Get current user
        User me = currentUserOrThrow();

        // Fetch only notes owned by this user
        return noteRepository.findAllMineNative(me.getId());
    }

    /*
     * Returns one note only if it belongs to the current user.
     */
    public Note getMineOr404(Integer noteId) {

        // Get current user
        User me = currentUserOrThrow();

        // Look up note by ID + owner ID
        return noteRepository.findByIdAndOwner_Id(noteId, me.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found")
                );
    }

    // ============================================================
    // CREATE operations
    // ============================================================

    /*
     * Creates a new note and assigns ownership
     */
    public Note create(String title, String content) {

        // Get current user
        User me = currentUserOrThrow();

        // Create note with owner set to current user
        Note note = new Note(title, content, me);

        // Save to database
        return noteRepository.save(note);
    }

    // ============================================================
    // UPDATE operations
    // ============================================================

    /*
     * Updates a note ONLY if it belongs to the current user.
     */
    public Note updateMine(Integer noteId, String title, String content) {

        // Verify ownership first
        Note note = getMineOr404(noteId);

        // Update fields
        note.setTitle(title);
        note.setContent(content);

        // Save changes
        return noteRepository.save(note);
    }

    // ============================================================
    // DELETE operations
    // ============================================================

    /*
     * Deletes a note ONLY if it belongs to the current user.
     */
    public void deleteMine(Integer noteId) {

        // Verify ownership first
        Note note = getMineOr404(noteId);

        // Delete from database
        noteRepository.delete(note);
    }
}