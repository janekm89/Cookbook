package pl.chief.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.chief.cookbook.features.RecipeCategory;
import pl.chief.cookbook.model.Ingredient;
import pl.chief.cookbook.model.Recipe;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Optional<Recipe> findByName(String name);

    List<Recipe> findByCaloriesBetween(int caloriesMin, int caloriesMax);

    List<Recipe> findByRecipeCategory(RecipeCategory recipeCategory);

    List<Recipe> findDistinctByIngredientsIn(Set<Ingredient> ingredientSet);

    List<Recipe> findByIngredientsContaining(Ingredient ingredient);

    List<Recipe> findByDescription(String name);

    @Query(value = "SELECT r FROM Recipe r JOIN r.ingredients i WHERE i IN :ingredients GROUP BY r HAVING count(i)=:numOfIngredients ")
    List<Recipe> findByIngredients(@Param("ingredients") Collection<Ingredient> ingredients, @Param("numOfIngredients") Long ingredientsSize);

}
