<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Conexión a base de datos
$host = "34.31.145.38";
$db_name = "el_economico_db";
$username = "root";
$password = "@lexNain1234";

try {
    $conn = new PDO("mysql:host=$host;dbname=$db_name;charset=utf8mb4", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    http_response_code(500);
    echo json_encode(["mensaje" => "Error de conexión a la base de datos: " . $e->getMessage()]);
    exit;
}

// Obtener datos JSON POST
$data = json_decode(file_get_contents("php://input"));

if (
    !empty($data->nombre) &&
    !empty($data->correo) &&
    !empty($data->contrasena)
) {
    // Preparar la consulta INSERT
    $query = "INSERT INTO repartidores (nombre, correo, contrasena, disponible, fecha_registro) VALUES (:nombre, :correo, :contrasena, 1, NOW())";
    $stmt = $conn->prepare($query);

    $stmt->bindParam(":nombre", $data->nombre);
    $stmt->bindParam(":correo", $data->correo);
    $stmt->bindParam(":contrasena", $data->contrasena);

    try {
        if ($stmt->execute()) {
            http_response_code(201);
            echo json_encode(["mensaje" => "Repartidor creado exitosamente"]);
        } else {
            http_response_code(400);
            echo json_encode(["mensaje" => "No se pudo crear el repartidor"]);
        }
    } catch (PDOException $e) {
        http_response_code(400);
        echo json_encode(["mensaje" => "Error: " . $e->getMessage()]);
    }
} else {
    http_response_code(400);
    echo json_encode(["mensaje" => "Datos incompletos, se requieren nombre, correo y contraseña"]);
}
?>

