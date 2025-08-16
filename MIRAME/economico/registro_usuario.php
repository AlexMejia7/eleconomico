<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

include_once('database.php');

$database = new Database();
$conn = $database->getConnection();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents("php://input"));

    // Validar campos obligatorios
    if (!isset($data->nombre) || !isset($data->correo) || !isset($data->contrasena)) {
        http_response_code(400);
        echo json_encode(["mensaje" => "Faltan datos obligatorios: nombre, correo o contraseña"]);
        exit;
    }

    $nombre = trim($data->nombre);
    $correo = trim($data->correo);
    $contrasena = trim($data->contrasena);
    $fotoPerfilBase64 = isset($data->fotoPerfilBase64) ? $data->fotoPerfilBase64 : null;
    $descripcion = isset($data->descripcion) ? trim($data->descripcion) : null;
    $ubicacionGps = isset($data->ubicacionGps) ? trim($data->ubicacionGps) : null;
    $codigoVerificacion = isset($data->codigoVerificacion) ? trim($data->codigoVerificacion) : null;
    $verificado = isset($data->verificado) ? (int)$data->verificado : 0;
    $apiKey = isset($data->apiKey) ? trim($data->apiKey) : null;
    $rol = isset($data->rol) ? trim($data->rol) : "usuario";

    if (empty($nombre) || empty($correo) || empty($contrasena)) {
        http_response_code(400);
        echo json_encode(["mensaje" => "Nombre, correo o contraseña vacíos"]);
        exit;
    }

    if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
        http_response_code(400);
        echo json_encode(["mensaje" => "Formato de correo inválido"]);
        exit;
    }

    // Verificar si el correo ya existe
    $stmtCheck = $conn->prepare("SELECT id_usuario FROM usuarios WHERE correo = ?");
    $stmtCheck->execute([$correo]);
    if ($stmtCheck->fetch()) {
        http_response_code(409);
        echo json_encode(["mensaje" => "Correo ya registrado"]);
        exit;
    }

    // Contraseña en texto plano (como pediste)
    $hashedPassword = $contrasena;

    try {
        $sql = "INSERT INTO usuarios 
            (nombre, correo, contrasena, foto_perfil_base64, descripcion, ubicacion_gps, codigo_verificacion, verificado, api_key, rol)
            VALUES 
            (:nombre, :correo, :contrasena, :fotoPerfilBase64, :descripcion, :ubicacionGps, :codigoVerificacion, :verificado, :apiKey, :rol)";

        $stmt = $conn->prepare($sql);

        $stmt->bindParam(':nombre', $nombre);
        $stmt->bindParam(':correo', $correo);
        $stmt->bindParam(':contrasena', $hashedPassword);
        $stmt->bindParam(':fotoPerfilBase64', $fotoPerfilBase64);
        $stmt->bindParam(':descripcion', $descripcion);
        $stmt->bindParam(':ubicacionGps', $ubicacionGps);
        $stmt->bindParam(':codigoVerificacion', $codigoVerificacion);
        $stmt->bindParam(':verificado', $verificado, PDO::PARAM_INT);
        $stmt->bindParam(':apiKey', $apiKey);
        $stmt->bindParam(':rol', $rol);

        if ($stmt->execute()) {
            http_response_code(201);
            echo json_encode(["mensaje" => "Usuario registrado correctamente", "id_usuario" => $conn->lastInsertId()]);
        } else {
            http_response_code(500);
            echo json_encode(["mensaje" => "Error al registrar usuario"]);
        }
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode(["mensaje" => "Error en la base de datos: " . $e->getMessage()]);
    }
} else {
    http_response_code(405);
    echo json_encode(["mensaje" => "Método no permitido"]);
}
?>

