package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * The {@code Hotele} class represents a hotel entity in the system.
 *
 * <p>
 * This entity contains basic information about a hotel, such as its code, name, address, email, and telephone number.
 * It is associated with a {@link Miasta} entity representing the city in which the hotel is located and may have multiple
 * {@link ListyHoteli} entries associated with it.
 * </p>
 *
 * <p>
 * The class uses JPA annotations to map to a database table and Lombok annotations to generate boilerplate code (getters and setters).
 * </p>
 *
 * @see Miasta
 * @see ListyHoteli
 */
@Getter
@Setter
@Entity
public class Hotele implements Models {

    /**
     * The unique code identifying the hotel.
     */
    @Id
    @Column(name = "kod")
    private String kod;

    /**
     * The name of the hotel.
     */
    private String nazwa;

    /**
     * The city in which the hotel is located.
     *
     * <p>
     * This is defined as a many-to-one relationship with the {@link Miasta} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "miasto")
    private Miasta miasto;

    /**
     * The list of hotel lists associated with this hotel.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link ListyHoteli} entity.
     * </p>
     */
    @OneToMany(mappedBy = "hotel")
    private List<ListyHoteli> listyHoteli;

    /**
     * The address of the hotel.
     */
    private String adres;

    /**
     * The email address of the hotel.
     */
    private String mail;

    /**
     * The telephone number of the hotel.
     */
    private String nr_tel;

    /**
     * Default no-argument constructor.
     */
    public Hotele() {}

    /**
     * Constructs a new {@code Hotele} instance with the specified details.
     *
     * @param kod    the unique hotel code
     * @param nazwa  the name of the hotel
     * @param miasto the city in which the hotel is located
     * @param adres  the address of the hotel
     * @param mail   the email address of the hotel
     * @param nr_tel the telephone number of the hotel
     */
    public Hotele(String kod, String nazwa, Miasta miasto, String adres, String mail, String nr_tel) {
        this.kod = kod;
        this.nazwa = nazwa;
        this.miasto = miasto;
        this.adres = adres;
        this.mail = mail;
        this.nr_tel = nr_tel;
    }

    /**
     * Returns a string representation of the hotel.
     *
     * <p>
     * This implementation returns the hotel code.
     * </p>
     *
     * @return the hotel code as a string
     */
    @Override
    public String toString() {
        return kod;
    }
}
