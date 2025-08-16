<?php
header('Content-Type: application/json; charset=UTF-8');

// Configuración conexión MySQL
$host = 'localhost';
$db   = 'el_economico_db';
$user = 'root';    // Cambia esto por tu usuario
$pass = '';        // Cambia esto por tu contraseña si la tienes

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $stmt = $pdo->query("SELECT * FROM vista_ventas_con_localizacion");
    $ventas = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "success" => true,
        "data" => $ventas
    ]);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Error en la base de datos: " . $e->getMessage()
    ]);
}
?>
