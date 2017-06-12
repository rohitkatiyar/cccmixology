package adaptation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitTest {
	
	Map<Integer, Integer> recScore = new HashMap<Integer, Integer>();
	ArrayList<String> desIngList = new ArrayList<String>();
	ArrayList<String> undesIngList = new ArrayList<String>();
	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	Map<Integer, ArrayList<String>> missingDesiredIngList = new HashMap<Integer, ArrayList<String>>();
	Map<Integer, ArrayList<String>> haveUndesiredIngList = new HashMap<Integer, ArrayList<String>>();
	Map<Integer, Map<String,String>> replacementMap = new HashMap<Integer, Map<String,String>>();
	Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList = new HashMap<Integer, ArrayList<String>>();
	
	public static void main(String args[])
	{
		UnitTest ut = new UnitTest();
		
		ut.startTest();
	}
	
	public void startTest()
	{
		populateRecipes();
		populateDesiredUndesiredIngredients();
		
		System.out.println(desIngList);
		System.out.println(undesIngList);
		System.out.println(recipeList);
		
		Adapt adapt = new Adapt();
		
		adapt.getAllRecipeIngredientNames(recipeList, mapRecipeIngredientNamesList);
		
		adapt.calculateScore(desIngList, undesIngList, recipeList, recScore, 
				missingDesiredIngList, haveUndesiredIngList,mapRecipeIngredientNamesList);
		
		System.out.println("SCORE LIST::" + recScore);
		System.out.println("Missing Desired Ingredient List" + missingDesiredIngList);
		System.out.println("Have UnDesired Ingredient List" + haveUndesiredIngList);
		
		ArrayList<String> substitutionList = new ArrayList<String>();
		
		substitutionList = adapt.findSubstitution("Tequila");
		System.out.println("SUBSTITUTION LIST1::" + substitutionList);
		
		substitutionList = adapt.findSubstitution("papaya");
		System.out.println("SUBSTITUTION LIST2::" + substitutionList);
		
		for(Recipe rec : recipeList)
		{
			adapt.adaptRecipe(rec, 
				missingDesiredIngList, 
				haveUndesiredIngList, 
				replacementMap,
				mapRecipeIngredientNamesList);
		}
		
		System.out.println("ADAPTATION::" + replacementMap);
		System.out.println("Final Replaced Ingredients List::" + mapRecipeIngredientNamesList);
	}
	
	public void populateDesiredUndesiredIngredients()
	{
		desIngList.add("vodka");
		desIngList.add("rum");
		desIngList.add("blueberries");
		
		undesIngList.add("gin");
		undesIngList.add("strawberries");
		undesIngList.add("whisky");
	}
	
	public void populateRecipes()
	{
		ArrayList<Ingredient> addIng = new ArrayList<Ingredient>();
		Ingredient ig1 = new Ingredient(1, "vodka", 2, "d");
		Ingredient ig2 = new Ingredient(2, "blueberries", 2, "d");
		Ingredient ig3 = new Ingredient(3, "raspberries", 2, "d");
		addIng.add(ig1);
		addIng.add(ig2);
		addIng.add(ig3);
		Recipe recipe1 = new Recipe(101,addIng);
		recipeList.add(recipe1);
		
		addIng = new ArrayList<Ingredient>();
		ig1 = new Ingredient(1, "vodka", 2, "d");
		ig2 = new Ingredient(4, "rum", 2, "d");
		ig3 = new Ingredient(5, "gin", 2, "d");
		addIng.add(ig1);
		addIng.add(ig2);
		addIng.add(ig3);		
		
		Recipe recipe2 = new Recipe(102,addIng);
		recipeList.add(recipe2);
		
		addIng = new ArrayList<Ingredient>();
		ig1 = new Ingredient(1, "vodka", 2, "d");
		ig2 = new Ingredient(4, "rum", 2, "d");
		ig3 = new Ingredient(2, "blueberries", 2, "d");
		Ingredient ig4 = new Ingredient(6, "whiskey", 2, "d");
		addIng.add(ig1);
		addIng.add(ig2);
		addIng.add(ig3);
		addIng.add(ig4);
		
		Recipe recipe3 = new Recipe(103,addIng);
		recipeList.add(recipe3);
		
		addIng = new ArrayList<Ingredient>();
		ig1 = new Ingredient(1, "vodka", 2, "d");
		ig2 = new Ingredient(2, "raspberries", 2, "d");
		addIng.add(ig1);
		addIng.add(ig2);
				
		Recipe recipe4 = new Recipe(104,addIng);
		recipeList.add(recipe4);
		
		addIng = new ArrayList<Ingredient>();
		ig1 = new Ingredient(1, "vodka", 2, "d");
		ig2 = new Ingredient(5, "gin", 2, "d");
		ig3 = new Ingredient(7, "strawberries", 2, "d");
		ig4 = new Ingredient(6, "whiskey", 2, "d");
		addIng.add(ig1);
		addIng.add(ig2);
		addIng.add(ig3);
		addIng.add(ig4);
		
		Recipe recipe5 = new Recipe(105,addIng);
		recipeList.add(recipe5);
	}

}
