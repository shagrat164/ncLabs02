CREATE DATABASE  IF NOT EXISTS `itrain` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `itrain`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: itrain
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `routes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dep_id` int(11) NOT NULL COMMENT 'отправление',
  `arr_id` int(11) NOT NULL COMMENT 'прибытие',
  PRIMARY KEY (`id`),
  KEY `dep_idx` (`dep_id`),
  KEY `arr_idx` (`arr_id`),
  CONSTRAINT `arr` FOREIGN KEY (`arr_id`) REFERENCES `stations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dep` FOREIGN KEY (`dep_id`) REFERENCES `stations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes` DISABLE KEYS */;
INSERT INTO `routes` VALUES (1,1,2),(2,1,3),(3,2,4),(4,1,5),(5,1,6),(6,1,7),(7,1,8);
/*!40000 ALTER TABLE `routes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `train_num` int(10) NOT NULL,
  `route_id` int(10) NOT NULL,
  `date` datetime NOT NULL,
  `hour` int(10) NOT NULL,
  `min` int(2) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `route_idx` (`route_id`),
  KEY `train_numx` (`train_num`),
  CONSTRAINT `route` FOREIGN KEY (`route_id`) REFERENCES `routes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `train` FOREIGN KEY (`train_num`) REFERENCES `trains` (`number`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (9,1000,1,'2016-12-14 15:00:00',4,0),(10,1000,1,'2016-12-14 21:00:00',4,0),(11,1000,1,'2016-12-15 15:00:00',4,0),(12,1000,1,'2016-12-15 21:00:00',4,0),(13,1000,1,'2016-12-16 15:00:00',4,0),(14,1000,1,'2016-12-16 21:00:00',4,0),(15,1001,2,'2016-12-14 15:00:00',4,0),(16,1001,2,'2016-12-14 18:00:00',4,0),(17,1001,2,'2016-12-16 15:00:00',4,0),(18,1001,2,'2016-12-14 18:00:00',4,0),(19,1002,3,'2016-12-14 15:00:00',4,0),(20,1002,3,'2016-12-21 18:00:00',18,0),(21,1005,4,'2016-12-15 15:00:00',4,0),(22,1005,4,'2016-12-24 15:00:00',4,0),(23,1030,7,'2016-12-14 15:00:00',14,0);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stations`
--

DROP TABLE IF EXISTS `stations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stations`
--

LOCK TABLES `stations` WRITE;
/*!40000 ALTER TABLE `stations` DISABLE KEYS */;
INSERT INTO `stations` VALUES (3,'АТКАРСК-1'),(4,'БАЛАКОВО'),(5,'БАЛАШОВ'),(11,'ВОЛАПРВСК'),(10,'ВОЛБСК'),(8,'ВОЛЬСК'),(1,'САРАТОВ-1'),(6,'СЕННАЯ'),(7,'ТАРХАНЫ'),(2,'ТАТИЩЕВО');
/*!40000 ALTER TABLE `stations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trains`
--

DROP TABLE IF EXISTS `trains`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trains` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trains`
--

LOCK TABLES `trains` WRITE;
/*!40000 ALTER TABLE `trains` DISABLE KEYS */;
INSERT INTO `trains` VALUES (1,1000),(2,1001),(3,1002),(4,1003),(5,1004),(6,1005),(7,1006),(8,1007),(9,1008),(10,1009),(12,1020),(13,1025),(11,1030);
/*!40000 ALTER TABLE `trains` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-14 15:33:54
