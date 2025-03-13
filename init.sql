-- MySQL dump 10.13  Distrib 9.2.0, for macos14.7 (arm64)
--
-- Host: localhost    Database: users
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) NOT NULL,
  `next_val` bigint DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('default',100);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_backdrops`
--

DROP TABLE IF EXISTS `movie_backdrops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_backdrops` (
  `movie_id` bigint NOT NULL,
  `backdrop` varchar(255) DEFAULT NULL,
  KEY `FK78kpksmkwcvy8redyf28bncru` (`movie_id`),
  CONSTRAINT `FK78kpksmkwcvy8redyf28bncru` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_backdrops`
--

LOCK TABLES `movie_backdrops` WRITE;
/*!40000 ALTER TABLE `movie_backdrops` DISABLE KEYS */;
INSERT INTO `movie_backdrops` VALUES (1,'https://image.tmdb.org/t/p/original/r9PkFnRUIthgBp2JZZzD380MWZy.jpg'),(1,'https://image.tmdb.org/t/p/original/faXT8V80JRhnArTAeYXz0Eutpv9.jpg'),(2,'https://image.tmdb.org/t/p/original/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg'),(2,'https://image.tmdb.org/t/p/original/evaFLqtswezLosllRZtJNMiO1UR.jpg'),(3,'https://image.tmdb.org/t/p/original/5kAGbi9MFAobQTVfK4kWPnIfnP0.jpg'),(3,'https://image.tmdb.org/t/p/original/dlxzUj7z1MqEcFiwvvrj0bvBKDY.jpg'),(3,'https://image.tmdb.org/t/p/original/q2fY4kMXKoGv4CQf310MCxpXlRI.jpg'),(3,'https://image.tmdb.org/t/p/original/pTxwFdsdDWzpCRYuk1QbggdaOlL.jpg'),(3,'https://image.tmdb.org/t/p/original/1zuz2RgFoOjulkjjNHHFc3WiHGB.jpg'),(3,'https://image.tmdb.org/t/p/original/7HqxI1IXMloT9VTSuDC8ikaj810.jpg'),(3,'https://image.tmdb.org/t/p/original/vpK2rp3J5LiC01HoNM0j9DEHQ1T.jpg'),(3,'https://image.tmdb.org/t/p/original/cNHXdmr4amP6EPCMa0dqD8rwzDV.jpg'),(3,'https://image.tmdb.org/t/p/original/txQLFd6rfQrskQhFENkS1jElptt.jpg'),(3,'https://image.tmdb.org/t/p/original/dC4tj1ONdlZ2TWv4XD2SA1KUnJN.jpg'),(4,'https://image.tmdb.org/t/p/original/e9Qb2kmBnMXHCmNMI8NX1JbWhh1.jpg'),(4,'https://image.tmdb.org/t/p/original/2WjOOOGUu6dp4r8VqR5n48DY7JG.jpg'),(5,'https://image.tmdb.org/t/p/original/bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg'),(5,'https://image.tmdb.org/t/p/original/9hNtTwY8P5v2MKnUeb7iuREI7Yb.jpg');
/*!40000 ALTER TABLE `movie_backdrops` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_genres`
--

DROP TABLE IF EXISTS `movie_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_genres` (
  `movie_id` bigint NOT NULL,
  `genre` varchar(255) DEFAULT NULL,
  KEY `FK4ak9svw913jblkfgru84h2phd` (`movie_id`),
  CONSTRAINT `FK4ak9svw913jblkfgru84h2phd` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_genres`
--

LOCK TABLES `movie_genres` WRITE;
/*!40000 ALTER TABLE `movie_genres` DISABLE KEYS */;
INSERT INTO `movie_genres` VALUES (2,'Science Fiction'),(2,'Action'),(2,'Adventure'),(1,'Animation'),(1,'Action'),(1,'Adventure'),(1,'Comedy'),(1,'Family');
/*!40000 ALTER TABLE `movie_genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `imdb_id` varchar(255) DEFAULT NULL,
  `poster` varchar(255) DEFAULT NULL,
  `release_date` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `trailer_link` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgqxr8m8iu1p4r6w3s0d875gc2` (`imdb_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'tt3915174','https://image.tmdb.org/t/p/w500/1NqwE6LP9IEdOZ57NCT51ftHtWT.jpg','2022-12-21','Puss in Boots: The Last Wish','https://www.youtube.com/watch?v=tHb7WlgyaUc'),(2,'tt1630029','https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg','2022-12-16','Avatar: The Way of Water','https://www.youtube.com/watch?v=d9MyW72ELq0'),(3,'tt8760708','https://image.tmdb.org/t/p/w500/xBl5AGw7HXZcv1nNXPlzGgO4Cfo.jpg','2023-01-06','M3GAN','https://www.youtube.com/watch?v=BRb4U99OU80'),(4,'tt11116912','https://image.tmdb.org/t/p/w500/9z4jRr43JdtU66P0iy8h18OyLql.jpg','2022-12-01','Troll','https://www.youtube.com/watch?v=AiohkY_XQYQ'),(5,'tt6443346','https://image.tmdb.org/t/p/w500/pFlaoHTZeyNkG83vxsAJiGzfSsa.jpg','2022-10-19','Black Adam','https://www.youtube.com/watch?v=JaV7mmc9HGw');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `movie_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_movie_id` (`movie_id`),
  CONSTRAINT `FK87tlqya0rq8ijfjscldpvvdyq` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `fk_movie_id` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (25,'it was awesome',1),(26,'it was not that awesome',1),(27,'it was terrible',2);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'USER'),(3,'CREATOR'),(4,'EDITOR');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `account_non_locked` bit(1) DEFAULT b'1',
  `enabled` bit(1) NOT NULL,
  `failed_attempt` int DEFAULT NULL,
  `lock_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'rob@rob.com','$2a$14$YbRwINcrh3STiO8Gby9e6uPYPZbA6tLuC5276fe6Ih91QY9nnQ1g2','robo',_binary '',_binary '',0,'2025-03-01 10:52:54'),(7,NULL,'$2a$14$AuTOEGe8TI8hOrSMF6wYNeHFdYNyxIv9yvDSW17kxTMAHXOdcT23O','marco',_binary '',_binary '',0,NULL),(10,NULL,'$2a$14$PL4HI5VEndAXIn.4cKROWeLe8jjle60M3Vyovkurlo9s/zOIG/Hta','hakim',_binary '',_binary '',0,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (4,1),(1,2),(2,2),(3,2),(5,2),(6,2),(7,2),(8,2),(9,2),(10,2);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-12 15:45:32
