<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

// Manejo de preflight OPTIONS para CORS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

include_once('database.php');
$database = new Database();
$db = $database->getConnection();

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            $stmt = $db->prepare("SELECT * FROM usuarios WHERE id_usuario = ?");
            if ($stmt->execute([$_GET['id']])) {
                $usuario = $stmt->fetch(PDO::FETCH_ASSOC);
                if ($usuario) {
                    unset($usuario['contrasena']); // Ocultar contraseña
                    echo json_encode($usuario);
                } else {
                    http_response_code(404);
                    echo json_encode(["mensaje" => "Usuario no encontrado"]);
                }
            } else {
                http_response_code(500);
                echo json_encode(["mensaje" => "Error en la consulta"]);
            }
        } else {
            $stmt = $db->query("SELECT * FROM usuarios");
            $usuarios = $stmt->fetchAll(PDO::FETCH_ASSOC);
            foreach ($usuarios as &$u) {
                unset($u['contrasena']); // Ocultar contraseña
            }
            echo json_encode($usuarios);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true);
        if (!isset($data['nombre'], $data['correo'], $data['contrasena'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Faltan datos obligatorios: nombre, correo o contrasena"]);
            break;
        }

        $nombre = trim($data['nombre']);
        $correo = trim($data['correo']);
        $contrasena = trim($data['contrasena']);
        $fotoPerfilBase64 = $data['foto_perfil_base64'] ?? null;
        $descripcion = $data['descripcion'] ?? null;
        $ubicacionGps = $data['ubicacion_gps'] ?? null;
        $codigoVerificacion = $data['codigo_verificacion'] ?? null;
        $verificado = 0; // Por defecto no verificado
        $apiKey = null;
        $rol = $data['rol'] ?? 'usuario';

        // Validar correo
        if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Formato de correo inválido"]);
            break;
        }

        // Verificar si correo existe
        $stmtCheck = $db->prepare("SELECT id_usuario FROM usuarios WHERE correo = ?");
        $stmtCheck->execute([$correo]);
        if ($stmtCheck->fetch()) {
            http_response_code(409);
            echo json_encode(["mensaje" => "Correo ya registrado"]);
            break;
        }

        // Aquí guardamos la contraseña en texto plano (no recomendado, pero es lo que pediste)
        $hashedPassword = $contrasena;

        $stmt = $db->prepare("INSERT INTO usuarios 
            (nombre, correo, contrasena, foto_perfil_base64, descripcion, ubicacion_gps, codigo_verificacion, verificado, api_key, rol) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        $params = [
            $nombre,
            $correo,
            $hashedPassword,
            $fotoPerfilBase64,
            $descripcion,
            $ubicacionGps,
            $codigoVerificacion,
            $verificado,
            $apiKey,
            $rol
        ];

        if ($stmt->execute($params)) {
            http_response_code(201);
            echo json_encode([
                "mensaje" => "Usuario creado",
                "id" => $db->lastInsertId()
            ]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al crear usuario"]);
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

        $allowedFields = ['nombre', 'correo', 'contrasena', 'foto_perfil_base64', 'descripcion', 'ubicacion_gps', 'codigo_verificacion', 'verificado', 'api_key', 'rol'];
        $fields = [];
        $values = [];

        foreach ($allowedFields as $field) {
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
        $sql = "UPDATE usuarios SET " . implode(", ", $fields) . " WHERE id_usuario = ?";
        $stmt = $db->prepare($sql);

        if ($stmt->execute($values)) {
            echo json_encode(["mensaje" => "Usuario actualizado"]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al actualizar usuario"]);
        }
        break;

    case 'DELETE':
        if (!isset($_GET['id'])) {
            http_response_code(400);
            echo json_encode(["mensaje" => "Se necesita id para eliminar"]);
            break;
        }

        $stmt = $db->prepare("DELETE FROM usuarios WHERE id_usuario = ?");
        if ($stmt->execute([$_GET['id']])) {
            echo json_encode(["mensaje" => "Usuario eliminado"]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al eliminar usuario"]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["mensaje" => "Método no permitido"]);
        break;
}
?>

