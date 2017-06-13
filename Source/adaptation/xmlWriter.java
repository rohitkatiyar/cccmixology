package adaptation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class xmlWriter {

    public static void writeOuputXml(String urlQuery,
    		                         Recipe recipe,
    		                         Map<Integer, Map<String,String>> replacementMap,
    		                         Map<Integer, ArrayList<String>> mapRecipeIngredientNamesList
    		                         ) 
    {
    Document dom;
    Element e = null;

    // instance of a DocumentBuilderFactory
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
        // use factory to get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
        dom = db.newDocument();
        
        // create the root element
        Element rootEle = dom.createElement("cccAnswer");

        // create data elements and place them under root
        e = dom.createElement("system");
        e.appendChild(dom.createTextNode("http://getCccCocktail.net"));
        rootEle.appendChild(e);

        e = dom.createElement("query");
        e.appendChild(dom.createTextNode(urlQuery));
        rootEle.appendChild(e);
        

        Element rootRetrieve = dom.createElement("retrieve");
        rootEle.appendChild(rootRetrieve);
        
        xmlWriter.writeRecipe(recipe,rootEle,dom);
        
        Element rootReuse = dom.createElement("reuse");
        rootEle.appendChild(rootReuse);

        Element rootAdaptation = dom.createElement("Adaptation");
        
        Map<String,String> mapReplacementForRecipe =  replacementMap.get(recipe.getRecId());
        String replacementString = "";
        
        for(String key : mapReplacementForRecipe.keySet())
        {
        	if(key.contains("ADDED_") == true)
        	{
        		replacementString = replacementString + "Add " + mapReplacementForRecipe.get(key) + ". ";
        	}
        	else if(key.contains("REMOVE_") == true)
        	{
        		replacementString = replacementString + "Remove " + mapReplacementForRecipe.get(key) + ". ";
        	}
        	else if(key.compareTo("PERFECT") == 0)
        	{
        		replacementString = "Perfect recipe. No adaptation required";
        	}
        	else
        	{
        		replacementString = replacementString + "Replace " + key + " with " + mapReplacementForRecipe.get(key) + ". ";
        	}
        }
        
        rootAdaptation.appendChild(dom.createTextNode(replacementString));
        rootReuse.appendChild(rootAdaptation);

        if(recipe.isAdapted())
        {
        	Recipe adaptedRecipe = createTheAdaptedRecipe(recipe, mapReplacementForRecipe);
            xmlWriter.writeRecipe(adaptedRecipe,rootReuse,dom);
        }
       

        dom.appendChild(rootEle);
        xmlWriter.writeXml(dom);

    }
    catch (ParserConfigurationException pce) {
        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
    }
    }
    
    public static void writeXml(Document dom){
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"mixology");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult result = new StreamResult(new File("output.xml"));
            // send DOM to file
            tr.transform(new DOMSource(dom), result);

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        }
    }
    
    public static Element writeRecipe(Recipe recipe, Element root, Document dom){
        Element rootrecipe = dom.createElement("recipe");
        root.appendChild(rootrecipe);
        
        Element e = dom.createElement("title");
        e.appendChild(dom.createTextNode(recipe.getTitle()));
        rootrecipe.appendChild(e);
        
        Element rootIngredients = dom.createElement("ingredients");
        rootrecipe.appendChild(rootIngredients);
        
        for (Ingredient ing : recipe.getIngredients())
        {
            Element rootIngredient = dom.createElement("ingredient");
            rootIngredients.appendChild(rootIngredient);
        
            Attr quantity = dom.createAttribute("quantity");
            quantity.setValue("" + ing.getQuantity());
            rootIngredient.setAttributeNode(quantity);

            Attr unit= dom.createAttribute("unit");
            unit.setValue(ing.getUnit());    
            rootIngredient.setAttributeNode(unit);
            
            Attr food= dom.createAttribute("food");
            food.setValue(ing.getName());    
            rootIngredient.setAttributeNode(food);
         
            rootIngredient.appendChild(dom.createTextNode(ing.getQuantity() + " " + ing.getUnit() + " " + ing.getName()));
            rootrecipe.appendChild(rootIngredient);
        }
        
        Element rootPreperation = dom.createElement("preparation");
        rootrecipe.appendChild(rootPreperation);
        
        String steps[] = recipe.getSteps().split("|");
        
        for (String step : steps)
        {
            Element rootStep = dom.createElement("step");
            rootStep.appendChild(dom.createTextNode(step.trim()));
            rootPreperation.appendChild(rootStep);
        }
        return root;
    }
    
    public static Recipe createTheAdaptedRecipe(Recipe recipe, Map<String,String> mapReplacementForRecipe)
    {
    	int max = 500000;
    	int min = 1000;
    	int random = (int )(Math.random() * max + min);
    	
    	Recipe adaptedRecipe = new Recipe(random);
    	
    	adaptedRecipe.setTitle("Adaptation of " + recipe.getTitle());
    	adaptedRecipe.setSteps(recipe.getSteps());//TODO Have to adapt steps too
    	adaptedRecipe.setAdapted(true);
    	
    	ArrayList<Ingredient> newIngList = new ArrayList<Ingredient>();
    	newIngList = recipe.getIngredients();
    	
    	int ingId = 1000;
    	
    	for(String key : mapReplacementForRecipe.keySet())
        {
        	if(key.contains("ADDED_") == true)
        	{
        		Ingredient newIng = new Ingredient(ingId, mapReplacementForRecipe.get(key),0.0, "");
        		ingId++;
        		newIngList.add(newIng);	
        	}
        	else if(key.contains("REMOVE_") == true)
        	{
        		for(Ingredient in : newIngList)
        		{
        			if(in.getName().equals(mapReplacementForRecipe.get(key)))
        			{
        				newIngList.remove(in);
        				break;
        			}
        		}
        	}
        	else if(key.compareTo("PERFECT") == 0)
        	{
        		
        	}
        	else
        	{
        		for(Ingredient in : newIngList)
        		{
        			if(in.getName().equals(mapReplacementForRecipe.get(key)))
        			{
        				in.setId(ingId);
        				in.setName(mapReplacementForRecipe.get(key));
        				ingId++;
        			}
        		}
        	}
        }
    	return adaptedRecipe;
    }

}
