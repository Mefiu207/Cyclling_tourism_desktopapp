package com.project.springbootjavafx.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.springbootjavafx.repositories.CenyRepository;
import com.project.springbootjavafx.models.Ceny;


@Service
public class CenyService extends AbstractServices<Ceny, Integer>{

    private CenyRepository cenyRepository;

    @Autowired
    public CenyService(CenyRepository cenyRepository) {
        super(cenyRepository, Ceny.class, Integer.class);
        this.cenyRepository = cenyRepository;
    }

    @Override
    public Ceny add(Ceny ceny) {
        return cenyRepository.save(ceny);
    }
}
