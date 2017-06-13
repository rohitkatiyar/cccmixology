package datatype;

import java.util.ArrayList;

public class Recipe {
	private int recId;
	private ArrayList<Ingredient> ingredients;
	private String steps;
	private String title;
	private boolean isAdapted;
	
	public Recipe() {
		
	}
	
	public Recipe(int recId) {
		super();
		this.recId = recId;
		this.isAdapted = false;
	}
	
	public Recipe(int recId, ArrayList<Ingredient> ingredients) {
		super();
		this.recId = recId;
		this.ingredients = ingredients;
		this.isAdapted = false;
	}
	
	public Recipe(int recId, ArrayList<Ingredient> ingredients, String steps) {
		super();
		this.recId = recId;
		this.ingredients = ingredients;
		this.steps = steps;
		this.isAdapted = false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAdapted() {
		return isAdapted;
	}

	public void setAdapted(boolean isAdapted) {
		this.isAdapted = isAdapted;
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public String getSteps() {
		return steps;
	}
	
	public void setSteps(String steps) {
		this.steps = steps;
	}
	
	@Override
	public String toString() {
		return "Recipe [ID : " + recId + "\nIngredients:" + ingredients.toString() + "\nPreparation:\n "+steps + "\n]";
	}
	
}
