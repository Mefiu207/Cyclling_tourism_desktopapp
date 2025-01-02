package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.MiastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiastaService implements ServicesInterface <Miasta, String>{

    private final MiastaRepository miastaRepository;

    @Autowired
    public MiastaService(MiastaRepository miastaRepository) {
        this.miastaRepository = miastaRepository;
    }

    @Override
    public Miasta add(Miasta miasto) {
        return miastaRepository.save(miasto);
    }

    @Override
    public List<Miasta> getAll() {
        return miastaRepository.findAll();
    }

    @Override
    public Miasta getById(String miasto) {return miastaRepository.findById(miasto).get(); }

    @Override
    public void delete(String miasto) { miastaRepository.deleteById(miasto); }
}