package com.project.springbootjavafx.services;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.repositories.TypyWycieczekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code TypyWycieczekService} class provides service operations for managing trip types
 * (represented by the {@link TypyWycieczek} entity) in the application.
 *
 * <p>
 * This service extends {@link AbstractServices} to inherit common CRUD operations.
 * It implements domain-specific validation when adding new trip types, such as ensuring that the trip type
 * is unique and that its code length is either 2 or 3 characters. In addition, it provides a method to retrieve
 * the list of city assignments (represented by {@link MiastaWycieczek}) for a given trip type.
 * </p>
 *
 * @see TypyWycieczek
 * @see MiastaWycieczek
 * @see TypyWycieczekRepository
 */
@Service
public class TypyWycieczekService extends AbstractServices<TypyWycieczek, String> {

    /**
     * The repository used for data access operations on {@code TypyWycieczek} entities.
     */
    private TypyWycieczekRepository typy_wycieczekRepository;

    /**
     * Constructs a new {@code TypyWycieczekService} with the specified {@link TypyWycieczekRepository}.
     *
     * @param typy_wycieczekRepository the repository used for CRUD operations on {@code TypyWycieczek} entities
     */
    @Autowired
    public TypyWycieczekService(TypyWycieczekRepository typy_wycieczekRepository) {
        super(typy_wycieczekRepository, TypyWycieczek.class, String.class);
        this.typy_wycieczekRepository = typy_wycieczekRepository;
    }

    /**
     * Adds a new trip type to the database after performing necessary validations.
     *
     * <p>
     * This method checks if a trip type with the same code already exists by using
     * {@link TypyWycieczekRepository#existsByTyp(String)}. If a duplicate is found, it throws a
     * {@link DuplicatedEntityExceptionn}. Additionally, it validates that the trip type code, after trimming,
     * has a length of either 2 or 3 characters; if not, a {@link WrongCodeLengthException} is thrown.
     * </p>
     *
     * @param typy_wycieczek the {@code TypyWycieczek} entity to be added
     * @return the saved {@code TypyWycieczek} entity
     * @throws DuplicatedEntityExceptionn if a trip type with the same code already exists
     * @throws WrongCodeLengthException   if the trip type code is not 2 or 3 characters long
     */
    @Override
    public TypyWycieczek add(TypyWycieczek typy_wycieczek) {
        if (typy_wycieczekRepository.existsByTyp(typy_wycieczek.getTyp())) {
            throw new DuplicatedEntityExceptionn("Wycieczka o typie " + typy_wycieczek.getTyp() + " już istnieje");
        } else if (typy_wycieczek.getTyp().trim().length() != 3 && typy_wycieczek.getTyp().trim().length() != 2) {
            throw new WrongCodeLengthException("Za krótka lub za długa nazwa typu (mają być 3 albo 2 znaki)");
        }
        return typy_wycieczekRepository.save(typy_wycieczek);
    }

    /**
     * Retrieves the list of city assignments for a given trip type.
     *
     * <p>
     * This method uses a custom query defined in {@link TypyWycieczekRepository} to fetch all {@code MiastaWycieczek}
     * records associated with the specified trip type.
     * </p>
     *
     * @param typWycieczki the {@code TypyWycieczek} entity for which to retrieve city assignments
     * @return a {@code List} of {@code MiastaWycieczek} entities corresponding to the specified trip type
     */
    public List<MiastaWycieczek> getMiastaWycieczki(TypyWycieczek typWycieczki) {
        return typy_wycieczekRepository.getMiastaWycieczki(typWycieczki.getTyp());
    }
}
