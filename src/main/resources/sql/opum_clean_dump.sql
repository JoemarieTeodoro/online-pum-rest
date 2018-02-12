CREATE DATABASE  IF NOT EXISTS `opum` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `opum`;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


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

INSERT INTO `employee_role` VALUES ('123456PH1', 1);

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

INSERT INTO `employee_team` VALUES ('123456PH1', 1, '2017-08-08','2020-01-30','A');

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

INSERT INTO `employee_project` VALUES ('123456PH1', 1);

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
INSERT INTO `team` VALUES (1, 1, '123456PH1', 'Team A', 'Y', '2017-08-16 08:42:23','ADMINISTRATOR','2017-08-16 14:05:57','ADMINISTRATOR');
    
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `Leave_Date` date NOT NULL UNIQUE,
  `Leave_Type` varchar(45) NOT NULL,
  `CreateDate` timestamp(6) NULL DEFAULT NULL,
  `UpdateDate` timestamp(6) NULL DEFAULT NULL,
  `Hours` int(11) DEFAULT NULL,
  PRIMARY KEY (`Employee_Leave_ID`),
  KEY `HoursIdx` (`Employee_ID`,`Leave_Date`,`Leave_Type`,`Status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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

-- Dump completed on 2018-01-23 22:30:27