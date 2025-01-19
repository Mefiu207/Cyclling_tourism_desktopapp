package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.MiastaWycieczek;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.repositories.MiastaWycieczekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<MiastaWycieczek> findByTypWycieczki(TypyWycieczek typ){
        if (typ == null) {
            throw new IllegalArgumentException("Typ wycieczki nie może być null");
        }
        return repository.findByTypWycieczki(typ.getTyp());
    }


}
