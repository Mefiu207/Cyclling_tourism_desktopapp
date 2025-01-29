package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.ListaNocyHoteli;
import com.project.springbootjavafx.models.Pokoje;
import com.project.springbootjavafx.repositories.ListaNocyHoteliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ListaNocyHoteliService {

    private final ListaNocyHoteliRepository repository;

    @Autowired
    public ListaNocyHoteliService(ListaNocyHoteliRepository repository) {
        this.repository = repository;
    }

    public List<ListaNocyHoteli> getAll() {
        return repository.findAll();
    }

    // Zwraca Listy nocy hoteli dla danego pokoju
    public List<ListaNocyHoteli> getListyByPokoj(Pokoje pokoj){
        return repository.findByPokojId(pokoj.getId());
    }

}
