<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Manejo preflight CORS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

include_once 'database.php';

$database = new Database();
$db = $database->getConnection();

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            $stmt = $db->prepare("SELECT * FROM productos WHERE id_producto = ?");
            $stmt->execute([$_GET['id']]);
            $producto = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($producto) {
                echo json_encode($producto);
            } else {
                http_response_code(404);
                echo json_encode(["mensaje" => "Producto no encontrado"]);
            }
        } else {
            $stmt = $db->query("SELECT * FROM productos");
            $productos = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($productos);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true);
        if (!isset($data['nombre'], $data['precio'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Faltan datos obligatorios: nombre o precio"]);
            break;
        }

        $disponible = isset($data['disponible']) ? (int)$data['disponible'] : 1;
        $stock = isset($data['stock']) ? (int)$data['stock'] : 0;
        $supermercado_nombre = $data['supermercado_nombre'] ?? 'El Económico';

        $stmt = $db->prepare("INSERT INTO productos (nombre, descripcion, imagen_base64, precio, stock, disponible, supermercado_nombre) VALUES (?, ?, ?, ?, ?, ?, ?)");
        if ($stmt->execute([
            $data['nombre'],
            $data['descripcion'] ?? null,
            $data['imagen_base64'] ?? null,
            $data['precio'],
            $stock,
            $disponible,
            $supermercado_nombre
        ])) {
            http_response_code(201);
            echo json_encode(["mensaje" => "Producto creado", "id" => $db->lastInsertId()]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al crear producto"]);
        }
        break;

    case 'PUT':
        if (!isset($_GET['id'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Se necesita id para actualizar"]);
            break;
        }
        $data = json_decode(file_get_contents("php://input"), true);
        $id = $_GET['id'];

        $fields = [];
        $values = [];

        foreach (['nombre', 'descripcion', 'imagen_base64', 'precio', 'stock', 'disponible', 'supermercado_nombre'] as $field) {
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
        $sql = "UPDATE productos SET " . implode(", ", $fields) . " WHERE id_producto = ?";
        $stmt = $db->prepare($sql);

        if ($stmt->execute($values)) {
            echo json_encode(["mensaje" => "Producto actualizado"]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al actualizar producto"]);
        }
        break;

    case 'DELETE':
        if (!isset($_GET['id'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Se necesita id para eliminar"]);
            break;
        }
        $stmt = $db->prepare("DELETE FROM productos WHERE id_producto = ?");
        if ($stmt->execute([$_GET['id']])) {
            echo json_encode(["mensaje" => "Producto eliminado"]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al eliminar producto"]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["mensaje" => "Método no permitido"]);
        break;
}
?>
