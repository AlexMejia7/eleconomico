<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include_once('database.php');

$database = new Database();
$db = $database->getConnection();

$method = $_SERVER['REQUEST_METHOD'];

// Responder rápido a OPTIONS para CORS
if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

try {
    switch ($method) {
        case 'GET':
            if (isset($_GET['id'])) {
                $stmt = $db->prepare("SELECT * FROM repartidores WHERE id_repartidor = ?");
                $stmt->execute([$_GET['id']]);
                $repartidor = $stmt->fetch(PDO::FETCH_ASSOC);
                if ($repartidor) {
                    unset($repartidor['contrasena']); // No mostrar contraseña
                    echo json_encode($repartidor);
                } else {
                    http_response_code(404);
                    echo json_encode(["mensaje" => "Repartidor no encontrado"]);
                }
            } else {
                $stmt = $db->query("SELECT * FROM repartidores");
                $repartidores = $stmt->fetchAll(PDO::FETCH_ASSOC);
                foreach ($repartidores as &$r) {
                    unset($r['contrasena']);
                }
                echo json_encode($repartidores);
            }
            break;

        case 'POST':
            $data = json_decode(file_get_contents("php://input"), true);
            if (!isset($data['nombre'], $data['correo'], $data['contrasena'])) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Faltan datos obligatorios: nombre, correo o contrasena"]);
                break;
            }

            $stmt = $db->prepare("INSERT INTO repartidores 
                (nombre, correo, contrasena, foto_perfil_base64, ubicacion_gps, disponible, api_key) 
                VALUES (?, ?, ?, ?, ?, ?, ?)");

            $disponible = isset($data['disponible']) ? (int)$data['disponible'] : 1;
            $api_key = $data['api_key'] ?? null;

            $stmt->execute([
                $data['nombre'],
                $data['correo'],
                $data['contrasena'], // texto plano, ¡no recomendado!
                $data['foto_perfil_base64'] ?? null,
                $data['ubicacion_gps'] ?? null,
                $disponible,
                $api_key
            ]);
            echo json_encode(["mensaje" => "Repartidor creado", "id" => $db->lastInsertId()]);
            break;

        case 'PUT':
            if (!isset($_GET['id'])) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Se necesita id para actualizar"]);
                break;
            }

            $id = $_GET['id'];
            $data = json_decode(file_get_contents("php://input"), true);

            $fields = [];
            $values = [];

            foreach(['nombre', 'correo', 'contrasena', 'foto_perfil_base64', 'ubicacion_gps', 'disponible', 'api_key'] as $field) {
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
            $sql = "UPDATE repartidores SET " . implode(", ", $fields) . " WHERE id_repartidor = ?";
            $stmt = $db->prepare($sql);
            $stmt->execute($values);
            echo json_encode(["mensaje" => "Repartidor actualizado"]);
            break;

        case 'DELETE':
            if (!isset($_GET['id'])) {
                http_response_code(400);
                echo json_encode(["mensaje" => "Se necesita id para eliminar"]);
                break;
            }
            $stmt = $db->prepare("DELETE FROM repartidores WHERE id_repartidor = ?");
            $stmt->execute([$_GET['id']]);
            echo json_encode(["mensaje" => "Repartidor eliminado"]);
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
