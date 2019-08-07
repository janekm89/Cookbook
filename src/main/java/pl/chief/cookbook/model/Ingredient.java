package pl.chief.cookbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Setter
@Getter
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("ingredientName")
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.ORDINAL)
    @Column(length = 10)
    private MeasurementUnit unit;
    @JsonIgnoreProperties({"Ingredients in Recipe", "Ingredients - Amounts"})
    @JsonProperty("Recipes containing")
    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit=" + unit +
                ", ingredientCategory=" + ingredientCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id;
    }


}