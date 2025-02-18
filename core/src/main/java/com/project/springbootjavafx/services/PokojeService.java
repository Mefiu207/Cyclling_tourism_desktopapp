package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.repositories.PokojeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code PokojeService} class provides service methods for managing room entities
 * (represented by the {@link Pokoje} class) in the application.
 *
 * <p>
 * This service extends {@link AbstractServices} to inherit common CRUD operations and adds domain-specific
 * functionality related to rooms. It provides methods to retrieve rooms associated with a given trip,
 * rooms that have an associated hotel list, update the hotel list flag for rooms, as well as retrieve related
 * hotel list and client information for a room.
 * </p>
 *
 * @see Pokoje
 * @see Wycieczki
 * @see ListyHoteli
 * @see Klienci
 * @see PokojeRepository
 */
@Service
public class PokojeService extends AbstractServices<Pokoje, Integer> {

    /**
     * The repository used for data access operations on {@code Pokoje} entities.
     */
    private final PokojeRepository repository;

    /**
     * Constructs a new {@code PokojeService} with the specified {@link PokojeRepository}.
     *
     * @param repository the repository used for CRUD operations on {@code Pokoje} entities
     */
    @Autowired
    public PokojeService(PokojeRepository repository) {
        super(repository, Pokoje.class, Integer.class);
        this.repository = repository;
    }

    /**
     * Adds a new room to the database.
     *
     * @param pokoj the {@code Pokoje} entity to be added
     * @return the saved {@code Pokoje} entity
     */
    @Override
    public Pokoje add(Pokoje pokoj) {
        return repository.save(pokoj);
    }

    /**
     * Retrieves a list of rooms associated with a specific trip.
     *
     * @param wycieczka the {@code Wycieczki} entity representing the trip
     * @return a list of {@code Pokoje} entities for the specified trip
     */
    public List<Pokoje> getPokojeWycieczki(Wycieczki wycieczka) {
        return repository.getByWycieczka(wycieczka.getWycieczka());
    }

    /**
     * Retrieves a list of rooms for a given trip that have an associated hotel list.
     *
     * @param wycieczka the {@code Wycieczki} entity representing the trip
     * @return a list of {@code Pokoje} entities that have a hotel list for the specified trip
     */
    public List<Pokoje> getPokojeZListami(Wycieczki wycieczka) {
        return repository.getByWycieczkaAndListaHoteli(wycieczka.getWycieczka());
    }

    /**
     * Sets the flag indicating that rooms have an associated hotel list to true.
     *
     * <p>
     * This method iterates over the provided list of rooms and updates each room's flag by calling
     * {@link PokojeRepository#updateListaHoteliTrue(Integer)}.
     * </p>
     *
     * @param pokoje the list of {@code Pokoje} entities to update
     */
    @Transactional
    public void setListaHoteliTrue(List<Pokoje> pokoje) {
        for (Pokoje pokoj : pokoje) {
            repository.updateListaHoteliTrue(pokoj.getId());
        }
    }

    /**
     * Sets the flag indicating that rooms have an associated hotel list to false.
     *
     * <p>
     * This method iterates over the provided list of rooms and updates each room's flag by calling
     * {@link PokojeRepository#updateListaHoteliFalse(Integer)}.
     * </p>
     *
     * @param pokoje the list of {@code Pokoje} entities to update
     */
    @Transactional
    public void setListaHoteliFalse(List<Pokoje> pokoje) {
        for (Pokoje pokoj : pokoje) {
            repository.updateListaHoteliFalse(pokoj.getId());
        }
    }

    /**
     * Retrieves the hotel list entries associated with a specific room.
     *
     * @param pokoj the {@code Pokoje} entity for which to retrieve hotel list entries
     * @return a list of {@code ListyHoteli} entities associated with the given room
     */
    public List<ListyHoteli> getListyHoteli(Pokoje pokoj) {
        return repository.getListeHoteli(pokoj.getId());
    }

    /**
     * Retrieves the list of clients assigned to a specific room.
     *
     * @param pokoj the {@code Pokoje} entity for which to retrieve clients
     * @return a list of {@code Klienci} entities associated with the given room
     */
    public List<Klienci> getKlienci(Pokoje pokoj) {
        return repository.getKlienciPokoju(pokoj.getId());
    }
}
