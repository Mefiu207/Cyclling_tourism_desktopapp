package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.repositories.KlienciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code KlienciService} class provides service methods for managing client data
 * (represented by the {@link Klienci} entity) within the application.
 *
 * <p>
 * It extends {@link AbstractServices} to inherit common CRUD operations and adds additional
 * functionality specific to clients, such as retrieving a list of clients associated with a particular room.
 * </p>
 *
 * @see Klienci
 * @see Pokoje
 * @see KlienciRepository
 */
@Service
public class KlienciService extends AbstractServices<Klienci, Integer> {

    /**
     * The repository used for data access operations on {@code Klienci} entities.
     */
    private KlienciRepository repository;

    /**
     * Constructs a new {@code KlienciService} with the specified {@link KlienciRepository}.
     *
     * @param repository the repository used for CRUD operations on {@code Klienci} entities
     */
    @Autowired
    public KlienciService(KlienciRepository repository) {
        super(repository, Klienci.class, Integer.class);
        this.repository = repository;
    }

    /**
     * Adds a new client to the database.
     *
     * <p>
     * This method saves the provided {@code Klienci} entity using the underlying repository and returns
     * the persisted entity.
     * </p>
     *
     * @param klient the {@code Klienci} entity to be added
     * @return the saved {@code Klienci} entity
     */
    @Override
    public Klienci add(Klienci klient) {
        return repository.save(klient);
    }

    /**
     * Retrieves a list of clients associated with the specified room.
     *
     * <p>
     * This method uses a custom query defined in {@link KlienciRepository} to fetch all clients
     * that are assigned to the given room, identified by its ID.
     * </p>
     *
     * @param pokoj the {@code Pokoje} object representing the room
     * @return a list of {@code Klienci} entities associated with the specified room
     */
    public List<Klienci> getKlienciPokoju(Pokoje pokoj) {
        return repository.getKlienciByPokoj(pokoj.getId());
    }
}
