package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.MiastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiastaService {

    private final MiastaRepository miastaRepository;

    @Autowired
    public MiastaService(MiastaRepository miastaRepository) {
        this.miastaRepository = miastaRepository;
    }

    // Dodaj nowe miasto
    public Miasta addMiasto(Miasta miasto) {
        return miastaRepository.save(miasto);
    }

    // Pobierz wszystkie miasta
    public List<Miasta> getAllMiasta() {
        return miastaRepository.findAll();
    }

    // Pobierz miasto na podstawie nazwy
    public Miasta getMiastoByName(String miasto) {
        return miastaRepository.findById(miasto).orElse(null);
    }

    // Usuń miasto
    public void deleteMiasto(String miasto) {
        miastaRepository.deleteById(miasto);
    }
}