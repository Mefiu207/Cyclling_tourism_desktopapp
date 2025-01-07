package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.springbootjavafx.models.Hotele;

public interface HoteleRepository extends JpaRepository<Hotele, String> {

    boolean existsByKod(String kod);
}

