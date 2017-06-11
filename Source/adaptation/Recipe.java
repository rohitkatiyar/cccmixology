package adaptation;

import java.util.ArrayList;

public class Recipe {
	private int recId;
	private ArrayList<Ingredient> ingredients;
	private String steps;
	
	public Recipe(int recId) {
		super();
		this.recId = recId;
	}
	
	public Recipe(int recId, ArrayList<Ingredient> ingredients) {
		super();
		this.recId = recId;
		this.ingredients = ingredients;
	}
	
	public Recipe(int recId, ArrayList<Ingredient> ingredients, String steps) {
		super();
		this.recId = recId;
		this.ingredients = ingredients;
		this.steps = steps;
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
