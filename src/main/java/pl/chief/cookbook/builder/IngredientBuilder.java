package pl.chief.cookbook.builder;

import pl.chief.cookbook.features.IngredientCategory;
import pl.chief.cookbook.features.MeasurementUnit;
import pl.chief.cookbook.model.Ingredient;

public class IngredientBuilder {
    private Ingredient ingredient;

    public IngredientBuilder(){
        ingredient = new Ingredient();
    }

    public IngredientBuilder withName(String name){
        ingredient.setName(name);
        return this;
    }

    public IngredientBuilder withUnit(MeasurementUnit unit){
        ingredient.setUnit(unit);
        return this;
    }

    public IngredientBuilder withUnit(String unit){
        switch (unit.toLowerCase()){
            case "gram":
                return this.withUnit(MeasurementUnit.GRAM);
            case "kilogram":
                return this.withUnit(MeasurementUnit.KILOGRAM);
            case "litr": case "liter":
                return this.withUnit(MeasurementUnit.LITER);
            case "mililitr": case "mililiter":
                return this.withUnit(MeasurementUnit.MILILITER);
            case "szt": case "pcs":
                return this.withUnit(MeasurementUnit.PCS);
        }
        return this;
    }

    public IngredientBuilder withCategory(IngredientCategory category){
        ingredient.setIngredientCategory(category);
        return this;
    }

    public IngredientBuilder withCategory(String category){
        switch (category.toLowerCase()){
            case "owoce": case "fruits":
                return this.withCategory(IngredientCategory.FRUITS);
            case "warzywa": case "vegetables":
                return this.withCategory(IngredientCategory.VEGETABLES);
            case "mięso": case "meat":
                return this.withCategory(IngredientCategory.MEAT);
            case "nabiał": case "dairy": case "jajka": case "eggs":
                return this.withCategory(IngredientCategory.DAIRY_AND_EGGS);
            case "makaron": case "kasza": case "ryż": case "pasta": case "grits": case "rice":
                return this.withCategory(IngredientCategory.PASTA_GRITS_RICE);
            case "mąka": case "flour":
                return this.withCategory(IngredientCategory.FLOUR);
            case "zioła": case "herbs":
                return this.withCategory(IngredientCategory.HERBS);
            case "alkohole": case "alcohol":
                return this.withCategory(IngredientCategory.ALCOHOL);
            case "bulion": case "broth":
                return this.withCategory(IngredientCategory.BROTH);
            case "ciasta": case "cakes":
                return this.withCategory(IngredientCategory.CAKE_SUPPLEMENTS);
            case "grzyby": case "mushrooms":
                return this.withCategory(IngredientCategory.MUSHROOMS);
            case "inne": case "other":
                return this.withCategory(IngredientCategory.OTHER);
            case "miód": case "honey":
                return this.withCategory(IngredientCategory.HONEY);
            case "napoje": case "drinks":
                return this.withCategory(IngredientCategory.DRINKS);
            case "ocet": case "vinegar":
                return this.withCategory(IngredientCategory.VINEGAR);
            case "pieczywo": case "bread":
                return this.withCategory(IngredientCategory.BREAD);
            case "przetwory": case "preparations":
                return this.withCategory(IngredientCategory.PREPARATIONS);
            case "przyprawy": case "spices":
                return this.withCategory(IngredientCategory.SPICES);
            case "sos": case "sauce": case "paste":
                return this.withCategory(IngredientCategory.SAUCE_PASTE);
            case "tłuszcz": case "fat":
                return this.withCategory(IngredientCategory.FAT);
            case "nasiona": case "ziarna": case "seeds": case "sprouts":
                return this.withCategory(IngredientCategory.SEEDS_SPROUTS);
        }
        return this;
    }

    public Ingredient createIngredient(){
        return ingredient;
    }
}
