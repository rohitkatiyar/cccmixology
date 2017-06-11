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
	
	ArrayList<Integer> recipeIds = new ArrayList<Integer>();
	ArrayList<String> desIngList = new ArrayList<String>();
	ArrayList<String> undesIngList = new ArrayList<String>();
	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	Map<Integer, Integer> recScore = new HashMap<Integer, Integer>();// Recipe Id, Score
	Map<Integer, ArrayList<String>> missingDesiredIngList = new HashMap<Integer, ArrayList<String>>();
	Map<Integer, ArrayList<String>> haveUndesiredIngList = new HashMap<Integer, ArrayList<String>>();
	
	public static void main(String args[])
	{
		Adapt obj = new Adapt();
		obj.fetchRecipe();
	}
	
	public void fetchRecipe()
	{
		Connection connection = null;
		try {
			connection = JdbcConnection.getConnection();
			String query = "SELECT DISTINCT(recId) from recipe_ing where igId IN (Select ingId from ingredients where name IN('white rum', 'cane syrup'));";
			PreparedStatement initialQuery = connection.prepareStatement(query);
			
			ResultSet rs = initialQuery.executeQuery();
			
			while(rs.next())
			{
				int recId = rs.getInt("recId");
				
				System.out.println(recId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Recipe adaptRecipe(Recipe recipe, 
			Map<Integer, ArrayList<String>> missingDesiredIngList,
			Map<Integer, ArrayList<String>> haveUndesiredIngList)
	{
		ArrayList<String> newIngList, substitutionList, currentIngList;
		//Map<String, ArrayList<String>> mapSubstitution;
		
		if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == true && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == true)
		{
			// Recipe has all Desired Ingredients
			// Recipe has no Undesired Ingredients
			// Perfect Recipe, No Adaptation
			
			
			return recipe;
		}
		else if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == false && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == true)
		{
			// Recipe has missing Desired Ingredients
			// Recipe has no Undesired Ingredients
			// Close to perfect Recipe, adapt by adding the missing ingredient by replacing with other
			
			//mapSubstitution = new HashMap<String, ArrayList<String>>();
			
			for(String ing : missingDesiredIngList.get(recipe.getRecId()))
			{
				//mapSubstitution.put(ing, findSubstitution(ing));
				substitutionList = new ArrayList<String>();
				substitutionList = findSubstitution(ing);
				
				currentIngList = recipe.getRecipeIng();
				for(String recipeIng : currentIngList)
				{
					if(substitutionList.contains(recipeIng) == true && desIngList.contains(recipeIng) == false)
					{
						newIngList = new ArrayList<String>();
						newIngList = recipe.getRecipeIng();
						Collections.replaceAll(newIngList, recipeIng, ing);
						recipe.setRecipeIng(newIngList);
						break;
					}
				}
			}
			
			recipe.setAdapted(true);
			
			return recipe;
		}
		else if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == true && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == false)
		{
			// Recipe has all Desired Ingredients
			// Recipe has Undesired Ingredients
			// Close to perfect Recipe, adapt by removing Undesired Ingredient(Remove from steps too)
			
			newIngList = new ArrayList<String>();
			newIngList = recipe.getRecipeIng();
			
			for(String ing : haveUndesiredIngList.get(recipe.getRecId()))
			{
				newIngList.remove(ing);
			}
			
			recipe.setRecipeIng(newIngList);
			
			recipe.setAdapted(true);
			
			return recipe;
			
		}
		else if(missingDesiredIngList.get(recipe.getRecId()).isEmpty() == false && haveUndesiredIngList.get(recipe.getRecId()).isEmpty() == false)
		{
			// Recipe has missing Desired Ingredients
			// Recipe has Undesired Ingredients
			// Adapt by adding Desired Ingredient through replacement and Removing Undesired Ingredient
			
			for(String ing : missingDesiredIngList.get(recipe.getRecId()))
			{
				substitutionList = new ArrayList<String>();
				substitutionList = findSubstitution(ing);
				
				currentIngList = recipe.getRecipeIng();
				for(String recipeIng : currentIngList)
				{
					if(substitutionList.contains(recipeIng) == true && desIngList.contains(recipeIng) == false)
					{
						newIngList = new ArrayList<String>();
						newIngList = recipe.getRecipeIng();
						Collections.replaceAll(newIngList, recipeIng, ing);
						recipe.setRecipeIng(newIngList);
						break;
					}
				}
			}
			
			newIngList = new ArrayList<String>();
			newIngList = recipe.getRecipeIng();
			
			for(String ing : haveUndesiredIngList.get(recipe.getRecId()))
			{
				if(newIngList.contains(ing))
				{
					newIngList.remove(ing);
				}
			}
			
			recipe.setRecipeIng(newIngList);
			recipe.setAdapted(true);
			
			return recipe;
		}
		
		return null;
	}
	
	public void calculateScore(
			ArrayList<String> desIngList, 
			ArrayList<String> undesIngList, 
			ArrayList<Recipe> recipeList, 
			Map<Integer, Integer> recScore, 
			Map<Integer, ArrayList<String>> missingDesiredIngList, 
			Map<Integer, ArrayList<String>> haveUndesiredIngList)
	{
		int score = 0;
		ArrayList<String> aMisDesIngList, aHaveUndesIngList;
		
		for(int i=0; i < recipeList.size(); i++)
		{
			score = 0;
			aMisDesIngList = new ArrayList<String>();
			aHaveUndesIngList = new ArrayList<String>();
			
			for(int j=0; j < desIngList.size(); j++)
			{
				if(recipeList.get(i).getRecipeIng().contains(desIngList.get(j)))
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
				if(recipeList.get(i).getRecipeIng().contains(undesIngList.get(k)))
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
}
