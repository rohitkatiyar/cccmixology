package adaptation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitTest {
	
	Map<Integer, Integer> recScore = new HashMap<Integer, Integer>();
	ArrayList<String> desIngList = new ArrayList<String>();
	ArrayList<String> undesIngList = new ArrayList<String>();
	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	
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
		
		adapt.calculateScore(desIngList, undesIngList, recipeList, recScore);
		
		System.out.println(recScore);
	}
	
	public void populateDesiredUndesiredIngredients()
	{
		desIngList.add("vodka");
		desIngList.add("rum");
		desIngList.add("blueberry");
		
		undesIngList.add("gin");
		undesIngList.add("strawberry");
		undesIngList.add("whisky");
	}
	
	public void populateRecipes()
	{
		ArrayList<String> addIng = new ArrayList<String>();
		
		addIng.add("vodka");
		addIng.add("blueberry");
		addIng.add("raspberry");
		Recipe recipe1 = new Recipe(101,addIng);
		recipeList.add(recipe1);
		
		addIng = new ArrayList<String>();
		addIng.add("vodka");
		addIng.add("rum");
		addIng.add("gin");
		Recipe recipe2 = new Recipe(102,addIng);
		recipeList.add(recipe2);
		
		addIng = new ArrayList<String>();
		addIng.add("vodka");
		addIng.add("rum");
		addIng.add("blueberry");
		addIng.add("whisky");
		Recipe recipe3 = new Recipe(103,addIng);
		recipeList.add(recipe3);
		
		addIng = new ArrayList<String>();
		addIng.add("vodka");
		addIng.add("gin");
		addIng.add("whisky");
		addIng.add("strawberry");
		Recipe recipe4 = new Recipe(104,addIng);
		recipeList.add(recipe4);
		
		addIng = new ArrayList<String>();
		addIng.add("vodka");
		addIng.add("raspberry");
		Recipe recipe5 = new Recipe(105,addIng);
		recipeList.add(recipe5);
	}

}
