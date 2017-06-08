package adaptation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import connection.JdbcConnection;

public class Adapt {
	
	ArrayList<Integer> recipeIds = new ArrayList<Integer>();
	ArrayList<String> desIngList = new ArrayList<String>();
	ArrayList<String> undesIngList = new ArrayList<String>();
	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
	Map<Integer, Integer> recScore = new HashMap<Integer, Integer>();
	
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
	
	public void adaptRecipe()
	{
		
	}
	
	public void calculateScore()
	{
		int score = 0;
		
		for(int i=0; i < recipeList.size(); i++)
		{
			score = 0;
			
			for(int j=0; j < desIngList.size(); j++)
			{
				if(recipeList.get(i).getRecipeIng().contains(desIngList.get(j)))
				{
					score = score + 500;
				}
			}
			
			for(int k=0; k < undesIngList.size(); k++)
			{
				if(recipeList.get(i).getRecipeIng().contains(undesIngList.get(k)))
				{
					score = score - 500;
				}
			}
			
			recScore.put(recipeList.get(i).getRecId(), score);
		}
	}

}