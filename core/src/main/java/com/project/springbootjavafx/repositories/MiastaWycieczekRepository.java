package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.MiastaWycieczek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The {@code MiastaWycieczekRepository} interface provides CRUD operations and custom query methods
 * for the {@link MiastaWycieczek} entity.
 *
 * <p>
 * In addition to the standard CRUD methods inherited from {@link JpaRepository}, this repository defines a custom
 * native query to retrieve a list of {@code MiastaWycieczek} records based on a specified trip type.
 * </p>
 *
 * @see JpaRepository
 * @see MiastaWycieczek
 */
@Repository
public interface MiastaWycieczekRepository extends JpaRepository<MiastaWycieczek, Integer> {

    /**
     * Retrieves all {@code MiastaWycieczek} records associated with the specified trip type.
     *
     * <p>
     * This method uses a native SQL query to select all records from the {@code miasta_wycieczek} table where the
     * {@code typ_wycieczki} column matches the provided parameter.
     * </p>
     *
     * @param typ_wycieczki the trip type identifier used to filter the records
     * @return a list of {@code MiastaWycieczek} entities that match the specified trip type
     */
    @Query(value = "SELECT * FROM miasta_wycieczek m WHERE m.typ_wycieczki = :typ_wycieczki", nativeQuery = true)
    List<MiastaWycieczek> findByTypWycieczki(@Param("typ_wycieczki") String typ_wycieczki);
}
