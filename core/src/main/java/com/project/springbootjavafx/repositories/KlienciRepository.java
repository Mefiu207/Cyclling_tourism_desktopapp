package com.project.springbootjavafx.repositories;


import com.project.springbootjavafx.models.Klienci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KlienciRepository extends JpaRepository<Klienci, Integer> {

    @Query(value = "SELECT * FROM klienci WHERE klienci.pokoj = :ID", nativeQuery = true)
    List<Klienci> getKlienciByPokoj(Integer ID);
}
