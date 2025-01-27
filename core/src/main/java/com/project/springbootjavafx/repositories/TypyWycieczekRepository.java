package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypyWycieczekRepository extends JpaRepository<TypyWycieczek, String> {

    boolean existsByTyp(String s);

    @Query(value = "SELECT * FROM miasta_wycieczek WHERE typ_wycieczki = :typ ORDER BY nr_nocy", nativeQuery = true)
    List<MiastaWycieczek> getMiastaWycieczki(@Param("typ") String typ);
}
