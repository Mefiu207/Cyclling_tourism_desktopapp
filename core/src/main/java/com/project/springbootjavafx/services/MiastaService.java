package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.MiastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;

@Service
public class MiastaService extends AbstractServices<Miasta, String> {

    // Trzyma 2 kopie tego sameg repisitory aby móc korzystać z metod w miastaRepository
    private MiastaRepository miastaRepository;

    @Autowired
    public MiastaService(MiastaRepository miastaRepository) {
        super(miastaRepository, Miasta.class, String.class);
        this.miastaRepository = miastaRepository;
    }

    @Override
    public Miasta add(Miasta miasto) throws DuplicatedEntityExceptionn {

        // Sprawdzanie czy dodawane miasto już istnieje
        if (miastaRepository.existsByMiasto(miasto.getMiasto())) {
            throw new DuplicatedEntityExceptionn("Miasto " + miasto.getMiasto()+ " już istnieje");
        }

        return miastaRepository.save(miasto);
    }

}