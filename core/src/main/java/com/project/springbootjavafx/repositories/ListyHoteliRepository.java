package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.ListyHoteliKey;
import com.project.springbootjavafx.models.Pokoje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListyHoteliRepository extends JpaRepository<ListyHoteli, ListyHoteliKey> {



    @Modifying
    @Query(value = "DELETE FROM listy_hoteli WHERE pokoj = :pokojID", nativeQuery = true)
    void deleteByPokoj(@Param("pokojID")Integer pokojID);

}
