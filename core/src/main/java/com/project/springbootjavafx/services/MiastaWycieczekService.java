package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.repositories.MiastaWycieczekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code MiastaWycieczekService} class provides service methods for managing
 * trip night city assignments, represented by the {@link MiastaWycieczek} entity.
 *
 * <p>
 * This service extends {@link AbstractServices} to inherit common CRUD operations.
 * In addition, it provides a method to retrieve a list of {@code MiastaWycieczek} records
 * based on a specific trip type. The trip type parameter is validated to ensure it is not null.
 * </p>
 *
 * @see MiastaWycieczek
 * @see TypyWycieczek
 * @see MiastaWycieczekRepository
 */
@Service
public class MiastaWycieczekService extends AbstractServices<MiastaWycieczek, Integer> {

    /**
     * The repository used for data access operations on {@code MiastaWycieczek} entities.
     */
    private MiastaWycieczekRepository repository;

    /**
     * Constructs a new {@code MiastaWycieczekService} with the specified {@link MiastaWycieczekRepository}.
     *
     * @param miastaWycieczekRepository the repository used for CRUD operations on {@code MiastaWycieczek} entities
     */
    @Autowired
    public MiastaWycieczekService(MiastaWycieczekRepository miastaWycieczekRepository) {
        super(miastaWycieczekRepository, MiastaWycieczek.class, Integer.class);
        this.repository = miastaWycieczekRepository;
    }

    /**
     * Adds a new trip night city assignment to the database.
     *
     * @param miastoWycieczki the {@code MiastaWycieczek} entity to be added
     * @return the saved {@code MiastaWycieczek} entity
     */
    @Override
    public MiastaWycieczek add(MiastaWycieczek miastoWycieczki) {
        return repository.save(miastoWycieczki);
    }

    /**
     * Retrieves a list of trip night city assignments associated with the specified trip type.
     *
     * <p>
     * This method checks that the provided {@code TypyWycieczek} parameter is not null, and then uses a custom query
     * from {@link MiastaWycieczekRepository} to fetch all {@code MiastaWycieczek} records where the trip type matches.
     * </p>
     *
     * @param typ the {@code TypyWycieczek} object representing the trip type
     * @return a {@code List} of {@code MiastaWycieczek} entities corresponding to the given trip type
     * @throws IllegalArgumentException if the provided trip type is null
     */
    public List<MiastaWycieczek> findByTypWycieczki(TypyWycieczek typ) {
        if (typ == null) {
            throw new IllegalArgumentException("Typ wycieczki nie może być null");
        }
        return repository.findByTypWycieczki(typ.getTyp());
    }
}
