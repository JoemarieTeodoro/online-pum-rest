-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: opum
-- ------------------------------------------------------
-- Server version	5.7.19-log


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
  `IsAdmin` tinyint(1) NOT NULL DEFAULT '0',
  `FullName` varchar(60) DEFAULT NULL,
  `Password` tinytext,
  `IsActive` tinyint(1) NOT NULL DEFAULT '0',
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` varchar(45) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UpdatedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Employee_ID`),
  UNIQUE KEY `Employee_ID_Email_UNIQUE` (`Employee_ID`,`Email`),
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
	('123456PH1','admin@ph.ibm.com', '123456PH1', NULL,NULL,NULL,NULL,1,'Admin','47b82bd4ffff817f1ce3ffffa55bffffd52bffffb44cffffbc446c9348b77e81fffffa06ffff986848b71de237c8',0,'2017-08-08 14:19:52','ADMIN','2017-08-08 19:27:13','ADMIN'),
	('121212PH1','magdanc@ph.ibm.com', '123456PH1', NULL,NULL,NULL,NULL,0,'Claude Magdangal',NULL,0,'2017-08-16 08:42:23','ADMIN','2017-08-16 08:42:23',NULL),
	('131313PH1','aumana@ph.ibm.com', '123456PH1', NULL,NULL,NULL,NULL,0,'Aldaina Auman','1fe02bd4ffffb947619effffac5458a7075aa5ffff966a4ab50714eb2cd322dd4ab56f90',0,'2017-08-16 08:42:23','ADMIN','2017-08-16 13:36:15','aumana@ph.ibm.com'),
	('141414PH1', 'admin', '123456PH1', NULL,NULL,NULL,NULL,1,'admin','20df22dd2ed128d7798656a9ffffa55bffffa75942bdffff897749b60e49b6ffff80801ee1ffffc33d',0,'2017-10-25 03:23:16','ADMIN','2017-10-25 03:23:16','ADMIN');
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
	('123456PH1', 1),
    ('141414PH1', 2),
    ('121212PH1', 4),
    ('121212PH1', 5),
    ('131313PH1', 5);

--
-- Table structure for mapping table `employee_team`
--
CREATE TABLE `employee_team` (
  `Employee_ID` char(9) NOT NULL,
  `Team_ID` smallint(2) NOT NULL,
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Team_ID` (`Team_ID`),
  INDEX `Team_ID_idx` (`Team_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `employee_team` VALUES 
	('123456PH1', 1),
    ('141414PH1', 2),
    ('121212PH1', 4),
    ('121212PH1', 5),
    ('131313PH1', 5);

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
	('123456PH1', 1),
    ('141414PH1', 2),
    ('121212PH1', 4),
    ('121212PH1', 5),
    ('131313PH1', 5);

--
-- Table structure for table `team`
--
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for `team`
--
INSERT INTO `team` VALUES 
	(1, 1, '123456PH1', 'Team A', '2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),
    (2, 1, '131313PH1', 'Team B', '2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),
    (3, 1, '131313PH1', 'Team C', '2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),
    (4, 1, '1ASDASPH1', 'Team D', '2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),
	(5, 1, '121212PH1', 'Team E', '2017-08-16 13:36:15','ADMIN','2017-08-16 14:04:54','ADMIN');
    
--
-- Table structure for table `holiday`
--
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
	(12,1,'123456PH1','2017-08-01','2018-09-27','2017-08-16 08:42:23','ADMIN','2017-08-16 14:05:57','ADMIN'),
	(14,1,'121212PH1','2017-08-16','2017-08-31','2017-08-16 13:36:15','ADMIN','2017-08-16 14:04:54','ADMIN');

--
-- Table structure for table `quarter`
--
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

--
-- Table structure for table `utilization`
--
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

--
-- Dumping data for table `utilization`
--
INSERT INTO `utilization` VALUES 
	(9,'27',2017,'{\"utilization_JSON\":[{\"month\":8,\"dayOfMonth\":16,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":8,\"dayOfMonth\":17,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":8,\"dayOfMonth\":18,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":8,\"dayOfMonth\":19,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":8,\"dayOfMonth\":20,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":8,\"dayOfMonth\":21,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":8,\"dayOfMonth\":22,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":8,\"dayOfMonth\":23,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":8,\"dayOfMonth\":24,\"editable\":\"\",\"utilizationHours\":\"VL\",\"day\":5},{\"month\":8,\"dayOfMonth\":25,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":8,\"dayOfMonth\":26,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":8,\"dayOfMonth\":27,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":8,\"dayOfMonth\":28,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":8,\"dayOfMonth\":29,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":8,\"dayOfMonth\":30,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":8,\"dayOfMonth\":31,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":1,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":9,\"dayOfMonth\":2,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":3,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":4,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":9,\"dayOfMonth\":5,\"editable\":\"\",\"utilizationHours\":\"SL\",\"day\":3},{\"month\":9,\"dayOfMonth\":6,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":7,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":8,\"editable\":\"\",\"utilizationHours\":\"CDO\",\"day\":6},{\"month\":9,\"dayOfMonth\":9,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":10,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":11,\"editable\":\"\",\"utilizationHours\":\"EL\",\"day\":2},{\"month\":9,\"dayOfMonth\":12,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":9,\"dayOfMonth\":13,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":14,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":15,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":9,\"dayOfMonth\":16,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":17,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":18,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":2},{\"month\":9,\"dayOfMonth\":19,\"editable\":\"\",\"utilizationHours\":\"OL\",\"day\":3},{\"month\":9,\"dayOfMonth\":20,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":21,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":22,\"editable\":\"\",\"utilizationHours\":\"TR\",\"day\":6},{\"month\":9,\"dayOfMonth\":23,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7},{\"month\":9,\"dayOfMonth\":24,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":1},{\"month\":9,\"dayOfMonth\":25,\"editable\":\"\",\"utilizationHours\":\"HO\",\"day\":2},{\"month\":9,\"dayOfMonth\":26,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":3},{\"month\":9,\"dayOfMonth\":27,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":4},{\"month\":9,\"dayOfMonth\":28,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":5},{\"month\":9,\"dayOfMonth\":29,\"editable\":\"\",\"utilizationHours\":\"8\",\"day\":6},{\"month\":9,\"dayOfMonth\":30,\"editable\":\"\",\"utilizationHours\":\"\",\"day\":7}],\"year\":2017}','2017-08-16 13:39:40','27','2017-08-16 13:39:40','27');

--
-- Table structure for table `week`
--
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

/*!40014 SET FOREIGN_KEY_CHECKS=1 */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-16 22:30:27
