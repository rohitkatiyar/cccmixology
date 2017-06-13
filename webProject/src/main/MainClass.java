package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adaptation.Adapt;
import datatype.Recipe;
import query.GeneralizeAndQuery;

public class MainClass {

	public static String getCocktailRecipe(String desiredIngredients, String undesiredIngredients) {

		System.out.println(desiredIngredients);
		System.out.println(undesiredIngredients);

		ArrayList<String> desiredIngList = parseIngredients(desiredIngredients);
		ArrayList<String> undesiredIngList = parseIngredients(undesiredIngredients);

		//query existing recipe
		ArrayList<Recipe> recipeList = null;
		recipeList = queryRecipe(desiredIngList, undesiredIngList);

		if(recipeList.size() == 0)
		{
			System.out.println("Exact search returned NULL. Going for generalized search.");
			
			recipeList = GeneralizeAndQuery.getGeneralizedRecipeList(GeneralizeAndQuery.getIngIdList(desiredIngList), 
					GeneralizeAndQuery.getIngIdList(undesiredIngList));
			
			if(recipeList.size() == 0)
			{
				System.out.println("Generalized search returned NULL. Fetching all recipes.");
				
				recipeList = GeneralizeAndQuery.getAllRecipeList();
				
				if(recipeList.size() == 0)
				{
					System.out.println("ERROR: Could not fetch the recipes.");
					return;
				}
			}
		}
		
		static Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList = new HashMap<Integer, ArrayList<String>>();
		
		Map<Integer, Integer> recScore = new HashMap<Integer, Integer>();// Recipe Id, Score
		Map<Integer, ArrayList<String>> missingDesiredIngList = new HashMap<Integer, ArrayList<String>>();
		Map<Integer, ArrayList<String>> haveUndesiredIngList = new HashMap<Integer, ArrayList<String>>();
		
		// Map to record the adaptation for each recipe, will be used while giving the output XML
		Map<Integer, Map<String,String>> replacementMap = new HashMap<Integer, Map<String,String>>();
		
        Adapt adapt = new Adapt();
		
		adapt.getAllRecipeIngredientNames(recipeList, mapRecipeIngredientNamesList);
		
		adapt.calculateScore(desiredIngList, undesiredIngList, recipeList, recScore, 
				missingDesiredIngList, haveUndesiredIngList,mapRecipeIngredientNamesList);
		
		System.out.println("SCORE LIST::" + recScore);
		System.out.println("Missing Desired Ingredient List" + missingDesiredIngList);
		System.out.println("Have UnDesired Ingredient List" + haveUndesiredIngList);
		
		adapt.adaptRecipe(recipeList.get(0), 
				missingDesiredIngList, 
				haveUndesiredIngList, 
				replacementMap,
				mapRecipeIngredientNamesList);
		
		System.out.println("ADAPTATION::" + replacementMap);
		System.out.println("Final Replaced Ingredients List::" + mapRecipeIngredientNamesList);
		
		//Recipe = TODO

		//Convert recipe to xml
		String output = null; // = xmlParser(recipe);

		return output;
	}

	private static ArrayList<String> parseIngredients(String ingredients) {
		ArrayList<String> ingredientsList = new ArrayList<String>();
		String[] splitIngList = ingredients.split("\\|");
		for(int i=0; i<splitIngList.length; i++) {
			ingredientsList.add(splitIngList[i]);
		}

		return ingredientsList;
	}

	private static ArrayList<Recipe> queryRecipe(ArrayList<String> desiredIngList, ArrayList<String> undesiredIngList) {

		ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
		try {
			// get id for ingredients - desired and undesired 
			ArrayList<Integer> desiredIngIdList = GeneralizeAndQuery.getIngIdList(desiredIngList);
			ArrayList<Integer> undesiredIngIdList = GeneralizeAndQuery.getIngIdList(undesiredIngList);


			// get recipes that exactly match user request
			if(GeneralizeAndQuery.allValidIng) {
				recipeList = GeneralizeAndQuery.getRecipeList(desiredIngIdList, undesiredIngIdList);		
			}

			// generalize ingredients and get recipes if recipe list is empty or not all ingredients are valid
			if(recipeList.isEmpty()) {
				System.out.println("Generalizing ingredients and querying");
				ArrayList<Integer> generalizeDesiredIngIdList = GeneralizeAndQuery.getGeneralizedIngIdList(desiredIngList);
				ArrayList<Integer> generalizeUndesiredIngIdList = GeneralizeAndQuery.getGeneralizedIngIdList(undesiredIngList);

				recipeList = GeneralizeAndQuery.getGeneralizedRecipeList(generalizeDesiredIngIdList, generalizeUndesiredIngIdList);
			}

			// if generalized recipe list is still empty return all recipes
			if(recipeList.isEmpty()) {
				recipeList = GeneralizeAndQuery.getAllRecipeList();
			}

			System.out.println("Result:");
			for(Recipe recipe: recipeList) {
				System.out.println(recipe.toString());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return recipeList;
	}

}
