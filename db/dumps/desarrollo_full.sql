CREATE DATABASE  IF NOT EXISTS `desarrollo` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `desarrollo`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: desarrollo
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asignacion`
--

DROP TABLE IF EXISTS `asignacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignacion` (
  `idasignacion` int NOT NULL AUTO_INCREMENT,
  `profesor_id` int NOT NULL,
  `unidad_id` int NOT NULL,
  `horario` varchar(50) NOT NULL,
  PRIMARY KEY (`idasignacion`),
  UNIQUE KEY `uk_asig_prof_uni_hor` (`profesor_id`,`unidad_id`,`horario`),
  KEY `fk_asig_uni` (`unidad_id`),
  CONSTRAINT `fk_asig_prof` FOREIGN KEY (`profesor_id`) REFERENCES `profesor` (`idprofesor`),
  CONSTRAINT `fk_asig_uni` FOREIGN KEY (`unidad_id`) REFERENCES `unidad` (`idunidad`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignacion`
--

LOCK TABLES `asignacion` WRITE;
/*!40000 ALTER TABLE `asignacion` DISABLE KEYS */;
INSERT INTO `asignacion` VALUES (1,1,3,'7:00-8:00 matutino');
/*!40000 ALTER TABLE `asignacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesor`
--

DROP TABLE IF EXISTS `profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profesor` (
  `idprofesor` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidop` varchar(50) NOT NULL,
  `apellidom` varchar(50) NOT NULL,
  `rfc` char(13) NOT NULL,
  `idusuario` int NOT NULL,
  PRIMARY KEY (`idprofesor`),
  UNIQUE KEY `uk_profesor_rfc` (`rfc`),
  UNIQUE KEY `uk_profesor_usuario` (`idusuario`),
  CONSTRAINT `fk_profesor_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesor`
--

LOCK TABLES `profesor` WRITE;
/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` VALUES (1,'Ashley','Ramirez','Castillo','KKXB490511BU6',11),(2,'Aylin','Alvarado','Diaz','IJLZ530714KY5',12),(3,'Aylin','Alvarado','Diaz','RHDJ580213WZ0',13);
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unidad`
--

DROP TABLE IF EXISTS `unidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `unidad` (
  `idunidad` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `tipo` varchar(15) NOT NULL,
  `horas` tinyint NOT NULL,
  PRIMARY KEY (`idunidad`),
  KEY `idx_unidad_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unidad`
--

LOCK TABLES `unidad` WRITE;
/*!40000 ALTER TABLE `unidad` DISABLE KEYS */;
INSERT INTO `unidad` VALUES (3,'Bases de datos','TALLER',4),(4,'matematicas','CLASE',4),(5,'Calculo2','TALLER',2);
/*!40000 ALTER TABLE `unidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idusuario` int NOT NULL AUTO_INCREMENT,
  `correo` varchar(100) NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `es_admin` tinyint(1) NOT NULL DEFAULT '0',
  `intentos_fallidos` int NOT NULL DEFAULT '0',
  `bloqueado_hasta` datetime DEFAULT NULL,
  `session_token` varchar(64) DEFAULT NULL,
  `token_expira` datetime DEFAULT NULL,
  PRIMARY KEY (`idusuario`),
  UNIQUE KEY `correo` (`correo`),
  UNIQUE KEY `uk_usuario_correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin@uabc.edu.mx','$2a$10$AV5a.NJHFYOILwcpblA0juQLiFx56XgbQEu7dvhlbJowUImLJ41FC',1,0,NULL,NULL,NULL),(2,'profe@uabc.edu.mx','$2a$10$.Tfzps4mkmChJ6/UC/CA8ulaD98.ZuiGhaEB4DdQQY0AU/fJhDY5C',0,0,NULL,NULL,NULL),(3,'cristhian.castanos@uabc.edu.mx','$2a$10$5IJNYL3KxTH1se.iNXaLI.7mXVI2c4dezkYaLwzI6Dti0Wa.m6Fwa',0,0,NULL,NULL,NULL),(4,'ian.castanos@uabc.edu.mx','2vqL%jMW',0,0,NULL,NULL,NULL),(5,'yael@uabc.edu.mx','$2a$10$QV.j4h7jFoB2mm8akbd3c.Hi58qZncn6uRX7AZYWvX7GhluhU10uG',0,0,NULL,'BRlkWZ-r26wWNPbL_W75ARMCjgIcN5wJurZHJNqBOn4','2025-09-17 14:43:15'),(6,'juan@outlook.com','hdZbBnu5',0,0,NULL,NULL,NULL),(7,'juanalberto@outlook.com','e#JLVG4N',0,0,NULL,NULL,NULL),(8,'juanalberto2@outlook.com','KLwd3tLV',0,0,NULL,NULL,NULL),(9,'yaelon@uabc.edu.mx','nypgzSH2',0,0,NULL,NULL,NULL),(10,'yaelonq@uabc.edu.mx','$2a$10$RM5x8ft/Q42orP/7RLQg8eLaG1f76dYfgAwy39BiPPkuAIXEZMO.W',0,0,NULL,NULL,NULL),(11,'ashley@uabc.edu.mx','WUiriSSy',0,0,NULL,NULL,NULL),(12,'aylin@uabc.edu.mx','a#u7Cp79',0,0,NULL,NULL,NULL),(13,'aylin1@uabc.edu.mx','cTz5ey$i',0,0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'desarrollo'
--

--
-- Dumping routines for database 'desarrollo'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-18 20:46:01
