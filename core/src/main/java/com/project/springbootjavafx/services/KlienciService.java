package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Klienci;
import com.project.springbootjavafx.repositories.KlienciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KlienciService extends AbstractServices<Klienci, Integer> {

    private KlienciRepository repository;

    @Autowired
    public KlienciService(KlienciRepository repository) {
        super(repository, Klienci.class, Integer.class);
        this.repository = repository;
    }

    @Override
    public Klienci add(Klienci klient){
        return repository.save(klient);
    }

}
