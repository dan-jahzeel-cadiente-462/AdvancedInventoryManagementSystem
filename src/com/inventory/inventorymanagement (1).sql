-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 01, 2025 at 06:24 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventorymanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `loginhistory`
--

CREATE TABLE `loginhistory` (
  `LoginHistoryID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL,
  `LoginTime` timestamp NOT NULL DEFAULT current_timestamp(),
  `LogoutTime` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loginhistory`
--

INSERT INTO `loginhistory` (`LoginHistoryID`, `UserID`, `LoginTime`, `LogoutTime`) VALUES
(162, 1, '2025-04-29 08:36:29', '2025-04-29 08:45:48'),
(163, 1, '2025-04-29 08:45:44', '2025-04-29 08:45:48'),
(164, 1, '2025-04-29 08:46:11', '2025-04-29 09:02:03'),
(165, 1, '2025-04-29 09:04:02', '2025-04-29 09:07:21'),
(166, 1, '2025-04-29 09:07:08', '2025-04-29 09:07:21'),
(167, 1, '2025-04-29 09:09:27', '2025-04-29 10:04:56'),
(168, 1, '2025-04-29 09:42:07', '2025-04-29 10:04:56'),
(169, 1, '2025-04-29 09:46:45', '2025-04-29 10:04:56'),
(170, 1, '2025-04-29 09:56:41', '2025-04-29 10:04:56'),
(171, 1, '2025-04-29 10:02:47', '2025-04-29 10:04:56'),
(172, 1, '2025-04-29 10:58:07', '2025-04-29 11:09:28'),
(173, 1, '2025-04-29 11:07:38', '2025-04-29 11:09:28'),
(174, 1, '2025-04-29 11:10:00', '2025-04-29 11:35:20'),
(175, 1, '2025-04-29 11:24:55', '2025-04-29 11:35:20'),
(176, 1, '2025-04-29 11:27:04', '2025-04-29 11:35:20'),
(177, 1, '2025-04-29 11:32:53', '2025-04-29 11:35:20'),
(178, 1, '2025-04-29 11:34:01', '2025-04-29 11:35:20'),
(179, 1, '2025-04-29 11:35:34', '2025-04-29 11:36:20'),
(180, 1, '2025-04-29 11:41:09', '2025-04-29 11:41:14'),
(181, 1, '2025-04-29 11:42:10', '2025-04-29 11:42:13'),
(182, 1, '2025-04-29 11:43:17', '2025-04-29 11:45:01'),
(183, 1, '2025-04-29 11:45:11', '2025-04-29 11:49:53'),
(184, 1, '2025-04-29 11:49:31', '2025-04-29 11:49:53'),
(185, 1, '2025-04-29 11:50:06', '2025-04-29 11:50:38'),
(186, 1, '2025-04-29 11:56:13', '2025-04-29 12:01:58'),
(187, 1, '2025-04-29 12:02:27', '2025-04-29 12:05:08'),
(188, 1, '2025-04-29 12:13:54', '2025-04-29 12:15:09'),
(189, 1, '2025-04-29 12:15:20', '2025-04-29 12:25:20'),
(190, 1, '2025-04-29 12:21:39', '2025-04-29 12:25:20'),
(191, 1, '2025-04-29 12:24:36', '2025-04-29 12:25:20'),
(192, 1, '2025-04-29 12:30:10', '2025-04-29 12:49:46'),
(193, 1, '2025-04-29 15:23:55', '2025-04-29 15:43:33'),
(194, 1, '2025-04-29 15:54:03', '2025-04-29 15:57:19'),
(195, 1, '2025-04-29 15:58:04', '2025-04-29 15:58:56'),
(196, 1, '2025-04-29 16:00:37', '2025-04-29 16:12:29'),
(197, 1, '2025-04-30 03:44:43', '2025-04-30 03:45:51'),
(198, 1, '2025-04-30 05:18:27', '2025-04-30 05:24:56'),
(199, 1, '2025-04-30 05:22:49', '2025-04-30 05:24:56'),
(200, 1, '2025-04-30 05:51:43', '2025-04-30 05:52:36'),
(201, 1, '2025-04-30 05:53:56', '2025-04-30 05:59:52'),
(202, 1, '2025-04-30 06:04:11', '2025-04-30 06:09:56'),
(203, 1, '2025-04-30 06:10:41', '2025-04-30 06:10:44'),
(204, 1, '2025-04-30 06:11:13', '2025-04-30 06:11:32'),
(205, 1, '2025-04-30 06:13:43', '2025-04-30 06:15:23'),
(206, 1, '2025-04-30 06:26:19', '2025-04-30 06:26:36'),
(207, 1, '2025-04-30 06:38:43', '2025-04-30 07:14:35'),
(208, 1, '2025-04-30 06:57:16', '2025-04-30 07:14:35'),
(209, 1, '2025-04-30 14:49:48', '2025-04-30 14:51:02'),
(210, 1, '2025-04-30 14:50:32', '2025-04-30 14:51:02'),
(211, 1, '2025-04-30 14:53:38', '2025-04-30 15:25:15'),
(212, 1, '2025-04-30 15:25:29', '2025-04-30 15:34:55'),
(213, 1, '2025-04-30 15:28:02', '2025-04-30 15:34:55'),
(214, 1, '2025-04-30 15:35:12', '2025-04-30 15:35:18'),
(215, 1, '2025-04-30 15:35:26', '2025-04-30 15:37:28'),
(216, 1, '2025-04-30 15:38:16', '2025-04-30 15:38:22'),
(217, 1, '2025-04-30 15:40:21', '2025-04-30 15:43:07'),
(218, 1, '2025-04-30 15:49:38', '2025-04-30 15:50:05'),
(219, 1, '2025-04-30 15:52:11', '2025-04-30 15:53:01'),
(220, 1, '2025-04-30 15:56:31', '2025-04-30 16:09:25'),
(221, 1, '2025-04-30 16:00:50', '2025-04-30 16:09:25'),
(222, 1, '2025-04-30 16:07:00', '2025-04-30 16:09:25'),
(223, 1, '2025-04-30 16:08:35', '2025-04-30 16:09:25'),
(224, 1, '2025-04-30 16:08:57', '2025-04-30 16:09:25'),
(225, 1, '2025-04-30 16:11:52', '2025-04-30 16:21:00'),
(226, 1, '2025-04-30 16:14:13', '2025-04-30 16:21:00'),
(227, 1, '2025-04-30 16:30:59', '2025-04-30 16:42:26'),
(228, 1, '2025-04-30 16:33:36', '2025-04-30 16:42:26'),
(229, 1, '2025-05-01 04:03:10', '2025-05-01 04:18:31');

-- --------------------------------------------------------

--
-- Table structure for table `productcategories`
--

CREATE TABLE `productcategories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `ProductID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `Price` decimal(10,2) NOT NULL,
  `userID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`ProductID`, `Name`, `Category`, `Price`, `userID`) VALUES
(4, 'Lemon Juice', 'Beverages', 45.00, 1),
(5, 'Bar Soap', 'Toiletries', 40.00, 1),
(7, 'Ganador Rice (50 kg)', 'Grains', 2650.00, 7),
(10, 'NFA Rice(20 pesos per kilo), 50 kg', 'Grains', 1000.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `ReportID` int(11) NOT NULL,
  `ProductID` int(11) DEFAULT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `TotalRevenue` decimal(10,2) NOT NULL,
  `SupplierContribution` decimal(5,2) NOT NULL,
  `ReportDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `RoleID` int(11) NOT NULL,
  `RoleName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`RoleID`, `RoleName`) VALUES
(1, 'Admin'),
(2, 'Staff');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `SaleID` int(11) NOT NULL,
  `ProductID` int(11) DEFAULT NULL,
  `QuantitySold` int(11) NOT NULL,
  `SaleDate` date NOT NULL,
  `TotalAmount` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`SaleID`, `ProductID`, `QuantitySold`, `SaleDate`, `TotalAmount`) VALUES
(13, 4, 300, '2025-04-29', 13500.00),
(15, 7, 500, '2025-04-29', 1325000.00),
(17, 7, 50, '2025-04-29', 132500.00),
(19, 4, 100, '2025-04-29', 4500.00);

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE `stock` (
  `StockID` int(11) NOT NULL,
  `ProductID` int(11) DEFAULT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `QuantityAdded` int(11) NOT NULL,
  `DateAdded` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `supplierproducts`
--

CREATE TABLE `supplierproducts` (
  `SupplierID` int(11) NOT NULL,
  `ProductID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `SupplierID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `ContactInfo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`SupplierID`, `Name`, `ContactInfo`) VALUES
(2, 'Cadiente Trading Inc.', '09362251838'),
(3, 'Payalin Trading', '09064495091');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `RoleID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `Username`, `Password`, `RoleID`) VALUES
(1, 'admin001', 'admin101', 1),
(6, 'staff01', 'staff101', 2),
(7, 'admin02', 'admin101', 1),
(9, 'staff02', 'staff102', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `loginhistory`
--
ALTER TABLE `loginhistory`
  ADD PRIMARY KEY (`LoginHistoryID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `productcategories`
--
ALTER TABLE `productcategories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`ProductID`),
  ADD KEY `FK_Products_Users` (`userID`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`ReportID`),
  ADD KEY `ProductID` (`ProductID`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`RoleID`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`SaleID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `stock`
--
ALTER TABLE `stock`
  ADD PRIMARY KEY (`StockID`),
  ADD KEY `ProductID` (`ProductID`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Indexes for table `supplierproducts`
--
ALTER TABLE `supplierproducts`
  ADD PRIMARY KEY (`SupplierID`,`ProductID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`SupplierID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `Username` (`Username`),
  ADD KEY `RoleID` (`RoleID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `loginhistory`
--
ALTER TABLE `loginhistory`
  MODIFY `LoginHistoryID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=230;

--
-- AUTO_INCREMENT for table `productcategories`
--
ALTER TABLE `productcategories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `ProductID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `ReportID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `RoleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `SaleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `stock`
--
ALTER TABLE `stock`
  MODIFY `StockID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `SupplierID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `loginhistory`
--
ALTER TABLE `loginhistory`
  ADD CONSTRAINT `loginhistory_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FK_Products_Users` FOREIGN KEY (`userID`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`),
  ADD CONSTRAINT `reports_ibfk_2` FOREIGN KEY (`SupplierID`) REFERENCES `suppliers` (`SupplierID`);

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`);

--
-- Constraints for table `stock`
--
ALTER TABLE `stock`
  ADD CONSTRAINT `stock_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`),
  ADD CONSTRAINT `stock_ibfk_2` FOREIGN KEY (`SupplierID`) REFERENCES `suppliers` (`SupplierID`);

--
-- Constraints for table `supplierproducts`
--
ALTER TABLE `supplierproducts`
  ADD CONSTRAINT `supplierproducts_ibfk_1` FOREIGN KEY (`SupplierID`) REFERENCES `suppliers` (`SupplierID`),
  ADD CONSTRAINT `supplierproducts_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`RoleID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
