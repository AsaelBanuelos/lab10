package com.example.lab10.repository;

import com.example.lab10.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/*
 * Repository for Note entity.
 * I only use this for database queries.
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {

    /*
     * Gets all notes that belong to one user.
     * Spring creates the query automatically.
     */
    List<Note> findAllByOwner_Id(Integer ownerId);

    /*
     * Gets a note only if it belongs to the user.
     */
    Optional<Note> findByIdAndOwner_Id(Integer id, Integer ownerId);

    /*
     * Gets all notes for a user using native SQL.
     * here I use parameters to avoid SQL injection.
     */
    @Query(
            value = "SELECT * FROM notes WHERE user_id = :uid ORDER BY id DESC",
            nativeQuery = true
    )
    List<Note> findAllMineNative(@Param("uid") Integer userId);
}