package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.project.springbootjavafx.models.Ceny;

/**
 * The {@code CenyRepository} interface provides methods for performing CRUD operations on
 * {@link Ceny} entities as well as a custom query to retrieve pricing information based on a trip type.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard data access methods. In addition, a custom native query is defined
 * to find a {@code Ceny} record by its associated trip type.
 * </p>
 *
 * @see JpaRepository
 * @see Ceny
 */
@Repository
public interface CenyRepository extends JpaRepository<Ceny, Integer> {

    /**
     * Retrieves a {@link Ceny} entity based on the associated trip type.
     *
     * <p>
     * This method uses a native SQL query to select a record from the {@code ceny} table where the
     * column {@code typ_wycieczki} matches the specified parameter.
     * </p>
     *
     * @param typ_wycieczki the trip type identifier used to filter the pricing record
     * @return the {@code Ceny} entity matching the specified trip type, or {@code null} if none is found
     */
    @Query(value = "SELECT * FROM ceny c WHERE c.typ_wycieczki = :typ_wycieczki", nativeQuery = true)
    Ceny findByTypWycieczki(@Param("typ_wycieczki") String typ_wycieczki);
}
