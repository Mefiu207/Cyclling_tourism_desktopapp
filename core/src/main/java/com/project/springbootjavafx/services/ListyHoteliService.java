package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.ListyHoteliKey;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.repositories.ListyHoteliRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@code ListyHoteliService} class provides service methods for managing hotel list entries,
 * represented by the {@link ListyHoteli} entity, in the application.
 *
 * <p>
 * This service extends {@link AbstractServices} to inherit common CRUD operations and adds additional
 * functionality specific to hotel lists. In particular, it allows adding new hotel list records and
 * deleting all hotel list records associated with a given room.
 * </p>
 *
 * <p>
 * The service uses a {@link ListyHoteliRepository} to perform database operations. The {@code @Transactional}
 * annotation is used for the deletion method to ensure the delete operation is executed within a transaction.
 * </p>
 *
 * @see ListyHoteli
 * @see ListyHoteliKey
 * @see Pokoje
 * @see ListyHoteliRepository
 */
@Getter
@Setter
@Service
public class ListyHoteliService extends AbstractServices<ListyHoteli, ListyHoteliKey> {

    /**
     * The repository used for data access operations on {@code ListyHoteli} entities.
     */
    private ListyHoteliRepository repository;

    /**
     * Constructs a new {@code ListyHoteliService} with the specified repository.
     *
     * @param repository the {@link ListyHoteliRepository} used for performing CRUD operations on {@code ListyHoteli} entities
     */
    @Autowired
    public ListyHoteliService(ListyHoteliRepository repository) {
        super(repository, ListyHoteli.class, ListyHoteliKey.class);
        this.repository = repository;
    }

    /**
     * Adds a new hotel list entry to the database.
     *
     * @param entity the {@code ListyHoteli} entity to be added
     * @return the saved {@code ListyHoteli} entity
     */
    @Override
    public ListyHoteli add(ListyHoteli entity) {
        return repository.save(entity);
    }

    /**
     * Deletes all hotel list entries associated with the specified room.
     *
     * <p>
     * This method deletes all {@code ListyHoteli} records where the room identifier matches the ID of the provided
     * {@link Pokoje} entity. The operation is executed within a transaction.
     * </p>
     *
     * @param pokoj the {@code Pokoje} entity whose associated hotel list entries should be deleted
     */
    @Transactional
    public void usunDlaPokoju(Pokoje pokoj) {
        repository.deleteByPokoj(pokoj.getId());
    }
}
