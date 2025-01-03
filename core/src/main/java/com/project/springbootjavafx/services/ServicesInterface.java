package com.project.springbootjavafx.services;

import java.util.List;

//  Interfejs który implementują wszystkie klasy @Service co umozliwia tworzenie ogólnych przycisków w klasie CustomButton
public interface ServicesInterface<T, ID> {

    T add(T model);

    List<T> getAll();

    T getById(ID id);

    void delete(ID id);

}
