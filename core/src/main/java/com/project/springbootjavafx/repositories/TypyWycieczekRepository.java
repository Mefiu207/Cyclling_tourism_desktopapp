package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The {@code TypyWycieczekRepository} interface provides CRUD operations for the {@link TypyWycieczek} entity,
 * as well as custom query methods for related operations.
 *
 * <p>
 * In addition to the standard methods inherited from {@link JpaRepository}, this repository defines:
 * </p>
 * <ul>
 *   <li>
 *     {@code existsByTyp(String s)} to check if a trip type with the given identifier exists.
 *   </li>
 *   <li>
 *     {@code getMiastaWycieczki(String typ)} to retrieve a list of {@link MiastaWycieczek} records associated with
 *     a specific trip type, ordered by the night number.
 *   </li>
 * </ul>
 *
 * @see JpaRepository
 * @see TypyWycieczek
 * @see MiastaWycieczek
 */
@Repository
public interface TypyWycieczekRepository extends JpaRepository<TypyWycieczek, String> {

    /**
     * Checks whether a trip type with the specified identifier exists in the database.
     *
     * @param s the trip type identifier
     * @return {@code true} if a trip type with the given identifier exists, {@code false} otherwise
     */
    boolean existsByTyp(String s);

    /**
     * Retrieves all {@link MiastaWycieczek} records associated with the specified trip type, ordered by the night number.
     *
     * <p>
     * This method uses a native SQL query to select records from the {@code miasta_wycieczek} table where the
     * {@code typ_wycieczki} column matches the given trip type. The results are ordered by the {@code nr_nocy} column.
     * </p>
     *
     * @param typ the identifier of the trip type
     * @return a list of {@code MiastaWycieczek} entities associated with the specified trip type
     */
    @Query(value = "SELECT * FROM miasta_wycieczek WHERE typ_wycieczki = :typ ORDER BY nr_nocy", nativeQuery = true)
    List<MiastaWycieczek> getMiastaWycieczki(@Param("typ") String typ);
}
