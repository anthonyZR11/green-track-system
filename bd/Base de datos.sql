-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.13.0.7147
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for greentrack
CREATE DATABASE IF NOT EXISTS `greentrack` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `greentrack`;

-- Dumping structure for table greentrack.equipments
CREATE TABLE IF NOT EXISTS `equipments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brand` varchar(50) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `deletedAt` timestamp NULL DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `status` enum('DISPONIBLE','PRESTADO','MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
  `type` varchar(50) NOT NULL,
  `updatedAt` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfseq6l526t0ifqdxy8bni50v2` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table greentrack.equipments: ~6 rows (approximately)
INSERT INTO `equipments` (`id`, `brand`, `createdAt`, `deletedAt`, `name`, `status`, `type`, `updatedAt`) VALUES
	(null, 'LG', '2025-11-10 23:45:53', '2025-11-12 03:24:44', 'PANTALLA LG', 'DISPONIBLE', 'DESKTOP', '2025-11-12 06:13:05'),
	(null, 'AOC', '2025-11-10 23:45:53', '2025-11-12 02:16:06', 'PANTALLA AOC', 'DISPONIBLE', 'DESKTOP', '2025-11-12 06:13:11'),
	(null, 'toshiba', '2025-11-12 02:57:18', NULL, 'LAPTOP TOSHIBA', 'PRESTADO', 'escritorio', '2025-11-12 06:13:16'),
	(null, 'LG', '2025-11-12 03:20:56', NULL, 'TV LG', 'DISPONIBLE', 'mesa', '2025-11-12 06:13:21'),
	(null, 'LG', '2025-11-12 03:40:01', NULL, 'PC LG', 'DISPONIBLE', 'DESKTOP', '2025-11-12 06:13:32'),
	(null, 'toshiba', '2025-11-12 03:47:13', NULL, 'LAPTOP TOSHIBA ESCRITORIO', 'DISPONIBLE', 'escritorio', '2025-11-12 06:13:52');
	
	
	
	-- Dumping structure for table greentrack.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `deletedAt` timestamp NULL DEFAULT NULL,
  `email` varchar(80) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL DEFAULT 'ADMIN',
  `updatedAt` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table greentrack.users: ~2 rows (approximately)
INSERT INTO `users` (`id`, `createdAt`, `deletedAt`, `email`, `name`, `password`, `role`, `updatedAt`, `username`) VALUES
	(null, '2025-11-11 21:46:18', NULL, 'tonyz24.z.roldan@gmail.com', 'Anthony', '$2a$10$8QG7whqJ5ZKG5cZqcCXFRu2iVSJur6KD85H0Vea4QqSe4WgnYTI.6', 'USER', '2025-11-12 06:12:39', 'USER'),
	(null, '2025-11-11 21:47:49', NULL, 'anthony.z.roldan@gmail.com', 'Anthony', '$2a$10$v6JkLl0vod5dhBiHPHvJDenh.wUZYPc6iEJSyTZpzQ8Vbhs.58OtS', 'ADMIN', '2025-11-12 06:12:36', 'ADMIN');


-- Dumping structure for table greentrack.loans
CREATE TABLE IF NOT EXISTS `loans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deletedAt` timestamp NULL DEFAULT NULL,
  `loanDate` datetime(6) NOT NULL,
  `status` enum('ACTIVO','ATRASADO','DEVUELTO') NOT NULL DEFAULT 'ACTIVO',
  `updatedAt` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `equipmentId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `expectedReturnDate` datetime(6) DEFAULT NULL,
  `returnDate` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrcspevx7cd9agpllra8s3k294` (`equipmentId`),
  KEY `FK3p15dca21t8ibix8iclq0ej2t` (`userId`),
  CONSTRAINT `FK3p15dca21t8ibix8iclq0ej2t` FOREIGN KEY (`userId`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrcspevx7cd9agpllra8s3k294` FOREIGN KEY (`equipmentId`) REFERENCES `equipments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table greentrack.loans: ~0 rows (approximately)


/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
