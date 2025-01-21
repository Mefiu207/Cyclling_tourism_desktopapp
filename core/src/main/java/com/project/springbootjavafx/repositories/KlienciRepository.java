package com.project.springbootjavafx.repositories;


import com.project.springbootjavafx.models.Klienci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlienciRepository extends JpaRepository<Klienci, Integer> {
}
