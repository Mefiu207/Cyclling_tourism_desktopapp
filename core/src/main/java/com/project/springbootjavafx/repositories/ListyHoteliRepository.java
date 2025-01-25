package com.project.springbootjavafx.repositories;

import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.ListyHoteliKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ListyHoteliRepository extends JpaRepository<ListyHoteli, ListyHoteliKey> {

}
