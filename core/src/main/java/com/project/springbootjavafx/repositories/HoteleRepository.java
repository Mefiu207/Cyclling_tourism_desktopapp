package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.springbootjavafx.models.Hotele;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoteleRepository extends JpaRepository<Hotele, String> {

    boolean existsByKod(String kod);

    @Query(value = "SELECT * FROM hotele h WHERE h.miasto = :miasto", nativeQuery = true)
    List<Hotele> getHoteleByMiasto(@Param("miasto")String miasto);
}

