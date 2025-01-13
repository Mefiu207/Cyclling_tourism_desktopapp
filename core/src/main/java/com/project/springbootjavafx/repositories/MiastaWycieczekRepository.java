package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.MiastaWycieczek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiastaWycieczekRepository extends JpaRepository<MiastaWycieczek, Integer> {
}
