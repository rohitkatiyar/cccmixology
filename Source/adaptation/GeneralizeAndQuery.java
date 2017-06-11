package adaptation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.JdbcConnection;

public class GeneralizeAndQuery {

	private static boolean allValidIng = true;

	public static String generalizeIngredients(String ingredient) {
		String generalizedIng = "";
		Connection connection = null;
		try {
			connection = JdbcConnection.getConnection();
			String query = "SELECT * from ingredients where name='"+ingredient+"';";
			PreparedStatement initialQuery = connection.prepareStatement(query);
			ResultSet rs = initialQuery.executeQuery();
			while(rs.next()) {
				generalizedIng = rs.getString("baseName");
			}
			if(generalizedIng == null) {
				generalizedIng = ingredient;
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return generalizedIng;

	}

	public static ArrayList<String> generalizedIngList(ArrayList<String> ingList) {
		ArrayList<String> genIngList = new ArrayList<String>();

		return genIngList;
	}

	/* get Generalized Recipe List */
	public static ArrayList<Recipe> getGeneralizedRecipeList(ArrayList<String> desiredIngList, ArrayList<String> undesiredIngList) {

		return null;
	}


	public static ArrayList<Integer> getIngIdList(ArrayList<String> ingList) {
		Connection connection= null;
		ArrayList<Integer> ingIdList = new ArrayList<Integer>();
		try {
			for(String ing: ingList) {
				String query = "SELECT * from ingredients where name='"+ing+"';";
				connection = JdbcConnection.getConnection();
				PreparedStatement initialQuery = connection.prepareStatement(query);
				ResultSet rs = initialQuery.executeQuery();
				Integer id = null;
				while(rs.next()) {
					id = rs.getInt("ingId");					
				}
				if(id == null) {
					id = -1;
					allValidIng = false;
				}
				ingIdList.add(id);
			}
			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}	
		return ingIdList;
	}


	/* get Recipe List */
	public static ArrayList<Recipe> getRecipeList(ArrayList<Integer> desiredIngIdList, ArrayList<Integer> undesiredIngIdList) {
		ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
		ArrayList<Integer> recipeIdList = new ArrayList<Integer>();
		String desiredQuery = "SELECT *, count(*) c from recipe_ing where igId=";
		for(int i=0; i< desiredIngIdList.size(); i++) {
			desiredQuery += desiredIngIdList.get(i);
			if(i <desiredIngIdList.size()-1) {
				desiredQuery += "  OR igId= ";
			}
		}
		desiredQuery += " GROUP BY recId HAVING c="+desiredIngIdList.size() +";";
		System.out.println(desiredQuery);


		String undesiredQuery = "SELECT *, count(*) c from recipe_ing where igId =";
		for(int i=0; i< undesiredIngIdList.size(); i++) {
			undesiredQuery += undesiredIngIdList.get(i);
			if(i < undesiredIngIdList.size()-1) {
				undesiredQuery += " OR igId= ";
			}
		}
		undesiredQuery += " GROUP BY recId;";
		System.out.println(undesiredQuery);

		Connection connection = null;
		try {
			connection = JdbcConnection.getConnection();
			PreparedStatement prepStmt = connection.prepareStatement(desiredQuery);
			ResultSet rs = prepStmt.executeQuery();

			while(rs.next()) {
				int recId = rs.getInt("recId");
				if(!recipeIdList.contains(recId)) {
					recipeIdList.add(recId);
				}
			}

			//remove recipe
			prepStmt = connection.prepareStatement(undesiredQuery);

			rs = prepStmt.executeQuery();

			while(rs.next()) {
				int recId = rs.getInt("recId");
				if(recipeIdList.contains(recId)) {
					System.out.println(recId);
					recipeIdList.remove(new Integer(recId));
				}
			}


			//convert recipeIdList to Araylist of recipes
			for(int id: recipeIdList) {
				Recipe recipe = new Recipe(id);
				
				// get ingredients from recipe_ing
				ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
				//
				String ingQuery = "Select * from recipe_ing where recId="+id+";";
				prepStmt = connection.prepareStatement(ingQuery);
				rs = prepStmt.executeQuery();
				while(rs.next()) {
					int ingId = rs.getInt("igId");
					double ingQuantity = rs.getDouble("quantity");
					String ingUnit = rs.getString("unit");
					String ingName = null;
					
					String igQ = "Select * from ingredients where ingId="+ingId+";";
					PreparedStatement prepStmtN = connection.prepareStatement(igQ);
					ResultSet rsN = prepStmtN.executeQuery();
					while(rsN.next()) {
						ingName = rsN.getString("name");
					}
					
					Ingredient ing = new Ingredient(ingId, ingName, ingQuantity, ingUnit);
					ingredients.add(ing);
				}				
				recipe.setIngredients(ingredients);
				
				// get preparation for recipe
				String prepQuery = "Select * from recipe where recipeId="+id+";";
				prepStmt = connection.prepareStatement(prepQuery);
				rs = prepStmt.executeQuery();
				while(rs.next()) {
					recipe.setSteps(rs.getString("preparation"));
				}
				
				recipeList.add(recipe);
			}

			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println(recipeList.size());
		return recipeList;
	}



	public static void main(String args[]) {
		ArrayList<String> desiredIngList = new ArrayList<String>();
		ArrayList<String> undesiredIngList = new ArrayList<String>();

		desiredIngList.add("white rum");
		desiredIngList.add("lime");

		undesiredIngList.add("lemon juice");
		undesiredIngList.add("coca-cola");

		// get id for ingredients - desired and undesired 
		ArrayList<Integer> desiredIngIdList = getIngIdList(desiredIngList);
		ArrayList<Integer> undesiredIngIdList = getIngIdList(undesiredIngList);

		ArrayList<Recipe> recipeList = null;

		// get recipes that exactly match user request
		if(allValidIng) {
			recipeList = getRecipeList(desiredIngIdList, undesiredIngIdList);

			System.out.println("Result:");
			for(Recipe recipe: recipeList) {
				System.out.println(recipe.toString());
			}
		}
		
		// generalize ingredients and get recipes if recipe list is empty or not all ingredients are valid
		/*if(recipeList.isEmpty()) {
			ArrayList<String> generalizeDesiredIng = generalizedIngList(desiredIngList);
			ArrayList<String> generalizeUndesiredIng = generalizedIngList(undesiredIngList);

			ArrayList<Recipe> generalizedRecipeList = getGeneralizedRecipeList(generalizeDesiredIng, generalizeUndesiredIng);
		}
		 */



	}
}
