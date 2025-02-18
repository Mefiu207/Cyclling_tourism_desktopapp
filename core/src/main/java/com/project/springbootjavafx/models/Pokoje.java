package com.project.springbootjavafx.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * The {@code Pokoje} class represents a room entity in the system.
 *
 * <p>
 * This entity contains information about a room used in a trip, including its type, the number of clients currently assigned,
 * its capacity, and whether it has an associated hotel list. It is related to a {@link Wycieczki} entity (the trip), a list of
 * {@link ListyHoteli} (hotel lists), and a list of {@link Klienci} (clients) who have booked the room.
 * </p>
 *
 * <p>
 * The class is mapped to a database table using JPA annotations, and Lombok is used to automatically generate constructors,
 * getters, and setters.
 * </p>
 *
 * @see Wycieczki
 * @see ListyHoteli
 * @see Klienci
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokoje implements Models {

    /**
     * The unique identifier for the room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The trip associated with this room.
     *
     * <p>
     * This is a many-to-one relationship with the {@link Wycieczki} entity.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wycieczka")
    private Wycieczki wycieczka;

    /**
     * The list of hotel lists associated with this room.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link ListyHoteli} entity.
     * </p>
     */
    @OneToMany(mappedBy = "pokoj")
    private List<ListyHoteli> listyHoteli;

    /**
     * The list of clients assigned to this room.
     *
     * <p>
     * This represents a one-to-many relationship with the {@link Klienci} entity.
     * Cascade and orphan removal are enabled to manage the client entities.
     * </p>
     */
    @OneToMany(mappedBy = "pokoj", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Klienci> klienci;

    /**
     * The type of the room.
     */
    @Column(name = "typ_pokoju")
    private String typPokoju;

    /**
     * The number of clients currently assigned to the room.
     */
    @Column(name = "il_klientow")
    private Integer ilKlientow = 0;

    /**
     * The total capacity of the room.
     */
    @Column(name = "il_miejsc")
    private Integer ilMiejsc;

    /**
     * Indicates whether the room has an associated hotel list.
     */
    @Column(name = "czy_lista_hoteli")
    private Boolean listaHoteli;

    /**
     * Returns a string representation of the room.
     *
     * <p>
     * This implementation returns the room type.
     * </p>
     *
     * @return the room type as a string
     */
    @Override
    public String toString() {
        return typPokoju;
    }
}
