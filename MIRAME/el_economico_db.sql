-- phpMyAdmin SQL Dump
-- version 5.1.1deb5ubuntu1
-- https://www.phpmyadmin.net/
--
-- Servidor: 34.31.145.38
-- Tiempo de generación: 11-08-2025 a las 05:40:50
-- Versión del servidor: 8.0.42-0ubuntu0.22.04.2
-- Versión de PHP: 8.1.2-1ubuntu2.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `el_economico_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalles_pedido`
--

CREATE TABLE `detalles_pedido` (
  `id_detalle` int NOT NULL,
  `id_pedido` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id_pedido` int NOT NULL,
  `id_usuario` int NOT NULL,
  `id_repartidor` int DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `impuesto` decimal(10,2) DEFAULT NULL,
  `estado` enum('pendiente','preparando','en_camino','entregado','cancelado') COLLATE utf8mb4_general_ci DEFAULT 'pendiente',
  `ubicacion_entrega` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `fecha_pedido` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_general_ci,
  `imagen_base64` longtext COLLATE utf8mb4_general_ci,
  `precio` decimal(10,2) NOT NULL,
  `stock` int DEFAULT '0',
  `disponible` tinyint(1) DEFAULT '1',
  `supermercado_nombre` varchar(100) COLLATE utf8mb4_general_ci DEFAULT 'El Económico',
  `fecha_creacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `descripcion`, `imagen_base64`, `precio`, `stock`, `disponible`, `supermercado_nombre`, `fecha_creacion`) VALUES
(1, 'Six Pack Corona (2 unidades)', 'Pack de 2 cervezas Corona', NULL, '800.00', 100, 1, 'El Económico', '2025-08-09 04:00:18'),
(2, 'Pañales para bebé 45 unidades', 'Pañales talla única', NULL, '750.00', 200, 1, 'El Económico', '2025-08-09 04:00:18'),
(3, 'Leche Ceteco 500g', 'Leche en polvo', NULL, '450.00', 150, 1, 'El Económico', '2025-08-09 04:00:18'),
(4, '10 Libras de Chuleta', 'Carne de cerdo', NULL, '380.00', 50, 1, 'El Económico', '2025-08-09 04:00:18'),
(5, 'Cerveza', 'Paquete de 6', NULL, '15.50', 100, 1, 'El Económico', '2025-08-09 04:00:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `repartidores`
--

CREATE TABLE `repartidores` (
  `id_repartidor` int NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `correo` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `contrasena` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `foto_perfil_base64` longtext COLLATE utf8mb4_general_ci,
  `ubicacion_gps` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `disponible` tinyint(1) DEFAULT '1',
  `api_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `repartidores`
--

INSERT INTO `repartidores` (`id_repartidor`, `nombre`, `correo`, `contrasena`, `foto_perfil_base64`, `ubicacion_gps`, `disponible`, `api_key`, `fecha_registro`) VALUES
(1, 'Alex Mejia', 'alex@.com', '1234567', NULL, NULL, 1, NULL, '2025-08-09 04:00:18'),
(2, 'Pedro', 'pedro@example.com', 'pass456', NULL, NULL, 1, NULL, '2025-08-09 04:00:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resenas`
--

CREATE TABLE `resenas` (
  `id_resena` int NOT NULL,
  `id_usuario` int NOT NULL,
  `id_producto` int DEFAULT NULL,
  `id_repartidor` int DEFAULT NULL,
  `calificacion` int DEFAULT NULL,
  `comentario` text COLLATE utf8mb4_general_ci,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `correo` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `contrasena` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `foto_perfil_base64` longtext COLLATE utf8mb4_general_ci,
  `descripcion` text COLLATE utf8mb4_general_ci,
  `ubicacion_gps` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `codigo_verificacion` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `verificado` tinyint(1) DEFAULT '0',
  `api_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rol` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'usuario'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre`, `correo`, `contrasena`, `foto_perfil_base64`, `descripcion`, `ubicacion_gps`, `codigo_verificacion`, `verificado`, `api_key`, `fecha_registro`, `rol`) VALUES
(1, 'Alex_Nain', 'admin@gmail.com', 'admin123', NULL, 'Administrador del sistema', NULL, NULL, 0, NULL, '2025-08-09 04:00:18', 'admin'),
(2, 'Alex', 'admin@tudominio.com', 'admin123', NULL, 'Administrador del sistema', NULL, NULL, 0, NULL, '2025-08-09 04:00:18', 'admin'),
(3, 'Juan', 'juan@example.com', 'pass123', NULL, NULL, NULL, NULL, 0, NULL, '2025-08-09 04:00:18', 'usuario'),
(4, 'Alex', 'alex@gmail.com', 'admin123', NULL, 'Usuario de prueba', NULL, NULL, 0, NULL, '2025-08-09 04:00:18', 'cliente'),
(5, 'Alex Nain Mejia', 'alexnain83@gmail.com', '@lexNain1234', NULL, 'Usuario admin', '15.5040,-88.0256', '123456', 0, NULL, '2025-08-09 08:43:33', 'usuario'),
(11, 'Usuario desde Postman', 'postman@example.com', 'clave123', NULL, 'Prueba con Postman', '15.5000,-88.0000', '654321', 0, NULL, '2025-08-09 19:39:37', 'usuario');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_ventas_con_localizacion`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_ventas_con_localizacion` (
`id_pedido` int
,`nombre_usuario` varchar(100)
,`nombre_repartidor` varchar(100)
,`subtotal` decimal(10,2)
,`impuesto` decimal(10,2)
,`total` decimal(10,2)
,`estado` enum('pendiente','preparando','en_camino','entregado','cancelado')
,`ubicacion_entrega` varchar(255)
,`fecha_pedido` timestamp
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_ventas_con_localizacion`
--
DROP TABLE IF EXISTS `vista_ventas_con_localizacion`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_ventas_con_localizacion`  AS SELECT `p`.`id_pedido` AS `id_pedido`, `u`.`nombre` AS `nombre_usuario`, `r`.`nombre` AS `nombre_repartidor`, `p`.`subtotal` AS `subtotal`, `p`.`impuesto` AS `impuesto`, `p`.`total` AS `total`, `p`.`estado` AS `estado`, `p`.`ubicacion_entrega` AS `ubicacion_entrega`, `p`.`fecha_pedido` AS `fecha_pedido` FROM ((`pedidos` `p` join `usuarios` `u` on((`p`.`id_usuario` = `u`.`id_usuario`))) left join `repartidores` `r` on((`p`.`id_repartidor` = `r`.`id_repartidor`))) ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `id_pedido` (`id_pedido`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id_pedido`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_repartidor` (`id_repartidor`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`);

--
-- Indices de la tabla `repartidores`
--
ALTER TABLE `repartidores`
  ADD PRIMARY KEY (`id_repartidor`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Indices de la tabla `resenas`
--
ALTER TABLE `resenas`
  ADD PRIMARY KEY (`id_resena`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_producto` (`id_producto`),
  ADD KEY `id_repartidor` (`id_repartidor`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  MODIFY `id_detalle` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id_pedido` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `repartidores`
--
ALTER TABLE `repartidores`
  MODIFY `id_repartidor` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `resenas`
--
ALTER TABLE `resenas`
  MODIFY `id_resena` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  ADD CONSTRAINT `detalles_pedido_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`id_pedido`),
  ADD CONSTRAINT `detalles_pedido_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`);

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `fk_repartidor` FOREIGN KEY (`id_repartidor`) REFERENCES `repartidores` (`id_repartidor`),
  ADD CONSTRAINT `fk_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `resenas`
--
ALTER TABLE `resenas`
  ADD CONSTRAINT `resenas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  ADD CONSTRAINT `resenas_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  ADD CONSTRAINT `resenas_ibfk_3` FOREIGN KEY (`id_repartidor`) REFERENCES `repartidores` (`id_repartidor`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
