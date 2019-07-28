package pl.chief.cookbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.chief.cookbook.features.RecipeCategory;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("recipeName")
    private String name;
    @Column(name = "descr")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private RecipeCategory recipeCategory;
    @JsonIgnoreProperties(value = "Recipes containing")
    @JsonProperty("Ingredients in Recipe")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingr_id", referencedColumnName = "id"))
    private Set<Ingredient> ingredients;
    @ElementCollection
    @MapKeyColumn(name = "ingr_id")
    @Column(name = "amount")
    @JoinTable(name = "recipe_ingredients")
    @JsonProperty("Ingredients - Amounts")
    private Map<Integer, Double> ingredientsAmount;
    private int calories;
