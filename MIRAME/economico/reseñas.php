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
            $stmt = $db->prepare("SELECT * FROM resenas WHERE id_resena = ?");
            $stmt->execute([$_GET['id']]);
            $resena = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($resena) {
                echo json_encode($resena);
            } else {
                http_response_code(404);
                echo json_encode(["mensaje" => "Reseña no encontrada"]);
            }
        } else {
            $stmt = $db->query("SELECT * FROM resenas");
            $resenas = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($resenas);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true);
        if (!isset($data['id_usuario']) || (!isset($data['id_producto']) && !isset($data['id_repartidor'])) || !isset($data['comentario'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Faltan datos obligatorios: id_usuario, comentario y al menos id_producto o id_repartidor"]);
            break;
        }

        // Validar calificación si viene
        $calificacion = null;
        if (isset($data['calificacion'])) {
            $cal = (int)$data['calificacion'];
            if ($cal < 1 || $cal > 5) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Calificación debe estar entre 1 y 5"]);
                break;
            }
            $calificacion = $cal;
        }

        $fecha = $data['fecha'] ?? date('Y-m-d H:i:s');
        $id_producto = $data['id_producto'] ?? null;
        $id_repartidor = $data['id_repartidor'] ?? null;

        $stmt = $db->prepare("INSERT INTO resenas (id_usuario, id_producto, id_repartidor, calificacion, comentario, fecha) VALUES (?, ?, ?, ?, ?, ?)");
        try {
            $stmt->execute([
                $data['id_usuario'],
                $id_producto,
                $id_repartidor,
                $calificacion,
                $data['comentario'],
                $fecha
            ]);
            echo json_encode(["mensaje" => "Reseña creada", "id" => $db->lastInsertId()]);
        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al crear reseña: " . $e->getMessage()]);
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

        foreach (['id_usuario', 'id_producto', 'id_repartidor', 'calificacion', 'comentario', 'fecha'] as $field) {
            if (isset($data[$field])) {
                if ($field === 'calificacion') {
                    $cal = (int)$data[$field];
                    if ($cal < 1 || $cal > 5) {
                        http_response_code(400);
                        echo json_encode(["mensaje" => "Calificación debe estar entre 1 y 5"]);
                        exit;
                    }
                }
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
        $sql = "UPDATE resenas SET " . implode(", ", $fields) . " WHERE id_resena = ?";
        $stmt = $db->prepare($sql);
        try {
            $stmt->execute($values);
            echo json_encode(["mensaje" => "Reseña actualizada"]);
        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al actualizar reseña: " . $e->getMessage()]);
        }
        break;

    case 'DELETE':
        if (!isset($_GET['id'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Se necesita id para eliminar"]);
            break;
        }
        $stmt = $db->prepare("DELETE FROM resenas WHERE id_resena = ?");
        try {
            $stmt->execute([$_GET['id']]);
            echo json_encode(["mensaje" => "Reseña eliminada"]);
        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al eliminar reseña: " . $e->getMessage()]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["mensaje" => "Método no permitido"]);
        break;
}
?>
