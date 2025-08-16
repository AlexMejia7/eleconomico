<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Responder rápido a OPTIONS para CORS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

include_once('database.php');

$method = $_SERVER['REQUEST_METHOD'];

// Obtener el endpoint desde la URL
$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$uri_parts = explode('/', trim($uri, '/'));
$endpoint = end($uri_parts);

$database = new Database();
$db = $database->getConnection();

switch ($endpoint) {
    case 'login':
        if ($method !== 'POST') {
            http_response_code(405);
            echo json_encode(["success" => false, "message" => "Método no permitido"]);
            exit();
        }

        $data = json_decode(file_get_contents("php://input"), true);
        if (empty($data['correo']) || empty($data['contrasena'])) {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Faltan campos requeridos"]);
            exit();
        }

        try {
            $stmt = $db->prepare("SELECT * FROM usuarios WHERE correo = ?");
            $stmt->execute([$data['correo']]);
            $usuario = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($usuario && $usuario['contrasena'] === $data['contrasena']) {
                // Excluir contrasena de la respuesta
                unset($usuario['contrasena']);

                echo json_encode([
                    "success" => true,
                    "usuario" => [
                        "id" => $usuario['id_usuario'],
                        "nombre" => $usuario['nombre'],
                        "correo" => $usuario['correo']
                    ]
                ]);
            } else {
                http_response_code(401);
                echo json_encode(["success" => false, "message" => "Credenciales inválidas"]);
            }
        } catch (PDOException $e) {
            http_response_code(500);
            echo json_encode(["success" => false, "message" => "Error en la base de datos: " . $e->getMessage()]);
        }
        break;

    default:
        http_response_code(404);
        echo json_encode(["success" => false, "message" => "Endpoint no válido: /$endpoint"]);
        break;
}
