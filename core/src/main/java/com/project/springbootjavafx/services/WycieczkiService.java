package com.project.springbootjavafx.services;

import com.project.springbootjavafx.exceptions.DuplicatedEntityExceptionn;
import com.project.springbootjavafx.exceptions.WrongCodeLengthException;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.repositories.WycieczkiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WycieczkiService extends AbstractServices<Wycieczki, String> {

    private WycieczkiRepository repository;

    @Autowired
    public WycieczkiService(WycieczkiRepository repository) {
        super(repository, Wycieczki.class, String.class);
        this.repository = repository;
    }

    @Override
    public Wycieczki add(Wycieczki wycieczka){
        if(repository.existsByWycieczka(wycieczka.getWycieczka())){
            throw new DuplicatedEntityExceptionn("Wycieczka o kodzie: " + wycieczka.getWycieczka() + " juz istnieje");
        }else if(wycieczka.getWycieczka().trim().length() != 5 && wycieczka.getWycieczka().trim().length() != 4){
            throw new WrongCodeLengthException("Za krótka lub za długa nazwa wycieczki (mają być 5 albo 4 znaki)");
        }
        return repository.save(wycieczka);
    }


}
