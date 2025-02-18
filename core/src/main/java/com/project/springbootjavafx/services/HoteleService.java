package com.project.springbootjavafx.services;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.Hotele;
import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.HoteleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The {@code HoteleService} class provides service methods for managing hotel entities
 * (represented by the {@link Hotele} class) in the application.
 *
 * <p>
 * This service extends {@link AbstractServices} to inherit common CRUD operations and additional functionality.
 * It offers functionality to add a new hotel while enforcing business rules such as checking for duplicate hotel codes
 * and validating that the hotel code is exactly 6 characters long. In addition, it provides a method to retrieve hotels
 * for a specific city.
 * </p>
 *
 * @see Hotele
 * @see Miasta
 * @see HoteleRepository
 */
@Service
public class HoteleService extends AbstractServices<Hotele, String> {

    /**
     * The repository used to perform CRUD operations on {@code Hotele} entities.
     */
    private HoteleRepository hoteleRepository;

    /**
     * Constructs a new {@code HoteleService} instance with the specified {@link HoteleRepository}.
     *
     * @param hoteleRepository the repository used for data access operations on {@code Hotele} entities
     */
    @Autowired
    public HoteleService(HoteleRepository hoteleRepository) {
        super(hoteleRepository, Hotele.class, String.class);
        this.hoteleRepository = hoteleRepository;
    }

    /**
     * Adds a new hotel to the database after performing necessary validations.
     *
     * <p>
     * This method first checks if a hotel with the given code already exists. If so, it throws a
     * {@link DuplicatedEntityExceptionn}. It also validates that the hotel code is exactly 6 characters long;
     * if not, a {@link WrongCodeLengthException} is thrown. If all validations pass, the hotel is saved using
     * the repository.
     * </p>
     *
     * @param hotel the {@code Hotele} entity to add
     * @return the saved {@code Hotele} entity
     * @throws DuplicatedEntityExceptionn if a hotel with the same code already exists
     * @throws WrongCodeLengthException   if the hotel code is not exactly 6 characters long
     */
    @Override
    public Hotele add(Hotele hotel) throws DuplicatedEntityExceptionn, WrongCodeLengthException {
        if (hoteleRepository.existsByKod(hotel.getKod())) {
            throw new DuplicatedEntityExceptionn("Hotel o kodzie " + hotel.getKod() + " już istnieje");
        } else if (hotel.getKod().trim().length() != 6) {
            throw new WrongCodeLengthException("Za krótki lub za długi kod hotelu (ma być 6 znaków)");
        }
        return hoteleRepository.save(hotel);
    }

    /**
     * Retrieves a list of hotels located in the specified city.
     *
     * <p>
     * This method uses the {@code HoteleRepository} to fetch all hotels associated with the given city.
     * The city is provided via a {@link Miasta} object, and the method returns a list of {@link Hotele}
     * entities for that city.
     * </p>
     *
     * @param miasto the {@code Miasta} object representing the city
     * @return a list of {@code Hotele} entities located in the specified city
     */
    public List<Hotele> getHoteleMiasta(Miasta miasto) {
        return hoteleRepository.getHoteleByMiasto(miasto.getMiasto());
    }
}
