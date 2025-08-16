<?php
header("Content-Type: application/json; charset=UTF-8");

include_once 'database.php';

try {
    $database = new Database();
    $conn = $database->getConnection();

    if ($conn) {
        echo json_encode(["mensaje" => "Conexión exitosa a la base de datos"]);
    } else {
        echo json_encode(["mensaje" => "Error al conectar a la base de datos"]);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["mensaje" => "Excepción: " . $e->getMessage()]);
}
?>
