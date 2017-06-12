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
        
        xmlWriter.writeRecipe(rootEle,dom);
        
        Element rootReuse = dom.createElement("reuse");
        rootEle.appendChild(rootReuse);

        Element rootAdaptation = dom.createElement("Adaptation");
        
        Map<String,String> mapReplacementForRecipe =  replacementMap.get(recipe.getRecId());
        String replacementString = "";
        
        for(String key : mapReplacementForRecipe.keySet())
        {
        	/*if()
        	{
        		
        	}*/
        }
        
        rootAdaptation.appendChild(dom.createTextNode("replace pineapple juice with apple juice"));
        rootReuse.appendChild(rootAdaptation);

        xmlWriter.writeRecipe(rootReuse,dom);
       

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
    
    public static Element writeRecipe(Element root,Document dom){
        Element rootrecipe = dom.createElement("recipe");
        root.appendChild(rootrecipe);
        
        Element e = dom.createElement("title");
        e.appendChild(dom.createTextNode("Bora Bora"));
        rootrecipe.appendChild(e);
        
        Element rootIngredients = dom.createElement("ingredients");
        rootrecipe.appendChild(rootIngredients);
        
        for (int i=0;i<4;i++){
            Element rootIngredient = dom.createElement("ingredient");
            rootIngredients.appendChild(rootIngredient);
        
            Attr quantity = dom.createAttribute("quantity");
            quantity.setValue("10");
            rootIngredient.setAttributeNode(quantity);

            Attr unit= dom.createAttribute("unit");
            unit.setValue("cl");    
            rootIngredient.setAttributeNode(unit);
            
            Attr food= dom.createAttribute("food");
            food.setValue("pinaple juice");    
            rootIngredient.setAttributeNode(food);
         
            rootIngredient.appendChild(dom.createTextNode("10 cl pineapple juice"));
            rootrecipe.appendChild(rootIngredient);
        }
        
        Element rootPreperation = dom.createElement("preperation");
        rootrecipe.appendChild(rootPreperation);
        
        for (int i=0;i<3;i++){
            Element rootStep = dom.createElement("step");
            rootStep.appendChild(dom.createTextNode("This Recipe has to be made in a shaker"));
            rootPreperation.appendChild(rootStep);
        }
        return root;
    }

}
