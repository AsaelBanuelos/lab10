package com.example.lab10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Note {

    // Primary key generated automatically by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Title column, cannot be null
    @Column(nullable = false)
    private String title;

    // Content column, cannot be null and limited in size
    @Column(nullable = false, length = 1000)
    private String content;

    // Default constructor required by JPA
    public Note() {
    }

    // Constructor used to create new Note objects
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter for the note ID
    public Integer getId() {
        return id;
    }

    // Getter for the note title
    public String getTitle() {
        return title;
    }

    // Setter for the note title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for the note content
    public String getContent() {
        return content;
    }

    // Setter for the note content
    public void setContent(String content) {
        this.content = content;
    }
}
