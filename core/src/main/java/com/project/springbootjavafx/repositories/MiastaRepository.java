package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Miasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@code MiastaRepository} interface provides CRUD operations for the {@link Miasta} entity.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard methods for performing create, read, update, and delete operations.
 * In addition, it declares a custom method to check for the existence of a city by its name.
 * </p>
 *
 * @see JpaRepository
 * @see Miasta
 */
@Repository
public interface MiastaRepository extends JpaRepository<Miasta, String> {

    /**
     * Checks if a city with the specified name exists in the database.
     *
     * @param miasto the name of the city to check
     * @return {@code true} if a city with the given name exists; {@code false} otherwise
     */
    boolean existsByMiasto(String miasto);
}
