package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.MiastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;

/**
 * The {@code MiastaService} class provides service operations for managing cities
 * (represented by the {@link Miasta} entity) in the system.
 *
 * <p>
 * It extends {@link AbstractServices} to inherit basic CRUD operations and adds domain-specific functionality,
 * such as verifying the uniqueness of a city before adding it to the database.
 * </p>
 *
 * <p>
 * The service uses {@link MiastaRepository} for data access, ensuring that no duplicate city entries are created.
 * </p>
 *
 * @see Miasta
 * @see MiastaRepository
 * @see DuplicatedEntityExceptionn
 */
@Service
public class MiastaService extends AbstractServices<Miasta, String> {

    /**
     * The repository used for data access operations on {@code Miasta} entities.
     */
    private MiastaRepository miastaRepository;

    /**
     * Constructs a new {@code MiastaService} instance with the specified {@link MiastaRepository}.
     *
     * @param miastaRepository the repository used for CRUD operations on {@code Miasta} entities
     */
    @Autowired
    public MiastaService(MiastaRepository miastaRepository) {
        super(miastaRepository, Miasta.class, String.class);
        this.miastaRepository = miastaRepository;
    }

    /**
     * Adds a new city to the database after verifying that it does not already exist.
     *
     * <p>
     * This method checks if a city with the same name already exists by using the repository's
     * {@code existsByMiasto} method. If a duplicate is found, a {@link DuplicatedEntityExceptionn} is thrown.
     * Otherwise, the new {@code Miasta} entity is saved and returned.
     * </p>
     *
     * @param miasto the {@code Miasta} entity to be added
     * @return the saved {@code Miasta} entity
     * @throws DuplicatedEntityExceptionn if a city with the same name already exists
     */
    @Override
    public Miasta add(Miasta miasto) throws DuplicatedEntityExceptionn {
        if (miastaRepository.existsByMiasto(miasto.getMiasto())) {
            throw new DuplicatedEntityExceptionn("Miasto " + miasto.getMiasto() + " już istnieje");
        }
        return miastaRepository.save(miasto);
    }
}
