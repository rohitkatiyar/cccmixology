-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.7.15-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema cbr
--

CREATE DATABASE IF NOT EXISTS cbr;
USE cbr;

--
-- Definition of table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
CREATE TABLE `ingredients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `baseName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ingredients`
--

/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
INSERT INTO `ingredients` (`id`,`name`,`baseName`) VALUES 
 (1,'White rum','Rum'),
 (2,'Rum','Rum'),
 (3,'Dark rum','Rum'),
 (4,'Amber rum','Rum'),
 (5,'Malibu rum','Rum');
/*!40000 ALTER TABLE `ingredients` ENABLE KEYS */;


--
-- Definition of table `recipe`
--

DROP TABLE IF EXISTS `recipe`;
CREATE TABLE `recipe` (
  `recipeId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `preparation` varchar(500) NOT NULL,
  PRIMARY KEY (`recipeId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `recipe`
--

/*!40000 ALTER TABLE `recipe` DISABLE KEYS */;
INSERT INTO `recipe` (`recipeId`,`title`,`preparation`) VALUES 
 (1,'Ti\'punch','Step - 1 : Wash a lime, remove top and bottom, and thinly slice into half rings. Add lime pieces to glass and crush with the flat end of a barspoon.\nStep - 2 : Add rum and cane syrup, top with broken ice, and muddle together. Add a short straw, and serve.\n'),
 (2,'Exotic Cocktail passion fruit','Step - 1 : In a shaker, combine syrup curacao syrup, passion fruit and lemon juice.\nStep - 2 : Shake well and pour the mixture into a container.\nStep - 3 : Stir liter of lemonade and mix well.\nStep - 4 : Add 5 ice cubes and serve.\n'),
 (3,'Builder','Step - 1 : Mix a cucumber in a centrifuge, put the lemon juice, the sugar and the icebergs.\nStep - 2 : Serve in a glass decorated of a lemon slice\n'),
 (4,'Raspberry cocktail','Step - 1 : The day before: put the raspberries, lemon slices, 3 glasses of cane sugar syrup, kirsch and half lemonade in a large container and marinate in the fridge.\nStep - 2 : Put the rest of lemonade and bottles of sparkling wine in the fridge.\nStep - 3 : Just before serving:\nStep - 4 : Added to the preparation the day before the rest of lemonade and bottles of Crémant. Taste and add sugar cane if necessary because certain sound more bitter than other crémants.\n'),
 (5,'Cocktail for lovers','Step - 1 : Mix all ingredients (in advance if possible put in the fridge) with a spoon or shaker.\nStep - 2 : Add just before serving raspberries (so they keep their consistency) or one hour before if you require well soaked.\n'),
 (6,'Coconut kiss','Step - 1 : Mix juices, syrups and sour cream in a shaker.\nStep - 2 : Serve in a glass with an ananas slice and a piece of orange\n'),
 (7,'Shot spice','Step - 1 : In a small glass (about 4 cm in diameter and 5 cm high), add 3 mm grenadine syrup.\nStep - 2 : Add 4-6 drops of tabasco, then slowly pour 2-3 cm vodka (cold, preferably), not to mix the ingredients.\nStep - 3 : This cocktail is drunk \"cul-dry.\" It feels relatively little alcohol initially because the grenadine is in the glass. After a few seconds, you feel the spiciness of Tabasco.\n'),
 (8,'Cocktail Martini','Step - 1 : Mix the liquids. Cut the lemon into chunks and add it. Serve chilled with ice.\n');
/*!40000 ALTER TABLE `recipe` ENABLE KEYS */;


--
-- Definition of table `recipe_ing`
--

DROP TABLE IF EXISTS `recipe_ing`;
CREATE TABLE `recipe_ing` (
  `ingId` int(11) NOT NULL AUTO_INCREMENT,
  `recipeId` int(11) NOT NULL,
  `food` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`ingId`),
  KEY `recipeId` (`recipeId`),
  KEY `food` (`food`),
  CONSTRAINT `recipe_ing_ibfk_1` FOREIGN KEY (`recipeId`) REFERENCES `recipe` (`recipeId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `recipe_ing_ibfk_2` FOREIGN KEY (`food`) REFERENCES `ingredients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `recipe_ing`
--

/*!40000 ALTER TABLE `recipe_ing` DISABLE KEYS */;
/*!40000 ALTER TABLE `recipe_ing` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
