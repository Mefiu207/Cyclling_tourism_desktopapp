package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.springbootjavafx.models.Ceny;

@Repository
public interface CenyRepository extends JpaRepository<Ceny, Integer> {

}
