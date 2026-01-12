package com.example.lab10.controller;

import com.example.lab10.dto.CreateNoteRequest;
import com.example.lab10.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for note management operations.
 * Handles CRUD operations for notes (Create, Read, Update, Delete) and file uploads.
 *
 * All endpoints are protected and require authentication.
 * Users can only access and modify their own notes.
 *
 * URL structure: /notes/*
 * Supported operations:
 * - GET /notes - List all user's notes
 * - GET /notes/create - Show note creation form
 * - POST /notes/create - Submit new note from HTML form
 * - POST /notes/api - Create note from JSON (REST API)
 * - POST /notes/upload - Upload files
 * - GET /notes/{id}/edit - Show edit form for note
 * - POST /notes/{id}/edit - Submit edited note
 * - POST /notes/{id}/delete - Delete a note
 */
@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    /**
     * Constructor that injects the NoteService dependency.
     * Spring automatically provides the service when this controller is created.
     */
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // ============================================================
    // READ OPERATIONS - Getting and displaying notes
    // ============================================================

    /**
     * Displays the list of all notes belonging to the current user.
     * Also lists all uploaded files in the user's uploads directory.
     */
    @GetMapping
    public String list(
            @RequestParam(value = "uploaded", required = false) String uploaded,
            Model model
    ) throws Exception {
        // Fetch all notes belonging to the current authenticated user
        model.addAttribute("notes", noteService.findMyNotes());

        // Add a flag to show the upload success message if user just uploaded a file
        model.addAttribute("uploaded", uploaded != null);

        // Get the user's home directory and set up the uploads folder path
        String userHome = System.getProperty("user.home");
        java.nio.file.Path uploadDir = java.nio.file.Paths.get(userHome, "lab10_uploads");

        // List all uploaded files in the directory (if it exists)
        java.util.List<String> files = java.util.Collections.emptyList();
        if (java.nio.file.Files.exists(uploadDir)) {
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(uploadDir)) {
                // Get all files (not directories) and extract their names
                files = stream
                        .filter(java.nio.file.Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .toList();
            }
        }

        // Pass the list of uploaded files to the template for display
        model.addAttribute("uploadedFiles", files);

        return "note/list";
    }

    /**
     * Displays the form for creating a new note.
     * Prepares an empty note creation form for the user to fill in.
     */
    @GetMapping("/create")
    public String showCreate(Model model) {
        // Create an empty DTO object for the form to bind to
        model.addAttribute("createNoteRequest", new CreateNoteRequest());
        return "note/create";
    }

    /**
     * Displays the edit form for an existing note.
     * Pre-fills the form with the note's current title and content.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        // Retrieve the note - this also checks ownership (throws 404 if not owned by user)
        var note = noteService.getMineOr404(id);

        // Create a DTO and pre-fill it with the note's current values
        CreateNoteRequest dto = new CreateNoteRequest();
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());

        // Pass the note ID and pre-filled form to the template
        model.addAttribute("noteId", id);
        model.addAttribute("createNoteRequest", dto);
        return "note/edit";
    }

    // ============================================================
    // CREATE OPERATIONS - Creating new notes
    // ============================================================

    /**
     * Creates a new note from an HTML form submission.
     * This method accepts form-encoded data (typical HTML form).

     * Validation:
     * - Title: Not blank, minimum 3 characters
     * - Content: Not blank, maximum 1000 characters
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createFromForm(
            @Valid @ModelAttribute("createNoteRequest") CreateNoteRequest req,
            BindingResult binding,
            Model model
    ) {
        // Check if any validation errors occurred
        if (binding.hasErrors()) {
            // Return to the form so user can see and fix the validation errors
            return "note/create";
        }

        // Create the note using the service layer (handles DB save and ownership assignment)
        noteService.create(req.getTitle(), req.getContent());

        // Redirect to the notes list page (Post-Redirect-Get prevents form re-submission)
        return "redirect:/notes";
    }

    /**
     * Creates a new note from a JSON request body.
     * This is a REST API endpoint for programmatic note creation.
     * Useful for testing or integrating with JavaScript/other clients.
     *
     * Validation: Same as HTML form creation
     */
    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createFromJson(@Valid @RequestBody CreateNoteRequest req) {
        // Create note from JSON data (same service call as form submission)
        noteService.create(req.getTitle(), req.getContent());

        // Return redirect URL
        return "redirect:/notes";
    }

    // ============================================================
    // UPDATE OPERATIONS - Editing existing notes
    // ============================================================

    /**
     * Updates an existing note with new title and content.
     */
    @PostMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editSubmit(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("createNoteRequest") CreateNoteRequest req,
            BindingResult binding,
            Model model
    ) {
        // Check if any validation errors occurred
        if (binding.hasErrors()) {
            // Re-add the note ID so the form knows which note to edit
            model.addAttribute("noteId", id);
            // Return to the form so user can see and fix the validation errors
            return "note/edit";
        }

        // Update the note using the service layer (handles DB update and ownership check)
        noteService.updateMine(id, req.getTitle(), req.getContent());

        // Redirect to the notes list page
        return "redirect:/notes";
    }

    // ============================================================
    // DELETE OPERATIONS - Removing notes
    // ============================================================

    /**
     * Deletes a note belonging to the current user.
     *
     * Security: Ownership is verified by the service layer
     * (will throw 404 if the note doesn't exist or isn't owned by the user)
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        // Delete the note (service layer checks ownership)
        noteService.deleteMine(id);

        // Redirect to the notes list page
        return "redirect:/notes";
    }

    // ============================================================
    // FILE UPLOAD OPERATIONS - Handling file uploads
    // ============================================================

    /**
     * Handles multipart file uploads from the user.
     * Files are saved to a directory in the user's home folder.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) {
        // Validate that a file was selected (not null or empty)
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        try {
            // Determine the upload directory in the user's home folder
            String userHome = System.getProperty("user.home");
            java.nio.file.Path uploadDir = java.nio.file.Paths.get(userHome, "lab10_uploads");

            // Create the uploads directory if it doesn't already exist
            java.nio.file.Files.createDirectories(uploadDir);

            // Get the original file name from the upload
            String original = file.getOriginalFilename();

            // Sanitize the file name:
            // - Replace dangerous characters with underscores
            // - These characters can be used for path traversal or other attacks
            // - Dangerous characters: \ / : * ? " < > |
            String filename = (original == null || original.isBlank())
                    ? "upload.bin"  // Default name if filename is missing
                    : original.replaceAll("[\\\\/:*?\"<>|]", "_");

            // Build the full path where the file will be saved
            java.nio.file.Path target = uploadDir.resolve(filename);

            // Save the file to disk
            try (java.io.InputStream in = file.getInputStream()) {
                java.nio.file.Files.copy(
                        in,
                        target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
            }

        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();

            // Throw an error response to the user
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not save file"
            );
        }

        // Redirect to list page with upload success indicator
        return "redirect:/notes?uploaded=1";
    }
}
