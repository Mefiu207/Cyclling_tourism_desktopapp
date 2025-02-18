package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.ListyHoteliKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The {@code ListyHoteliRepository} interface provides data access operations for the
 * {@link ListyHoteli} entity, which is identified by a composite key {@link ListyHoteliKey}.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations. In addition, it declares a custom
 * modifying query to delete hotel list records associated with a specific room.
 * </p>
 *
 * @see JpaRepository
 * @see ListyHoteli
 * @see ListyHoteliKey
 */
@Repository
public interface ListyHoteliRepository extends JpaRepository<ListyHoteli, ListyHoteliKey> {

    /**
     * Deletes all {@code ListyHoteli} records associated with the specified room identifier.
     *
     * <p>
     * This method executes a native SQL DELETE query on the {@code listy_hoteli} table,
     * removing all records where the {@code pokoj} column matches the given room ID.
     * </p>
     *
     * @param pokojID the identifier of the room whose hotel list records should be deleted
     */
    @Modifying
    @Query(value = "DELETE FROM listy_hoteli WHERE pokoj = :pokojID", nativeQuery = true)
    void deleteByPokoj(@Param("pokojID") Integer pokojID);
}
