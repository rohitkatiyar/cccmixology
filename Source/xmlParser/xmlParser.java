package xmlParser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import connection.JdbcConnection;

import java.util.*;

public class xmlParser {
	public static void main(String args[]) {
		try {
			int id=101,ingId = 201;
			HashMap<String,Integer> ingList = new HashMap<String,Integer>();
			
			Connection connection = JdbcConnection.getConnection();
			
			PreparedStatement initializeStmt = connection.prepareStatement("DROP TABLE IF EXISTS RECIPE_ING");
			initializeStmt.execute();
			
			initializeStmt = connection.prepareStatement("DROP TABLE IF EXISTS RECIPE");
			initializeStmt.execute();
			
			initializeStmt = connection.prepareStatement("DROP TABLE IF EXISTS INGREDIENTS");
			initializeStmt.execute();
			
			initializeStmt = connection.prepareStatement("USE COMPUTERCOOKING");
			initializeStmt.execute();
			
			initializeStmt = connection.prepareStatement("CREATE TABLE Recipe(recipeId INT NOT NULL,title VARCHAR(250) NOT NULL,preparation VARCHAR(1500) NOT NULL,PRIMARY KEY(recipeId));");
			initializeStmt.execute();
			
			initializeStmt = connection.prepareStatement("CREATE TABLE INGREDIENTS(ingId INT NOT NULL,name VARCHAR(50) NOT NULL,baseName VARCHAR(50),PRIMARY KEY(ingId));");
			initializeStmt.execute();
			
			initializeStmt = connection.prepareStatement("CREATE TABLE Recipe_Ing(recingId INT AUTO_INCREMENT NOT NULL, recId INT NOT NULL,igId INT NOT NULL,quantity float,unit VARCHAR(15),PRIMARY KEY(recingId),FOREIGN KEY(igId) REFERENCES Ingredients(ingId));");
			initializeStmt.execute();
			
			String query = "INSERT INTO RECIPE (RECIPEID, TITLE, PREPARATION) VALUES (?, ?, ?)";
			String queryIng = "INSERT INTO INGREDIENTS (INGID, NAME, BASENAME) VALUES (?, ?, ?)";
			String queryRecIng = "INSERT INTO  Recipe_Ing(RECID, IGID, QUANTITY, UNIT) VALUES (?, ?, ?, ?)";
			PreparedStatement statement=connection.prepareStatement(query);
			PreparedStatement statementIng=connection.prepareStatement(queryIng);
			PreparedStatement statementRecIng=connection.prepareStatement(queryRecIng);
			
			File xmlFile = new File("resources\\ccc_cocktails2.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			//	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("recipe");

			//System.out.println(nList.getLength());
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				String recipeTitle = "";
				String recipePreparation = "";
				Node nNode = nList.item(temp);

				//	System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					recipeTitle = eElement.getElementsByTagName("title").item(0).getTextContent();
					//ingredients
					NodeList ingredientsList = eElement.getElementsByTagName("ingredient");

					for(int tempIng = 0; tempIng < ingredientsList.getLength(); tempIng++) {
						Node nIngredients = ingredientsList.item(tempIng);

						if (nIngredients.getNodeType() == Node.ELEMENT_NODE) {

							Element eIngredients = (Element) nIngredients;

							String ingName = eIngredients.getAttribute("food");
							double ingQuan = Double.parseDouble(eIngredients.getAttribute("quantity"));
							String ingUnit = eIngredients.getAttribute("unit");
							
							System.out.print("Food : " + ingName +";  ");
							System.out.print("Quantity : " + ingQuan +";  ");
							System.out.print("Unit : " + ingUnit +";  ");
							System.out.println();
							
							ingName = ingName.toLowerCase();
							
							String baseName = getBaseName(ingName);
							
							if(!ingList.containsKey(ingName))
							{
								ingList.put(ingName, ingId);
								statementIng.setInt(1,ingId);
								ingId++;
								statementIng.setString(2, ingName);
								statementIng.setString(3, baseName);
								statementIng.execute();
								
							}
							
							//Fill recipe_ing table
							statementRecIng.setInt(1, id);
							statementRecIng.setInt(2, ingList.get(ingName));
							statementRecIng.setDouble(3, ingQuan);
							statementRecIng.setString(4, ingUnit);
							statementRecIng.execute();	
						}

					}
					//preparation
					NodeList prepList = eElement.getElementsByTagName("step");
					StringBuilder sb = new StringBuilder();
					for(int tempPrep = 0; tempPrep < prepList.getLength(); tempPrep++) {
					Node nPrep = prepList.item(tempPrep);

					if (nPrep.getNodeType() == Node.ELEMENT_NODE) {

						Element ePrep = (Element) nPrep;

						sb.append("Step - "+ (tempPrep+1) + " : " + ePrep.getTextContent()+"\n");

					}
					}
					recipePreparation = sb.toString().replaceAll("}}", "");					
				}
				statement.setInt(1, id);
				id++;
				statement.setString(2, recipeTitle);
				statement.setString(3, recipePreparation);
				statement.execute();
			}
			
			connection.close();  
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static String getBaseName(String name)
	{
		String baseName = null;
		
		if(name.indexOf("rum") != -1)
		{
			baseName = "rum";
		}
		else if(name.indexOf("syrup") != -1)
		{
			baseName = "syrup";
		}
		else if(name.indexOf("wine") != -1)
		{
			baseName = "wine";
		}
		else if(name.indexOf("juice") != -1 || name.indexOf("lemonade") != -1)
		{
			baseName = "juice";
		}
		else if(name.indexOf("ice") != -1)
		{
			baseName = "ice";
		}
		else if(name.indexOf("sugar") != -1)
		{
			baseName = "sugar";
		}
		else if(name.indexOf("stout") != -1 || name.indexOf("beer") != -1)
		{
			baseName = "beer";
		}
		else if((name.indexOf("kumquat") != -1 || name.indexOf("litchi") != -1) 
				&& name.indexOf("juice") == -1)
		{
			baseName = "fruit";
		}
		else if(name.indexOf("berry") != -1 && name.indexOf("juice") == -1)
		{
			baseName = "berry";
		}
		else if(name.indexOf("liqueur") != -1)
		{
			baseName = "liqueur";
		}
		
		return baseName;
		
	}

}
