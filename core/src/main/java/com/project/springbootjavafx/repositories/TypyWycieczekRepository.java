package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.TypyWycieczek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypyWycieczekRepository extends JpaRepository<TypyWycieczek, String> {

    boolean existsByTyp(String s);
}
