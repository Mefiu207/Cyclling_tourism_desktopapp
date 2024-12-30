package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.Miasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiastaRepository extends JpaRepository<Miasta, String> {
    // Możesz dodać niestandardowe metody, jeśli będą potrzebne
}
