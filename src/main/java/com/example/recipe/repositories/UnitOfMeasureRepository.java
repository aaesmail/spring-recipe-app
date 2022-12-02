package com.example.recipe.repositories;

import java.util.Optional;

import com.example.recipe.domain.UnitOfMeasure;

import org.springframework.data.repository.CrudRepository;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByDescription(String description);
}
