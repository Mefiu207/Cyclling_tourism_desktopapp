package com.project.springbootjavafx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.springbootjavafx.models.Pokoje;
import org.springframework.stereotype.Repository;

@Repository
public interface PokojeRepository extends JpaRepository<Pokoje, Integer> {
}
