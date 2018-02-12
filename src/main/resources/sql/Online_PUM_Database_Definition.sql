-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: opum
-- ------------------------------------------------------
-- Server version   5.7.19-log


/*!40101 SET NAMES utf8 */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40101 SET character_set_client = utf8 */;
/*!40101 SET UNIQUE_CHECKS = 0 */;
/*!40101 SET FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40101 SET SQL_NOTES = 0 */;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS*/;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS*/;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE*/;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES*/;
/*!40101 SET @saved_cs_client=@@character_set_client */;

-- Drop any existing tables

DROP TABLE IF EXISTS `employee`;
DROP TABLE IF EXISTS `employee_team`;
DROP TABLE IF EXISTS `employee_role`;
DROP TABLE IF EXISTS `employee_project`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `team`;
DROP TABLE IF EXISTS `holiday`;
DROP TABLE IF EXISTS `project`;
DROP TABLE IF EXISTS `project_engagement`;
DROP TABLE IF EXISTS `quarter`;
DROP TABLE IF EXISTS `utilization`;
DROP TABLE IF EXISTS `week`;
DROP TABLE IF EXISTS `year`;
DROP TABLE IF EXISTS `PEM`;

--
-- Table structure for table `employee`
--


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
  `Emp_Status` varchar(45) NOT NULL DEFAULT 'A',
  `IsActive` tinyint(1) NOT NULL DEFAULT '0',
  `Roll_In_Date` date NOT NULL DEFAULT '0000-00-00',
  `Roll_Off_Date` date NOT NULL DEFAULT '0000-00-00',
  `Designation` varchar(45) DEFAULT 'ADMIN',
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
   KEY (`Employee_ID`),
  
  KEY `Project_Engagement_ID_idx` (`Project_Engagement_ID`),
  CONSTRAINT `FK_Project_Engagement_ID` 
    FOREIGN KEY (`Project_Engagement_ID`) 
    REFERENCES `project_engagement` (`Project_Engagement_ID`) 
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;


--
-- Dumping data for table `employee`
--
INSERT INTO `employee` VALUES 
    ('123456PH1','admin', '123456PH1', NULL,NULL,NULL,NULL,'Admin','20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d','A',0,'2017-08-08','2020-01-30','ADMIN','2017-08-08 14:19:52','ADMINISTRATOR','2017-08-08 19:27:13','ADMINISTRATOR');
   -- ('121212PH1','magdanc@ph.ibm.com', '123456PH1', NULL,NULL,NULL,NULL,'Claude Magdangal',NULL,0,'2017-08-08','2020-01-30','2017-08-16 08:42:23','ADMINISTRATOR','2017-08-16 08:42:23',NULL),
 --   ('131313PH1','aumana@ph.ibm.com', '123456PH1', NULL,NULL,NULL,NULL,'Aldaina Auman','1fe02bd4ffffb947619effffac5458a7075aa5ffff966a4ab50714eb2cd322dd4ab56f90',0,'2017-08-08','2020-01-30','2017-08-16 08:42:23','ADMINISTRATOR','2017-08-16 13:36:15','ADMINISTRATOR'),
  --  ('141414PH1', 'admin', '123456PH1', NULL,NULL,NULL,NULL,'admin','20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d',0,'2017-08-08','2020-01-30','2017-10-25 03:23:16','ADMINISTRATOR','2017-10-25 03:23:16','ADMINISTRATOR');
  --  ('151515PH1','onlinepumsender@gmail.com', '123456PH1', NULL,NULL,NULL,NULL,'opum','ffff9b650366992dd24fb03ac543bc4eb1ffffbe42ffffbf4139c6fffff30dffffb8484fb0ffffb64affffa45c',0,'2017-08-08','2020-01-30','2017-08-08 14:19:52','ADMIN','2017-08-08 19:27:13','ADMIN'),
 --   ('161616PH1', 'onlinepumrecipient@gmail.com', '123456PH1', NULL,NULL,NULL,NULL,'opum','42bd39c6ffffae52ffff8c74ffffee12ffffd9270016e916e97887ffffa95752adffff847c40bf30cf42bd',0,'2017-08-08','2020-01-30','2017-08-08 14:19:52','ADMIN','2017-08-08 19:27:13','ADMIN');
-- usr/pw as admin/admin credentials
-- (1001, '1001', 'admin', NULL, NULL, NULL, NULL, 1, 'System Administrator', '20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d', 0, '2017-10-25 18:19:52', 'ADMIN', now(), 'ADMIN');

CREATE TABLE `role` (
  `Role_ID` int(2) NOT NULL,
  `Name` varchar(20) NOT NULL,
  UNIQUE KEY `Role_ID_UNIQUE` (`Role_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `role` VALUES 
    (1, 'System Administrator'),
    (2, 'Administrator'),
    (3, 'People Manager'),
    (4, 'Team Lead'),
    (5, 'User');
-- usr/pw as admin/admin credentials
-- (1001, '1001', 'admin', NULL, NULL, NULL, NULL, 1, 'System Administrator', '20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d', 0, '2017-10-25 18:19:52', 'ADMIN', now(), 'ADMIN');

--
-- Table structure for mapping table `employee_role`
--
CREATE TABLE `employee_role` (
  `Employee_ID` char(9) NOT NULL,
  `Role_ID` smallint(2) NOT NULL,
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Role_ID` (`Role_ID`),
  INDEX `Role_ID_idx` (`Role_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `employee_role` VALUES 
    ('123456PH1', 1);

--
-- Table structure for mapping table `employee_team`
--
CREATE TABLE `employee_team` (
  `Employee_ID` char(9) NOT NULL,
  `Team_ID` smallint(2) NOT NULL,
   `Roll_In_Date` date NOT NULL DEFAULT '0000-00-00',
  `Roll_Off_Date` date NOT NULL DEFAULT '0000-00-00',
   `Emp_Team_Status` varchar(45) NOT NULL DEFAULT 'A',
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Team_ID` (`Team_ID`),
  INDEX `Team_ID_idx` (`Team_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `employee_team` VALUES 
    ('123456PH1', 1, '2017-08-08','2020-01-30','A');

--
-- Table structure for mapping table `employee_project`
--
CREATE TABLE `employee_project` (
  `Employee_ID` char(9) NOT NULL,
  `Project_ID` smallint(2) NOT NULL,
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Project_ID` (`Project_ID`),
  INDEX `Project_ID_idx` (`Project_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `employee_project` VALUES 
    ('123456PH1', 1);

--
-- Table structure for table `team`
--
CREATE TABLE `team` (
  `Team_ID` smallint(3) NOT NULL AUTO_INCREMENT,
  `Project_ID` smallint(3) NOT NULL,
  `Team_Lead_Employee_ID` char(9) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `IsRecoverable` varchar(1) NOT NULL,
  `CreateDate` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Team_ID`),
  UNIQUE KEY `Team_ID_UNIQUE` (`Team_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for `team`
--
INSERT INTO `team` VALUES 
    (1, 1, '123456PH1', 'Team A', 'Y', '2017-08-16 08:42:23','ADMINISTRATOR','2017-08-16 14:05:57','ADMINISTRATOR');
    
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday`
--

LOCK TABLES `holiday` WRITE;
/*!40000 ALTER TABLE `holiday` DISABLE KEYS */;
INSERT INTO `holiday` VALUES (1,'My Birthday','2017-11-01','2017-11-28 10:06:28',NULL,'2017-11-28 10:06:28',NULL,1),(2,'Holiday!','2017-01-06','2017-12-18 10:56:46',NULL,'2017-12-18 10:57:13',NULL,24);
/*!40000 ALTER TABLE `holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--
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

--
-- Dumping data for table `project`
--
INSERT INTO `project` VALUES 
    (1,'USAA','2017-08-08 18:41:34','ADMIN',NULL,NULL);

--
-- Table structure for table `project_engagement`
--
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `project_engagement`
--
INSERT INTO `project_engagement` VALUES 
    (12,1,'123456PH1','2017-08-01','2018-09-27','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN');

--
-- Table structure for table `quarter`
--
CREATE TABLE `quarter` (
  `Quarter_ID` tinyint(4) NOT NULL AUTO_INCREMENT,
  `Year_ID` smallint(6) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Start` date NOT NULL,
  `End` date NOT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Quarter_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `utilization`
--
CREATE TABLE `utilization` (
`utilization_id` smallint(15) NOT NULL auto_increment,
  `employee_serial` varchar(45) NOT NULL,
  `type` varchar(20) NOT NULL, `year_id` smallint(6),
  `week1` smallint(6) default 40,`week2` smallint(6) default 40,
  `week3` smallint(6) default 40,`week4` smallint(6) default 40,
  `week5` smallint(6) default 40,`week6` smallint(6) default 40,
  `week7` smallint(6) default 40,`week8` smallint(6) default 40,
  `week9` smallint(6) default 40,`week10` smallint(6) default 40,
  `week11` smallint(6) default 40,`week12` smallint(6) default 40,
  `week13` smallint(6) default 40,`week14` smallint(6) default 40,
  `week15` smallint(6) default 40,`week16` smallint(6) default 40,
  `week17` smallint(6) default 40,`week18` smallint(6) default 40,
  `week19` smallint(6) default 40,`week20` smallint(6) default 40,
  `week21` smallint(6) default 40,`week22` smallint(6) default 40,
  `week23` smallint(6) default 40,`week24` smallint(6) default 40,
  `week25` smallint(6) default 40,`week26` smallint(6) default 40,
  `week27` smallint(6) default 40,`week28` smallint(6) default 40,
  `week29` smallint(6) default 40,`week30` smallint(6) default 40,
  `week31` smallint(6) default 40,`week32` smallint(6) default 40,
  `week33` smallint(6) default 40,`week34` smallint(6) default 40,
  `week35` smallint(6) default 40,`week36` smallint(6) default 40,
  `week37` smallint(6) default 40,`week38` smallint(6) default 40,
  `week39` smallint(6) default 40,`week40` smallint(6) default 40,
  `week41` smallint(6) default 40,`week42` smallint(6) default 40,
  `week43` smallint(6) default 40,`week44` smallint(6) default 40,
  `week45` smallint(6) default 40,`week46` smallint(6) default 40,
  `week47` smallint(6) default 40,`week48` smallint(6) default 40,
  `week49` smallint(6) default 40,`week50` smallint(6) default 40,
  `week51` smallint(6) default 40,`week52` smallint(6) default 40,
   PRIMARY KEY (`utilization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `week`
--
CREATE TABLE `week` (
  `Week_ID` tinyint(4) NOT NULL AUTO_INCREMENT,
  `Quarter_ID` tinyint(4) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Year_ID` smallint(6) NOT NULL,
  `Start` date NOT NULL,
  `End` date NOT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Week_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `year`
--
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

--
-- Dumping data for table `year`
--
INSERT INTO `year` VALUES (1,2017,'2018-12-31','2017-01-10','2017-08-16 10:32:53','ADMIN','2017-08-16 10:42:24',NULL);

--
-- Table structure for table `pem`
--
CREATE TABLE `PEM` (
  `PEM_Serial` varchar(11) NOT NULL,
  `Employee_Serial` varchar(11) NOT NULL,
  `Start_Date` date NOT NULL DEFAULT '0000-00-00',
  `End_Date` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`PEM_Serial`),
  UNIQUE KEY `UNQ_PEM` (`PEM_Serial`,`Employee_Serial`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `year`
--
INSERT INTO `PEM` VALUES ('P100Y2', 'P100Y2', '2017-09-18', '2017-12-31');



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
) ENGINE=InnoDB AUTO_INCREMENT=5357 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fy_template`
--

LOCK TABLES `fy_template` WRITE;
/*!40000 ALTER TABLE `fy_template` DISABLE KEYS */;
INSERT INTO `fy_template` VALUES (4945,26,'2017-01-02','8',0,''),(4946,26,'2017-01-03','8',0,''),(4947,26,'2017-01-04','8',0,''),(4948,26,'2017-01-05','8',0,''),(4949,26,'2017-01-06','8',0,''),(4950,26,'2017-01-07','',0,''),(4951,26,'2017-01-08','',0,''),(4952,26,'2017-01-09','8',0,''),(4953,26,'2017-01-10','8',0,''),(4954,26,'2017-01-11','8',0,''),(4955,26,'2017-01-12','8',0,''),(4956,26,'2017-01-13','8',0,''),(4957,26,'2017-01-14','',0,''),(4958,26,'2017-01-15','',0,''),(4959,26,'2017-01-16','8',0,''),(4960,26,'2017-01-17','8',0,''),(4961,26,'2017-01-18','8',0,''),(4962,26,'2017-01-19','8',0,''),(4963,26,'2017-01-20','8',0,''),(4964,26,'2017-01-21','',0,''),(4965,26,'2017-01-22','',0,''),(4966,26,'2017-01-23','8',0,''),(4967,26,'2017-01-24','8',0,''),(4968,26,'2017-01-25','8',0,''),(4969,26,'2017-01-26','8',0,''),(4970,26,'2017-01-27','8',0,''),(4971,26,'2017-01-28','',0,''),(4972,26,'2017-01-29','',0,''),(4973,26,'2017-01-30','8',0,''),(4974,26,'2017-01-31','8',0,''),(4975,26,'2017-02-01','8',0,''),(4976,26,'2017-02-02','8',0,''),(4977,26,'2017-02-03','8',0,''),(4978,26,'2017-02-04','',0,''),(4979,26,'2017-02-05','',0,''),(4980,26,'2017-02-06','8',0,''),(4981,26,'2017-02-07','8',0,''),(4982,26,'2017-02-08','8',0,''),(4983,26,'2017-02-09','8',0,''),(4984,26,'2017-02-10','8',0,''),(4985,26,'2017-02-11','',0,''),(4986,26,'2017-02-12','',0,''),(4987,26,'2017-02-13','8',0,''),(4988,26,'2017-02-14','8',0,''),(4989,26,'2017-02-15','8',0,''),(4990,26,'2017-02-16','8',0,''),(4991,26,'2017-02-17','8',0,''),(4992,26,'2017-02-18','',0,''),(4993,26,'2017-02-19','',0,''),(4994,26,'2017-02-20','8',0,''),(4995,26,'2017-02-21','8',0,''),(4996,26,'2017-02-22','8',0,''),(4997,26,'2017-02-23','8',0,''),(4998,26,'2017-02-24','8',0,''),(4999,26,'2017-02-25','',0,''),(5000,26,'2017-02-26','',0,''),(5001,26,'2017-02-27','8',0,''),(5002,26,'2017-02-28','8',0,''),(5003,26,'2017-03-01','8',0,''),(5004,26,'2017-03-02','8',0,''),(5005,26,'2017-03-03','8',0,''),(5006,26,'2017-03-04','',0,''),(5007,26,'2017-03-05','',0,''),(5008,26,'2017-03-06','8',0,''),(5009,26,'2017-03-07','8',0,''),(5010,26,'2017-03-08','8',0,''),(5011,26,'2017-03-09','8',0,''),(5012,26,'2017-03-10','8',0,''),(5013,26,'2017-03-11','',0,''),(5014,26,'2017-03-12','',0,''),(5015,26,'2017-03-13','8',0,''),(5016,26,'2017-03-14','8',0,''),(5017,26,'2017-03-15','8',0,''),(5018,26,'2017-03-16','8',0,''),(5019,26,'2017-03-17','8',0,''),(5020,26,'2017-03-18','',0,''),(5021,26,'2017-03-19','',0,''),(5022,26,'2017-03-20','8',0,''),(5023,26,'2017-03-21','8',0,''),(5024,26,'2017-03-22','8',0,''),(5025,26,'2017-03-23','8',0,''),(5026,26,'2017-03-24','8',0,''),(5027,26,'2017-03-25','',0,''),(5028,26,'2017-03-26','',0,''),(5029,26,'2017-03-27','8',0,''),(5030,26,'2017-03-28','8',0,''),(5031,26,'2017-03-29','8',0,''),(5032,26,'2017-03-30','8',0,''),(5033,26,'2017-03-31','8',0,''),(5034,26,'2017-04-01','',0,''),(5035,26,'2017-04-02','',0,''),(5036,26,'2017-04-03','8',0,''),(5037,26,'2017-04-04','8',0,''),(5038,26,'2017-04-05','8',0,''),(5039,26,'2017-04-06','8',0,''),(5040,26,'2017-04-07','8',0,''),(5041,26,'2017-04-08','',0,''),(5042,26,'2017-04-09','',0,''),(5043,26,'2017-04-10','8',0,''),(5044,26,'2017-04-11','8',0,''),(5045,26,'2017-04-12','8',0,''),(5046,26,'2017-04-13','8',0,''),(5047,26,'2017-04-14','8',0,''),(5048,26,'2017-04-15','',0,''),(5049,26,'2017-04-16','',0,''),(5050,26,'2017-04-17','8',0,''),(5051,26,'2017-04-18','8',0,''),(5052,26,'2017-04-19','8',0,''),(5053,26,'2017-04-20','8',0,''),(5054,26,'2017-04-21','8',0,''),(5055,26,'2017-04-22','',0,''),(5056,26,'2017-04-23','',0,''),(5057,26,'2017-04-24','8',0,''),(5058,26,'2017-04-25','8',0,''),(5059,26,'2017-04-26','8',0,''),(5060,26,'2017-04-27','8',0,''),(5061,26,'2017-04-28','8',0,''),(5062,26,'2017-04-29','',0,''),(5063,26,'2017-04-30','',0,''),(5064,26,'2017-05-01','8',0,''),(5065,26,'2017-05-02','8',0,''),(5066,26,'2017-05-03','8',0,''),(5067,26,'2017-05-04','8',0,''),(5068,26,'2017-05-05','8',0,''),(5069,26,'2017-05-06','',0,''),(5070,26,'2017-05-07','',0,''),(5071,26,'2017-05-08','8',0,''),(5072,26,'2017-05-09','8',0,''),(5073,26,'2017-05-10','8',0,''),(5074,26,'2017-05-11','8',0,''),(5075,26,'2017-05-12','8',0,''),(5076,26,'2017-05-13','',0,''),(5077,26,'2017-05-14','',0,''),(5078,26,'2017-05-15','8',0,''),(5079,26,'2017-05-16','8',0,''),(5080,26,'2017-05-17','8',0,''),(5081,26,'2017-05-18','8',0,''),(5082,26,'2017-05-19','8',0,''),(5083,26,'2017-05-20','',0,''),(5084,26,'2017-05-21','',0,''),(5085,26,'2017-05-22','8',0,''),(5086,26,'2017-05-23','8',0,''),(5087,26,'2017-05-24','8',0,''),(5088,26,'2017-05-25','8',0,''),(5089,26,'2017-05-26','8',0,''),(5090,26,'2017-05-27','',0,''),(5091,26,'2017-05-28','',0,''),(5092,26,'2017-05-29','8',0,''),(5093,26,'2017-05-30','8',0,''),(5094,26,'2017-05-31','8',0,''),(5095,26,'2017-06-01','8',0,''),(5096,26,'2017-06-02','8',0,''),(5097,26,'2017-06-03','',0,''),(5098,26,'2017-06-04','',0,''),(5099,26,'2017-06-05','8',0,''),(5100,26,'2017-06-06','8',0,''),(5101,26,'2017-06-07','8',0,''),(5102,26,'2017-06-08','8',0,''),(5103,26,'2017-06-09','8',0,''),(5104,26,'2017-06-10','',0,''),(5105,26,'2017-06-11','',0,''),(5106,26,'2017-06-12','8',0,''),(5107,26,'2017-06-13','8',0,''),(5108,26,'2017-06-14','8',0,''),(5109,26,'2017-06-15','8',0,''),(5110,26,'2017-06-16','8',0,''),(5111,26,'2017-06-17','',0,''),(5112,26,'2017-06-18','',0,''),(5113,26,'2017-06-19','8',0,''),(5114,26,'2017-06-20','8',0,''),(5115,26,'2017-06-21','8',0,''),(5116,26,'2017-06-22','8',0,''),(5117,26,'2017-06-23','8',0,''),(5118,26,'2017-06-24','',0,''),(5119,26,'2017-06-25','',0,''),(5120,26,'2017-06-26','8',0,''),(5121,26,'2017-06-27','8',0,''),(5122,26,'2017-06-28','8',0,''),(5123,26,'2017-06-29','8',0,''),(5124,26,'2017-06-30','8',0,''),(5125,26,'2017-07-01','',0,''),(5126,26,'2017-07-02','',0,''),(5127,26,'2017-07-03','8',0,''),(5128,26,'2017-07-04','8',0,''),(5129,26,'2017-07-05','8',0,''),(5130,26,'2017-07-06','8',0,''),(5131,26,'2017-07-07','8',0,''),(5132,26,'2017-07-08','',0,''),(5133,26,'2017-07-09','',0,''),(5134,26,'2017-07-10','8',0,''),(5135,26,'2017-07-11','8',0,''),(5136,26,'2017-07-12','8',0,''),(5137,26,'2017-07-13','8',0,''),(5138,26,'2017-07-14','8',0,''),(5139,26,'2017-07-15','',0,''),(5140,26,'2017-07-16','',0,''),(5141,26,'2017-07-17','8',0,''),(5142,26,'2017-07-18','8',0,''),(5143,26,'2017-07-19','8',0,''),(5144,26,'2017-07-20','8',0,''),(5145,26,'2017-07-21','8',0,''),(5146,26,'2017-07-22','',0,''),(5147,26,'2017-07-23','',0,''),(5148,26,'2017-07-24','8',0,''),(5149,26,'2017-07-25','8',0,''),(5150,26,'2017-07-26','8',0,''),(5151,26,'2017-07-27','8',0,''),(5152,26,'2017-07-28','8',0,''),(5153,26,'2017-07-29','',0,''),(5154,26,'2017-07-30','',0,''),(5155,26,'2017-07-31','8',0,''),(5156,26,'2017-08-01','8',0,''),(5157,26,'2017-08-02','8',0,''),(5158,26,'2017-08-03','8',0,''),(5159,26,'2017-08-04','8',0,''),(5160,26,'2017-08-05','',0,''),(5161,26,'2017-08-06','',0,''),(5162,26,'2017-08-07','8',0,''),(5163,26,'2017-08-08','8',0,''),(5164,26,'2017-08-09','8',0,''),(5165,26,'2017-08-10','8',0,''),(5166,26,'2017-08-11','8',0,''),(5167,26,'2017-08-12','',0,''),(5168,26,'2017-08-13','',0,''),(5169,26,'2017-08-14','8',0,''),(5170,26,'2017-08-15','8',0,''),(5171,26,'2017-08-16','8',0,''),(5172,26,'2017-08-17','8',0,''),(5173,26,'2017-08-18','8',0,''),(5174,26,'2017-08-19','',0,''),(5175,26,'2017-08-20','',0,''),(5176,26,'2017-08-21','8',0,''),(5177,26,'2017-08-22','8',0,''),(5178,26,'2017-08-23','8',0,''),(5179,26,'2017-08-24','8',0,''),(5180,26,'2017-08-25','8',0,''),(5181,26,'2017-08-26','',0,''),(5182,26,'2017-08-27','',0,''),(5183,26,'2017-08-28','8',0,''),(5184,26,'2017-08-29','8',0,''),(5185,26,'2017-08-30','8',0,''),(5186,26,'2017-08-31','8',0,''),(5187,26,'2017-09-01','8',0,''),(5188,26,'2017-09-02','',0,''),(5189,26,'2017-09-03','',0,''),(5190,26,'2017-09-04','8',0,''),(5191,26,'2017-09-05','8',0,''),(5192,26,'2017-09-06','8',0,''),(5193,26,'2017-09-07','8',0,''),(5194,26,'2017-09-08','8',0,''),(5195,26,'2017-09-09','',0,''),(5196,26,'2017-09-10','',0,''),(5197,26,'2017-09-11','8',0,''),(5198,26,'2017-09-12','8',0,''),(5199,26,'2017-09-13','8',0,''),(5200,26,'2017-09-14','8',0,''),(5201,26,'2017-09-15','8',0,''),(5202,26,'2017-09-16','',0,''),(5203,26,'2017-09-17','',0,''),(5204,26,'2017-09-18','8',0,''),(5205,26,'2017-09-19','8',0,''),(5206,26,'2017-09-20','8',0,''),(5207,26,'2017-09-21','8',0,''),(5208,26,'2017-09-22','8',0,''),(5209,26,'2017-09-23','',0,''),(5210,26,'2017-09-24','',0,''),(5211,26,'2017-09-25','8',0,''),(5212,26,'2017-09-26','8',0,''),(5213,26,'2017-09-27','8',0,''),(5214,26,'2017-09-28','8',0,''),(5215,26,'2017-09-29','8',0,''),(5216,26,'2017-09-30','',0,''),(5217,26,'2017-10-01','',0,''),(5218,26,'2017-10-02','8',0,''),(5219,26,'2017-10-03','8',0,''),(5220,26,'2017-10-04','8',0,''),(5221,26,'2017-10-05','8',0,''),(5222,26,'2017-10-06','8',0,''),(5223,26,'2017-10-07','',0,''),(5224,26,'2017-10-08','',0,''),(5225,26,'2017-10-09','8',0,''),(5226,26,'2017-10-10','8',0,''),(5227,26,'2017-10-11','8',0,''),(5228,26,'2017-10-12','8',0,''),(5229,26,'2017-10-13','8',0,''),(5230,26,'2017-10-14','',0,''),(5231,26,'2017-10-15','',0,''),(5232,26,'2017-10-16','8',0,''),(5233,26,'2017-10-17','8',0,''),(5234,26,'2017-10-18','8',0,''),(5235,26,'2017-10-19','8',0,''),(5236,26,'2017-10-20','8',0,''),(5237,26,'2017-10-21','',0,''),(5238,26,'2017-10-22','',0,''),(5239,26,'2017-10-23','8',0,''),(5240,26,'2017-10-24','8',0,''),(5241,26,'2017-10-25','8',0,''),(5242,26,'2017-10-26','8',0,''),(5243,26,'2017-10-27','8',0,''),(5244,26,'2017-10-28','',0,''),(5245,26,'2017-10-29','',0,''),(5246,26,'2017-10-30','8',0,''),(5247,26,'2017-10-31','8',0,''),(5248,26,'2017-11-01','8',0,''),(5249,26,'2017-11-02','8',0,''),(5250,26,'2017-11-03','8',0,''),(5251,26,'2017-11-04','',0,''),(5252,26,'2017-11-05','',0,''),(5253,26,'2017-11-06','8',0,''),(5254,26,'2017-11-07','8',0,''),(5255,26,'2017-11-08','8',0,''),(5256,26,'2017-11-09','8',0,''),(5257,26,'2017-11-10','8',0,''),(5258,26,'2017-11-11','',0,''),(5259,26,'2017-11-12','',0,''),(5260,26,'2017-11-13','8',0,''),(5261,26,'2017-11-14','8',0,''),(5262,26,'2017-11-15','8',0,''),(5263,26,'2017-11-16','8',0,''),(5264,26,'2017-11-17','8',0,''),(5265,26,'2017-11-18','',0,''),(5266,26,'2017-11-19','',0,''),(5267,26,'2017-11-20','8',0,''),(5268,26,'2017-11-21','8',0,''),(5269,26,'2017-11-22','8',0,''),(5270,26,'2017-11-23','8',0,''),(5271,26,'2017-11-24','8',0,''),(5272,26,'2017-11-25','',0,''),(5273,26,'2017-11-26','',0,''),(5274,26,'2017-11-27','8',0,''),(5275,26,'2017-11-28','8',0,''),(5276,26,'2017-11-29','8',0,''),(5277,26,'2017-11-30','8',0,''),(5278,26,'2017-12-01','8',0,''),(5279,26,'2017-12-02','',0,''),(5280,26,'2017-12-03','',0,''),(5281,26,'2017-12-04','8',0,''),(5282,26,'2017-12-05','8',0,''),(5283,26,'2017-12-06','8',0,''),(5284,26,'2017-12-07','8',0,''),(5285,26,'2017-12-08','8',0,''),(5286,26,'2017-12-09','',0,''),(5287,26,'2017-12-10','',0,''),(5288,26,'2017-12-11','8',0,''),(5289,26,'2017-12-12','8',0,''),(5290,26,'2017-12-13','8',0,''),(5291,26,'2017-12-14','8',0,''),(5292,26,'2017-12-15','8',0,''),(5293,26,'2017-12-16','',0,''),(5294,26,'2017-12-17','',0,''),(5295,26,'2017-12-18','8',0,''),(5296,26,'2017-12-19','8',0,''),(5297,26,'2017-12-20','8',0,''),(5298,26,'2017-12-21','8',0,''),(5299,26,'2017-12-22','8',0,''),(5300,26,'2017-12-23','',0,''),(5301,26,'2017-12-24','',0,''),(5302,26,'2017-12-25','8',0,''),(5303,26,'2017-12-26','8',0,''),(5304,26,'2017-12-27','8',0,''),(5305,26,'2017-12-28','8',0,''),(5306,26,'2017-12-29','8',0,''),(5307,27,'2017-01-02','8',0,''),(5308,27,'2017-01-03','8',0,''),(5309,27,'2017-01-04','8',0,''),(5310,27,'2017-01-05','8',0,''),(5311,27,'2017-01-06','8',0,''),(5312,27,'2017-01-07','',0,''),(5313,27,'2017-01-08','',0,''),(5314,27,'2017-01-09','8',0,''),(5315,27,'2017-01-10','8',0,''),(5316,27,'2017-01-11','8',0,''),(5317,27,'2017-01-12','8',0,''),(5318,27,'2017-01-13','8',0,''),(5319,27,'2017-01-14','',0,''),(5320,27,'2017-01-15','',0,''),(5321,27,'2017-01-16','8',0,''),(5322,27,'2017-01-17','8',0,''),(5323,27,'2017-01-18','8',0,''),(5324,27,'2017-01-19','8',0,''),(5325,27,'2017-01-20','8',0,''),(5326,27,'2017-01-21','',0,''),(5327,27,'2017-01-22','',0,''),(5328,27,'2017-01-23','8',0,''),(5329,27,'2017-01-24','8',0,''),(5330,27,'2017-01-25','8',0,''),(5331,27,'2017-01-26','8',0,''),(5332,27,'2017-01-27','8',0,''),(5333,27,'2017-01-28','',0,''),(5334,27,'2017-01-29','',0,''),(5335,27,'2017-01-30','8',0,''),(5336,27,'2017-01-31','8',0,''),(5337,27,'2017-02-01','8',0,''),(5338,27,'2017-02-02','8',0,''),(5339,27,'2017-02-03','8',0,''),(5340,27,'2017-02-04','',0,''),(5341,27,'2017-02-05','',0,''),(5342,27,'2017-02-06','8',0,''),(5343,27,'2017-02-07','8',0,''),(5344,27,'2017-02-08','8',0,''),(5345,27,'2017-02-09','8',0,''),(5346,27,'2017-02-10','8',0,''),(5347,27,'2017-02-11','',0,''),(5348,27,'2017-02-12','',0,''),(5349,27,'2017-02-13','8',0,''),(5350,27,'2017-02-14','8',0,''),(5351,27,'2017-02-15','8',0,''),(5352,27,'2017-02-16','8',0,''),(5353,27,'2017-02-17','8',0,''),(5354,27,'2017-02-18','',0,''),(5355,27,'2017-02-19','',0,''),(5356,27,'2017-02-20','8',0,'');
/*!40000 ALTER TABLE `fy_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `month`
--
DROP TABLE IF EXISTS `month`;

CREATE TABLE `month` (
  `Month_ID` smallint(3) NOT NULL,
  `Month_Name` char(15) NOT NULL,
  `End_Date` date NOT NULL,
  `Year_ID` smallint(6) NOT NULL,
  UNIQUE KEY `month_UNIQUE` (`Month_ID`,`Year_ID`),
  KEY `Month_ID_idx` (`Month_ID`),
  KEY `Year_ID_idx` (`Year_ID`),
  CONSTRAINT `FK_Year_ID` FOREIGN KEY (`Year_ID`) REFERENCES `year` (`Year_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;   

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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_leave`
--

LOCK TABLES `employee_leave` WRITE;
/*!40000 ALTER TABLE `employee_leave` DISABLE KEYS */;
INSERT INTO `employee_leave` VALUES (1,'123456PH1',1,'Approved','2017-01-02','VL','2017-11-28 16:00:00.000000','2017-11-28 16:00:00.000000',NULL),(2,'123456PH1',1,'Pending','2017-01-03','VL',NULL,NULL,NULL),(49,'1',23,'approved','2017-01-03','VL',NULL,NULL,NULL),(52,'1',26,'approved','2017-01-02','RC','2017-12-18 14:00:34.149000','2017-12-18 14:00:34.149000',12),(54,'1',26,'pending','2017-01-04','CDO','2017-12-18 14:21:01.694000','2017-12-18 14:21:01.694000',0);
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
  `Employee_ID` varchar(45) NOT NULL,
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
-- Views
--
CREATE OR REPLACE VIEW manager_employee_v AS
    SELECT employee.Manager_ID, employee.Employee_ID 
    FROM employee;
    
CREATE OR REPLACE VIEW employee_role_v AS
    SELECT employee_role.Employee_ID, role.Name from employee_role 
    INNER JOIN role on employee_role.Role_ID = role.Role_ID;      

CREATE OR REPLACE VIEW employee_project_v AS
    SELECT employee_project.Employee_ID, project.Name from employee_project 
    INNER JOIN project on employee_project.Project_ID = project.Project_ID;  

CREATE OR REPLACE VIEW employee_team_v AS
    SELECT employee.Employee_ID, team.Team_ID, team.Name, team.Team_Lead_Employee_ID from employee 
    LEFT OUTER JOIN employee_team on employee_team.Employee_ID = employee.Employee_ID
    LEFT OUTER JOIN team on team.Team_ID = employee_team.Team_ID
    ORDER BY employee.Employee_ID ASC;
    
CREATE OR REPLACE VIEW team_employee_v AS
    SELECT team.Team_ID, team.Name, team.Team_Lead_Employee_ID, employee.Employee_ID from team 
    LEFT OUTER JOIN employee_team on team.Team_ID = employee_team.Team_ID
    LEFT OUTER JOIN employee on employee.Employee_ID = employee_team.Employee_ID
    ORDER BY team.Team_ID ASC;
    
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
        `fy`.`Year_ID`,
        `el`.`employee_leave_id`,
        `el`.`Status`,
        (CASE
            WHEN (`el`.`Status` = 'Approved' && `el`.`Leave_Type` != 'RC') THEN 0
            WHEN (`el`.`Status` = 'Approved' && `el`.`Leave_Type` = 'RC') THEN `el`.`Hours`
            ELSE `fy`.`Value`
        END) AS `Hours`,
        (CASE
            WHEN (`el`.`Status` = 'Approved' ||  `el`.`Status`='pending' || `el`.`Status`='draft') THEN `el`.`Leave_Type`
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

/*!40014 SET FOREIGN_KEY_CHECKS=1 */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*!50003 DROP PROCEDURE IF EXISTS `getCombinedUtilization` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getCombinedUtilization`(IN employeeId varchar(40), IN currFY INT)
BEGIN

SELECT EMPLOYEE_SERIAL, YEAR_ID,
(CASE WHEN week1 = 0 then (SELECT week1 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week1 END) AS week1,
(CASE WHEN week2 = 0 then (SELECT week2 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week2 END) AS week2,
(CASE WHEN week3 = 0 then (SELECT week3 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week3 END) AS week3,
(CASE WHEN week4 = 0 then (SELECT week4 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week4 END) AS week4,
(CASE WHEN week5 = 0 then (SELECT week5 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week5 END) AS week5,
(CASE WHEN week6 = 0 then (SELECT week6 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week6 END) AS week6,
(CASE WHEN week7 = 0 then (SELECT week7 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week7 END) AS week7,
(CASE WHEN week8 = 0 then (SELECT week8 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week8 END) AS week8,
(CASE WHEN week9 = 0 then (SELECT week9 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week9 END) AS week9,
(CASE WHEN week10 = 0 then (SELECT week10 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week10 END) AS week10,
(CASE WHEN week11 = 0 then (SELECT week11 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week11 END) AS week11,
(CASE WHEN week12 = 0 then (SELECT week12 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week12 END) AS week12,
(CASE WHEN week13 = 0 then (SELECT week13 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week13 END) AS week13,
(CASE WHEN week14 = 0 then (SELECT week14 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week14 END) AS week14,
(CASE WHEN week15 = 0 then (SELECT week15 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week15 END) AS week15,
(CASE WHEN week16 = 0 then (SELECT week16 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week16 END) AS week16,
(CASE WHEN week17 = 0 then (SELECT week17 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week17 END) AS week17,
(CASE WHEN week18 = 0 then (SELECT week18 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week18 END) AS week18,
(CASE WHEN week19 = 0 then (SELECT week19 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week19 END) AS week19,
(CASE WHEN week20 = 0 then (SELECT week20 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week20 END) AS week20,
(CASE WHEN week21 = 0 then (SELECT week21 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week21 END) AS week21,
(CASE WHEN week22 = 0 then (SELECT week22 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week22 END) AS week22,
(CASE WHEN week23 = 0 then (SELECT week23 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week23 END) AS week23,
(CASE WHEN week24 = 0 then (SELECT week24 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week24 END) AS week24,
(CASE WHEN week25 = 0 then (SELECT week25 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week25 END) AS week25,
(CASE WHEN week26 = 0 then (SELECT week26 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week26 END) AS week26,
(CASE WHEN week27 = 0 then (SELECT week27 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week27 END) AS week27,
(CASE WHEN week28 = 0 then (SELECT week28 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week28 END) AS week28,
(CASE WHEN week29 = 0 then (SELECT week29 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week29 END) AS week29,
(CASE WHEN week30 = 0 then (SELECT week30 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week30 END) AS week30,
(CASE WHEN week31 = 0 then (SELECT week31 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week31 END) AS week31,
(CASE WHEN week32 = 0 then (SELECT week32 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week32 END) AS week32,
(CASE WHEN week33 = 0 then (SELECT week33 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week33 END) AS week33,
(CASE WHEN week34 = 0 then (SELECT week34 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week34 END) AS week34,
(CASE WHEN week35= 0 then (SELECT week35 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week35 END) AS week35,
(CASE WHEN week36 = 0 then (SELECT week36 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week36 END) AS week36,
(CASE WHEN week37 = 0 then (SELECT week37 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week37 END) AS week37,
(CASE WHEN week38 = 0 then (SELECT week38 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week38 END) AS week38,
(CASE WHEN week39 = 0 then (SELECT week39 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week39 END) AS week39,
(CASE WHEN week40 = 0 then (SELECT week40 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week40 END) AS week40,
(CASE WHEN week41 = 0 then (SELECT week41 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week41 END) AS week41,
(CASE WHEN week42 = 0 then (SELECT week42 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week42 END) AS week42,
(CASE WHEN week43 = 0 then (SELECT week43 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week43 END) AS week43,
(CASE WHEN week44 = 0 then (SELECT week44 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week44 END) AS week44,
(CASE WHEN week45 = 0 then (SELECT week45 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week45 END) AS week45,
(CASE WHEN week46 = 0 then (SELECT week46 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week46 END) AS week46,
(CASE WHEN week47 = 0 then (SELECT week47 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week47 END) AS week47,
(CASE WHEN week48 = 0 then (SELECT week48 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week48 END) AS week48,
(CASE WHEN week49 = 0 then (SELECT week49 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week49 END) AS week49,
(CASE WHEN week50 = 0 then (SELECT week50 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week50 END) AS week50,
(CASE WHEN week51 = 0 then (SELECT week51 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week51 END) AS week51,
(CASE WHEN week52 = 0 then (SELECT week52 FROM UTILIZATION WHERE TYPE = 'FORECAST' AND EMPLOYEE_SERIAL = employeeId) ELSE week52 END) AS week52  
FROM UTILIZATION
WHERE EMPLOYEE_SERIAL = employeeId
AND TYPE = 'ACTUAL'
AND YEAR_ID = currFY;

END ;;
DELIMITER ;

-- Dump completed on 2017-08-16 22:30:27
