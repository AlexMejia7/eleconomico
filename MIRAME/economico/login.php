<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include_once('database.php');

class Usuario {
    private $conn;

    public function __construct($db) {
        $this->conn = $db;
    }

    public function login($correo, $contrasena) {
        $query = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->execute([$correo, $contrasena]);

        if ($stmt->rowCount() > 0) {
            $usuario = $stmt->fetch(PDO::FETCH_ASSOC);
            unset($usuario['contrasena']);
            $usuario['id'] = $usuario['id_usuario'];
            unset($usuario['id_usuario']);
            return $usuario;
        } else {
            return null;
        }
    }
}

$database = new Database();
$db = $database->getConnection();

// Leer JSON crudo y log para debug
$dataRaw = file_get_contents("php://input");
error_log("JSON recibido en login.php: " . $dataRaw);

$data = json_decode($dataRaw);

if (!empty($data->correo) && !empty($data->contrasena)) {
    $usuarioObj = new Usuario($db);
    $resultado = $usuarioObj->login($data->correo, $data->contrasena);

    if ($resultado) {
        echo json_encode([
            "mensaje" => "Inicio de sesión exitoso",
            "usuario" => $resultado
        ]);
    } else {
        http_response_code(401);
        echo json_encode(["mensaje" => "Correo o contraseña incorrectos"]);
    }
} else {
    http_response_code(400);
    echo json_encode(["mensaje" => "Faltan datos"]);
}
?>

