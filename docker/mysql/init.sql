-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: argus_clone
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) DEFAULT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `syllabus_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKtey8a1clsfsa5duxqjysxyns8` (`syllabus_id`),
  CONSTRAINT `FK44s12qsgqol24llop5vxhcm6t` FOREIGN KEY (`syllabus_id`) REFERENCES `syllabus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (12,'S046','Algorithms and Data Structures',16);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_group`
--

DROP TABLE IF EXISTS `course_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkt5730n2360qbi88t2wkdpsyp` (`course_id`),
  CONSTRAINT `FKkt5730n2360qbi88t2wkdpsyp` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_group`
--

LOCK TABLES `course_group` WRITE;
/*!40000 ALTER TABLE `course_group` DISABLE KEYS */;
INSERT INTO `course_group` VALUES (16,12,'Group 1'),(17,12,'Group 2'),(18,12,'Group 3');
/*!40000 ALTER TABLE `course_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_instructor`
--

DROP TABLE IF EXISTS `course_instructor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_instructor` (
  `course_id` int NOT NULL,
  `instructor_id` int NOT NULL,
  KEY `FKltori8qni3ivrlovca4gd9yw3` (`instructor_id`),
  KEY `FKeqej22fgwa29i98ucd9x9ycie` (`course_id`),
  CONSTRAINT `FKeqej22fgwa29i98ucd9x9ycie` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FKltori8qni3ivrlovca4gd9yw3` FOREIGN KEY (`instructor_id`) REFERENCES `instructor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_instructor`
--

LOCK TABLES `course_instructor` WRITE;
/*!40000 ALTER TABLE `course_instructor` DISABLE KEYS */;
INSERT INTO `course_instructor` VALUES (12,5);
/*!40000 ALTER TABLE `course_instructor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_student`
--

DROP TABLE IF EXISTS `group_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_student` (
  `group_id` int NOT NULL,
  `student_id` int NOT NULL,
  KEY `FKhsowqx64bm8qxnje3a4avlbh5` (`student_id`),
  KEY `FK2yftc7cidpyv35suqlmwxsh9o` (`group_id`),
  CONSTRAINT `FK2yftc7cidpyv35suqlmwxsh9o` FOREIGN KEY (`group_id`) REFERENCES `course_group` (`id`),
  CONSTRAINT `FKhsowqx64bm8qxnje3a4avlbh5` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_student`
--

LOCK TABLES `group_student` WRITE;
/*!40000 ALTER TABLE `group_student` DISABLE KEYS */;
INSERT INTO `group_student` VALUES (17,9),(16,8),(16,11),(18,12);
/*!40000 ALTER TABLE `group_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instructor`
--

DROP TABLE IF EXISTS `instructor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instructor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `academic_rank` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructor`
--

LOCK TABLES `instructor` WRITE;
/*!40000 ALTER TABLE `instructor` DISABLE KEYS */;
INSERT INTO `instructor` VALUES (5,'Invited lecturer','guga.rukhadze@iliauni.edu.ge','Guga Rukhadze');
/*!40000 ALTER TABLE `instructor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecture`
--

DROP TABLE IF EXISTS `lecture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lecture_date` date DEFAULT NULL,
  `lecture_end_time` time(6) DEFAULT NULL,
  `lecture_start_time` time(6) DEFAULT NULL,
  `room_number` varchar(255) DEFAULT NULL,
  `group_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_lecture_slot` (`lecture_date`,`lecture_start_time`,`lecture_end_time`,`room_number`),
  KEY `FK7lri5ksdkeb4y3ls0lf7e4lc8` (`group_id`),
  CONSTRAINT `FK7lri5ksdkeb4y3ls0lf7e4lc8` FOREIGN KEY (`group_id`) REFERENCES `course_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=341 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecture`
--

LOCK TABLES `lecture` WRITE;
/*!40000 ALTER TABLE `lecture` DISABLE KEYS */;
INSERT INTO `lecture` VALUES (1,'2025-09-29','09:50:00.000000','09:00:00.000000','T405',16),(2,'2025-09-29','10:50:00.000000','10:00:00.000000','T405',16),(3,'2025-09-29','11:50:00.000000','11:00:00.000000','T405',16),(4,'2025-10-06','09:50:00.000000','09:00:00.000000','T405',16),(5,'2025-10-06','10:50:00.000000','10:00:00.000000','T405',16),(6,'2025-10-06','11:50:00.000000','11:00:00.000000','T405',16),(7,'2025-10-13','09:50:00.000000','09:00:00.000000','T405',16),(8,'2025-10-13','10:50:00.000000','10:00:00.000000','T405',16),(9,'2025-10-13','11:50:00.000000','11:00:00.000000','T405',16),(10,'2025-10-20','09:50:00.000000','09:00:00.000000','T405',16),(11,'2025-10-20','10:50:00.000000','10:00:00.000000','T405',16),(12,'2025-10-20','11:50:00.000000','11:00:00.000000','T405',16),(13,'2025-10-27','09:50:00.000000','09:00:00.000000','T405',16),(14,'2025-10-27','10:50:00.000000','10:00:00.000000','T405',16),(15,'2025-10-27','11:50:00.000000','11:00:00.000000','T405',16),(16,'2025-11-03','09:50:00.000000','09:00:00.000000','T405',16),(17,'2025-11-03','10:50:00.000000','10:00:00.000000','T405',16),(18,'2025-11-03','11:50:00.000000','11:00:00.000000','T405',16),(19,'2025-11-10','09:50:00.000000','09:00:00.000000','T405',16),(20,'2025-11-10','10:50:00.000000','10:00:00.000000','T405',16),(21,'2025-11-10','11:50:00.000000','11:00:00.000000','T405',16),(22,'2025-11-17','09:50:00.000000','09:00:00.000000','T405',16),(23,'2025-11-17','10:50:00.000000','10:00:00.000000','T405',16),(24,'2025-11-17','11:50:00.000000','11:00:00.000000','T405',16),(25,'2025-11-24','09:50:00.000000','09:00:00.000000','T405',16),(26,'2025-11-24','10:50:00.000000','10:00:00.000000','T405',16),(27,'2025-11-24','11:50:00.000000','11:00:00.000000','T405',16),(28,'2025-12-01','09:50:00.000000','09:00:00.000000','T405',16),(29,'2025-12-01','10:50:00.000000','10:00:00.000000','T405',16),(30,'2025-12-01','11:50:00.000000','11:00:00.000000','T405',16),(31,'2025-12-08','09:50:00.000000','09:00:00.000000','T405',16),(32,'2025-12-08','10:50:00.000000','10:00:00.000000','T405',16),(33,'2025-12-08','11:50:00.000000','11:00:00.000000','T405',16),(34,'2025-12-15','09:50:00.000000','09:00:00.000000','T405',16),(35,'2025-12-15','10:50:00.000000','10:00:00.000000','T405',16),(36,'2025-12-15','11:50:00.000000','11:00:00.000000','T405',16),(37,'2025-12-22','09:50:00.000000','09:00:00.000000','T405',16),(38,'2025-12-22','10:50:00.000000','10:00:00.000000','T405',16),(39,'2025-12-22','11:50:00.000000','11:00:00.000000','T405',16),(118,'2025-09-29','12:50:00.000000','12:00:00.000000','T405',17),(119,'2025-09-29','13:50:00.000000','13:00:00.000000','T405',17),(120,'2025-09-29','14:50:00.000000','14:00:00.000000','T405',17),(121,'2025-10-06','12:50:00.000000','12:00:00.000000','T405',17),(122,'2025-10-06','13:50:00.000000','13:00:00.000000','T405',17),(123,'2025-10-06','14:50:00.000000','14:00:00.000000','T405',17),(124,'2025-10-13','12:50:00.000000','12:00:00.000000','T405',17),(125,'2025-10-13','13:50:00.000000','13:00:00.000000','T405',17),(126,'2025-10-13','14:50:00.000000','14:00:00.000000','T405',17),(127,'2025-10-20','12:50:00.000000','12:00:00.000000','T405',17),(128,'2025-10-20','13:50:00.000000','13:00:00.000000','T405',17),(129,'2025-10-20','14:50:00.000000','14:00:00.000000','T405',17),(130,'2025-10-27','12:50:00.000000','12:00:00.000000','T405',17),(131,'2025-10-27','13:50:00.000000','13:00:00.000000','T405',17),(132,'2025-10-27','14:50:00.000000','14:00:00.000000','T405',17),(133,'2025-11-03','12:50:00.000000','12:00:00.000000','T405',17),(134,'2025-11-03','13:50:00.000000','13:00:00.000000','T405',17),(135,'2025-11-03','14:50:00.000000','14:00:00.000000','T405',17),(136,'2025-11-10','12:50:00.000000','12:00:00.000000','T405',17),(137,'2025-11-10','13:50:00.000000','13:00:00.000000','T405',17),(138,'2025-11-10','14:50:00.000000','14:00:00.000000','T405',17),(139,'2025-11-17','12:50:00.000000','12:00:00.000000','T405',17),(140,'2025-11-17','13:50:00.000000','13:00:00.000000','T405',17),(141,'2025-11-17','14:50:00.000000','14:00:00.000000','T405',17),(142,'2025-11-24','12:50:00.000000','12:00:00.000000','T405',17),(143,'2025-11-24','13:50:00.000000','13:00:00.000000','T405',17),(144,'2025-11-24','14:50:00.000000','14:00:00.000000','T405',17),(145,'2025-12-01','12:50:00.000000','12:00:00.000000','T405',17),(146,'2025-12-01','13:50:00.000000','13:00:00.000000','T405',17),(147,'2025-12-01','14:50:00.000000','14:00:00.000000','T405',17),(148,'2025-12-08','12:50:00.000000','12:00:00.000000','T405',17),(149,'2025-12-08','13:50:00.000000','13:00:00.000000','T405',17),(150,'2025-12-08','14:50:00.000000','14:00:00.000000','T405',17),(151,'2025-12-15','12:50:00.000000','12:00:00.000000','T405',17),(152,'2025-12-15','13:50:00.000000','13:00:00.000000','T405',17),(153,'2025-12-15','14:50:00.000000','14:00:00.000000','T405',17),(154,'2025-12-22','12:50:00.000000','12:00:00.000000','T405',17),(155,'2025-12-22','13:50:00.000000','13:00:00.000000','T405',17),(156,'2025-12-22','14:50:00.000000','14:00:00.000000','T405',17),(302,'2025-09-29','15:50:00.000000','15:00:00.000000',NULL,18),(303,'2025-09-29','16:50:00.000000','16:00:00.000000',NULL,18),(304,'2025-09-29','17:50:00.000000','17:00:00.000000',NULL,18),(305,'2025-10-06','15:50:00.000000','15:00:00.000000',NULL,18),(306,'2025-10-06','16:50:00.000000','16:00:00.000000',NULL,18),(307,'2025-10-06','17:50:00.000000','17:00:00.000000',NULL,18),(308,'2025-10-13','15:50:00.000000','15:00:00.000000',NULL,18),(309,'2025-10-13','16:50:00.000000','16:00:00.000000',NULL,18),(310,'2025-10-13','17:50:00.000000','17:00:00.000000',NULL,18),(311,'2025-10-20','15:50:00.000000','15:00:00.000000',NULL,18),(312,'2025-10-20','16:50:00.000000','16:00:00.000000',NULL,18),(313,'2025-10-20','17:50:00.000000','17:00:00.000000',NULL,18),(314,'2025-10-27','15:50:00.000000','15:00:00.000000',NULL,18),(315,'2025-10-27','16:50:00.000000','16:00:00.000000',NULL,18),(316,'2025-10-27','17:50:00.000000','17:00:00.000000',NULL,18),(317,'2025-11-03','15:50:00.000000','15:00:00.000000',NULL,18),(318,'2025-11-03','16:50:00.000000','16:00:00.000000',NULL,18),(319,'2025-11-03','17:50:00.000000','17:00:00.000000',NULL,18),(320,'2025-11-10','15:50:00.000000','15:00:00.000000',NULL,18),(321,'2025-11-10','16:50:00.000000','16:00:00.000000',NULL,18),(322,'2025-11-10','17:50:00.000000','17:00:00.000000',NULL,18),(323,'2025-11-17','15:50:00.000000','15:00:00.000000',NULL,18),(324,'2025-11-17','16:50:00.000000','16:00:00.000000',NULL,18),(325,'2025-11-17','17:50:00.000000','17:00:00.000000',NULL,18),(326,'2025-11-24','15:50:00.000000','15:00:00.000000',NULL,18),(327,'2025-11-24','16:50:00.000000','16:00:00.000000',NULL,18),(328,'2025-11-24','17:50:00.000000','17:00:00.000000',NULL,18),(329,'2025-12-01','15:50:00.000000','15:00:00.000000',NULL,18),(330,'2025-12-01','16:50:00.000000','16:00:00.000000',NULL,18),(331,'2025-12-01','17:50:00.000000','17:00:00.000000',NULL,18),(332,'2025-12-08','15:50:00.000000','15:00:00.000000',NULL,18),(333,'2025-12-08','16:50:00.000000','16:00:00.000000',NULL,18),(334,'2025-12-08','17:50:00.000000','17:00:00.000000',NULL,18),(335,'2025-12-15','15:50:00.000000','15:00:00.000000',NULL,18),(336,'2025-12-15','16:50:00.000000','16:00:00.000000',NULL,18),(337,'2025-12-15','17:50:00.000000','17:00:00.000000',NULL,18),(338,'2025-12-22','15:50:00.000000','15:00:00.000000',NULL,18),(339,'2025-12-22','16:50:00.000000','16:00:00.000000',NULL,18),(340,'2025-12-22','17:50:00.000000','17:00:00.000000',NULL,18);
/*!40000 ALTER TABLE `lecture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecture_seq`
--

DROP TABLE IF EXISTS `lecture_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecture_seq`
--

LOCK TABLES `lecture_seq` WRITE;
/*!40000 ALTER TABLE `lecture_seq` DISABLE KEYS */;
INSERT INTO `lecture_seq` VALUES (401);
/*!40000 ALTER TABLE `lecture_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `id` int NOT NULL AUTO_INCREMENT,
  `component` varchar(255) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `student_id` int DEFAULT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `student_name` varchar(255) DEFAULT NULL,
  `threshold` int DEFAULT NULL,
  `max_score` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4r2i87mwev058q4nvnl36latl` (`course_id`),
  KEY `FKnap51mbove93yjb09idc9jic6` (`student_id`),
  CONSTRAINT `FK4r2i87mwev058q4nvnl36latl` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FKnap51mbove93yjb09idc9jic6` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (1,'Midterm Exam 1',35,12,8,'Algorithms and Data Structures','Aleksandre Japharidze',NULL,35),(2,'Midterm Exam 2',34,12,8,'Algorithms and Data Structures','Aleksandre Japharidze',NULL,35),(3,'Final Exam',20,12,8,'Algorithms and Data Structures','Aleksandre Japharidze',15,30),(4,'Midterm Exam 1',NULL,12,11,'Algorithms and Data Structures','Lasha Ghurtskaia',NULL,35),(5,'Midterm Exam 2',NULL,12,11,'Algorithms and Data Structures','Lasha Ghurtskaia',NULL,35),(6,'Final Exam',NULL,12,11,'Algorithms and Data Structures','Lasha Ghurtskaia',15,30),(7,'Midterm Exam 1',NULL,12,9,'Algorithms and Data Structures','Dachi Baghashvili',NULL,35),(8,'Midterm Exam 2',NULL,12,9,'Algorithms and Data Structures','Dachi Baghashvili',NULL,35),(9,'Final Exam',NULL,12,9,'Algorithms and Data Structures','Dachi Baghashvili',15,30),(10,'Midterm Exam 1',NULL,12,12,'Algorithms and Data Structures','Islam Hesri',NULL,35),(11,'Midterm Exam 2',NULL,12,12,'Algorithms and Data Structures','Islam Hesri',NULL,35),(12,'Final Exam',NULL,12,12,'Algorithms and Data Structures','Islam Hesri',15,30);
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score_seq`
--

DROP TABLE IF EXISTS `score_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score_seq`
--

LOCK TABLES `score_seq` WRITE;
/*!40000 ALTER TABLE `score_seq` DISABLE KEYS */;
INSERT INTO `score_seq` VALUES (101);
/*!40000 ALTER TABLE `score_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `academic_status` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (8,'Bachelor','aleksandre.japharidze.2@iliauni.edu.ge','Aleksandre Japharidze'),(9,'Bachelor','dachi.baghashvili.1@iliauni.edu.ge','Dachi Baghashvili'),(11,'Bachelor','lasha.ghurtskaia.1@iliauni.edu.ge','Lasha Ghurtskaia'),(12,'Bachelor','islam.hesri.1@iliauni.edu.ge','Islam Hesri');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_course_result`
--

DROP TABLE IF EXISTS `student_course_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course_result` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) DEFAULT NULL,
  `final_grade` int DEFAULT NULL,
  `has_passed` bit(1) DEFAULT NULL,
  `student_name` varchar(255) DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `student_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi5qr0knkninbyici0a7akue78` (`course_id`),
  KEY `FK6so59h08crb7q3nexf7qpvsdr` (`student_id`),
  CONSTRAINT `FK6so59h08crb7q3nexf7qpvsdr` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `FKi5qr0knkninbyici0a7akue78` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_course_result`
--

LOCK TABLES `student_course_result` WRITE;
/*!40000 ALTER TABLE `student_course_result` DISABLE KEYS */;
INSERT INTO `student_course_result` VALUES (1,'Algorithms and Data Structures',89,_binary '','Aleksandre Japharidze',12,8);
/*!40000 ALTER TABLE `student_course_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus`
--

DROP TABLE IF EXISTS `syllabus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syllabus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_mission` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus`
--

LOCK TABLES `syllabus` WRITE;
/*!40000 ALTER TABLE `syllabus` DISABLE KEYS */;
INSERT INTO `syllabus` VALUES (16,'The aim of this course is to give students a basic knowledge of DSA.');
/*!40000 ALTER TABLE `syllabus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus_course_schedule`
--

DROP TABLE IF EXISTS `syllabus_course_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syllabus_course_schedule` (
  `syllabus_id` int NOT NULL,
  `week` int NOT NULL,
  `position` int DEFAULT NULL,
  `activities` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`syllabus_id`,`week`),
  CONSTRAINT `FKacun4ikx7hs4b9hax2o3uek61` FOREIGN KEY (`syllabus_id`) REFERENCES `syllabus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus_course_schedule`
--

LOCK TABLES `syllabus_course_schedule` WRITE;
/*!40000 ALTER TABLE `syllabus_course_schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `syllabus_course_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus_grading_weights`
--

DROP TABLE IF EXISTS `syllabus_grading_weights`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syllabus_grading_weights` (
  `syllabus_id` int NOT NULL,
  `weight` int DEFAULT NULL,
  `component` varchar(255) NOT NULL,
  `position` int DEFAULT NULL,
  `threshold` int DEFAULT NULL,
  PRIMARY KEY (`syllabus_id`,`component`),
  CONSTRAINT `FK7lw668pdeeetjie9htb8g6qdj` FOREIGN KEY (`syllabus_id`) REFERENCES `syllabus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus_grading_weights`
--

LOCK TABLES `syllabus_grading_weights` WRITE;
/*!40000 ALTER TABLE `syllabus_grading_weights` DISABLE KEYS */;
INSERT INTO `syllabus_grading_weights` VALUES (16,30,'Final Exam',2,15),(16,35,'Midterm Exam 1',0,NULL),(16,35,'Midterm Exam 2',1,NULL);
/*!40000 ALTER TABLE `syllabus_grading_weights` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus_prerequisites`
--

DROP TABLE IF EXISTS `syllabus_prerequisites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syllabus_prerequisites` (
  `syllabus_id` int NOT NULL,
  `prerequisites` varchar(255) DEFAULT NULL,
  KEY `FKs2pw46abafxinrps01y2qtb5g` (`syllabus_id`),
  CONSTRAINT `FKs2pw46abafxinrps01y2qtb5g` FOREIGN KEY (`syllabus_id`) REFERENCES `syllabus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus_prerequisites`
--

LOCK TABLES `syllabus_prerequisites` WRITE;
/*!40000 ALTER TABLE `syllabus_prerequisites` DISABLE KEYS */;
INSERT INTO `syllabus_prerequisites` VALUES (16,'CS50: Introduction to Programming');
/*!40000 ALTER TABLE `syllabus_prerequisites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus_teaching_methods`
--

DROP TABLE IF EXISTS `syllabus_teaching_methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syllabus_teaching_methods` (
  `syllabus_id` int NOT NULL,
  `method` varchar(255) DEFAULT NULL,
  KEY `FKdrcxknodhabg0q4mecjhyebrt` (`syllabus_id`),
  CONSTRAINT `FKdrcxknodhabg0q4mecjhyebrt` FOREIGN KEY (`syllabus_id`) REFERENCES `syllabus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus_teaching_methods`
--

LOCK TABLES `syllabus_teaching_methods` WRITE;
/*!40000 ALTER TABLE `syllabus_teaching_methods` DISABLE KEYS */;
INSERT INTO `syllabus_teaching_methods` VALUES (16,'Lecture'),(16,'Verbal method'),(16,'Labs'),(16,'Problem-based learning');
/*!40000 ALTER TABLE `syllabus_teaching_methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus_topics`
--

DROP TABLE IF EXISTS `syllabus_topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syllabus_topics` (
  `syllabus_id` int NOT NULL,
  `topic` varchar(255) DEFAULT NULL,
  KEY `FK4xl1e6rkw4tyd32h0ohphueld` (`syllabus_id`),
  CONSTRAINT `FK4xl1e6rkw4tyd32h0ohphueld` FOREIGN KEY (`syllabus_id`) REFERENCES `syllabus` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus_topics`
--

LOCK TABLES `syllabus_topics` WRITE;
/*!40000 ALTER TABLE `syllabus_topics` DISABLE KEYS */;
INSERT INTO `syllabus_topics` VALUES (16,'Greedy algorithms'),(16,'Big O notation'),(16,'Arrays'),(16,'String manipulation'),(16,'Stacks');
/*!40000 ALTER TABLE `syllabus_topics` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-23 20:44:29
