package pl.chief.cookbook.service;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.IngredientNotFoundException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;


    public boolean addIngredient(Ingredient ingredient) {
        if (ingredientRepository.findByName(ingredient.getName()).isPresent()) {
            return false;
        } else {
            ingredientRepository.save(ingredient);
            return true;
        }
    }

    public List<Ingredient> findAllIngredients() {
        return new ArrayList<>(ingredientRepository.findAll());
    }

    public Ingredient findIngredientByName(String name) {
        return ingredientRepository.findByName(name).orElseThrow(IngredientNotFoundException::new);
    }

    public List<Ingredient> findIngredientsByCategory(IngredientCategory ingredientCategory) {
        return ingredientRepository.findByIngredientCategory(ingredientCategory);
    }

    public boolean updateIngredient(Ingredient ingredient, int ingredientId) {
        Ingredient existingIngredient = ingredientRepository.findById(ingredientId).orElseThrow(IngredientNotFoundException::new);
        existingIngredient.setName(ingredient.getName());
        existingIngredient.setUnit(ingredient.getUnit());
        existingIngredient.setIngredientCategory(ingredient.getIngredientCategory());
        ingredientRepository.save(existingIngredient);
        return true;
    }

    public boolean deleteIngredient(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
        return true;
    }

    public boolean deleteIngredientById(int ingredientId) {
        Ingredient existingIngredient = ingredientRepository.findById(ingredientId).orElseThrow(IngredientNotFoundException::new);
        return deleteIngredient(existingIngredient);
    }

    public List<Ingredient> findIngredientsByRecipe(Recipe recipe){
        return ingredientRepository.findByRecipes(recipe);
    }
}
