package com.example.recipe.repositories;

import java.util.Optional;

import com.example.recipe.domain.Category;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByDescription(String description);
}
