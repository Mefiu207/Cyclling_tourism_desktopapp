package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Klienci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The {@code KlienciRepository} interface provides data access operations for the {@link Klienci} entity.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations and pagination functionality.
 * In addition, it declares a custom query method to retrieve clients associated with a specific room.
 * </p>
 *
 * @see JpaRepository
 * @see Klienci
 */
@Repository
public interface KlienciRepository extends JpaRepository<Klienci, Integer> {

    /**
     * Retrieves a list of clients associated with a given room.
     *
     * <p>
     * This method uses a native SQL query to select all records from the {@code klienci} table where the
     * {@code pokoj} column matches the specified room ID.
     * </p>
     *
     * @param ID the identifier of the room
     * @return a list of {@code Klienci} entities that are associated with the specified room
     */
    @Query(value = "SELECT * FROM klienci WHERE klienci.pokoj = :ID", nativeQuery = true)
    List<Klienci> getKlienciByPokoj(Integer ID);
}
