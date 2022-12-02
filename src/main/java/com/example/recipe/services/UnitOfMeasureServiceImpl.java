package com.example.recipe.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.recipe.commands.UnitOfMeasureCommand;
import com.example.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.example.recipe.repositories.UnitOfMeasureRepository;

import org.springframework.stereotype.Service;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository,
            UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {

        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(this.unitOfMeasureRepository.findAll().spliterator(), false)
                .map(this.unitOfMeasureToUnitOfMeasureCommand::convert)
                .collect(Collectors.toSet());
    }
}
