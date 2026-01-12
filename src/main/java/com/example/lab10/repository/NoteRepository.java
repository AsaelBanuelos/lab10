package com.example.lab10.repository;

import com.example.lab10.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Note entity database operations.

 * This interface provides automatic CRUD methods and custom queries
 * for accessing notes from the database. Spring automatically implements
 * these methods at runtime.
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {

    /**
     * Finds all notes owned by a specific user.

     * How it works:
     * - "findAllBy" = SELECT * FROM notes WHERE
     * - "Owner_Id" = owner.id (accessing the related User's ID)
     * - Result: Gets all notes where the owner's ID matches the parameter
     */
    List<Note> findAllByOwner_Id(Integer ownerId);

    /**
     * Finds a single note by ID, but only if it belongs to the specified user.
     * Used to verify ownership before allowing operations like edit or delete.

     * This is also a derived query with two conditions:
     * - "findByIdAndOwner_Id" = SELECT * FROM notes WHERE id = ? AND owner.id = ?
     * - Returns Optional to handle the case where note doesn't exist

     * Usage example:
     * When a user tries to edit note #5:
     * - We call findByIdAndOwner_Id(5, currentUserId)
     * - If note 5 exists and belongs to the user, it returns Optional.of(note)
     * - If note 5 doesn't exist OR belongs to someone else, returns Optional.empty()
     * - We then throw 404 if Optional is empty, preventing unauthorized access
     */
    Optional<Note> findByIdAndOwner_Id(Integer id, Integer ownerId);

    /**
     * Retrieves all notes belonging to a user using a native SQL query.

     * The SQL query:
     * - SELECT * FROM notes = Get all columns from the notes table
     * - WHERE user_id = :uid = Filter by the owner's user ID
     * - ORDER BY id DESC = Sort with newest notes first
     */


    @Query(value = "SELECT * FROM notes WHERE user_id = :uid ORDER BY id DESC", nativeQuery = true)
    List<Note> findAllMineNative(@Param("uid") Integer userId);
}
