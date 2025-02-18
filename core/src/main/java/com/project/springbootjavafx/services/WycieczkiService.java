package com.project.springbootjavafx.services;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.repositories.WycieczkiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code WycieczkiService} class provides service methods for managing trip entities
 * (represented by the {@link Wycieczki} class) in the application.
 *
 * <p>
 * This service extends {@link AbstractServices} to inherit common CRUD operations.
 * It includes domain-specific validations when adding a new trip:
 * </p>
 * <ul>
 *   <li>
 *     It checks if a trip with the same code already exists using the repository's
 *     {@code existsByWycieczka} method. If so, it throws a {@link DuplicatedEntityExceptionn}.
 *   </li>
 *   <li>
 *     It validates that the trip code (obtained from {@code wycieczka.getWycieczka()}) has a length of either 4 or 5 characters.
 *     If the code length is not as expected, a {@link WrongCodeLengthException} is thrown.
 *   </li>
 * </ul>
 *
 * <p>
 * If all validations pass, the new {@code Wycieczki} entity is saved to the database.
 * </p>
 *
 * @see Wycieczki
 * @see WycieczkiRepository
 */
@Service
public class WycieczkiService extends AbstractServices<Wycieczki, String> {

    /**
     * The repository used for data access operations on {@code Wycieczki} entities.
     */
    private WycieczkiRepository repository;

    /**
     * Constructs a new {@code WycieczkiService} with the specified {@link WycieczkiRepository}.
     *
     * @param repository the repository used for CRUD operations on {@code Wycieczki} entities
     */
    @Autowired
    public WycieczkiService(WycieczkiRepository repository) {
        super(repository, Wycieczki.class, String.class);
        this.repository = repository;
    }

    /**
     * Adds a new trip to the database after performing validations.
     *
     * <p>
     * This method first checks whether a trip with the same code already exists. If it does,
     * a {@link DuplicatedEntityExceptionn} is thrown with an appropriate message.
     * It also validates that the trip code (after trimming) is either 4 or 5 characters long.
     * If not, a {@link WrongCodeLengthException} is thrown.
     * </p>
     *
     * @param wycieczka the {@code Wycieczki} entity to be added
     * @return the saved {@code Wycieczki} entity
     * @throws DuplicatedEntityExceptionn if a trip with the same code already exists
     * @throws WrongCodeLengthException   if the trip code length is not 4 or 5 characters
     */
    @Override
    public Wycieczki add(Wycieczki wycieczka) {
        if (repository.existsByWycieczka(wycieczka.getWycieczka())) {
            throw new DuplicatedEntityExceptionn("Wycieczka o kodzie: " + wycieczka.getWycieczka() + " juz istnieje");
        } else if (wycieczka.getWycieczka().trim().length() != 5 && wycieczka.getWycieczka().trim().length() != 4) {
            throw new WrongCodeLengthException("Za krótka lub za długa nazwa wycieczki (mają być 5 albo 4 znaki)");
        }
        return repository.save(wycieczka);
    }
}
