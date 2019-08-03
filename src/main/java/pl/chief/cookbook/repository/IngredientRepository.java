package pl.chief.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Optional<Ingredient> findByName(String name);

    List<Ingredient> findByIngredientCategory(IngredientCategory category);

    List<Ingredient> findByNameIn(String... ingredientNames);

    List<Ingredient> findByRecipes(Recipe recipe);

    @Query(value = "select name from Ingredient order by name")
    List<String> findAllIngredientNames();

    @Query(value = "select name from Ingredient where ingredientCategory = :category order by name")
    List<String> findAllIngredientNamesByCategory(@Param(value = "category") IngredientCategory category);

    @Query(nativeQuery = true, value = "select amount from recipe_ingredients where ingr_id =:ingredientId and recipe_id =:recipeId")
    double findIngredientAmountInRecipe(@Param(value = "ingredientId") int ingredientId,
                                        @Param(value = "recipeId") int recipeId);
}
