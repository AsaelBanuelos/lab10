package com.example.lab10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * DTO (Data Transfer Object) used when creating or editing a note.
 *
 * This class defines validation rules for user input coming from:
 * - HTML forms
 * - JSON API requests
 *
 * Validation is enforced server-side using Jakarta Bean Validation.
 */
public class CreateNoteRequest {

    /*
     * Title of the note.
     *
     * Rules:
     * - must not be empty or only whitespace
     * - must pass the custom @ValidTitle validator (minimum length, custom logic)
     */
    @NotBlank(message = "Title must not be blank")
    @ValidTitle(message = "Title must be at least 3 characters long")
    private String title;

    /*
     * Content/body of the note.
     *
     * Rules:
     * - must not be empty
     * - maximum length is 1000 characters to avoid very large inputs
     */
    @NotBlank(message = "Content must not be blank")
    @Size(max = 1000, message = "Content must be at most 1000 characters")
    private String content;

    /*
     * Getter for the note title.
     */
    public String getTitle() {
        return title;
    }

    /*
     * Setter for the note title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * Getter for the note content.
     */
    public String getContent() {
        return content;
    }

    /*
     * Setter for the note content.
     */
    public void setContent(String content) {
        this.content = content;
    }
}