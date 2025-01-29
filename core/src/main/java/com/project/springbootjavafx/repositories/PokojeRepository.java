package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.Wycieczki;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.springbootjavafx.models.Pokoje;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokojeRepository extends JpaRepository<Pokoje, Integer> {

    @Query(value = "SELECT * FROM pokoje p WHERE p.wycieczka = :wycieczka", nativeQuery = true)
    List<Pokoje> getByWycieczka(String wycieczka);

    @Query(value = "SELECT * FROM pokoje p WHERE p.wycieczka = :wycieczka AND p.czy_lista_hoteli = true", nativeQuery = true)
    List<Pokoje> getByWycieczkaAndListaHoteli(String wycieczka);

    // Zmiania wartosc czy_lista_hoteli na true
    @Modifying
    @Query(value = "UPDATE pokoje SET czy_lista_hoteli = true WHERE id = :pokojID", nativeQuery = true)
    void updateListaHoteliTrue(Integer pokojID);

    // Zmiania wartosc czy_lista_hoteli na false
    @Modifying
    @Query(value = "UPDATE pokoje SET czy_lista_hoteli = false WHERE id = :pokojID", nativeQuery = true)
    void updateListaHoteliFalse(Integer pokojID);

    @Query(value = "SELECT l.* FROM listy_hoteli l JOIN miasta_wycieczek m ON l.miasto_wycieczki = m.id WHERE l.pokoj = :ID ORDER BY m.nr_nocy", nativeQuery = true)
    List<ListyHoteli> getListeHoteli(Integer ID);

    @Query(value = "SELECT * FROM klienci k WHERE k.pokoj = :ID", nativeQuery = true)
    List<Klienci> getKlienciPokoju(Integer ID);

}
