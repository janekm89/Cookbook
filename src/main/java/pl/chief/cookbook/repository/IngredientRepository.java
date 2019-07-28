package pl.chief.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Optional<Ingredient> findByName(String name);
    List<Ingredient> findByIngredientCategory(IngredientCategory category);
}
