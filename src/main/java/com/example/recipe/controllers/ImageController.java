package com.example.recipe.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.example.recipe.commands.RecipeCommand;
import com.example.recipe.services.ImageService;
import com.example.recipe.services.RecipeService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/recipe/{recipeId}/image")
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public String showUploadForm(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", this.recipeService.findCommandById(Long.parseLong(recipeId)));

        return "recipe/imageuploadform";
    }

    @PostMapping
    public String handleImagePost(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file) {
        this.imageService.saveImageFile(Long.parseLong(recipeId), file);

        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("/show")
    public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = this.recipeService.findCommandById(Long.parseLong(recipeId));

        if (recipeCommand.getImage() != null) {
            byte[] byteArray = new byte[recipeCommand.getImage().length];

            int i = 0;

            for (Byte wrappedByte : recipeCommand.getImage()) {
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");

            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
