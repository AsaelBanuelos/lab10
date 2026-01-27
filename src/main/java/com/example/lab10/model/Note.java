package com.example.lab10.model;

import jakarta.persistence.*;

/*
 * JPA entity representing a note stored in the database.
 * Each note belongs to exactly one user.
 * This entity is mapped to the "notes" table.
 */
@Entity
@Table(name = "notes")
public class Note {

    /*
     * Primary key of the note.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * Title of the note.
     * Cannot be null in the database.
     */
    @Column(nullable = false)
    private String title;

    /*
     * Content of the note.
     * Cannot be null and limited to 1000 characters.
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /*
     * Owner of the note.
     * Many notes can belong to one user (ManyToOne).
     * The user_id column is a foreign key.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    /*
     * Default constructor required by JPA.
     */
    public Note() {}

    /*
     * Constructor used when creating a new note.
     */
    public Note(String title, String content, User owner) {
        this.title = title;
        this.content = content;
        this.owner = owner;
    }

    /*
     * Getter for note ID.
     */
    public Integer getId() {
        return id;
    }

    /*
     * Getter for note title.
     */
    public String getTitle() {
        return title;
    }

    /*
     * Setter for note title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * Getter for note content.
     */
    public String getContent() {
        return content;
    }

    /*
     * Setter for note content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /*
     * Getter for the note owner.
     */
    public User getOwner() {
        return owner;
    }

    /*
     * Setter for the note owner.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }
}