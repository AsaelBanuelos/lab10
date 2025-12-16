package com.example.lab10.service;

import com.example.lab10.model.Note;
import com.example.lab10.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    // Repository used to access note data from the database
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Returns all notes stored in the database
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    // Creates and saves a new note
    public Note create(String title, String content) {
        Note note = new Note(title, content);
        return noteRepository.save(note);
    }
}
