package com.example.recipe.services;

import java.util.Set;

import com.example.recipe.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
