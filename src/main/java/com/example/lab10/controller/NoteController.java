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

@Controller
@RequestMapping("/notes")
public class NoteController {

    // Service layer used to handle note-related logic
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // MVC page that shows the list of notes
    @GetMapping
    public String list(@RequestParam(value = "uploaded", required = false) String uploaded,
                       Model model) throws Exception {

        // Add all notes to the model
        model.addAttribute("notes", noteService.findAll());

        // Used to display a message when a file was uploaded
        model.addAttribute("uploaded", uploaded != null);

        // Read uploaded files from the local upload directory
        String userHome = System.getProperty("user.home");
        java.nio.file.Path uploadDir = java.nio.file.Paths.get(userHome, "lab10_uploads");

        java.util.List<String> files = java.util.Collections.emptyList();
        if (java.nio.file.Files.exists(uploadDir)) {
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(uploadDir)) {
                files = stream
                        .filter(java.nio.file.Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .toList();
            }
        }

        // Pass uploaded file names to the view
        model.addAttribute("uploadedFiles", files);

        return "note/list";
    }

    // Show the form used to create a new note
    @GetMapping("/create")
    public String showCreate(Model model) {
        // Bind an empty DTO to the form
        model.addAttribute("createNoteRequest", new CreateNoteRequest());
        return "note/create";
    }

    // Handle form submission (application/x-www-form-urlencoded)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createFromForm(
            @Valid @ModelAttribute("createNoteRequest") CreateNoteRequest req,
            BindingResult binding,
            Model model
    ) {
        // If validation fails, return the same form with error messages
        if (binding.hasErrors()) {
            return "note/create";
        }

        // Create the note using the service layer
        noteService.create(req.getTitle(), req.getContent());

        // Post-Redirect-Get pattern to avoid duplicate submissions
        return "redirect:/notes";
    }

    // Handle JSON requests (application/json) to demonstrate REST behavior
    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createFromJson(@Valid @RequestBody CreateNoteRequest req) {

        // Create a note from JSON data
        noteService.create(req.getTitle(), req.getContent());

        // Redirect to the notes list after creation
        return "redirect:/notes";
    }

    // Handle multipart file uploads
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) {

        // Check if a file was actually selected
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        try {
            // Use a safe directory inside the user's home folder
            String userHome = System.getProperty("user.home");
            java.nio.file.Path uploadDir = java.nio.file.Paths.get(userHome, "lab10_uploads");
            java.nio.file.Files.createDirectories(uploadDir);

            // Sanitize the file name
            String original = file.getOriginalFilename();
            String filename = (original == null || original.isBlank())
                    ? "upload.bin"
                    : original.replaceAll("[\\\\/:*?\"<>|]", "_");

            java.nio.file.Path target = uploadDir.resolve(filename);

            // Save the file to disk
            try (java.io.InputStream in = file.getInputStream()) {
                java.nio.file.Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            // Print error details to the console for debugging
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file");
        }

        // Redirect back to the list page with a success flag
        return "redirect:/notes?uploaded=1";
    }
}
