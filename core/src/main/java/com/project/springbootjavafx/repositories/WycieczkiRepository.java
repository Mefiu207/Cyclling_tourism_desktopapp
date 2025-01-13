package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.springbootjavafx.models.Wycieczki;
import org.springframework.stereotype.Repository;

@Repository
public interface WycieczkiRepository extends JpaRepository<Wycieczki, String> {

    boolean existsByWycieczka(String nazwa);
}
