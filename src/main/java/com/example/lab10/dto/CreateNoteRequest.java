package com.example.lab10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for creating a new note.
 * This class validates user input from forms or JSON requests.
 */
public class CreateNoteRequest {

    /**
     * The title of the note.
     * Must not be blank and must be at least 3 characters long.
     */
    @NotBlank(message = "Title must not be blank")
    @ValidTitle(message = "Title must be at least 3 characters long")
    private String title;

    /**
     * The content/body of the note.
     * Must not be blank and cannot exceed 1000 characters.
     */
    @NotBlank(message = "Content must not be blank")
    @Size(max = 1000, message = "Content must be at most 1000 characters")
    private String content;

    /**
     * Gets the title of the note.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the note.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the content of the note.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the note.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
