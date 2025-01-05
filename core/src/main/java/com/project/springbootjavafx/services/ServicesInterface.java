package com.project.springbootjavafx.services;

import java.util.List;
import java.util.ArrayList;

import com.project.springbootjavafx.utils.Pair;

//  Interfejs który implementują wszystkie klasy @Service co umozliwia tworzenie ogólnych przycisków w klasie CustomButton
public interface ServicesInterface<T, ID> {

    // Zwraca liste pol co daje mozliwosc uogolnienia klasy przycisk
    ArrayList<Pair<String, String>> getFieldsTypes();

    // Dodaje rekord do bazy
    T add(T model);

    // Zwraca wszystkie rekordy z danej tabeli
    List<T> getAll();

    // Zwraca rekord po ID
    T getById(ID id);

    // Usuwa rekord
    void delete(ID id);

}
