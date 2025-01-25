package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.repositories.PokojeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.springbootjavafx.models.Pokoje;

import java.util.List;

@Service
public class PokojeService extends AbstractServices<Pokoje, Integer>{

    private PokojeRepository repository;

    @Autowired
    public PokojeService(PokojeRepository repository) {
        super(repository, Pokoje.class, Integer.class);
        this.repository = repository;
    }

    @Override
    public Pokoje add(Pokoje pokoj) {
        return repository.save(pokoj);
    }

    public List<Pokoje> getPokojeWycieczki(Wycieczki wycieczka){
        return repository.getByWycieczka(wycieczka.getWycieczka());
    }

}
