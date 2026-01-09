-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 09, 2026 at 01:36 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `opizza`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `addresses_id` int(11) NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `house` varchar(255) DEFAULT NULL,
  `flat` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `is_default` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`addresses_id`, `user_id`, `city`, `zip_code`, `street`, `house`, `flat`, `type`, `is_default`) VALUES
(1, 3, 'Nitra', '941 01', 'Štúrova 1433', '16', '2', NULL, b'1'),
(2, 3, 'Nitra', '941 01', 'Štúrova', '111', '11', NULL, b'0'),
(3, 1, 'Nitra', '941 01', 'Štúrova 1433', '16', '2', NULL, b'1'),
(4, 1, 'Nitra', '941 01', 'Štúrova 1', '11', '11', NULL, b'0'),
(5, 3, 'Nitra', '941 01', 'Štúrova 1422', '111', '4', NULL, b'0'),
(6, 4, 'Nitra', '94901', 'Štúrova 1433', '1111', '2', NULL, b'1'),
(8, 7, 'Nitra', '941 01', 'Štúrova 1433', '12', '12', NULL, b'1');

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `carts_id` int(11) NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`carts_id`, `user_id`, `updated_at`) VALUES
(1, 1, '2025-12-27 13:12:44'),
(2, 3, '2025-12-27 19:53:50'),
(3, 4, '2026-01-08 21:31:17'),
(4, 7, '2026-01-09 12:02:48');

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `cart_items_id` int(11) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `product_variant_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`cart_items_id`, `cart_id`, `product_variant_id`, `quantity`) VALUES
(8, 2, 2, 1),
(9, 2, 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `sort_order` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `name`, `description`, `sort_order`) VALUES
(2, 'Pizza', NULL, NULL),
(3, 'Drink', NULL, NULL),
(4, 'Dezert', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `ingredients`
--

CREATE TABLE `ingredients` (
  `ingredient_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `is_spicy` bit(1) NOT NULL,
  `is_fish` bit(1) DEFAULT NULL,
  `is_meat` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `ingredients`
--

INSERT INTO `ingredients` (`ingredient_id`, `name`, `is_spicy`, `is_fish`, `is_meat`) VALUES
(1, 'Paradajkový základ', b'0', b'0', 0),
(2, 'Mozzarella', b'0', b'0', 0),
(3, 'Šunka', b'0', b'0', 1),
(4, 'Šampiňóny', b'0', b'0', 0),
(5, 'Saláma', b'1', b'0', 1),
(6, 'Kukurica', b'0', b'0', 0),
(7, 'Ananás', b'0', b'0', 0),
(8, 'Olivy', b'0', b'0', 0),
(9, 'Slanina', b'0', b'0', 1),
(10, 'Cibuľa', b'0', b'0', 0),
(11, 'Feferóny', b'1', b'0', 0),
(12, 'Niva', b'0', b'0', 0),
(13, 'Parmezán', b'0', b'0', 0),
(14, 'Tuniak', b'0', b'1', 0),
(15, 'Bazalka', b'0', b'0', 0);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `order_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(255) DEFAULT NULL,
  `total_price` double NOT NULL,
  `note` tinytext DEFAULT NULL,
  `delivery_address_id` int(11) DEFAULT NULL,
  `delivery_phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `order_time`, `status`, `total_price`, `note`, `delivery_address_id`, `delivery_phone`) VALUES
(2, 3, '2026-01-06 20:11:18', 'PREPARING', 20, '', 1, NULL),
(3, 3, '2026-01-06 20:23:25', 'READY', 10, '', 1, NULL),
(5, 3, '2026-01-07 09:21:48', 'DELIVERED', 10.02, '', 1, NULL),
(6, 4, '2026-01-08 22:05:46', 'CANCELED', 25, '', 6, NULL),
(7, 3, '2026-01-08 22:58:16', 'CANCELED', 15, '', 1, NULL),
(8, 7, '2026-01-09 12:03:11', 'PREPARING', 45, '', 8, NULL),
(9, 7, '2026-01-09 12:03:36', 'DELIVERING', 23.5, '', 8, NULL),
(10, 7, '2026-01-09 12:04:12', 'PENDING', 28, '', 8, NULL),
(11, 7, '2026-01-09 12:04:40', 'PENDING', 27, '', 8, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `item_id` int(11) NOT NULL,
  `price_at_order` double NOT NULL,
  `product_name_at_order` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_variant_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`item_id`, `price_at_order`, `product_name_at_order`, `quantity`, `order_id`, `product_variant_id`) VALUES
(1, 10, 'Test', 2, 2, 1),
(2, 10, 'Test', 1, 3, 1),
(4, 10.02, 'Test', 1, 5, 1),
(5, 15, 'Capriciosa', 1, 6, 2),
(6, 10, 'test', 1, 6, 3),
(7, 15, 'test', 1, 7, 4),
(8, 15, 'Carbonara Pizza', 1, 8, 12),
(9, 12, 'Margherita Classica', 2, 8, 43),
(10, 3, 'Nuka Cola', 2, 8, 30),
(11, 20, 'Gluten Free Primavera', 1, 9, 9),
(12, 3.5, 'Duff Beer', 1, 9, 31),
(13, 7, 'Strudel', 4, 10, 25),
(14, 9, 'Funghi', 3, 11, 35);

-- --------------------------------------------------------

--
-- Table structure for table `order_status_history`
--

CREATE TABLE `order_status_history` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `status` varchar(50) NOT NULL,
  `changed_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `order_status_history`
--

INSERT INTO `order_status_history` (`id`, `order_id`, `status`, `changed_at`) VALUES
(1, 5, 'PENDING', '2026-01-07 10:21:48'),
(2, 5, 'PREPARING', '2026-01-07 10:28:25'),
(3, 5, 'DELIVERING', '2026-01-07 10:28:28'),
(4, 5, 'DELIVERED', '2026-01-07 10:28:30'),
(5, 2, 'PREPARING', '2026-01-07 19:54:21'),
(6, 2, 'PENDING', '2026-01-07 19:54:23'),
(7, 2, 'PREPARING', '2026-01-07 21:43:20'),
(8, 3, 'READY', '2026-01-07 21:43:24'),
(9, 5, 'READY', '2026-01-07 21:43:27'),
(10, 6, 'PENDING', '2026-01-08 23:05:46'),
(11, 6, 'CANCELED', '2026-01-08 23:14:12'),
(12, 7, 'PENDING', '2026-01-08 23:58:16'),
(13, 7, 'CANCELED', '2026-01-08 23:58:24'),
(14, 8, 'PENDING', '2026-01-09 13:03:11'),
(15, 9, 'PENDING', '2026-01-09 13:03:36'),
(16, 10, 'PENDING', '2026-01-09 13:04:12'),
(17, 11, 'PENDING', '2026-01-09 13:04:40'),
(18, 8, 'PREPARING', '2026-01-09 13:05:30'),
(19, 9, 'READY', '2026-01-09 13:05:34'),
(20, 9, 'DELIVERING', '2026-01-09 13:06:00'),
(21, 5, 'DELIVERED', '2026-01-09 13:06:05');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `is_available` bit(1) NOT NULL,
  `slug` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `name`, `description`, `image_url`, `category_id`, `is_available`, `slug`) VALUES
(1, 'Capriciosa', 'Syr, cesto', '123', 2, b'1', 'capriciosa'),
(2, 'test', 'test', '/img/pizza_pictures/placeholder.webp', 2, b'0', 'test'),
(10, 'Carbonara Pizza', 'Smotanový základ, mozzarella, slanina, vajce, čierne korenie.', '/user-pics/products/carbonara-pizza.webp', 2, b'1', 'carbonara'),
(11, 'Delicatezza Rustica', 'Paradajkový základ, mozzarella, talianska saláma, rukola.', '/user-pics/products/delicatezza-rustica.webp', 2, b'1', 'rustica'),
(12, 'Diavola Piccante', 'Pikantná saláma, feferóny, chilli, mozzarella.', '/user-pics/products/diavola-piccante.webp', 2, b'1', 'diavola'),
(13, 'Funghi al Panna', 'Smotanový základ, čerstvé šampiňóny, niva, petržlenová vňať.', '/user-pics/products/funghi-al-panna.webp', 2, b'1', 'funghi-panna'),
(14, 'Funghi', 'Klasická pizza s paradajkovým základom a čerstvými hubami.', '/user-pics/products/funghi.webp', 2, b'1', 'funghi-classic'),
(15, 'Gluten Free Primavera', 'Bezlepkové cesto, čerstvá zelenina, kukurica, olivy.', '/user-pics/products/gluten-free-primavera.webp', 2, b'1', 'primavera'),
(16, 'Hawaii Classic', 'Šunka, sladký ananás a mozzarella.', '/user-pics/products/hawaii-classic.webp', 2, b'1', 'hawaii-new'),
(17, 'La Crema Bianca', 'Biely základ z ricotty, parmezán, cibuľa a bylinky.', '/user-pics/products/la-crema-bianca.webp', 2, b'1', 'crema-bianca'),
(18, 'Margherita Classica', 'Tradičná talianska margherita s čerstvou bazalkou.', '/user-pics/products/margherita-classica.webp', 2, b'1', 'margherita-classic'),
(19, 'Capricciosa Premium', 'Šunka, šampiňóny, olivy a artičoky.', '/user-pics/products/capricciosa.webp', 2, b'1', 'capricciosa-premium'),
(20, 'Duff Beer', 'Legendárne pivo zo Springfieldu.', '/user-pics/products/duff.webp', 3, b'1', 'duff'),
(21, 'Nuka Cola', 'Osviežujúci nápoj s nádychom rádioaktivity.', '/user-pics/products/nuka-cola.webp', 3, b'1', 'nuka-cola'),
(22, 'Schraderbräu', 'Domáce, pernikove pivo, varené s láskou.', '/user-pics/products/schraderbrau.jpg', 3, b'1', 'schraderbrau'),
(23, 'Vodka Martini', 'Pretrepať, nemiešať.', '/user-pics/products/vodka-martini.jpg', 3, b'1', 'vodka-martini'),
(24, 'Cherry Pie', 'Domáci višňový koláč pre najlepšieho detektíva.', '/user-pics/products/cherry-pie.webp', 4, b'1', 'cherry-pie'),
(25, 'Courtesan au Chocolat', 'Vychutnajte si chuť hotela Grand Budapest.', '/user-pics/products/courtesan-au-chocolat.jpg', 4, b'1', 'courtesan'),
(26, 'Strudel', 'Bezlepkova jablková štrúdľa so šľahačkou.', '/user-pics/products/strudel.jpg', 4, b'1', 'strudel');

-- --------------------------------------------------------

--
-- Table structure for table `product_images`
--

CREATE TABLE `product_images` (
  `id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `is_main` tinyint(1) DEFAULT 0,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `product_images`
--

INSERT INTO `product_images` (`id`, `url`, `is_main`, `product_id`) VALUES
(1, '/uploads/products/1767906492285_04.jpg', 1, 2),
(2, '/uploads/products/1767906529135_au.webp', 0, 2),
(3, '/uploads/products/capricciosa.webp', 1, 1),
(5, '/uploads/products/carbonara-pizza.webp', 1, 10),
(6, '/uploads/products/delicatezza-rustica.webp', 1, 11),
(7, '/uploads/products/diavola-piccante.webp', 1, 12),
(8, '/uploads/products/funghi-al-panna.webp', 1, 13),
(9, '/uploads/products/funghi.webp', 1, 14),
(10, '/uploads/products/gluten-free-primavera.webp', 1, 15),
(11, '/uploads/products/hawaii-classic.webp', 1, 16),
(12, '/uploads/products/la-crema-bianca.webp', 1, 17),
(13, '/uploads/products/margherita-classica.webp', 1, 18),
(14, '/uploads/products/capricciosa.webp', 1, 19),
(15, '/uploads/products/duff.webp', 1, 20),
(16, '/uploads/products/nuka-cola.webp', 1, 21),
(17, '/uploads/products/schraderbrau.jpg', 1, 22),
(18, '/uploads/products/vodka-martini.jpg', 1, 23),
(19, '/uploads/products/cherry-pie.webp', 1, 24),
(20, '/uploads/products/courtesan-au-chocolat.jpg', 1, 25),
(21, '/uploads/products/strudel.jpg', 1, 26),
(22, '/uploads/products/1767950821494_pizza-capricciosa_1200x800.jpg', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `product_ingredients`
--

CREATE TABLE `product_ingredients` (
  `product_id` int(11) NOT NULL,
  `ingredient_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `product_ingredients`
--

INSERT INTO `product_ingredients` (`product_id`, `ingredient_id`) VALUES
(1, 2),
(1, 8),
(10, 2),
(10, 9),
(10, 13),
(11, 2),
(11, 5),
(11, 15),
(12, 1),
(12, 2),
(12, 5),
(12, 11),
(16, 3),
(16, 7),
(17, 10),
(17, 13),
(17, 15),
(19, 1),
(19, 2),
(19, 3),
(19, 4),
(19, 8);

-- --------------------------------------------------------

--
-- Table structure for table `product_tags`
--

CREATE TABLE `product_tags` (
  `product_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `product_tags`
--

INSERT INTO `product_tags` (`product_id`, `tag_id`) VALUES
(2, 2),
(10, 1),
(10, 5),
(11, 1),
(11, 3),
(12, 4),
(15, 2),
(16, 5),
(18, 3),
(19, 1),
(24, 5),
(26, 2);

-- --------------------------------------------------------

--
-- Table structure for table `product_variants`
--

CREATE TABLE `product_variants` (
  `variant_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `size_id` int(11) NOT NULL,
  `price` double NOT NULL,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `product_variants`
--

INSERT INTO `product_variants` (`variant_id`, `product_id`, `size_id`, `price`, `is_active`) VALUES
(1, 1, 1, 10, 1),
(2, 1, 2, 15, 1),
(3, 2, 1, 10, 1),
(4, 2, 2, 15, 1),
(5, 2, 3, 10, 1),
(6, 1, 3, 6, 1),
(7, 15, 3, 10, 1),
(8, 15, 1, 15, 1),
(9, 15, 2, 20, 1),
(10, 10, 3, 7, 1),
(11, 10, 1, 10, 1),
(12, 10, 2, 15, 1),
(13, 11, 3, 8, 1),
(14, 11, 1, 10, 1),
(15, 11, 2, 14, 1),
(16, 12, 3, 10, 1),
(17, 12, 1, 12, 1),
(18, 12, 2, 14, 1),
(19, 16, 3, 9, 1),
(20, 16, 1, 11, 1),
(21, 16, 2, 13, 1),
(22, 19, 3, 8, 1),
(23, 19, 1, 10, 1),
(24, 19, 2, 12, 1),
(25, 26, 10, 7, 1),
(26, 25, 10, 8, 1),
(27, 24, 10, 7, 1),
(28, 23, 11, 6, 1),
(29, 22, 8, 5, 1),
(30, 21, 9, 3, 1),
(31, 20, 8, 3.5, 1),
(32, 13, 3, 8, 1),
(33, 13, 1, 10, 1),
(34, 13, 2, 12, 1),
(35, 14, 3, 9, 1),
(36, 14, 1, 11, 1),
(37, 14, 2, 13, 1),
(38, 17, 3, 8, 1),
(39, 17, 1, 10, 1),
(40, 17, 2, 13, 1),
(41, 18, 3, 8, 1),
(42, 18, 1, 10, 1),
(43, 18, 2, 12, 1);

-- --------------------------------------------------------

--
-- Table structure for table `sizes`
--

CREATE TABLE `sizes` (
  `size_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `weight_grams` int(11) NOT NULL,
  `sort_order` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `sizes`
--

INSERT INTO `sizes` (`size_id`, `name`, `weight_grams`, `sort_order`) VALUES
(1, '35cm', 500, NULL),
(2, '45cm', 800, NULL),
(3, '25cm', 350, NULL),
(8, '500ml', 500, NULL),
(9, '330ml', 330, NULL),
(10, '150g', 150, NULL),
(11, '200ml', 200, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE `tags` (
  `tag_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `color_hex` varchar(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`tag_id`, `name`, `color_hex`) VALUES
(1, 'New', '#ffcc00'),
(2, 'Gluten-free', '#00cc99'),
(3, 'Vegan', '#8575ff'),
(4, 'Hot', '#ff5252'),
(5, 'Bestseller', '#e91e63');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(10) UNSIGNED NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_active` bit(1) NOT NULL,
  `default_address_id` int(11) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `token_expiry` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_slovak_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `first_name`, `last_name`, `phone`, `role`, `avatar_url`, `created_at`, `is_active`, `default_address_id`, `reset_token`, `token_expiry`) VALUES
(1, 'info@shishov.dev', '$2a$10$nS7m1Lp9y1M9j9.zG9qOYeQf5M8y3Gz/p6.U2qA8gLh7iR9v1lG2y', 'Mikhail', 'Shishov', '+421952031817', 'USER', NULL, '2025-12-27 09:13:35', b'1', 4, NULL, NULL),
(3, 'shishovmike96@gmail.com', '$2a$10$eKa3iFYGouHsAKOMLOT9VOY0YsMe1Z8mVi96KuE8pLzTxqfbIyV8.', 'Admin', 'User', '+421952031817', 'ADMIN', '/uploads/avatars/user_3_5.jpg', '2025-12-27 19:45:33', b'1', 1, NULL, NULL),
(4, 'cook@opizza.sk', '$2a$10$EQ0FHJQEZPkddczhJHjYgOPo7PckNou/BORyZOXhI/fEpIufWy.2i', 'James', 'Cook', '+421901234567', 'COOK', NULL, '2026-01-07 20:27:01', b'1', 6, NULL, NULL),
(5, 'driver@opizza.sk', '$2a$10$4oVMxDATx3YyYtRp3bOlbu0nrHecFrHov2aIPDEV9U3Sj9Zcwj7Ay', 'Ryan', 'Gosling', '+421901234567', 'COURIER', NULL, '2026-01-07 20:29:11', b'1', NULL, NULL, NULL),
(6, 'jozko.mrkvicka@gmail.com', '$2a$10$nS7m1Lp9y1M9j9.zG9qOYeQf5M8y3Gz/p6.U2qA8gLh7iR9v1lG2y', 'Jozko', 'Mrkvicka', '+421905111222', 'USER', NULL, '2026-01-09 11:47:00', b'1', NULL, NULL, NULL),
(7, 'test.user@opizza.sk', '$2a$10$bPyiu8tjZs.fnwYH1hwMQenErTmn2R47/jobQ0Rri93gh.biFoFby', 'Test', 'User', '+421900123456', 'USER', '/uploads/avatars/user_7_fresh.png', '2026-01-09 11:47:00', b'1', 8, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`addresses_id`),
  ADD KEY `fk_addresses_users_idx` (`user_id`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`carts_id`),
  ADD UNIQUE KEY `fk_carts_users_idx` (`user_id`);

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`cart_items_id`),
  ADD KEY `fk_cart_items_cart_idx` (`cart_id`),
  ADD KEY `FKn1s4l7h0vm4o259wpu7ft0y2y` (`product_variant_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD PRIMARY KEY (`ingredient_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `fk_orders_user` (`user_id`),
  ADD KEY `fk_orders_address` (`delivery_address_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  ADD KEY `FKltmtlue0wixrg1cf0xo7x0l4d` (`product_variant_id`);

--
-- Indexes for table `order_status_history`
--
ALTER TABLE `order_status_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_order_status_order` (`order_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `slug` (`slug`),
  ADD KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`);

--
-- Indexes for table `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_product_images_products_idx` (`product_id`);

--
-- Indexes for table `product_ingredients`
--
ALTER TABLE `product_ingredients`
  ADD PRIMARY KEY (`product_id`,`ingredient_id`),
  ADD KEY `fk_ingredient` (`ingredient_id`);

--
-- Indexes for table `product_tags`
--
ALTER TABLE `product_tags`
  ADD PRIMARY KEY (`product_id`,`tag_id`),
  ADD KEY `FKpur2885qb9ae6fiquu77tcv1o` (`tag_id`);

--
-- Indexes for table `product_variants`
--
ALTER TABLE `product_variants`
  ADD PRIMARY KEY (`variant_id`),
  ADD KEY `FKosqitn4s405cynmhb87lkvuau` (`product_id`),
  ADD KEY `FKt7j608wes333gojuoh0f8l488` (`size_id`);

--
-- Indexes for table `sizes`
--
ALTER TABLE `sizes`
  ADD PRIMARY KEY (`size_id`);

--
-- Indexes for table `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`tag_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`),
  ADD KEY `FK87ccnpkij6d1sorog5li3sq8x` (`default_address_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `addresses_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `carts_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `cart_items_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `order_status_history`
--
ALTER TABLE `order_status_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `product_images`
--
ALTER TABLE `product_images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `product_variants`
--
ALTER TABLE `product_variants`
  MODIFY `variant_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `sizes`
--
ALTER TABLE `sizes`
  MODIFY `size_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tags`
--
ALTER TABLE `tags`
  MODIFY `tag_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
  ADD CONSTRAINT `fk_addresses_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `fk_carts_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `FKn1s4l7h0vm4o259wpu7ft0y2y` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`variant_id`),
  ADD CONSTRAINT `fk_cart_items_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`carts_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_address` FOREIGN KEY (`delivery_address_id`) REFERENCES `addresses` (`addresses_id`),
  ADD CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `FKltmtlue0wixrg1cf0xo7x0l4d` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`variant_id`);

--
-- Constraints for table `order_status_history`
--
ALTER TABLE `order_status_history`
  ADD CONSTRAINT `FK_order_status_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `fk_product_images_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Constraints for table `product_ingredients`
--
ALTER TABLE `product_ingredients`
  ADD CONSTRAINT `fk_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Constraints for table `product_tags`
--
ALTER TABLE `product_tags`
  ADD CONSTRAINT `FK5rk6s19k3risy7q7wqdr41uss` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `FKpur2885qb9ae6fiquu77tcv1o` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`);

--
-- Constraints for table `product_variants`
--
ALTER TABLE `product_variants`
  ADD CONSTRAINT `FKosqitn4s405cynmhb87lkvuau` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `FKt7j608wes333gojuoh0f8l488` FOREIGN KEY (`size_id`) REFERENCES `sizes` (`size_id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK87ccnpkij6d1sorog5li3sq8x` FOREIGN KEY (`default_address_id`) REFERENCES `addresses` (`addresses_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
