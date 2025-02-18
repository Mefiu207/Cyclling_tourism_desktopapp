package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.ListaNocyHoteli;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.repositories.ListaNocyHoteliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code ListaNocyHoteliService} class provides service methods for managing hotel night list data.
 *
 * <p>
 * This service encapsulates data access operations for {@link ListaNocyHoteli} entities through
 * the {@link ListaNocyHoteliRepository}. It offers methods to retrieve all hotel night list records
 * or to filter them by a specific room.
 * </p>
 *
 * @see ListaNocyHoteli
 * @see Pokoje
 * @see ListaNocyHoteliRepository
 */
@Service
public class ListaNocyHoteliService {

    /**
     * The repository used to perform CRUD operations on {@code ListaNocyHoteli} entities.
     */
    private final ListaNocyHoteliRepository repository;

    /**
     * Constructs a new {@code ListaNocyHoteliService} with the specified repository.
     *
     * @param repository the {@link ListaNocyHoteliRepository} used for data access operations
     */
    @Autowired
    public ListaNocyHoteliService(ListaNocyHoteliRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all hotel night list records.
     *
     * @return a {@link List} of all {@code ListaNocyHoteli} entities in the database
     */
    public List<ListaNocyHoteli> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves the hotel night list records for a specific room.
     *
     * <p>
     * This method uses the room's identifier to filter and return the corresponding
     * {@code ListaNocyHoteli} entries.
     * </p>
     *
     * @param pokoj the {@link Pokoje} entity for which to retrieve hotel night lists
     * @return a {@link List} of {@code ListaNocyHoteli} entities associated with the specified room
     */
    public List<ListaNocyHoteli> getListyByPokoj(Pokoje pokoj) {
        return repository.findByPokojId(pokoj.getId());
    }
}
