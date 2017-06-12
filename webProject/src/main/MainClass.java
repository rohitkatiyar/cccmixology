package main;

import java.util.ArrayList;
import datatype.Recipe;
import query.GeneralizeAndQuery;

public class MainClass {

	public static String getCocktailRecipe(String desiredIngredients, String undesiredIngredients) {

		System.out.println(desiredIngredients);
		System.out.println(undesiredIngredients);

		ArrayList<String> desiredIngList = parseIngredients(desiredIngredients);
		ArrayList<String> undesiredIngList = parseIngredients(undesiredIngredients);

		//query existing recipe
		ArrayList<Recipe> recipeList = queryRecipe(desiredIngList, undesiredIngList);

		//adapt recipe
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
