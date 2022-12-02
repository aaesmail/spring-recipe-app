package com.example.recipe.services;

import java.io.IOException;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repositories.RecipeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("Received a file!");

        try {
            Recipe recipe = this.recipeRepository.findById(recipeId).get();

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);

            this.recipeRepository.save(recipe);

        } catch (IOException e) {
            log.error("Failed to save Image");

            e.printStackTrace();
        }
    }
}
