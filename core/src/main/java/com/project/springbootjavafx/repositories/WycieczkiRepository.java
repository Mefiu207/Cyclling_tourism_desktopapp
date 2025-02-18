package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Wycieczki;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@code WycieczkiRepository} interface provides CRUD operations for the {@link Wycieczki} entity.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard data access methods for managing {@code Wycieczki} objects,
 * and includes a custom method to check the existence of a trip by its unique name.
 * </p>
 *
 * @see JpaRepository
 * @see Wycieczki
 */
@Repository
public interface WycieczkiRepository extends JpaRepository<Wycieczki, String> {

    /**
     * Checks if a trip with the specified name exists in the database.
     *
     * @param nazwa the name of the trip to check for existence
     * @return {@code true} if a trip with the given name exists, {@code false} otherwise
     */
    boolean existsByWycieczka(String nazwa);
}
