/*
 Navicat Premium Data Transfer

 Source Server         : MAMP
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : fitnutrition

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 05/12/2020 19:01:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alimentos
-- ----------------------------
DROP TABLE IF EXISTS `alimentos`;
CREATE TABLE `alimentos` (
  `idAlimento` int(255) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `calorias_por_porcion` varchar(255) DEFAULT NULL,
  `porcion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idAlimento`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of alimentos
-- ----------------------------
BEGIN;
INSERT INTO `alimentos` VALUES (1, 'Manzana Roja', '52', '1 pieza');
INSERT INTO `alimentos` VALUES (2, 'Pera', '39', '1');
COMMIT;

-- ----------------------------
-- Table structure for citas
-- ----------------------------
DROP TABLE IF EXISTS `citas`;
CREATE TABLE `citas` (
  `idCita` int(255) NOT NULL,
  `fecha` varchar(255) DEFAULT NULL,
  `hora` varchar(255) DEFAULT NULL,
  `idPaciente` int(255) DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idCita`),
  KEY `idPaciente` (`idPaciente`),
  CONSTRAINT `idPaciente` FOREIGN KEY (`idPaciente`) REFERENCES `pacientes` (`idPaciente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of citas
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for consultas
-- ----------------------------
DROP TABLE IF EXISTS `consultas`;
CREATE TABLE `consultas` (
  `idConsulta` int(255) NOT NULL,
  `idPaciente` int(255) DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  `peso` float(5) DEFAULT NULL,
  `talla` float(3) DEFAULT NULL,
  `IMC` float(3) DEFAULT NULL,
  `idDieta` int(11) DEFAULT NULL,
  PRIMARY KEY (`idConsulta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of consultas
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for dietas
-- ----------------------------
DROP TABLE IF EXISTS `dietas`;
CREATE TABLE `dietas` (
  `idDieta` int(255) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `alimento` varchar(255) DEFAULT NULL,
  `cantidad` varchar(255) DEFAULT NULL,
  `horaDia` varchar(255) DEFAULT NULL,
  `caloriasDieta` float(255) DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idDieta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dietas
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for medicos
-- ----------------------------
DROP TABLE IF EXISTS `medicos`;
CREATE TABLE `medicos` (
  `idMedico` int(255) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `apellidos` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `domicilio` varchar(255) DEFAULT NULL,
  `num_personal` varchar(255) DEFAULT NULL,
  `num_cedula` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `fotografia` mediumblob,
  PRIMARY KEY (`idMedico`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medicos
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pacientes
-- ----------------------------
DROP TABLE IF EXISTS `pacientes`;
CREATE TABLE `pacientes` (
  `idPaciente` int(255) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `apellidos` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `peso` float(255,0) DEFAULT NULL,
  `estatura` varchar(255) DEFAULT NULL,
  `talla` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `telefono` varchar(0) DEFAULT NULL,
  `domicilio` varchar(255) DEFAULT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `fotrografia` mediumblob,
  `idMedico` int(255) DEFAULT NULL,
  PRIMARY KEY (`idPaciente`),
  KEY `idMedico` (`idMedico`),
  CONSTRAINT `idMedico` FOREIGN KEY (`idMedico`) REFERENCES `medicos` (`idMedico`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pacientes
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
