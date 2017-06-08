package adaptation;

import java.util.ArrayList;

public class Recipe {
	private int recId;
	private ArrayList<String> recipeIng;
	
	public Recipe(int recId, ArrayList<String> recipeIng) {
		super();
		this.recId = recId;
		this.recipeIng = recipeIng;
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public ArrayList<String> getRecipeIng() {
		return recipeIng;
	}

	public void setRecipeIng(ArrayList<String> recipeIng) {
		this.recipeIng = recipeIng;
	}

	@Override
	public String toString() {
		return "Recipe [recId=" + recId + ", recipeIng=" + recipeIng + "]";
	}
	
}
