package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Ceny;
import com.project.springbootjavafx.models.MiastaWycieczek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiastaWycieczekRepository extends JpaRepository<MiastaWycieczek, Integer> {

    @Query(value = "SELECT * FROM miasta_wycieczek m WHERE m.typ_wycieczki = :typ_wycieczki", nativeQuery = true)
    List<MiastaWycieczek> findByTypWycieczki(@Param("typ_wycieczki") String typ_wycieczki);
}
