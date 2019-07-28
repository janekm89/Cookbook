package pl.chief.cookbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("ingredientName")
    private String name;
    @Enumerated(EnumType.ORDINAL)
    @Column(length = 10)
    private MeasurementUnit unit;
    @JsonIgnoreProperties({"Ingredients in Recipe", "Ingredients - Amounts"})
    @JsonProperty("Recipes containing")
    @ManyToMany(mappedBy = "ingredientsAmount", fetch = FetchType.LAZY)
    private List<Recipe> recipes;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category")
    private IngredientCategory ingredientCategory;

    public Ingredient() {
        recipes = new ArrayList<>();
    }

    public Ingredient(String name, MeasurementUnit unit) {
        this();
        this.name = name;
        this.unit = unit;
    }

    public Ingredient(String name, MeasurementUnit unit, IngredientCategory category) {
        this(name, unit);
        this.ingredientCategory = category;
    }
}