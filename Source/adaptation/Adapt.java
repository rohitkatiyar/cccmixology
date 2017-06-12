package adaptation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import connection.JdbcConnection;

public class Adapt {
	
	//ArrayList<Integer> recipeIds = new ArrayList<Integer>();
	static ArrayList<String> desIngList = new ArrayList<String>(); // Pending to populate from Front end
	static ArrayList<String> undesIngList = new ArrayList<String>(); // Pending to populate from Front end
	
	static ArrayList<Integer> desIngIDsList = new ArrayList<Integer>();
	static ArrayList<Integer> undesIngIDsList = new ArrayList<Integer>();
	
	static ArrayList<Recipe> recipeList = new ArrayList<Recipe>(); // Populate from database
	
	static Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList = new HashMap<Integer, ArrayList<String>>();
	
	Map<Integer, Integer> recScore = new HashMap<Integer, Integer>();// Recipe Id, Score
	Map<Integer, ArrayList<String>> missingDesiredIngList = new HashMap<Integer, ArrayList<String>>();
	Map<Integer, ArrayList<String>> haveUndesiredIngList = new HashMap<Integer, ArrayList<String>>();
	
	// Map to record the adaptation for each recipe, will be used while giving the output XML
	Map<Integer, Map<String,String>> replacementMap = new HashMap<Integer, Map<String,String>>();
	
	public static void main(String args[])
	{
		Adapt obj = new Adapt();
		
		desIngIDsList = GeneralizeAndQuery.getIngIdList(desIngList);
		undesIngIDsList = GeneralizeAndQuery.getIngIdList(undesIngList);
		
		recipeList = GeneralizeAndQuery.getRecipeList(desIngIDsList, undesIngIDsList);
		
		obj.getAllRecipeIngredientNames(recipeList, mapRecipeIngredientNamesList);
	}
	
	public void adaptRecipe(Recipe recipe, 
			Map<Integer, ArrayList<String>> missingDesiredIngList,
			Map<Integer, ArrayList<String>> haveUndesiredIngList,
			Map<Integer, Map<String,String>> replacementMap,
			Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList)
	{
		// substitutionList contains list of ingredients the which can be replaced with the desired one
		ArrayList<String> newIngList, substitutionList, currentIngList;
		Map<String, String> mapReplace;
		
		if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == true && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == true)
		{
			// Recipe has all Desired Ingredients
			// Recipe has no Undesired Ingredients
			// Perfect Recipe, No Adaptation
			mapReplace = new HashMap<String, String>();
			mapReplace.put("PERFECT", "RECIPE");
			
			replacementMap.put(recipe.getRecId(), mapReplace);
		}
		else if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == false && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == true)
		{
			// Recipe has missing Desired Ingredients
			// Recipe has no Undesired Ingredients
			// Close to perfect Recipe, adapt by adding the missing ingredient by replacing with other
			
			boolean isReplaced = false;
			
			// Map to save the details of replacement
			mapReplace = new HashMap<String, String>();
			
			for(String ing : missingDesiredIngList.get(recipe.getRecId()))
			{
				isReplaced = false;
				substitutionList = new ArrayList<String>();
				substitutionList = findSubstitution(ing);
				
				currentIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
				for(String recipeIng : currentIngList)
				{
					if(substitutionList.contains(recipeIng) == true && desIngList.contains(recipeIng) == false)
					{
						newIngList = new ArrayList<String>();
						newIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
						Collections.replaceAll(newIngList, recipeIng, ing);
						mapRecipeIngredientNamesList.put(recipe.getRecId(), newIngList);
						isReplaced = true;
						
						mapReplace.put(recipeIng, ing);
						break;
					}
				}
				
				if(isReplaced == false)
				{
					newIngList = new ArrayList<String>();
					newIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
					newIngList.add(ing);
					mapRecipeIngredientNamesList.put(recipe.getRecId(), newIngList);
					
					mapReplace.put("ADDED", ing);
				}
			}
			
			recipe.setAdapted(true);
			
			replacementMap.put(recipe.getRecId(), mapReplace);
		}
		else if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == true && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == false)
		{
			// Recipe has all Desired Ingredients
			// Recipe has Undesired Ingredients
			// Close to perfect Recipe, adapt by removing Undesired Ingredient(Remove from steps too)
			mapReplace = new HashMap<String, String>();
			
			newIngList = new ArrayList<String>();
			newIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
			
			for(String ing : haveUndesiredIngList.get(recipe.getRecId()))
			{
				newIngList.remove(ing);
				mapReplace.put("REMOVE", ing);
			}
			
			mapRecipeIngredientNamesList.put(recipe.getRecId(), newIngList);
			
			recipe.setAdapted(true);
			
			replacementMap.put(recipe.getRecId(), mapReplace);
		}
		else if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == false && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == false)
		{
			// Recipe has missing Desired Ingredients
			// Recipe has Undesired Ingredients
			// Adapt by adding Desired Ingredient through replacement and Removing Undesired Ingredient
			boolean isReplaced = false;
			mapReplace = new HashMap<String, String>();
			
			for(String ing : missingDesiredIngList.get(recipe.getRecId()))
			{
				isReplaced = false;
				substitutionList = new ArrayList<String>();
				substitutionList = findSubstitution(ing);
				
				currentIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
				for(String recipeIng : currentIngList)
				{
					if(substitutionList.contains(recipeIng) == true && desIngList.contains(recipeIng) == false)
					{
						newIngList = new ArrayList<String>();
						newIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
						Collections.replaceAll(newIngList, recipeIng, ing);
						mapRecipeIngredientNamesList.put(recipe.getRecId(), newIngList);
						
						mapReplace.put(recipeIng, ing);
						break;
					}
				}
				
				if(isReplaced == false)
				{
					newIngList = new ArrayList<String>();
					newIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
					newIngList.add(ing);
					mapRecipeIngredientNamesList.put(recipe.getRecId(), newIngList);
					
					mapReplace.put("ADDED", ing);
				}
			}
			
			newIngList = new ArrayList<String>();
			newIngList = mapRecipeIngredientNamesList.get(recipe.getRecId());
			
			for(String ing : haveUndesiredIngList.get(recipe.getRecId()))
			{
				if(newIngList.contains(ing))
				{
					newIngList.remove(ing);
					mapReplace.put("REMOVE", ing);
				}
			}
			
			mapRecipeIngredientNamesList.put(recipe.getRecId(), newIngList);
			recipe.setAdapted(true);
			
			replacementMap.put(recipe.getRecId(), mapReplace);
		}
	}
	
	public void calculateScore(
			ArrayList<String> desIngList, 
			ArrayList<String> undesIngList, 
			ArrayList<Recipe> recipeList, 
			Map<Integer, Integer> recScore, 
			Map<Integer, ArrayList<String>> missingDesiredIngList, 
			Map<Integer, ArrayList<String>> haveUndesiredIngList,
			Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList)
	{
		int score = 0;
		ArrayList<String> aMisDesIngList, aHaveUndesIngList;
		
		//mapRecipeIngredientNamesList = new HashMap<Integer, ArrayList<String>>();
		//getAllRecipeIngredientNames(recipeList, mapRecipeIngredientNamesList);
		
		for(int i=0; i < recipeList.size(); i++)
		{
			score = 0;
			aMisDesIngList = new ArrayList<String>();
			aHaveUndesIngList = new ArrayList<String>();
			
			for(int j=0; j < desIngList.size(); j++)
			{
				if(mapRecipeIngredientNamesList.get(recipeList.get(i).getRecId()).contains(desIngList.get(j)))
				{
					score = score + 500;
				}
				else
				{
					aMisDesIngList.add(desIngList.get(j));
				}
			}
			
			for(int k=0; k < undesIngList.size(); k++)
			{
				if(mapRecipeIngredientNamesList.get(recipeList.get(i).getRecId()).contains(undesIngList.get(k)))
				{
					score = score - 500;
					aHaveUndesIngList.add(undesIngList.get(k));
				}
			}
			
			recScore.put(recipeList.get(i).getRecId(), score);
			missingDesiredIngList.put(recipeList.get(i).getRecId(), aMisDesIngList);
			haveUndesiredIngList.put(recipeList.get(i).getRecId(), aHaveUndesIngList);
		}
	}

	public ArrayList<String> findSubstitution(String ingredient)
	{
		ArrayList<String> substitutionList = new ArrayList<String>();
		Connection connection = null;
		String result = null;
		String[] ingArr = null;
		
		try 
		{
			connection = JdbcConnection.getConnection();
			String query = "Select Substitution from substitution where Name='" + ingredient + "';";
			PreparedStatement initialQuery = connection.prepareStatement(query);
			
			ResultSet rs = initialQuery.executeQuery();
			
			while(rs.next())
			{
				result = rs.getString("Substitution");
			}
			
			if(result != null)
			{
				ingArr = result.split(",");
			}
			
			for(String ing : ingArr)
			{
				substitutionList.add(ing.trim());
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return substitutionList;
	}
	
	public void getAllRecipeIngredientNames(ArrayList<Recipe> recipeList,
			Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList)
	{
		ArrayList<String> ingNames;
		for(Recipe rec : recipeList)
		{
			ingNames = new ArrayList<String>();
			for(Ingredient ing : rec.getIngredients())
			{
				ingNames.add(ing.getName());
			}
			
			mapRecipeIngredientNamesList.put(rec.getRecId(), ingNames);
		}
	}
}
