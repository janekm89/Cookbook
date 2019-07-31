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

    @Query(value = "SELECT DISTINCT * FROM recipe r JOIN recipe_ingredients ri ON r.id=ri.recipe_id WHERE ri.ingr_id IN :listOfIngredientsId GROUP BY r.id HAVING count(ri.ingr_id)= :sizeOfListOfIngredientsId", nativeQuery = true)
    List<Recipe> findByIngredients(@Param("listOfIngredientsId") Collection<Integer> listOfIngredientsId, @Param("sizeOfListOfIngredientsId") int sizeOfListOfIngredientsId);

}
