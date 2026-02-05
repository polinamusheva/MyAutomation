package com.example.my_automation.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseService<T, ID> {

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T findById(ID id) {
        Optional<T> findById = getRepository().findById(id);
        if (findById.isPresent()) {
            return findById.get();
        }
        throw new EntityNotFoundException(MessageFormat.format("Entity {0} not found", id));
    }

    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    public void flush() {
        getRepository().flush();
    }

    public abstract JpaRepository<T, ID> getRepository();

}
