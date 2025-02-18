package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.springbootjavafx.models.Hotele;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The {@code HoteleRepository} interface provides data access operations for the {@link Hotele} entity.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD and pagination methods.
 * In addition, it declares custom query methods for checking the existence of a hotel by its code
 * and for retrieving hotels based on a specific city.
 * </p>
 *
 * @see JpaRepository
 * @see Hotele
 */
@Repository
public interface HoteleRepository extends JpaRepository<Hotele, String> {

    /**
     * Checks if a hotel with the specified code exists in the database.
     *
     * @param kod the unique hotel code to check
     * @return {@code true} if a hotel with the given code exists; {@code false} otherwise
     */
    boolean existsByKod(String kod);

    /**
     * Retrieves a list of hotels located in the specified city.
     *
     * <p>
     * This method uses a native SQL query to select all hotels from the {@code hotele} table
     * where the {@code miasto} column matches the provided city parameter.
     * </p>
     *
     * @param miasto the name of the city for which to retrieve hotels
     * @return a list of {@code Hotele} entities that are located in the specified city
     */
    @Query(value = "SELECT * FROM hotele h WHERE h.miasto = :miasto", nativeQuery = true)
    List<Hotele> getHoteleByMiasto(@Param("miasto") String miasto);
}
