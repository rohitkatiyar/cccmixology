



Specific Search:

	Getting the IngredientID from the Ingredient table

	Desired Ingredient ID == SELECT ID FROM ingredients WHERE Name IN DesiredList
	Undesired Ingredient ID == SELECT ID FROM ingredients WHERE Name IN unDesiredList


	recipeList == SELECT recipeId FROM recipe_ing Where food in DesiredIngredientID AND food NOT IN unDesiredList

	SELECT title , preparation FROM recipe WHERE recipeId in recipeList

Base Ingredient Search:

	Desired Base Ingredient ID List == SELECT ID from Ingredients WHERE baseName IN (SELECT baseName from Ingredients Where Name in DesiredList)

	Un Desired Base Ingredient ID List == SELECT ID from Ingredients WHERE baseName IN (SELECT baseName from Ingredients Where Name in UNDesiredList)

	

