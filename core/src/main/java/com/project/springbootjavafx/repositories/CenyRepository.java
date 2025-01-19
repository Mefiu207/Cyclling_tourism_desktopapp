package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.springbootjavafx.models.Ceny;

@Repository
public interface CenyRepository extends JpaRepository<Ceny, Integer> {

    @Query(value = "SELECT * FROM ceny c WHERE c.typ_wycieczki = :typ_wycieczki", nativeQuery = true)
    Ceny findByTypWycieczki(@Param("typ_wycieczki") String typ_wycieczki);
}
