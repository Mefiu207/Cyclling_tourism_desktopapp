package com.project.springbootjavafx.services;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.TypyWycieczek;
import com.project.springbootjavafx.repositories.TypyWycieczekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypyWycieczekService extends AbstractServices<TypyWycieczek, String>{

    private TypyWycieczekRepository typy_wycieczekRepository;

    @Autowired
    public TypyWycieczekService(TypyWycieczekRepository typy_wycieczekRepository) {

        super(typy_wycieczekRepository, TypyWycieczek.class, String.class);
        this.typy_wycieczekRepository = typy_wycieczekRepository;

    }

    @Override
    public TypyWycieczek add(TypyWycieczek typy_wycieczek) {

        if (typy_wycieczekRepository.existsByTyp(typy_wycieczek.getTyp())) {
            throw new DuplicatedEntityExceptionn("Wycieczka o typie " + typy_wycieczek.getTyp() + " już istnieje");
        }
        else if (typy_wycieczek.getTyp().trim().length() != 3 && typy_wycieczek.getTyp().trim().length() != 2) {
            throw new WrongCodeLengthException("Za krótka lub za długa nazwa typu (mają być 3 albo 2 znaki)");
        }

        return typy_wycieczekRepository.save(typy_wycieczek);
    }
}
