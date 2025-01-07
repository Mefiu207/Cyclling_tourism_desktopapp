package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Miasta;
import com.project.springbootjavafx.repositories.MiastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import com.project.springbootjavafx.utils.Pair;

import com.project.springbootjavafx.exceptions.DuplicatedMiastoException;

@Service
public class MiastaService implements ServicesInterface <Miasta, String>{

    private final MiastaRepository miastaRepository;

    @Autowired
    public MiastaService(MiastaRepository miastaRepository) {
        this.miastaRepository = miastaRepository;
    }

    @Override
    public Miasta add(Miasta miasto) throws DuplicatedMiastoException{

        // Sprawdzanie czy dodawane miasto już istnieje
        if (miastaRepository.existsByMiasto(miasto.getMiasto())) {
            throw new DuplicatedMiastoException("Miasto " + miasto.getMiasto()+ " już istnieje");
        }

        return miastaRepository.save(miasto);
    }

    @Override
    public List<Miasta> getAll() {
        return miastaRepository.findAll();
    }

    // Pamiętaj by obsłużyc wyjątek mordo
    @Override
    public Miasta getById(String miasto) {return miastaRepository.findById(miasto).get(); }

    @Override
    public void delete(String miasto) { miastaRepository.deleteById(miasto); }

    @Override
    public ArrayList<Pair<String, String>> getFieldsTypes(){
        ArrayList<Pair<String, String>> fieldInfo = new ArrayList<>();

        Field[] fields = Miasta.class.getDeclaredFields();

        for(Field field : fields){
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            fieldInfo.add(new Pair<>(fieldName, fieldType));
        }

        return fieldInfo;
    }
}