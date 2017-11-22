CREATE DATABASE  IF NOT EXISTS `opum` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `opum`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: opum
-- ------------------------------------------------------
-- Server version	5.6.37-log

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
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `Employee_ID` char(9) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `Manager_ID` varchar(9) NOT NULL,
  `Project_Engagement_ID` int(11) DEFAULT NULL,
  `FirstName` varchar(45) DEFAULT NULL,
  `LastName` varchar(45) DEFAULT NULL,
  `MiddleName` varchar(45) DEFAULT NULL,
  `IsAdmin` tinyint(1) NOT NULL DEFAULT '0',
  `FullName` varchar(60) DEFAULT NULL,
  `Password` tinytext,
  `IsActive` tinyint(1) NOT NULL DEFAULT '0',
  `Roll_In_Date` date NOT NULL DEFAULT '0000-00-00',
  `Roll_Off_Date` date NOT NULL DEFAULT '0000-00-00',
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Employee_ID`),
  UNIQUE KEY `Employee_ID_Email_UNIQUE` (`Employee_ID`,`Email`),
  KEY `Project_Engagement_ID_idx` (`Project_Engagement_ID`),
  CONSTRAINT `FK_Project_Engagement_ID` FOREIGN KEY (`Project_Engagement_ID`) REFERENCES `project_engagement` (`Project_Engagement_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('121212PH1','magdanc@ph.ibm.com','123456PH1',NULL,NULL,NULL,NULL,0,'Claude Magdangal',NULL,0,'2012-04-28','2020-09-25','2017-08-16 08:42:23','ADMIN','2017-08-16 08:42:23',NULL),('123456PH1','admin@ph.ibm.com','123456PH1',NULL,NULL,NULL,NULL,1,'Admin','47b82bd4ffff817f1ce3ffffa55bffffd52bffffb44cffffbc446c9348b77e81fffffa06ffff986848b71de237c8',0,'2017-04-28','2018-09-10','2017-08-08 14:19:52','ADMIN','2017-08-08 19:27:13','ADMIN'),('131313PH1','aumana@ph.ibm.com','123456PH1',NULL,NULL,NULL,NULL,0,'Aldaina Auman','1fe02bd4ffffb947619effffac5458a7075aa5ffff966a4ab50714eb2cd322dd4ab56f90',0,'2016-01-30','2025-12-25','2017-08-16 08:42:23','ADMIN','2017-08-16 13:36:15','aumana@ph.ibm.com'),('141414PH1','admin','123456PH1',NULL,NULL,NULL,NULL,1,'admin','20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d',0,'2014-03-22','2022-10-10','2017-10-25 03:23:16','ADMIN','2017-10-25 03:23:16','ADMIN'),('P100XU','dacana@ph.ibm.com','P2XXXXX',NULL,NULL,NULL,NULL,1,'Jay Dacanay',NULL,1,'2017-10-28','3000-01-01','2017-11-14 08:53:49',NULL,NULL,NULL),('P100XX','teodorj@ph.ibm.com','P2XXXXX',NULL,NULL,NULL,NULL,1,'Jom Teodoro',NULL,1,'2016-10-17','3000-01-01','2017-11-14 08:53:49',NULL,NULL,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_project`
--

DROP TABLE IF EXISTS `employee_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_project` (
  `Employee_ID` char(9) NOT NULL,
  `Project_ID` smallint(2) NOT NULL,
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Project_ID` (`Project_ID`),
  KEY `Project_ID_idx` (`Project_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_project`
--

LOCK TABLES `employee_project` WRITE;
/*!40000 ALTER TABLE `employee_project` DISABLE KEYS */;
INSERT INTO `employee_project` VALUES ('123456PH1',1),('141414PH1',2),('121212PH1',4),('121212PH1',5),('131313PH1',5);
/*!40000 ALTER TABLE `employee_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `employee_project_v`
--

DROP TABLE IF EXISTS `employee_project_v`;
/*!50001 DROP VIEW IF EXISTS `employee_project_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee_project_v` AS SELECT 
 1 AS `Employee_ID`,
 1 AS `Name`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `employee_role`
--

DROP TABLE IF EXISTS `employee_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_role` (
  `Employee_ID` char(9) NOT NULL,
  `Role_ID` smallint(2) NOT NULL,
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Role_ID` (`Role_ID`),
  KEY `Role_ID_idx` (`Role_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_role`
--

LOCK TABLES `employee_role` WRITE;
/*!40000 ALTER TABLE `employee_role` DISABLE KEYS */;
INSERT INTO `employee_role` VALUES ('123456PH1',1),('141414PH1',2),('121212PH1',4),('121212PH1',5),('131313PH1',5);
/*!40000 ALTER TABLE `employee_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `employee_role_v`
--

DROP TABLE IF EXISTS `employee_role_v`;
/*!50001 DROP VIEW IF EXISTS `employee_role_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee_role_v` AS SELECT 
 1 AS `Employee_ID`,
 1 AS `Name`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `employee_team`
--

DROP TABLE IF EXISTS `employee_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_team` (
  `Employee_ID` char(9) NOT NULL,
  `Team_ID` smallint(2) NOT NULL,
  PRIMARY KEY (`Employee_ID`),
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Team_ID` (`Team_ID`),
  KEY `Team_ID_idx` (`Team_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_team`
--

LOCK TABLES `employee_team` WRITE;
/*!40000 ALTER TABLE `employee_team` DISABLE KEYS */;
INSERT INTO `employee_team` VALUES ('P100XX',1);
/*!40000 ALTER TABLE `employee_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `employee_team_v`
--

DROP TABLE IF EXISTS `employee_team_v`;
/*!50001 DROP VIEW IF EXISTS `employee_team_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee_team_v` AS SELECT 
 1 AS `Employee_ID`,
 1 AS `Team_ID`,
 1 AS `Name`,
 1 AS `Team_Lead_Employee_ID`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `holiday`
--

DROP TABLE IF EXISTS `holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `holiday` (
  `Holiday_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Date` date DEFAULT NULL,
  `CreateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Holiday_ID`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  UNIQUE KEY `UNQ_Day` (`Date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday`
--

LOCK TABLES `holiday` WRITE;
/*!40000 ALTER TABLE `holiday` DISABLE KEYS */;
/*!40000 ALTER TABLE `holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `manager_employee_v`
--

DROP TABLE IF EXISTS `manager_employee_v`;
/*!50001 DROP VIEW IF EXISTS `manager_employee_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `manager_employee_v` AS SELECT 
 1 AS `Manager_ID`,
 1 AS `Employee_ID`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `Project_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) NOT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT 'ADMIN',
  PRIMARY KEY (`Project_ID`),
  UNIQUE KEY `NAME_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'USAA','2017-08-08 18:41:34','ADMIN',NULL,NULL);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_engagement`
--

DROP TABLE IF EXISTS `project_engagement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_engagement` (
  `Project_Engagement_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Project_ID` int(11) NOT NULL,
  `Employee_ID` char(9) NOT NULL,
  `Start` date DEFAULT NULL,
  `End` date DEFAULT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT 'ADMIN',
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT 'ADMIN',
  PRIMARY KEY (`Project_Engagement_ID`),
  UNIQUE KEY `UNQ_Project_Engagement` (`Project_ID`,`Employee_ID`,`Start`,`End`),
  KEY `Project_ID_idx` (`Project_ID`),
  KEY `Employee_ID_idx` (`Employee_ID`),
  CONSTRAINT `FK_Employee_ID` FOREIGN KEY (`Employee_ID`) REFERENCES `employee` (`Employee_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Project_ID` FOREIGN KEY (`Project_ID`) REFERENCES `project` (`Project_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_engagement`
--

LOCK TABLES `project_engagement` WRITE;
/*!40000 ALTER TABLE `project_engagement` DISABLE KEYS */;
INSERT INTO `project_engagement` VALUES (12,1,'123456PH1','2017-08-01','2018-09-27','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),(14,1,'121212PH1','2017-08-16','2017-08-31','2017-08-16 13:36:15','ADMIN','2017-08-16 14:04:54','ADMIN'),(15,1,'P100XU',NULL,NULL,'2017-11-14 08:53:49','ADMIN','2017-11-14 08:53:49','ADMIN'),(16,1,'P100XX',NULL,NULL,'2017-11-14 08:53:49','ADMIN','2017-11-14 08:53:49','ADMIN');
/*!40000 ALTER TABLE `project_engagement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quarter`
--

DROP TABLE IF EXISTS `quarter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quarter` (
  `Quarter_ID` tinyint(4) NOT NULL,
  `Year_ID` smallint(6) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Start` date NOT NULL,
  `End` date NOT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) NOT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Quarter_ID`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quarter`
--

LOCK TABLES `quarter` WRITE;
/*!40000 ALTER TABLE `quarter` DISABLE KEYS */;
/*!40000 ALTER TABLE `quarter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `Role_ID` int(2) NOT NULL,
  `Name` varchar(20) NOT NULL,
  UNIQUE KEY `Role_ID_UNIQUE` (`Role_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'System Administrator'),(2,'Administrator'),(3,'People Manager'),(4,'Team Lead'),(5,'User');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team` (
  `Team_ID` smallint(3) NOT NULL AUTO_INCREMENT,
  `Project_ID` smallint(3) NOT NULL,
  `Team_Lead_Employee_ID` char(9) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `CreateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Team_ID`),
  UNIQUE KEY `Team_ID_UNIQUE` (`Team_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,1,'123456PH1','Team A','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),(2,1,'131313PH1','Team B','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),(3,1,'131313PH1','Team C','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),(4,1,'1ASDASPH1','Team D','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),(5,1,'121212PH1','Team E','2017-08-16 13:36:15','ADMIN','2017-08-16 14:04:54','ADMIN');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `team_employee_v`
--

DROP TABLE IF EXISTS `team_employee_v`;
/*!50001 DROP VIEW IF EXISTS `team_employee_v`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `team_employee_v` AS SELECT 
 1 AS `Team_ID`,
 1 AS `Name`,
 1 AS `Team_Lead_Employee_ID`,
 1 AS `Employee_ID`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `utilization`
--

DROP TABLE IF EXISTS `utilization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utilization` (
  `Utilization_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Employee_ID` char(9) NOT NULL,
  `Year` smallint(6) NOT NULL,
  `Utilization_JSON` mediumtext,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT 'ADMIN',
  PRIMARY KEY (`Utilization_ID`),
  UNIQUE KEY `UNQ_Utilization` (`Employee_ID`,`Year`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilization`
--

LOCK TABLES `utilization` WRITE;
/*!40000 ALTER TABLE `utilization` DISABLE KEYS */;
INSERT INTO `utilization` VALUES (9,'27',2017,'{\"utilization_JSON\":[{\"month\":8,\"dayOfMonth\":16,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":8,\"dayOfMonth\":17,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":8,\"dayOfMonth\":18,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":8,\"dayOfMonth\":19,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":8,\"dayOfMonth\":20,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":8,\"dayOfMonth\":21,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":8,\"dayOfMonth\":22,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":8,\"dayOfMonth\":23,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":8,\"dayOfMonth\":24,\"editable\":\"\",\"utilizationHours\":\"VL\",\"day\":5},{\"month\":8,\"dayOfMonth\":25,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":8,\"dayOfMonth\":26,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":8,\"dayOfMonth\":27,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":8,\"dayOfMonth\":28,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":8,\"dayOfMonth\":29,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":8,\"dayOfMonth\":30,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":8,\"dayOfMonth\":31,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":1,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":9,\"dayOfMonth\":2,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":3,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":4,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":9,\"dayOfMonth\":5,\"editable\":\"\",\"utilizationHours\":\"SL\",\"day\":3},{\"month\":9,\"dayOfMonth\":6,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":7,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":8,\"editable\":\"\",\"utilizationHours\":\"CDO\",\"day\":6},{\"month\":9,\"dayOfMonth\":9,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":10,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":11,\"editable\":\"\",\"utilizationHours\":\"EL\",\"day\":2},{\"month\":9,\"dayOfMonth\":12,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":9,\"dayOfMonth\":13,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":14,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":15,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":9,\"dayOfMonth\":16,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":17,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":18,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":9,\"dayOfMonth\":19,\"editable\":\"\",\"utilizationHours\":\"OL\",\"day\":3},{\"month\":9,\"dayOfMonth\":20,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":21,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":22,\"editable\":\"\",\"utilizationHours\":\"TR\",\"day\":6},{\"month\":9,\"dayOfMonth\":23,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":24,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":25,\"editable\":\"\",\"utilizationHours\":\"HO\",\"day\":2},{\"month\":9,\"dayOfMonth\":26,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":9,\"dayOfMonth\":27,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":28,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":29,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":9,\"dayOfMonth\":30,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7}],\"year\":2017}','2017-08-16 13:39:40','27','2017-08-16 13:39:40','27');
/*!40000 ALTER TABLE `utilization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `week`
--

DROP TABLE IF EXISTS `week`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `week` (
  `Week_ID` tinyint(4) NOT NULL,
  `Quarter_ID` tinyint(4) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Start` date NOT NULL,
  `End` date NOT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) NOT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Week_ID`),
  UNIQUE KEY `Quarter_Week_UNIQUE` (`Quarter_ID`,`Name`),
  CONSTRAINT `FK_Quarter_ID` FOREIGN KEY (`Quarter_ID`) REFERENCES `quarter` (`Quarter_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `week`
--

LOCK TABLES `week` WRITE;
/*!40000 ALTER TABLE `week` DISABLE KEYS */;
/*!40000 ALTER TABLE `week` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `year`
--

DROP TABLE IF EXISTS `year`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `year` (
  `Year_ID` smallint(6) NOT NULL AUTO_INCREMENT,
  `PUMYear` smallint(6) NOT NULL,
  `End` date NOT NULL,
  `Start` date NOT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) NOT NULL DEFAULT 'ADMIN',
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Year_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `year`
--

LOCK TABLES `year` WRITE;
/*!40000 ALTER TABLE `year` DISABLE KEYS */;
INSERT INTO `year` VALUES (1,2017,'2018-12-31','2017-01-10','2017-08-16 10:32:53','ADMIN','2017-08-16 10:42:24',NULL);
/*!40000 ALTER TABLE `year` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `employee_project_v`
--

/*!50001 DROP VIEW IF EXISTS `employee_project_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_project_v` AS select `employee_project`.`Employee_ID` AS `Employee_ID`,`project`.`Name` AS `Name` from (`employee_project` join `project` on((`employee_project`.`Project_ID` = `project`.`Project_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `employee_role_v`
--

/*!50001 DROP VIEW IF EXISTS `employee_role_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_role_v` AS select `employee_role`.`Employee_ID` AS `Employee_ID`,`role`.`Name` AS `Name` from (`employee_role` join `role` on((`employee_role`.`Role_ID` = `role`.`Role_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `employee_team_v`
--

/*!50001 DROP VIEW IF EXISTS `employee_team_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_team_v` AS select `employee`.`Employee_ID` AS `Employee_ID`,`team`.`Team_ID` AS `Team_ID`,`team`.`Name` AS `Name`,`team`.`Team_Lead_Employee_ID` AS `Team_Lead_Employee_ID` from ((`employee` left join `employee_team` on((`employee_team`.`Employee_ID` = `employee`.`Employee_ID`))) left join `team` on((`team`.`Team_ID` = `employee_team`.`Team_ID`))) order by `employee`.`Employee_ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `manager_employee_v`
--

/*!50001 DROP VIEW IF EXISTS `manager_employee_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `manager_employee_v` AS select `employee`.`Manager_ID` AS `Manager_ID`,`employee`.`Employee_ID` AS `Employee_ID` from `employee` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `team_employee_v`
--

/*!50001 DROP VIEW IF EXISTS `team_employee_v`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `team_employee_v` AS select `team`.`Team_ID` AS `Team_ID`,`team`.`Name` AS `Name`,`team`.`Team_Lead_Employee_ID` AS `Team_Lead_Employee_ID`,`employee`.`Employee_ID` AS `Employee_ID` from ((`team` left join `employee_team` on((`team`.`Team_ID` = `employee_team`.`Team_ID`))) left join `employee` on((`employee`.`Employee_ID` = `employee_team`.`Employee_ID`))) order by `team`.`Team_ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-20 22:39:00
