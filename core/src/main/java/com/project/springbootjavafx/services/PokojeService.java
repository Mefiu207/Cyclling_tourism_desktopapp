package com.project.springbootjavafx.services;


import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.Wycieczki;
import com.project.springbootjavafx.repositories.PokojeRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokojeService extends AbstractServices<Pokoje, Integer>{

    private final PokojeRepository repository;

    @Autowired
    public PokojeService(PokojeRepository repository) {
        super(repository, Pokoje.class, Integer.class);
        this.repository = repository;
    }

    @Override
    public Pokoje add(Pokoje pokoj) {
        return repository.save(pokoj);
    }

    // Zwraca liste pokoi dla danej wycieczki
    public List<Pokoje> getPokojeWycieczki(Wycieczki wycieczka){
        return repository.getByWycieczka(wycieczka.getWycieczka());
    }

    // Zwraca liste pokoi dla wycieczki które maja liste hoteli
    public List<Pokoje> getPokojeZListami(Wycieczki wycieczka){
        return repository.getByWycieczkaAndListaHoteli(wycieczka.getWycieczka());
    }

    @Transactional
    public void setListaHoteliTrue(List<Pokoje> pokoje){
        for(Pokoje pokoj : pokoje){
            repository.updateListaHoteliTrue(pokoj.getId());
        }
    }

    @Transactional
    public void setListaHoteliFalse(List<Pokoje> pokoje){
        for(Pokoje pokoj : pokoje){
            repository.updateListaHoteliFalse(pokoj.getId());
        }
    }

    // Zwraca listy hoteli pokoju
    public List<ListyHoteli> getListyHoteli(Pokoje pokoj){
        return repository.getListeHoteli(pokoj.getId());
    }


    // Zwraca klientow danego pokoju
    public List<Klienci> getKlienci(Pokoje pokoj) { return repository.getKlienciPokoju(pokoj.getId()); }
}
