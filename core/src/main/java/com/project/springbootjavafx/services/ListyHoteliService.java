package com.project.springbootjavafx.services;

import com.project.springbootjavafx.models.ListyHoteli;
import com.project.springbootjavafx.models.ListyHoteliKey;
import com.project.springbootjavafx.repositories.ListyHoteliRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
public class ListyHoteliService extends AbstractServices<ListyHoteli, ListyHoteliKey> {

    private ListyHoteliRepository repository;

    @Autowired
    public ListyHoteliService(ListyHoteliRepository repository) {
        super(repository, ListyHoteli.class, ListyHoteliKey.class);
        this.repository = repository;
    }

    @Override
    public ListyHoteli add(ListyHoteli entity) {
        return repository.save(entity);
    }

}
