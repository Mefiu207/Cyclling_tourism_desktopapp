package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.ListaNocyHoteli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The {@code ListaNocyHoteliRepository} interface provides data access operations for the
 * {@link ListaNocyHoteli} entity.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD operations and additional query capabilities.
 * In particular, it declares a custom method for retrieving a list of {@code ListaNocyHoteli} records
 * based on a given room identifier.
 * </p>
 *
 * @see JpaRepository
 * @see ListaNocyHoteli
 */
@Repository
public interface ListaNocyHoteliRepository extends JpaRepository<ListaNocyHoteli, Integer> {

    /**
     * Retrieves all {@code ListaNocyHoteli} entities associated with the specified room identifier.
     *
     * @param pokojId the identifier of the room
     * @return a list of {@code ListaNocyHoteli} records corresponding to the given room identifier
     */
    List<ListaNocyHoteli> findByPokojId(Integer pokojId);
}
