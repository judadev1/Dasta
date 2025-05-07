-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: localhost    Database: favores_db
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `calificacion`
--

DROP TABLE IF EXISTS `calificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calificacion` (
  `idcalificacion` int NOT NULL AUTO_INCREMENT,
  `iddelivery` int NOT NULL,
  `idfavor` int NOT NULL,
  `idpersona` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nota` tinyint unsigned NOT NULL,
  `comentario` longtext COLLATE utf8mb4_unicode_ci,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idcalificacion`),
  UNIQUE KEY `uk_calificacion_favor` (`idfavor`),
  KEY `idx_calificacion_delivery` (`iddelivery`),
  KEY `idx_calificacion_persona` (`idpersona`),
  CONSTRAINT `fk_calificacion_delivery` FOREIGN KEY (`iddelivery`) REFERENCES `delivery` (`iddelivery`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_calificacion_favor` FOREIGN KEY (`idfavor`) REFERENCES `favor` (`idfavor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_calificacion_persona` FOREIGN KEY (`idpersona`) REFERENCES `persona` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_nota` CHECK (((`nota` >= 1) and (`nota` <= 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calificacion`
--

LOCK TABLES `calificacion` WRITE;
/*!40000 ALTER TABLE `calificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `calificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery` (
  `iddelivery` int NOT NULL AUTO_INCREMENT,
  `nombre1` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nombre2` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apellido1` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apellido2` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`iddelivery`),
  UNIQUE KEY `telefono` (`telefono`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (1,'Juan','David','Arboleda','Vallecilla','3127768874','juan.arboleda@ucp.edu.co');
/*!40000 ALTER TABLE `delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favor`
--

DROP TABLE IF EXISTS `favor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favor` (
  `idfavor` int NOT NULL AUTO_INCREMENT,
  `idpersona` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `iddelivery` int DEFAULT NULL,
  `idtienda` int DEFAULT NULL,
  `descripcion` longtext COLLATE utf8mb4_unicode_ci,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valor` decimal(10,2) NOT NULL DEFAULT '0.00',
  `estado` enum('ASIGNADO','CANCELADO','ENTREGADO','EN_PROCESO','SOLICITADO') COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idfavor`),
  KEY `idx_favor_persona` (`idpersona`),
  KEY `idx_favor_delivery` (`iddelivery`),
  KEY `idx_favor_tienda` (`idtienda`),
  KEY `idx_favor_estado` (`estado`),
  CONSTRAINT `fk_favor_delivery` FOREIGN KEY (`iddelivery`) REFERENCES `delivery` (`iddelivery`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_favor_persona` FOREIGN KEY (`idpersona`) REFERENCES `persona` (`username`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_favor_tienda` FOREIGN KEY (`idtienda`) REFERENCES `tienda` (`idtienda`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favor`
--

LOCK TABLES `favor` WRITE;
/*!40000 ALTER TABLE `favor` DISABLE KEYS */;
/*!40000 ALTER TABLE `favor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_descontar_saldo_favor` AFTER INSERT ON `favor` FOR EACH ROW BEGIN
    -- Asume que la validación de saldo suficiente se hizo en la app
    IF NEW.valor > 0 THEN
        UPDATE persona
        SET saldo = saldo - NEW.valor
        WHERE username = NEW.idpersona;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `favor_producto`
--

DROP TABLE IF EXISTS `favor_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favor_producto` (
  `idfavor` int NOT NULL,
  `idproducto` int NOT NULL,
  `cantidad` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`idfavor`,`idproducto`),
  KEY `idx_fp_favor` (`idfavor`),
  KEY `idx_fp_producto` (`idproducto`),
  CONSTRAINT `fk_fp_favor` FOREIGN KEY (`idfavor`) REFERENCES `favor` (`idfavor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_fp_producto` FOREIGN KEY (`idproducto`) REFERENCES `productos` (`idproductos`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favor_producto`
--

LOCK TABLES `favor_producto` WRITE;
/*!40000 ALTER TABLE `favor_producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `favor_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre1` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre2` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apellido1` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido2` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `saldo` decimal(10,2) NOT NULL DEFAULT '3000.00',
  PRIMARY KEY (`username`),
  UNIQUE KEY `telefono` (`telefono`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES ('juan.arboleda@ucp.edu.co','$2a$10$OQ.IX9umteRGUtbVDhE35OMTkf24NmbLPzGlC9L5nmB3xVoabeDp2','Juan','David','Arboleda','Vallecilla','3127768874',99999999.00);
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `idproductos` int NOT NULL AUTO_INCREMENT,
  `idtienda` int NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`idproductos`),
  KEY `idx_productos_tienda` (`idtienda`),
  CONSTRAINT `fk_productos_tienda` FOREIGN KEY (`idtienda`) REFERENCES `tienda` (`idtienda`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,1,'Bon Bon Bum',500.00),(2,1,'Choco Break',500.00),(3,1,'Barquillo',500.00);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tienda`
--

DROP TABLE IF EXISTS `tienda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tienda` (
  `idtienda` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idtienda`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tienda`
--

LOCK TABLES `tienda` WRITE;
/*!40000 ALTER TABLE `tienda` DISABLE KEYS */;
INSERT INTO `tienda` VALUES (1,'La Tienda del Lobo');
/*!40000 ALTER TABLE `tienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'favores_db'
--

--
-- Dumping routines for database 'favores_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-07  0:15:26
