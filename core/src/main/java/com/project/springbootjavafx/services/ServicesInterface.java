package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.Models;

import java.util.List;

public interface ServicesInterface<T, ID> {

    T add(T model);

    List<T> getAll();

    T getById(ID id);

    void delete(ID id);
}
