package com.example.lab10.repository;

import com.example.lab10.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for Note entity
// Provides basic CRUD operations without writing SQL
public interface NoteRepository extends JpaRepository<Note, Long> {
}
