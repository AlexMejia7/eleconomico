<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Responder a preflight OPTIONS (CORS)
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Configura la conexión a la base de datos
$host = '34.31.145.38';
$dbname = 'el_economico_db';
$username = 'root';  // Cambia según tu configuración
$password = '@lexNain1234';      // Cambia según tu configuración

try {
    $db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $username, $password);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["mensaje" => "Error de conexión: " . $e->getMessage()]);
    exit();
}

// Obtener método HTTP
$method = $_SERVER['REQUEST_METHOD'];

try {
    switch ($method) {
        case 'GET':
            if (isset($_GET['id'])) {
                $stmt = $db->prepare("SELECT * FROM detalles_pedido WHERE id_detalle = ?");
                $stmt->execute([$_GET['id']]);
                $detalle = $stmt->fetch(PDO::FETCH_ASSOC);
                if ($detalle) {
                    echo json_encode($detalle);
                } else {
                    http_response_code(404);
                    echo json_encode(["mensaje" => "Detalle no encontrado"]);
                }
            } else {
                $stmt = $db->query("SELECT * FROM detalles_pedido");
                $detalles = $stmt->fetchAll(PDO::FETCH_ASSOC);
                echo json_encode($detalles);
            }
            break;

        case 'POST':
            $data = json_decode(file_get_contents("php://input"), true);
            if (!isset($data['id_pedido'], $data['id_producto'], $data['cantidad'], $data['precio_unitario'])) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Faltan datos obligatorios"]);
                break;
            }
            $stmt = $db->prepare("INSERT INTO detalles_pedido (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)");
            if ($stmt->execute([
                $data['id_pedido'],
                $data['id_producto'],
                $data['cantidad'],
                $data['precio_unitario']
            ])) {
                echo json_encode(["mensaje" => "Detalle creado", "id" => $db->lastInsertId()]);
            } else {
                http_response_code(500);
                echo json_encode(["mensaje" => "Error al crear detalle"]);
            }
            break;

        case 'PUT':
            parse_str(file_get_contents("php://input"), $put_vars);
            $id = $_GET['id'] ?? null;
            if (!$id) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Se necesita id para actualizar"]);
                break;
            }
            $data = json_decode(file_get_contents("php://input"), true);
            $fields = [];
            $values = [];

            foreach(['id_pedido', 'id_producto', 'cantidad', 'precio_unitario'] as $field){
                if (isset($data[$field])) {
                    $fields[] = "$field = ?";
                    $values[] = $data[$field];
                }
            }
            if (count($fields) === 0) {
                http_response_code(400);
                echo json_encode(["mensaje" => "No hay campos para actualizar"]);
                break;
            }
            $values[] = $id;
            $sql = "UPDATE detalles_pedido SET ".implode(", ", $fields)." WHERE id_detalle = ?";
            $stmt = $db->prepare($sql);
            if ($stmt->execute($values)) {
                echo json_encode(["mensaje" => "Detalle actualizado"]);
            } else {
                http_response_code(500);
                echo json_encode(["mensaje" => "Error al actualizar detalle"]);
            }
            break;

        case 'DELETE':
            if (!isset($_GET['id'])) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Se necesita id para eliminar"]);
                break;
            }
            $stmt = $db->prepare("DELETE FROM detalles_pedido WHERE id_detalle = ?");
            if ($stmt->execute([$_GET['id']])) {
                echo json_encode(["mensaje" => "Detalle eliminado"]);
            } else {
                http_response_code(500);
                echo json_encode(["mensaje" => "Error al eliminar detalle"]);
            }
            break;

        default:
            http_response_code(405);
            echo json_encode(["mensaje" => "Método no permitido"]);
            break;
    }
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["mensaje" => "Error en la base de datos: " . $e->getMessage()]);
}
