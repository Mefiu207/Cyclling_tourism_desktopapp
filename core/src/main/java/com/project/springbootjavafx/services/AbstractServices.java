package com.project.springbootjavafx.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;

import com.project.springbootjavafx.utils.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;


//  Klasa abstrakcyjna którą dziedziczą wszystkie klasy @Service co umozliwia tworzenie ogólnych przycisków do wyświetlania i usuwania
public abstract class AbstractServices<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final Class<T> domainClass;
    protected final Class<ID> idClass;

    /**
     *
     * @param repository Repozytorium jakie service ma przechowywać
     * @param domainClass Typ.class typ jaki repozytorium przechowuje
     * @param idClass ID.class typ jakiego ID jest w dany repozytorium
     */
    public AbstractServices(JpaRepository<T, ID> repository, Class<T> domainClass, Class<ID> idClass) {
        this.repository = repository;
        this.domainClass = domainClass;
        this.idClass = idClass;
    }

    // Dodaje rekord do bazy. Do nadpisania w każdej klasie pochodnej
    public abstract T add(T model);

    public Class<ID> getIdClass(){
        return idClass;
    }

    // Zwraca liste pol co daje mozliwosc uogolnienia klasy przycisk
    public ArrayList<Pair<String, String>> getFieldsTypes(){
        ArrayList<Pair<String, String>> fieldInfo = new ArrayList<>();

        Field[] fields = domainClass.getDeclaredFields();


        for(Field field : fields){
            // Sprawdzenie, czy pole ma adnotację relacyjną
            if(field.isAnnotationPresent(OneToMany.class) ||
                    field.isAnnotationPresent(ManyToOne.class) ||
                    field.isAnnotationPresent(OneToOne.class) ||
                    field.isAnnotationPresent(ManyToMany.class)){
                continue; // Pominięcie pól relacyjnych
            }

            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            fieldInfo.add(new Pair<>(fieldName, fieldType));
        }

        return fieldInfo;
    }

    // Zwraca wszystkie rekordy z danej tabeli
    public List<T> getAll(){
        return repository.findAll();
    }

    // Zwraca rekord po ID
    public T getById(ID id){
        return repository.findById(id).get();
    }

    // Usuwa rekord
    public void delete(ID id){
        repository.deleteById(id);
    };

}

