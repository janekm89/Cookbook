package pl.chief.cookbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chief.cookbook.exception.EntityAlreadyExistException;
import pl.chief.cookbook.exception.IngredientNotFoundException;
import pl.chief.cookbook.exception.WrongNameException;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;
import pl.chief.cookbook.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

import static pl.chief.cookbook.validation.CommonTraitsValidator.validName;

@Service
public class IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;


    public void addIngredient(Ingredient ingredient) {
        if (ingredientRepository.findByName(ingredient.getName()).isPresent()) {
            throw new EntityAlreadyExistException(ingredient.getName());
        } else if(!validName(ingredient.getName())){
            throw new WrongNameException(ingredient.getName());}
        else {
            ingredientRepository.save(ingredient);
        }
    }

    public List<Ingredient> findAllIngredients() {
        return new ArrayList<>(ingredientRepository.findAll());
    }

    public Ingredient findIngredientByName(String name) {
        return ingredientRepository.findByName(name).orElseThrow(IngredientNotFoundException::new);
    }

    public Ingredient findIngredientById(int id) {
        return ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);
    }

    public List<Ingredient> findIngredientsByCategory(IngredientCategory ingredientCategory) {
        return ingredientRepository.findByIngredientCategory(ingredientCategory);
    }

    public List<Ingredient> findIngredientsWithNames(String... ingredientNames){
        return ingredientRepository.findByNameIn(ingredientNames);
    }

    public double findIngredientAmountByIngredientIdAndRecipeId(int ingredientId, int recipeId){
        return ingredientRepository.findIngredientAmountInRecipe(ingredientId, recipeId);
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

    public List<String> findAllIngredientNames(){
        return ingredientRepository.findAllIngredientNames();
    }

    public List<String> findAllIngredientNamesByIngredientCategory(IngredientCategory ingredientCategory){
        return ingredientRepository.findAllIngredientNamesByCategory(ingredientCategory);
    }

    public MeasurementUnit findUnitByIngredientName(String name){
        return ingredientRepository.findUnitByIngredientName(name);
    }
}
