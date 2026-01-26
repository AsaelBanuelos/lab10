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

/*
 * MVC controller for notes.
 * Here I handle listing, creating, editing, deleting notes,
 * plus a JSON endpoint and file uploads.
 *
 * User isolation is enforced in NoteService, not here.
 */
@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    // I inject NoteService using constructor injection
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // -----------------------------
    // LIST NOTES (only mine)
    // -----------------------------

    /*
     * Shows the list of my notes.
     * I also show uploaded files from the local upload folder.
     */
    @GetMapping
    public String list(
            @RequestParam(value = "uploaded", required = false) String uploaded,
            Model model
    ) throws Exception {

        // I only load notes that belong to the logged-in user
        model.addAttribute("notes", noteService.findMyNotes());

        // This flag is used to show an "upload successful" message
        model.addAttribute("uploaded", uploaded != null);

        // Upload directory is inside the OS user home folder
        String userHome = System.getProperty("user.home");
        java.nio.file.Path uploadDir = java.nio.file.Paths.get(userHome, "lab10_uploads");

        // If the folder exists, I list the uploaded files
        java.util.List<String> files = java.util.Collections.emptyList();
        if (java.nio.file.Files.exists(uploadDir)) {
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.list(uploadDir)) {
                files = stream
                        .filter(java.nio.file.Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .toList();
            }
        }

        // I send the file names to the view
        model.addAttribute("uploadedFiles", files);

        return "note/list";
    }

    // -----------------------------
    // CREATE NOTE (form)
    // -----------------------------

    /*
     * Shows the create note form.
     * I send an empty DTO so Thymeleaf can bind the inputs.
     */
    @GetMapping("/create")
    public String showCreate(Model model) {
        model.addAttribute("createNoteRequest", new CreateNoteRequest());
        return "note/create";
    }

    /*
     * Creates a note from an HTML form.
     * @Valid runs validation on the DTO.
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createFromForm(
            @Valid @ModelAttribute("createNoteRequest") CreateNoteRequest req,
            BindingResult binding,
            Model model
    ) {
        // If validation fails, I reload the form with error messages
        if (binding.hasErrors()) {
            return "note/create";
        }

        // I save the note and link it to the current user
        noteService.create(req.getTitle(), req.getContent());

        // I redirect to avoid form resubmission on refresh
        return "redirect:/notes";
    }

    // -----------------------------
    // CREATE NOTE (JSON)
    // -----------------------------

    /*
     * Creates a note using JSON.
     * This is mainly to demonstrate JSON + validation.
     */
    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createFromJson(@Valid @RequestBody CreateNoteRequest req) {
        noteService.create(req.getTitle(), req.getContent());
        return "redirect:/notes";
    }

    // -----------------------------
    // EDIT NOTE
    // -----------------------------

    /*
     * Shows the edit form for a note.
     * getMineOr404() makes sure the note belongs to me.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        var note = noteService.getMineOr404(id);

        // I pre-fill the form with the current note values
        CreateNoteRequest dto = new CreateNoteRequest();
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());

        // I need the noteId in the view to build the edit URL
        model.addAttribute("noteId", id);
        model.addAttribute("createNoteRequest", dto);
        return "note/edit";
    }

    /*
     * Saves changes from the edit form.
     * Ownership is checked inside the service.
     */
    @PostMapping(value = "/{id}/edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editSubmit(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("createNoteRequest") CreateNoteRequest req,
            BindingResult binding,
            Model model
    ) {
        // If validation fails, I stay on the edit page
        if (binding.hasErrors()) {
            model.addAttribute("noteId", id);
            return "note/edit";
        }

        // I update the note only if it belongs to the current user
        noteService.updateMine(id, req.getTitle(), req.getContent());

        return "redirect:/notes";
    }

    // -----------------------------
    // DELETE NOTE
    // -----------------------------

    /*
     * Deletes a note.
     * If the note is not mine, the service throws 404.
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        noteService.deleteMine(id);
        return "redirect:/notes";
    }

    // -----------------------------
    // FILE UPLOAD
    // -----------------------------

    /*
     * Handles file uploads.
     * Files are saved in {user.home}/lab10_uploads.
     * This is fine for a lab demo.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) {

        // If no file is selected, I return 400
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        try {
            // I make sure the upload folder exists
            String userHome = System.getProperty("user.home");
            java.nio.file.Path uploadDir = java.nio.file.Paths.get(userHome, "lab10_uploads");
            java.nio.file.Files.createDirectories(uploadDir);

            // I get the original filename from the browser
            String original = file.getOriginalFilename();

            // I clean the filename to avoid dangerous characters
            String filename = (original == null || original.isBlank())
                    ? "upload.bin"
                    : original.replaceAll("[\\\\/:*?\"<>|]", "_");

            // Final path where the file will be stored
            java.nio.file.Path target = uploadDir.resolve(filename);

            // I save the file to disk
            try (java.io.InputStream in = file.getInputStream()) {
                java.nio.file.Files.copy(
                        in,
                        target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
            }

        } catch (Exception e) {
            // I log the error on the server side only
            e.printStackTrace();

            // I return a safe error message to the user
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not save file"
            );
        }

        return "redirect:/notes?uploaded=1";
    }
}