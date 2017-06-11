package adaptation;

import java.util.ArrayList;

public class Recipe {
	private int recId;
	boolean isAdapted;
	private ArrayList<String> recipeIng;
	
	public Recipe(int recId, ArrayList<String> recipeIng) {
		super();
		this.recId = recId;
		this.isAdapted = false;
		this.recipeIng = recipeIng;
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
