-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2024 at 08:43 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_perpustakaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `available` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `title`, `author`, `available`) VALUES
(1, 'The Catcher in the Rye', 'J.D. Salinger', 1),
(2, 'To Kill a Mockingbird', 'Harper Lee', 1),
(3, '1984', 'George Orwell', 1),
(4, 'Pride and Prejudice', 'Jane Austen', 1),
(6, 'atomic habits', 'james clear', 1),
(42, 'sebuah seni untuk bersikap bodo amat', 'mark manson', 1);

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','member') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`id`, `name`, `email`, `phone`, `username`, `password`, `role`) VALUES
(1, 'krisna', 'krisna@example.com', '1234567890', 'krisna', 'member', 'member'),
(2, 'budi', 'budi@example.com', '0987654321', 'budi', 'member', 'member'),
(3, 'rudi', 'rudi@example.com', '5556667777', 'rudi', 'member', 'member'),
(4, 'kevin', 'kevin@example.com', '4445556666', 'kevin', 'member', 'member'),
(5, 'davis', 'davis@example.com', '3334445555', 'davis', 'member', 'member'),
(6, 'Admin User', 'admin@example.com', '0000000000', 'admin', 'admin', 'admin'),
(7, 'test', 'tes@example.com', '329402', 'tes1', 'tes', 'member');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `book_id` int(11) DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL,
  `borrow_date` date DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `late_fee` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `book_id`, `member_id`, `borrow_date`, `due_date`, `return_date`, `late_fee`) VALUES
(7, 1, 1, '2024-07-13', '2024-07-20', '2024-07-13', 0),
(8, 2, 4, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(10, 4, 4, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(11, 1, 2, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(12, 2, 2, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(13, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(14, 6, 2, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(15, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(16, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(18, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(20, 3, 3, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(21, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(22, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(23, 1, 1, '2024-07-14', '2024-07-28', '2024-07-14', 0),
(24, 4, 2, '2024-07-14', '2024-07-28', '2024-07-14', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `member_id` (`member_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `members`
--
ALTER TABLE `members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
