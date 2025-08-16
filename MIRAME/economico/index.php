<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Permitir preflight OPTIONS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

include_once('database.php');
$db = (new Database())->getConnection();

$tabla = $_GET['tabla'] ?? null;

if (!$tabla) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "Falta el parámetro 'tabla'"]);
    exit();
}

switch ($tabla) {
    case 'usuarios':
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'GET':
                if (isset($_GET['id'])) {
                    $stmt = $db->prepare("SELECT * FROM usuarios WHERE id_usuario = ?");
                    if ($stmt->execute([$_GET['id']])) {
                        $usuario = $stmt->fetch(PDO::FETCH_ASSOC);
                        if ($usuario) {
                            unset($usuario['contrasena']); // no enviar contraseña
                            echo json_encode(["success" => true, "data" => $usuario]);
                        } else {
                            http_response_code(404);
                            echo json_encode(["success" => false, "message" => "Usuario no encontrado"]);
                        }
                    } else {
                        http_response_code(500);
                        echo json_encode(["success" => false, "message" => "Error en la consulta"]);
                    }
                } else {
                    $stmt = $db->query("SELECT * FROM usuarios");
                    $usuarios = $stmt->fetchAll(PDO::FETCH_ASSOC);
                    foreach ($usuarios as &$u) {
                        unset($u['contrasena']);
                    }
                    echo json_encode(["success" => true, "data" => $usuarios]);
                }
                break;

            case 'POST':
                $data = json_decode(file_get_contents("php://input"), true);
                if (!isset($data['nombre']) || !isset($data['correo'])) {
                    http_response_code(400);
                    echo json_encode(["success" => false, "message" => "Faltan datos obligatorios: nombre o correo"]);
                    break;
                }
                $stmt = $db->prepare("INSERT INTO usuarios (nombre, correo) VALUES (?, ?)");
                if ($stmt->execute([$data['nombre'], $data['correo']])) {
                    http_response_code(201);
                    echo json_encode(["success" => true, "message" => "Usuario creado", "id" => $db->lastInsertId()]);
                } else {
                    http_response_code(500);
                    echo json_encode(["success" => false, "message" => "Error al crear usuario"]);
                }
                break;

            default:
                http_response_code(405);
                echo json_encode(["success" => false, "message" => "Método no permitido"]);
                break;
        }
        break;

    default:
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Endpoint no válido: tabla=$tabla"]);
        break;
}
