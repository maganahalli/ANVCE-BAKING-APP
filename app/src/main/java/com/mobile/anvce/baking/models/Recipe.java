package com.mobile.anvce.baking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Models Recipe for Retrofit2 library
 *
 * @author Venkatesh Maganahalli
 */

public class Recipe implements Parcelable {

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = new ArrayList<Step>();
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    protected Recipe(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.ingredients, (com.mobile.anvce.baking.models.Ingredient.class.getClassLoader()));
        in.readList(this.steps, (com.mobile.anvce.baking.models.Step.class.getClassLoader()));
        this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Recipe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Recipe withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Recipe withName(String name) {
        this.name = name;
        return this;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Recipe withIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Recipe withSteps(ArrayList<Step> steps) {
        this.steps = steps;
        return this;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Recipe withServings(Integer servings) {
        this.servings = servings;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recipe withImage(String image) {
        this.image = image;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}
