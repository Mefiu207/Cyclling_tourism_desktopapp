package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.TypyWycieczek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.springbootjavafx.repositories.CenyRepository;
import com.project.springbootjavafx.models.Ceny;
import java.util.List;

/**
 * The {@code CenyService} class provides service methods for managing pricing information
 * (represented by the {@link Ceny} entity) in the application.
 *
 * <p>
 * It extends {@link AbstractServices} to inherit common CRUD operations and additional functionality.
 * This service allows adding new pricing records and retrieving pricing information based on a given trip type.
 * </p>
 *
 * @see Ceny
 * @see TypyWycieczek
 */
@Service
public class CenyService extends AbstractServices<Ceny, Integer> {

    private CenyRepository cenyRepository;

    /**
     * Constructs a new {@code CenyService} instance with the specified {@link CenyRepository}.
     *
     * @param cenyRepository the repository used for data access operations on {@code Ceny} entities
     */
    @Autowired
    public CenyService(CenyRepository cenyRepository) {
        super(cenyRepository, Ceny.class, Integer.class);
        this.cenyRepository = cenyRepository;
    }

    /**
     * Adds a new pricing record to the database.
     *
     * @param ceny the {@code Ceny} entity to be added
     * @return the saved {@code Ceny} entity
     */
    @Override
    public Ceny add(Ceny ceny) {
        return cenyRepository.save(ceny);
    }

    /**
     * Retrieves pricing information for the specified trip type.
     *
     * <p>
     * This method retrieves a {@code Ceny} entity based on the trip type by calling
     * {@link CenyRepository#findByTypWycieczki(String)}. If the provided trip type is null, an
     * {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param typ the {@link TypyWycieczek} instance representing the trip type
     * @return the {@code Ceny} entity corresponding to the specified trip type
     * @throws IllegalArgumentException if the provided trip type is null
     */
    public Ceny findByTypWycieczki(TypyWycieczek typ) {
        if (typ == null) {
            throw new IllegalArgumentException("Typ wycieczki nie może być null");
        }
        return cenyRepository.findByTypWycieczki(typ.getTyp());
    }
}
