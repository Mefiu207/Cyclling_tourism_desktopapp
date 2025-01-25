package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Wycieczki;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.springbootjavafx.models.Pokoje;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokojeRepository extends JpaRepository<Pokoje, Integer> {

    @Query(value = "SELECT * FROM pokoje p WHERE p.wycieczka = :wycieczka", nativeQuery = true)
    List<Pokoje> getByWycieczka(String wycieczka);

}
