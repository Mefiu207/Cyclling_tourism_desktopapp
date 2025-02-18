package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.models.Pokoje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The {@code PokojeRepository} interface provides data access operations for the {@link Pokoje} entity.
 *
 * <p>
 * It extends {@link JpaRepository} to inherit standard CRUD and pagination methods. Additionally, this repository
 * defines custom query methods to:
 * </p>
 * <ul>
 *   <li>Retrieve rooms by trip.</li>
 *   <li>Retrieve rooms by trip that have an associated hotel list.</li>
 *   <li>Update the flag indicating the presence of a hotel list for a room.</li>
 *   <li>Retrieve the list of hotel lists for a specific room, ordered by night number.</li>
 *   <li>Retrieve the list of clients assigned to a specific room.</li>
 * </ul>
 *
 * @see JpaRepository
 * @see Pokoje
 */
@Repository
public interface PokojeRepository extends JpaRepository<Pokoje, Integer> {

    /**
     * Retrieves a list of rooms associated with the specified trip.
     *
     * <p>
     * This method uses a native SQL query to select all rooms from the {@code pokoje} table where the
     * {@code wycieczka} column matches the given trip identifier.
     * </p>
     *
     * @param wycieczka the trip identifier (as a String)
     * @return a list of {@code Pokoje} entities associated with the specified trip
     */
    @Query(value = "SELECT * FROM pokoje p WHERE p.wycieczka = :wycieczka", nativeQuery = true)
    List<Pokoje> getByWycieczka(String wycieczka);

    /**
     * Retrieves a list of rooms for the specified trip that have an associated hotel list.
     *
     * <p>
     * This method uses a native SQL query to select rooms from the {@code pokoje} table where the {@code wycieczka}
     * column matches the given trip identifier and the {@code czy_lista_hoteli} flag is true.
     * </p>
     *
     * @param wycieczka the trip identifier (as a String)
     * @return a list of {@code Pokoje} entities for the specified trip that have hotel lists
     */
    @Query(value = "SELECT * FROM pokoje p WHERE p.wycieczka = :wycieczka AND p.czy_lista_hoteli = true", nativeQuery = true)
    List<Pokoje> getByWycieczkaAndListaHoteli(String wycieczka);

    /**
     * Updates the {@code czy_lista_hoteli} flag to true for a room with the specified identifier.
     *
     * <p>
     * This method uses a native SQL query to update the {@code pokoje} table and set the {@code czy_lista_hoteli}
     * column to true for the room identified by the given ID.
     * </p>
     *
     * @param pokojID the identifier of the room
     */
    @Modifying
    @Query(value = "UPDATE pokoje SET czy_lista_hoteli = true WHERE id = :pokojID", nativeQuery = true)
    void updateListaHoteliTrue(Integer pokojID);

    /**
     * Updates the {@code czy_lista_hoteli} flag to false for a room with the specified identifier.
     *
     * <p>
     * This method uses a native SQL query to update the {@code pokoje} table and set the {@code czy_lista_hoteli}
     * column to false for the room identified by the given ID.
     * </p>
     *
     * @param pokojID the identifier of the room
     */
    @Modifying
    @Query(value = "UPDATE pokoje SET czy_lista_hoteli = false WHERE id = :pokojID", nativeQuery = true)
    void updateListaHoteliFalse(Integer pokojID);

    /**
     * Retrieves a list of hotel lists associated with a specific room, ordered by the night number.
     *
     * <p>
     * This method performs a join between the {@code listy_hoteli} and {@code miasta_wycieczek} tables to order the
     * hotel list entries by the {@code nr_nocy} column from the {@code miasta_wycieczek} table.
     * </p>
     *
     * @param ID the identifier of the room
     * @return a list of {@code ListyHoteli} entities associated with the specified room, ordered by night number
     */
    @Query(value = "SELECT l.* FROM listy_hoteli l JOIN miasta_wycieczek m ON l.miasto_wycieczek = m.id WHERE l.pokoj = :ID ORDER BY m.nr_nocy", nativeQuery = true)
    List<ListyHoteli> getListeHoteli(Integer ID);

    /**
     * Retrieves a list of clients associated with a specific room.
     *
     * <p>
     * This method uses a native SQL query to select all client records from the {@code klienci} table where the
     * {@code pokoj} column matches the specified room identifier.
     * </p>
     *
     * @param ID the identifier of the room
     * @return a list of {@code Klienci} entities associated with the specified room
     */
    @Query(value = "SELECT * FROM klienci k WHERE k.pokoj = :ID", nativeQuery = true)
    List<Klienci> getKlienciPokoju(Integer ID);
}
