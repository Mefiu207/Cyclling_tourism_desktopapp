package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.repositories.MiastaWycieczekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiastaWycieczekService extends AbstractServices<MiastaWycieczek, Integer>{

    private MiastaWycieczekRepository repository;

    @Autowired
    public MiastaWycieczekService(MiastaWycieczekRepository miastaWycieczekRepository) {
        super(miastaWycieczekRepository, MiastaWycieczek.class, Integer.class);
        this.repository = miastaWycieczekRepository;
    }

    @Override
    public MiastaWycieczek add(MiastaWycieczek miastoWycieczki) {
        return repository.save(miastoWycieczki);
    }


}
