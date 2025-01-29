package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.ListaNocyHoteli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaNocyHoteliRepository extends JpaRepository<ListaNocyHoteli, Integer> {

    List<ListaNocyHoteli> findByPokojId(Integer pokojId);

}
