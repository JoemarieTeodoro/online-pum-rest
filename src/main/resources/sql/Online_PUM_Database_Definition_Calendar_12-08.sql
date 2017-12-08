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
INSERT INTO `employee` VALUES ('1','pol','1',NULL,NULL,NULL,NULL,NULL,'ffffb54b0affffc43c1de2ffffc23e0630cfffffc739ffffb64affffbe42718efffff0106f90ffffd828ffffff016699',1,'0000-00-00','0000-00-00','2017-11-28 13:14:54',NULL,'2017-12-01 12:20:47',NULL),('123456PH1','admin','123456PH1',NULL,NULL,NULL,NULL,'Admin','20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d',0,'2017-08-08','2020-01-30','2017-08-08 14:19:52','ADMINISTRATOR','2017-08-08 19:27:13','ADMINISTRATOR');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_leave`
--

DROP TABLE IF EXISTS `employee_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_leave` (
  `Employee_Leave_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Employee_ID` varchar(45) NOT NULL,
  `Year_ID` int(11) NOT NULL,
  `Status` varchar(45) DEFAULT NULL,
  `Leave_Date` date NOT NULL,
  `Leave_Type` varchar(45) NOT NULL,
  `CreateDate` timestamp(6) NULL DEFAULT NULL,
  `UpdateDate` timestamp(6) NULL DEFAULT NULL,
  `Hours` int(11) DEFAULT NULL,
  PRIMARY KEY (`Employee_Leave_ID`),
  KEY `HoursIdx` (`Employee_ID`,`Leave_Date`,`Leave_Type`,`Status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_leave`
--

LOCK TABLES `employee_leave` WRITE;
/*!40000 ALTER TABLE `employee_leave` DISABLE KEYS */;
INSERT INTO `employee_leave` VALUES (1,'123456PH1',1,'Approved','2017-01-02','VL','2017-11-28 16:00:00.000000','2017-11-28 16:00:00.000000',NULL),(2,'123456PH1',1,'Pending','2017-01-03','VL',NULL,NULL,NULL),(3,'123456PH1',1,'','2017-01-04','RC',NULL,NULL,10);
/*!40000 ALTER TABLE `employee_leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_leave_history`
--

DROP TABLE IF EXISTS `employee_leave_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_leave_history` (
  `Employee_Leave_Hist_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Employee_Leave_ID` int(11) NOT NULL,
  `Employee_ID` int(11) NOT NULL,
  `Year_ID` int(11) NOT NULL,
  `Status` varchar(45) DEFAULT NULL,
  `Leave_Date` date NOT NULL,
  `Leave_Type` varchar(45) NOT NULL,
  `CreateDate` timestamp(6) NULL DEFAULT NULL,
  `UpdateDate` timestamp(6) NULL DEFAULT NULL,
  PRIMARY KEY (`Employee_Leave_Hist_ID`),
  KEY `Employee_Leave_ID_idx` (`Employee_Leave_ID`),
  CONSTRAINT `Employee_Leave_ID` FOREIGN KEY (`Employee_Leave_ID`) REFERENCES `employee_leave` (`Employee_Leave_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_leave_history`
--

LOCK TABLES `employee_leave_history` WRITE;
/*!40000 ALTER TABLE `employee_leave_history` DISABLE KEYS */;
INSERT INTO `employee_leave_history` VALUES (1,1,1100,1,'Pending','2017-01-02','VL',NULL,NULL),(2,1,1100,1,'Approved','2017-01-02','VL',NULL,NULL),(3,2,1100,1,'Pending','2017-01-03','VL',NULL,NULL);
/*!40000 ALTER TABLE `employee_leave_history` ENABLE KEYS */;
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
INSERT INTO `employee_project` VALUES ('123456PH1',1);
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
INSERT INTO `employee_role` VALUES ('123456PH1',1),('1',3);
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
INSERT INTO `employee_team` VALUES ('123456PH1',1);
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
-- Table structure for table `fy_template`
--

DROP TABLE IF EXISTS `fy_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fy_template` (
  `FY_Template_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Year_ID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Value` varchar(45) DEFAULT NULL,
  `Is_Holiday` tinyint(4) DEFAULT '0',
  `Event_Name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`FY_Template_ID`),
  KEY `HoursIdx` (`Date`,`Value`)
) ENGINE=InnoDB AUTO_INCREMENT=3205 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fy_template`
--

LOCK TABLES `fy_template` WRITE;
/*!40000 ALTER TABLE `fy_template` DISABLE KEYS */;
INSERT INTO `fy_template` VALUES (2489,19,'2017-01-02','8',0,NULL),(2490,19,'2017-01-03','8',0,NULL),(2491,19,'2017-01-04','8',0,NULL),(2492,19,'2017-01-05','8',0,NULL),(2493,19,'2017-01-06','8',0,NULL),(2494,19,'2017-01-07','',0,NULL),(2495,19,'2017-01-08','',0,NULL),(2496,19,'2017-01-09','8',0,NULL),(2497,19,'2017-01-10','8',0,NULL),(2498,19,'2017-01-11','8',0,NULL),(2499,19,'2017-01-12','8',0,NULL),(2500,19,'2017-01-13','8',0,NULL),(2501,19,'2017-01-14','',0,NULL),(2502,19,'2017-01-15','',0,NULL),(2503,19,'2017-01-16','8',0,NULL),(2504,19,'2017-01-17','8',0,NULL),(2505,19,'2017-01-18','8',0,NULL),(2506,19,'2017-01-19','8',0,NULL),(2507,19,'2017-01-20','8',0,NULL),(2508,19,'2017-01-21','',0,NULL),(2509,19,'2017-01-22','',0,NULL),(2510,19,'2017-01-23','8',0,NULL),(2511,19,'2017-01-24','8',0,NULL),(2512,19,'2017-01-25','8',0,NULL),(2513,19,'2017-01-26','8',0,NULL),(2514,19,'2017-01-27','8',0,NULL),(2515,19,'2017-01-28','',0,NULL),(2516,19,'2017-01-29','',0,NULL),(2517,19,'2017-01-30','8',0,NULL),(2518,19,'2017-01-31','8',0,NULL),(2519,19,'2017-02-01','8',0,NULL),(2520,19,'2017-02-02','8',0,NULL),(2521,19,'2017-02-03','8',0,NULL),(2522,19,'2017-02-04','',0,NULL),(2523,19,'2017-02-05','',0,NULL),(2524,19,'2017-02-06','8',0,NULL),(2525,19,'2017-02-07','8',0,NULL),(2526,19,'2017-02-08','8',0,NULL),(2527,19,'2017-02-09','8',0,NULL),(2528,19,'2017-02-10','8',0,NULL),(2529,19,'2017-02-11','',0,NULL),(2530,19,'2017-02-12','',0,NULL),(2531,19,'2017-02-13','8',0,NULL),(2532,19,'2017-02-14','8',0,NULL),(2533,19,'2017-02-15','8',0,NULL),(2534,19,'2017-02-16','8',0,NULL),(2535,19,'2017-02-17','8',0,NULL),(2536,19,'2017-02-18','',0,NULL),(2537,19,'2017-02-19','',0,NULL),(2538,19,'2017-02-20','8',0,NULL),(2539,19,'2017-02-21','8',0,NULL),(2540,19,'2017-02-22','8',0,NULL),(2541,19,'2017-02-23','8',0,NULL),(2542,19,'2017-02-24','8',0,NULL),(2543,19,'2017-02-25','',0,NULL),(2544,19,'2017-02-26','',0,NULL),(2545,19,'2017-02-27','8',0,NULL),(2546,19,'2017-02-28','8',0,NULL),(2547,19,'2017-03-01','8',0,NULL),(2548,19,'2017-03-02','8',0,NULL),(2549,19,'2017-03-03','8',0,NULL),(2550,19,'2017-03-04','',0,NULL),(2551,19,'2017-03-05','',0,NULL),(2552,19,'2017-03-06','8',0,NULL),(2553,19,'2017-03-07','8',0,NULL),(2554,19,'2017-03-08','8',0,NULL),(2555,19,'2017-03-09','8',0,NULL),(2556,19,'2017-03-10','8',0,NULL),(2557,19,'2017-03-11','',0,NULL),(2558,19,'2017-03-12','',0,NULL),(2559,19,'2017-03-13','8',0,NULL),(2560,19,'2017-03-14','8',0,NULL),(2561,19,'2017-03-15','8',0,NULL),(2562,19,'2017-03-16','8',0,NULL),(2563,19,'2017-03-17','8',0,NULL),(2564,19,'2017-03-18','',0,NULL),(2565,19,'2017-03-19','',0,NULL),(2566,19,'2017-03-20','8',0,NULL),(2567,19,'2017-03-21','8',0,NULL),(2568,19,'2017-03-22','8',0,NULL),(2569,19,'2017-03-23','8',0,NULL),(2570,19,'2017-03-24','8',0,NULL),(2571,19,'2017-03-25','',0,NULL),(2572,19,'2017-03-26','',0,NULL),(2573,19,'2017-03-27','8',0,NULL),(2574,19,'2017-03-28','8',0,NULL),(2575,19,'2017-03-29','8',0,NULL),(2576,19,'2017-03-30','8',0,NULL),(2577,19,'2017-03-31','8',0,NULL),(2578,19,'2017-04-01','',0,NULL),(2579,19,'2017-04-02','',0,NULL),(2580,19,'2017-04-03','8',0,NULL),(2581,19,'2017-04-04','8',0,NULL),(2582,19,'2017-04-05','8',0,NULL),(2583,19,'2017-04-06','8',0,NULL),(2584,19,'2017-04-07','8',0,NULL),(2585,19,'2017-04-08','',0,NULL),(2586,19,'2017-04-09','',0,NULL),(2587,19,'2017-04-10','8',0,NULL),(2588,19,'2017-04-11','8',0,NULL),(2589,19,'2017-04-12','8',0,NULL),(2590,19,'2017-04-13','8',0,NULL),(2591,19,'2017-04-14','8',0,NULL),(2592,19,'2017-04-15','',0,NULL),(2593,19,'2017-04-16','',0,NULL),(2594,19,'2017-04-17','8',0,NULL),(2595,19,'2017-04-18','8',0,NULL),(2596,19,'2017-04-19','8',0,NULL),(2597,19,'2017-04-20','8',0,NULL),(2598,19,'2017-04-21','8',0,NULL),(2599,19,'2017-04-22','',0,NULL),(2600,19,'2017-04-23','',0,NULL),(2601,19,'2017-04-24','8',0,NULL),(2602,19,'2017-04-25','8',0,NULL),(2603,19,'2017-04-26','8',0,NULL),(2604,19,'2017-04-27','8',0,NULL),(2605,19,'2017-04-28','8',0,NULL),(2606,19,'2017-04-29','',0,NULL),(2607,19,'2017-04-30','',0,NULL),(2608,19,'2017-05-01','8',0,NULL),(2609,19,'2017-05-02','8',0,NULL),(2610,19,'2017-05-03','8',0,NULL),(2611,19,'2017-05-04','8',0,NULL),(2612,19,'2017-05-05','8',0,NULL),(2613,19,'2017-05-06','',0,NULL),(2614,19,'2017-05-07','',0,NULL),(2615,19,'2017-05-08','8',0,NULL),(2616,19,'2017-05-09','8',0,NULL),(2617,19,'2017-05-10','8',0,NULL),(2618,19,'2017-05-11','8',0,NULL),(2619,19,'2017-05-12','8',0,NULL),(2620,19,'2017-05-13','',0,NULL),(2621,19,'2017-05-14','',0,NULL),(2622,19,'2017-05-15','8',0,NULL),(2623,19,'2017-05-16','8',0,NULL),(2624,19,'2017-05-17','8',0,NULL),(2625,19,'2017-05-18','8',0,NULL),(2626,19,'2017-05-19','8',0,NULL),(2627,19,'2017-05-20','',0,NULL),(2628,19,'2017-05-21','',0,NULL),(2629,19,'2017-05-22','8',0,NULL),(2630,19,'2017-05-23','8',0,NULL),(2631,19,'2017-05-24','8',0,NULL),(2632,19,'2017-05-25','8',0,NULL),(2633,19,'2017-05-26','8',0,NULL),(2634,19,'2017-05-27','',0,NULL),(2635,19,'2017-05-28','',0,NULL),(2636,19,'2017-05-29','8',0,NULL),(2637,19,'2017-05-30','8',0,NULL),(2638,19,'2017-05-31','8',0,NULL),(2639,19,'2017-06-01','8',0,NULL),(2640,19,'2017-06-02','8',0,NULL),(2641,19,'2017-06-03','',0,NULL),(2642,19,'2017-06-04','',0,NULL),(2643,19,'2017-06-05','8',0,NULL),(2644,19,'2017-06-06','8',0,NULL),(2645,19,'2017-06-07','8',0,NULL),(2646,19,'2017-06-08','8',0,NULL),(2647,19,'2017-06-09','8',0,NULL),(2648,19,'2017-06-10','',0,NULL),(2649,19,'2017-06-11','',0,NULL),(2650,19,'2017-06-12','8',0,NULL),(2651,19,'2017-06-13','8',0,NULL),(2652,19,'2017-06-14','8',0,NULL),(2653,19,'2017-06-15','8',0,NULL),(2654,19,'2017-06-16','8',0,NULL),(2655,19,'2017-06-17','',0,NULL),(2656,19,'2017-06-18','',0,NULL),(2657,19,'2017-06-19','8',0,NULL),(2658,19,'2017-06-20','8',0,NULL),(2659,19,'2017-06-21','8',0,NULL),(2660,19,'2017-06-22','8',0,NULL),(2661,19,'2017-06-23','8',0,NULL),(2662,19,'2017-06-24','',0,NULL),(2663,19,'2017-06-25','',0,NULL),(2664,19,'2017-06-26','8',0,NULL),(2665,19,'2017-06-27','8',0,NULL),(2666,19,'2017-06-28','8',0,NULL),(2667,19,'2017-06-29','8',0,NULL),(2668,19,'2017-06-30','8',0,NULL),(2669,19,'2017-07-01','',0,NULL),(2670,19,'2017-07-02','',0,NULL),(2671,19,'2017-07-03','8',0,NULL),(2672,19,'2017-07-04','8',0,NULL),(2673,19,'2017-07-05','8',0,NULL),(2674,19,'2017-07-06','8',0,NULL),(2675,19,'2017-07-07','8',0,NULL),(2676,19,'2017-07-08','',0,NULL),(2677,19,'2017-07-09','',0,NULL),(2678,19,'2017-07-10','8',0,NULL),(2679,19,'2017-07-11','8',0,NULL),(2680,19,'2017-07-12','8',0,NULL),(2681,19,'2017-07-13','8',0,NULL),(2682,19,'2017-07-14','8',0,NULL),(2683,19,'2017-07-15','',0,NULL),(2684,19,'2017-07-16','',0,NULL),(2685,19,'2017-07-17','8',0,NULL),(2686,19,'2017-07-18','8',0,NULL),(2687,19,'2017-07-19','8',0,NULL),(2688,19,'2017-07-20','8',0,NULL),(2689,19,'2017-07-21','8',0,NULL),(2690,19,'2017-07-22','',0,NULL),(2691,19,'2017-07-23','',0,NULL),(2692,19,'2017-07-24','8',0,NULL),(2693,19,'2017-07-25','8',0,NULL),(2694,19,'2017-07-26','8',0,NULL),(2695,19,'2017-07-27','8',0,NULL),(2696,19,'2017-07-28','8',0,NULL),(2697,19,'2017-07-29','',0,NULL),(2698,19,'2017-07-30','',0,NULL),(2699,19,'2017-07-31','8',0,NULL),(2700,19,'2017-08-01','8',0,NULL),(2701,19,'2017-08-02','8',0,NULL),(2702,19,'2017-08-03','8',0,NULL),(2703,19,'2017-08-04','8',0,NULL),(2704,19,'2017-08-05','',0,NULL),(2705,19,'2017-08-06','',0,NULL),(2706,19,'2017-08-07','8',0,NULL),(2707,19,'2017-08-08','8',0,NULL),(2708,19,'2017-08-09','8',0,NULL),(2709,19,'2017-08-10','8',0,NULL),(2710,19,'2017-08-11','8',0,NULL),(2711,19,'2017-08-12','',0,NULL),(2712,19,'2017-08-13','',0,NULL),(2713,19,'2017-08-14','8',0,NULL),(2714,19,'2017-08-15','8',0,NULL),(2715,19,'2017-08-16','8',0,NULL),(2716,19,'2017-08-17','8',0,NULL),(2717,19,'2017-08-18','8',0,NULL),(2718,19,'2017-08-19','',0,NULL),(2719,19,'2017-08-20','',0,NULL),(2720,19,'2017-08-21','8',0,NULL),(2721,19,'2017-08-22','8',0,NULL),(2722,19,'2017-08-23','8',0,NULL),(2723,19,'2017-08-24','8',0,NULL),(2724,19,'2017-08-25','8',0,NULL),(2725,19,'2017-08-26','',0,NULL),(2726,19,'2017-08-27','',0,NULL),(2727,19,'2017-08-28','8',0,NULL),(2728,19,'2017-08-29','8',0,NULL),(2729,19,'2017-08-30','8',0,NULL),(2730,19,'2017-08-31','8',0,NULL),(2731,19,'2017-09-01','8',0,NULL),(2732,19,'2017-09-02','',0,NULL),(2733,19,'2017-09-03','',0,NULL),(2734,19,'2017-09-04','8',0,NULL),(2735,19,'2017-09-05','8',0,NULL),(2736,19,'2017-09-06','8',0,NULL),(2737,19,'2017-09-07','8',0,NULL),(2738,19,'2017-09-08','8',0,NULL),(2739,19,'2017-09-09','',0,NULL),(2740,19,'2017-09-10','',0,NULL),(2741,19,'2017-09-11','8',0,NULL),(2742,19,'2017-09-12','8',0,NULL),(2743,19,'2017-09-13','8',0,NULL),(2744,19,'2017-09-14','8',0,NULL),(2745,19,'2017-09-15','8',0,NULL),(2746,19,'2017-09-16','',0,NULL),(2747,19,'2017-09-17','',0,NULL),(2748,19,'2017-09-18','8',0,NULL),(2749,19,'2017-09-19','8',0,NULL),(2750,19,'2017-09-20','8',0,NULL),(2751,19,'2017-09-21','8',0,NULL),(2752,19,'2017-09-22','8',0,NULL),(2753,19,'2017-09-23','',0,NULL),(2754,19,'2017-09-24','',0,NULL),(2755,19,'2017-09-25','8',0,NULL),(2756,19,'2017-09-26','8',0,NULL),(2757,19,'2017-09-27','8',0,NULL),(2758,19,'2017-09-28','8',0,NULL),(2759,19,'2017-09-29','8',0,NULL),(2760,19,'2017-09-30','',0,NULL),(2761,19,'2017-10-01','',0,NULL),(2762,19,'2017-10-02','8',0,NULL),(2763,19,'2017-10-03','8',0,NULL),(2764,19,'2017-10-04','8',0,NULL),(2765,19,'2017-10-05','8',0,NULL),(2766,19,'2017-10-06','8',0,NULL),(2767,19,'2017-10-07','',0,NULL),(2768,19,'2017-10-08','',0,NULL),(2769,19,'2017-10-09','8',0,NULL),(2770,19,'2017-10-10','8',0,NULL),(2771,19,'2017-10-11','8',0,NULL),(2772,19,'2017-10-12','8',0,NULL),(2773,19,'2017-10-13','8',0,NULL),(2774,19,'2017-10-14','',0,NULL),(2775,19,'2017-10-15','',0,NULL),(2776,19,'2017-10-16','8',0,NULL),(2777,19,'2017-10-17','8',0,NULL),(2778,19,'2017-10-18','8',0,NULL),(2779,19,'2017-10-19','8',0,NULL),(2780,19,'2017-10-20','8',0,NULL),(2781,19,'2017-10-21','',0,NULL),(2782,19,'2017-10-22','',0,NULL),(2783,19,'2017-10-23','8',0,NULL),(2784,19,'2017-10-24','8',0,NULL),(2785,19,'2017-10-25','8',0,NULL),(2786,19,'2017-10-26','8',0,NULL),(2787,19,'2017-10-27','8',0,NULL),(2788,19,'2017-10-28','',0,NULL),(2789,19,'2017-10-29','',0,NULL),(2790,19,'2017-10-30','8',0,NULL),(2791,19,'2017-10-31','8',0,NULL),(2792,19,'2017-11-01','8',0,NULL),(2793,19,'2017-11-02','8',0,NULL),(2794,19,'2017-11-03','8',0,NULL),(2795,19,'2017-11-04','',0,NULL),(2796,19,'2017-11-05','',0,NULL),(2797,19,'2017-11-06','8',0,NULL),(2798,19,'2017-11-07','8',0,NULL),(2799,19,'2017-11-08','8',0,NULL),(2800,19,'2017-11-09','8',0,NULL),(2801,19,'2017-11-10','8',0,NULL),(2802,19,'2017-11-11','',0,NULL),(2803,19,'2017-11-12','',0,NULL),(2804,19,'2017-11-13','8',0,NULL),(2805,19,'2017-11-14','8',0,NULL),(2806,19,'2017-11-15','8',0,NULL),(2807,19,'2017-11-16','8',0,NULL),(2808,19,'2017-11-17','8',0,NULL),(2809,19,'2017-11-18','',0,NULL),(2810,19,'2017-11-19','',0,NULL),(2811,19,'2017-11-20','8',0,NULL),(2812,19,'2017-11-21','8',0,NULL),(2813,19,'2017-11-22','8',0,NULL),(2814,19,'2017-11-23','8',0,NULL),(2815,19,'2017-11-24','8',0,NULL),(2816,19,'2017-11-25','',0,NULL),(2817,19,'2017-11-26','',0,NULL),(2818,19,'2017-11-27','8',0,NULL),(2819,19,'2017-11-28','8',0,NULL),(2820,19,'2017-11-29','8',0,NULL),(2821,19,'2017-11-30','8',0,NULL),(2822,19,'2017-12-01','8',0,NULL),(2823,19,'2017-12-02','',0,NULL),(2824,19,'2017-12-03','',0,NULL),(2825,19,'2017-12-04','8',0,NULL),(2826,19,'2017-12-05','8',0,NULL),(2827,19,'2017-12-06','8',0,NULL),(2828,19,'2017-12-07','8',0,NULL),(2829,19,'2017-12-08','8',0,NULL),(2830,19,'2017-12-09','',0,NULL),(2831,19,'2017-12-10','',0,NULL),(2832,19,'2017-12-11','8',0,NULL),(2833,19,'2017-12-12','8',0,NULL),(2834,19,'2017-12-13','8',0,NULL),(2835,19,'2017-12-14','8',0,NULL),(2836,19,'2017-12-15','8',0,NULL),(2837,19,'2017-12-16','',0,NULL),(2838,19,'2017-12-17','',0,NULL),(2839,19,'2017-12-18','8',0,NULL),(2840,19,'2017-12-19','8',0,NULL),(2841,19,'2017-12-20','8',0,NULL),(2842,19,'2017-12-21','8',0,NULL),(2843,19,'2017-12-22','8',0,NULL),(2844,19,'2017-12-23','',0,NULL),(2845,19,'2017-12-24','',0,NULL),(2846,19,'2017-12-25','8',0,NULL),(2847,20,'2018-01-01','8',0,NULL),(2848,20,'2018-01-02','8',0,NULL),(2849,20,'2018-01-03','8',0,NULL),(2850,20,'2018-01-04','8',0,NULL),(2851,20,'2018-01-05','8',0,NULL),(2852,20,'2018-01-06','',0,NULL),(2853,20,'2018-01-07','',0,NULL),(2854,20,'2018-01-08','8',0,NULL),(2855,20,'2018-01-09','8',0,NULL),(2856,20,'2018-01-10','8',0,NULL),(2857,20,'2018-01-11','8',0,NULL),(2858,20,'2018-01-12','8',0,NULL),(2859,20,'2018-01-13','',0,NULL),(2860,20,'2018-01-14','',0,NULL),(2861,20,'2018-01-15','8',0,NULL),(2862,20,'2018-01-16','8',0,NULL),(2863,20,'2018-01-17','8',0,NULL),(2864,20,'2018-01-18','8',0,NULL),(2865,20,'2018-01-19','8',0,NULL),(2866,20,'2018-01-20','',0,NULL),(2867,20,'2018-01-21','',0,NULL),(2868,20,'2018-01-22','8',0,NULL),(2869,20,'2018-01-23','8',0,NULL),(2870,20,'2018-01-24','8',0,NULL),(2871,20,'2018-01-25','8',0,NULL),(2872,20,'2018-01-26','8',0,NULL),(2873,20,'2018-01-27','',0,NULL),(2874,20,'2018-01-28','',0,NULL),(2875,20,'2018-01-29','8',0,NULL),(2876,20,'2018-01-30','8',0,NULL),(2877,20,'2018-01-31','8',0,NULL),(2878,20,'2018-02-01','8',0,NULL),(2879,20,'2018-02-02','8',0,NULL),(2880,20,'2018-02-03','',0,NULL),(2881,20,'2018-02-04','',0,NULL),(2882,20,'2018-02-05','8',0,NULL),(2883,20,'2018-02-06','8',0,NULL),(2884,20,'2018-02-07','8',0,NULL),(2885,20,'2018-02-08','8',0,NULL),(2886,20,'2018-02-09','8',0,NULL),(2887,20,'2018-02-10','',0,NULL),(2888,20,'2018-02-11','',0,NULL),(2889,20,'2018-02-12','8',0,NULL),(2890,20,'2018-02-13','8',0,NULL),(2891,20,'2018-02-14','8',0,NULL),(2892,20,'2018-02-15','8',0,NULL),(2893,20,'2018-02-16','8',0,NULL),(2894,20,'2018-02-17','',0,NULL),(2895,20,'2018-02-18','',0,NULL),(2896,20,'2018-02-19','8',0,NULL),(2897,20,'2018-02-20','8',0,NULL),(2898,20,'2018-02-21','8',0,NULL),(2899,20,'2018-02-22','8',0,NULL),(2900,20,'2018-02-23','8',0,NULL),(2901,20,'2018-02-24','',0,NULL),(2902,20,'2018-02-25','',0,NULL),(2903,20,'2018-02-26','8',0,NULL),(2904,20,'2018-02-27','8',0,NULL),(2905,20,'2018-02-28','8',0,NULL),(2906,20,'2018-03-01','8',0,NULL),(2907,20,'2018-03-02','8',0,NULL),(2908,20,'2018-03-03','',0,NULL),(2909,20,'2018-03-04','',0,NULL),(2910,20,'2018-03-05','8',0,NULL),(2911,20,'2018-03-06','8',0,NULL),(2912,20,'2018-03-07','8',0,NULL),(2913,20,'2018-03-08','8',0,NULL),(2914,20,'2018-03-09','8',0,NULL),(2915,20,'2018-03-10','',0,NULL),(2916,20,'2018-03-11','',0,NULL),(2917,20,'2018-03-12','8',0,NULL),(2918,20,'2018-03-13','8',0,NULL),(2919,20,'2018-03-14','8',0,NULL),(2920,20,'2018-03-15','8',0,NULL),(2921,20,'2018-03-16','8',0,NULL),(2922,20,'2018-03-17','',0,NULL),(2923,20,'2018-03-18','',0,NULL),(2924,20,'2018-03-19','8',0,NULL),(2925,20,'2018-03-20','8',0,NULL),(2926,20,'2018-03-21','8',0,NULL),(2927,20,'2018-03-22','8',0,NULL),(2928,20,'2018-03-23','8',0,NULL),(2929,20,'2018-03-24','',0,NULL),(2930,20,'2018-03-25','',0,NULL),(2931,20,'2018-03-26','8',0,NULL),(2932,20,'2018-03-27','8',0,NULL),(2933,20,'2018-03-28','8',0,NULL),(2934,20,'2018-03-29','8',0,NULL),(2935,20,'2018-03-30','8',0,NULL),(2936,20,'2018-03-31','',0,NULL),(2937,20,'2018-04-01','',0,NULL),(2938,20,'2018-04-02','8',0,NULL),(2939,20,'2018-04-03','8',0,NULL),(2940,20,'2018-04-04','8',0,NULL),(2941,20,'2018-04-05','8',0,NULL),(2942,20,'2018-04-06','8',0,NULL),(2943,20,'2018-04-07','',0,NULL),(2944,20,'2018-04-08','',0,NULL),(2945,20,'2018-04-09','8',0,NULL),(2946,20,'2018-04-10','8',0,NULL),(2947,20,'2018-04-11','8',0,NULL),(2948,20,'2018-04-12','8',0,NULL),(2949,20,'2018-04-13','8',0,NULL),(2950,20,'2018-04-14','',0,NULL),(2951,20,'2018-04-15','',0,NULL),(2952,20,'2018-04-16','8',0,NULL),(2953,20,'2018-04-17','8',0,NULL),(2954,20,'2018-04-18','8',0,NULL),(2955,20,'2018-04-19','8',0,NULL),(2956,20,'2018-04-20','8',0,NULL),(2957,20,'2018-04-21','',0,NULL),(2958,20,'2018-04-22','',0,NULL),(2959,20,'2018-04-23','8',0,NULL),(2960,20,'2018-04-24','8',0,NULL),(2961,20,'2018-04-25','8',0,NULL),(2962,20,'2018-04-26','8',0,NULL),(2963,20,'2018-04-27','8',0,NULL),(2964,20,'2018-04-28','',0,NULL),(2965,20,'2018-04-29','',0,NULL),(2966,20,'2018-04-30','8',0,NULL),(2967,20,'2018-05-01','8',0,NULL),(2968,20,'2018-05-02','8',0,NULL),(2969,20,'2018-05-03','8',0,NULL),(2970,20,'2018-05-04','8',0,NULL),(2971,20,'2018-05-05','',0,NULL),(2972,20,'2018-05-06','',0,NULL),(2973,20,'2018-05-07','8',0,NULL),(2974,20,'2018-05-08','8',0,NULL),(2975,20,'2018-05-09','8',0,NULL),(2976,20,'2018-05-10','8',0,NULL),(2977,20,'2018-05-11','8',0,NULL),(2978,20,'2018-05-12','',0,NULL),(2979,20,'2018-05-13','',0,NULL),(2980,20,'2018-05-14','8',0,NULL),(2981,20,'2018-05-15','8',0,NULL),(2982,20,'2018-05-16','8',0,NULL),(2983,20,'2018-05-17','8',0,NULL),(2984,20,'2018-05-18','8',0,NULL),(2985,20,'2018-05-19','',0,NULL),(2986,20,'2018-05-20','',0,NULL),(2987,20,'2018-05-21','8',0,NULL),(2988,20,'2018-05-22','8',0,NULL),(2989,20,'2018-05-23','8',0,NULL),(2990,20,'2018-05-24','8',0,NULL),(2991,20,'2018-05-25','8',0,NULL),(2992,20,'2018-05-26','',0,NULL),(2993,20,'2018-05-27','',0,NULL),(2994,20,'2018-05-28','8',0,NULL),(2995,20,'2018-05-29','8',0,NULL),(2996,20,'2018-05-30','8',0,NULL),(2997,20,'2018-05-31','8',0,NULL),(2998,20,'2018-06-01','8',0,NULL),(2999,20,'2018-06-02','',0,NULL),(3000,20,'2018-06-03','',0,NULL),(3001,20,'2018-06-04','8',0,NULL),(3002,20,'2018-06-05','8',0,NULL),(3003,20,'2018-06-06','8',0,NULL),(3004,20,'2018-06-07','8',0,NULL),(3005,20,'2018-06-08','8',0,NULL),(3006,20,'2018-06-09','',0,NULL),(3007,20,'2018-06-10','',0,NULL),(3008,20,'2018-06-11','8',0,NULL),(3009,20,'2018-06-12','8',0,NULL),(3010,20,'2018-06-13','8',0,NULL),(3011,20,'2018-06-14','8',0,NULL),(3012,20,'2018-06-15','8',0,NULL),(3013,20,'2018-06-16','',0,NULL),(3014,20,'2018-06-17','',0,NULL),(3015,20,'2018-06-18','8',0,NULL),(3016,20,'2018-06-19','8',0,NULL),(3017,20,'2018-06-20','8',0,NULL),(3018,20,'2018-06-21','8',0,NULL),(3019,20,'2018-06-22','8',0,NULL),(3020,20,'2018-06-23','',0,NULL),(3021,20,'2018-06-24','',0,NULL),(3022,20,'2018-06-25','8',0,NULL),(3023,20,'2018-06-26','8',0,NULL),(3024,20,'2018-06-27','8',0,NULL),(3025,20,'2018-06-28','8',0,NULL),(3026,20,'2018-06-29','8',0,NULL),(3027,20,'2018-06-30','',0,NULL),(3028,20,'2018-07-01','',0,NULL),(3029,20,'2018-07-02','8',0,NULL),(3030,20,'2018-07-03','8',0,NULL),(3031,20,'2018-07-04','8',0,NULL),(3032,20,'2018-07-05','8',0,NULL),(3033,20,'2018-07-06','8',0,NULL),(3034,20,'2018-07-07','',0,NULL),(3035,20,'2018-07-08','',0,NULL),(3036,20,'2018-07-09','8',0,NULL),(3037,20,'2018-07-10','8',0,NULL),(3038,20,'2018-07-11','8',0,NULL),(3039,20,'2018-07-12','8',0,NULL),(3040,20,'2018-07-13','8',0,NULL),(3041,20,'2018-07-14','',0,NULL),(3042,20,'2018-07-15','',0,NULL),(3043,20,'2018-07-16','8',0,NULL),(3044,20,'2018-07-17','8',0,NULL),(3045,20,'2018-07-18','8',0,NULL),(3046,20,'2018-07-19','8',0,NULL),(3047,20,'2018-07-20','8',0,NULL),(3048,20,'2018-07-21','',0,NULL),(3049,20,'2018-07-22','',0,NULL),(3050,20,'2018-07-23','8',0,NULL),(3051,20,'2018-07-24','8',0,NULL),(3052,20,'2018-07-25','8',0,NULL),(3053,20,'2018-07-26','8',0,NULL),(3054,20,'2018-07-27','8',0,NULL),(3055,20,'2018-07-28','',0,NULL),(3056,20,'2018-07-29','',0,NULL),(3057,20,'2018-07-30','8',0,NULL),(3058,20,'2018-07-31','8',0,NULL),(3059,20,'2018-08-01','8',0,NULL),(3060,20,'2018-08-02','8',0,NULL),(3061,20,'2018-08-03','8',0,NULL),(3062,20,'2018-08-04','',0,NULL),(3063,20,'2018-08-05','',0,NULL),(3064,20,'2018-08-06','8',0,NULL),(3065,20,'2018-08-07','8',0,NULL),(3066,20,'2018-08-08','8',0,NULL),(3067,20,'2018-08-09','8',0,NULL),(3068,20,'2018-08-10','8',0,NULL),(3069,20,'2018-08-11','',0,NULL),(3070,20,'2018-08-12','',0,NULL),(3071,20,'2018-08-13','8',0,NULL),(3072,20,'2018-08-14','8',0,NULL),(3073,20,'2018-08-15','8',0,NULL),(3074,20,'2018-08-16','8',0,NULL),(3075,20,'2018-08-17','8',0,NULL),(3076,20,'2018-08-18','',0,NULL),(3077,20,'2018-08-19','',0,NULL),(3078,20,'2018-08-20','8',0,NULL),(3079,20,'2018-08-21','8',0,NULL),(3080,20,'2018-08-22','8',0,NULL),(3081,20,'2018-08-23','8',0,NULL),(3082,20,'2018-08-24','8',0,NULL),(3083,20,'2018-08-25','',0,NULL),(3084,20,'2018-08-26','',0,NULL),(3085,20,'2018-08-27','8',0,NULL),(3086,20,'2018-08-28','8',0,NULL),(3087,20,'2018-08-29','8',0,NULL),(3088,20,'2018-08-30','8',0,NULL),(3089,20,'2018-08-31','8',0,NULL),(3090,20,'2018-09-01','',0,NULL),(3091,20,'2018-09-02','',0,NULL),(3092,20,'2018-09-03','8',0,NULL),(3093,20,'2018-09-04','8',0,NULL),(3094,20,'2018-09-05','8',0,NULL),(3095,20,'2018-09-06','8',0,NULL),(3096,20,'2018-09-07','8',0,NULL),(3097,20,'2018-09-08','',0,NULL),(3098,20,'2018-09-09','',0,NULL),(3099,20,'2018-09-10','8',0,NULL),(3100,20,'2018-09-11','8',0,NULL),(3101,20,'2018-09-12','8',0,NULL),(3102,20,'2018-09-13','8',0,NULL),(3103,20,'2018-09-14','8',0,NULL),(3104,20,'2018-09-15','',0,NULL),(3105,20,'2018-09-16','',0,NULL),(3106,20,'2018-09-17','8',0,NULL),(3107,20,'2018-09-18','8',0,NULL),(3108,20,'2018-09-19','8',0,NULL),(3109,20,'2018-09-20','8',0,NULL),(3110,20,'2018-09-21','8',0,NULL),(3111,20,'2018-09-22','',0,NULL),(3112,20,'2018-09-23','',0,NULL),(3113,20,'2018-09-24','8',0,NULL),(3114,20,'2018-09-25','8',0,NULL),(3115,20,'2018-09-26','8',0,NULL),(3116,20,'2018-09-27','8',0,NULL),(3117,20,'2018-09-28','8',0,NULL),(3118,20,'2018-09-29','',0,NULL),(3119,20,'2018-09-30','',0,NULL),(3120,20,'2018-10-01','8',0,NULL),(3121,20,'2018-10-02','8',0,NULL),(3122,20,'2018-10-03','8',0,NULL),(3123,20,'2018-10-04','8',0,NULL),(3124,20,'2018-10-05','8',0,NULL),(3125,20,'2018-10-06','',0,NULL),(3126,20,'2018-10-07','',0,NULL),(3127,20,'2018-10-08','8',0,NULL),(3128,20,'2018-10-09','8',0,NULL),(3129,20,'2018-10-10','8',0,NULL),(3130,20,'2018-10-11','8',0,NULL),(3131,20,'2018-10-12','8',0,NULL),(3132,20,'2018-10-13','',0,NULL),(3133,20,'2018-10-14','',0,NULL),(3134,20,'2018-10-15','8',0,NULL),(3135,20,'2018-10-16','8',0,NULL),(3136,20,'2018-10-17','8',0,NULL),(3137,20,'2018-10-18','8',0,NULL),(3138,20,'2018-10-19','8',0,NULL),(3139,20,'2018-10-20','',0,NULL),(3140,20,'2018-10-21','',0,NULL),(3141,20,'2018-10-22','8',0,NULL),(3142,20,'2018-10-23','8',0,NULL),(3143,20,'2018-10-24','8',0,NULL),(3144,20,'2018-10-25','8',0,NULL),(3145,20,'2018-10-26','8',0,NULL),(3146,20,'2018-10-27','',0,NULL),(3147,20,'2018-10-28','',0,NULL),(3148,20,'2018-10-29','8',0,NULL),(3149,20,'2018-10-30','8',0,NULL),(3150,20,'2018-10-31','8',0,NULL),(3151,20,'2018-11-01','8',0,NULL),(3152,20,'2018-11-02','8',0,NULL),(3153,20,'2018-11-03','',0,NULL),(3154,20,'2018-11-04','',0,NULL),(3155,20,'2018-11-05','8',0,NULL),(3156,20,'2018-11-06','8',0,NULL),(3157,20,'2018-11-07','8',0,NULL),(3158,20,'2018-11-08','8',0,NULL),(3159,20,'2018-11-09','8',0,NULL),(3160,20,'2018-11-10','',0,NULL),(3161,20,'2018-11-11','',0,NULL),(3162,20,'2018-11-12','8',0,NULL),(3163,20,'2018-11-13','8',0,NULL),(3164,20,'2018-11-14','8',0,NULL),(3165,20,'2018-11-15','8',0,NULL),(3166,20,'2018-11-16','8',0,NULL),(3167,20,'2018-11-17','',0,NULL),(3168,20,'2018-11-18','',0,NULL),(3169,20,'2018-11-19','8',0,NULL),(3170,20,'2018-11-20','8',0,NULL),(3171,20,'2018-11-21','8',0,NULL),(3172,20,'2018-11-22','8',0,NULL),(3173,20,'2018-11-23','8',0,NULL),(3174,20,'2018-11-24','',0,NULL),(3175,20,'2018-11-25','',0,NULL),(3176,20,'2018-11-26','8',0,NULL),(3177,20,'2018-11-27','8',0,NULL),(3178,20,'2018-11-28','8',0,NULL),(3179,20,'2018-11-29','8',0,NULL),(3180,20,'2018-11-30','8',0,NULL),(3181,20,'2018-12-01','',0,NULL),(3182,20,'2018-12-02','',0,NULL),(3183,20,'2018-12-03','8',0,NULL),(3184,20,'2018-12-04','8',0,NULL),(3185,20,'2018-12-05','8',0,NULL),(3186,20,'2018-12-06','8',0,NULL),(3187,20,'2018-12-07','8',0,NULL),(3188,20,'2018-12-08','',0,NULL),(3189,20,'2018-12-09','',0,NULL),(3190,20,'2018-12-10','8',0,NULL),(3191,20,'2018-12-11','8',0,NULL),(3192,20,'2018-12-12','8',0,NULL),(3193,20,'2018-12-13','8',0,NULL),(3194,20,'2018-12-14','8',0,NULL),(3195,20,'2018-12-15','',0,NULL),(3196,20,'2018-12-16','',0,NULL),(3197,20,'2018-12-17','8',0,NULL),(3198,20,'2018-12-18','8',0,NULL),(3199,20,'2018-12-19','8',0,NULL),(3200,20,'2018-12-20','8',0,NULL),(3201,20,'2018-12-21','8',0,NULL),(3202,20,'2018-12-22','',0,NULL),(3203,20,'2018-12-23','',0,NULL),(3204,20,'2018-12-24','8',0,NULL);
/*!40000 ALTER TABLE `fy_template` ENABLE KEYS */;
UNLOCK TABLES;

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
  `Year_ID` int(11) NOT NULL,
  PRIMARY KEY (`Holiday_ID`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  UNIQUE KEY `UNQ_Day` (`Date`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday`
--

LOCK TABLES `holiday` WRITE;
/*!40000 ALTER TABLE `holiday` DISABLE KEYS */;
INSERT INTO `holiday` VALUES (1,'My Birthday','2017-11-01','2017-11-28 10:06:28',NULL,'2017-11-28 10:06:28',NULL,1);
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
-- Table structure for table `pem`
--

DROP TABLE IF EXISTS `pem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pem` (
  `PEM_Serial` varchar(11) NOT NULL,
  `Employee_Serial` varchar(11) NOT NULL,
  `Start_Date` date NOT NULL DEFAULT '0000-00-00',
  `End_Date` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`PEM_Serial`),
  UNIQUE KEY `UNQ_PEM` (`PEM_Serial`,`Employee_Serial`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pem`
--

LOCK TABLES `pem` WRITE;
/*!40000 ALTER TABLE `pem` DISABLE KEYS */;
INSERT INTO `pem` VALUES ('P100Y2','P100Y2','2017-09-18','2017-12-31');
/*!40000 ALTER TABLE `pem` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_engagement`
--

LOCK TABLES `project_engagement` WRITE;
/*!40000 ALTER TABLE `project_engagement` DISABLE KEYS */;
INSERT INTO `project_engagement` VALUES (12,1,'123456PH1','2017-08-01','2018-09-27','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN');
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
  `IsRecoverable` varchar(1) NOT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Team_ID`),
  UNIQUE KEY `Team_ID_UNIQUE` (`Team_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,1,'123456PH1','Team A','Y','2017-08-16 08:42:23','ADMINISTRATOR','2017-08-16 14:05:57','ADMINISTRATOR');
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
  `isActive` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Year_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `year`
--

LOCK TABLES `year` WRITE;
/*!40000 ALTER TABLE `year` DISABLE KEYS */;
INSERT INTO `year` VALUES (19,2017,'2017-12-25','2017-01-02','2017-12-08 10:12:56','ADMIN','2017-12-08 10:12:56',NULL,0);
/*!40000 ALTER TABLE `year` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'opum'
--
/*!50003 DROP PROCEDURE IF EXISTS `getEmpUtil` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getEmpUtil`(IN employeeId varchar(40), IN currFY INT)
BEGIN

SELECT 
        `fy`.`Date` AS `date`,
        employeeId,
        (CASE
            WHEN (`el`.`Status` = 'Approved') THEN 0
            WHEN (`el`.`Leave_Type` = 'RC') THEN `el`.`Hours`
            ELSE `fy`.`Value`
        END) AS `Hours`,
        (CASE
            WHEN (`el`.`Status` = 'Approved') THEN `el`.`Leave_Type`
            ELSE `fy`.`Event_Name`
        END) AS `Event_Name`,
        `fy`.`Is_Holiday` AS `Is_Holiday`
    FROM
        (`fy_template` `fy`
        LEFT JOIN `employee_leave` `el` ON ((`fy`.`Date` = `el`.`Leave_Date`)
        AND (`el`.`Employee_ID` = employeeId)
        AND (`fy`.`Year_ID` = currFY)))
    ORDER BY `date`;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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

-- Dump completed on 2017-12-08 22:01:41
