<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

// Configuración de la conexión
$host = '34.31.145.38';
$dbname = 'el_economico_db';
$username = 'root';
$password = '@lexNain1234';

try {
    $db = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $username, $password);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["status" => "error", "mensaje" => "Error de conexión: " . $e->getMessage()]);
    exit();
}

$data = json_decode(file_get_contents("php://input"), true);

// Validar datos obligatorios
if (
    !isset($data["id_usuario"]) ||
    !isset($data["productos"]) || !is_array($data["productos"]) || count($data["productos"]) == 0 ||
    !isset($data["ubicacion_entrega"])
) {
    http_response_code(400);
    echo json_encode([
        "status" => "error",
        "mensaje" => "Faltan datos obligatorios o productos vacíos"
    ]);
    exit;
}

$id_usuario = $data["id_usuario"];
$productos_pedido = $data["productos"];
$ubicacion_entrega = $data["ubicacion_entrega"];
// id_repartidor es opcional, si no viene queda null
$id_repartidor = isset($data["id_repartidor"]) ? $data["id_repartidor"] : null;

$subtotal = 0.0;

try {
    // Validar que el usuario existe
    $stmtUsuario = $db->prepare("SELECT COUNT(*) FROM usuarios WHERE id_usuario = ?");
    $stmtUsuario->execute([$id_usuario]);
    if ($stmtUsuario->fetchColumn() == 0) {
        throw new Exception("Usuario no existe");
    }

    // Calcular subtotal y validar productos y stock
    foreach ($productos_pedido as $item) {
        if (!isset($item["id"]) || !isset($item["cantidad"]) || $item["cantidad"] <= 0) {
            throw new Exception("Datos inválidos en productos");
        }

        $stmt = $db->prepare("SELECT precio, stock FROM productos WHERE id_producto = ?");
        $stmt->execute([$item["id"]]);
        $producto_db = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$producto_db) {
            throw new Exception("Producto ID {$item['id']} no encontrado");
        }

        if ($producto_db["stock"] < $item["cantidad"]) {
            throw new Exception("Stock insuficiente para producto ID {$item['id']}");
        }

        $subtotal += $producto_db["precio"] * $item["cantidad"];
    }

    $impuesto = round($subtotal * 0.15, 2); // 15% impuesto
    $total = round($subtotal + $impuesto, 2);

    // Iniciar transacción
    $db->beginTransaction();

    // Insertar pedido (incluyendo id_repartidor)
    $stmtPedido = $db->prepare("INSERT INTO pedidos (id_usuario, id_repartidor, estado, ubicacion_entrega, subtotal, impuesto, total) VALUES (?, ?, 'pendiente', ?, ?, ?, ?)");
    $stmtPedido->execute([$id_usuario, $id_repartidor, $ubicacion_entrega, $subtotal, $impuesto, $total]);
    $id_pedido = $db->lastInsertId();

    // Preparar inserción de detalles y actualización de stock
    $stmtDetalle = $db->prepare("INSERT INTO detalles_pedido (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)");
    $stmtUpdateStock = $db->prepare("UPDATE productos SET stock = stock - ? WHERE id_producto = ?");

    foreach ($productos_pedido as $item) {
        $stmtPrecio = $db->prepare("SELECT precio FROM productos WHERE id_producto = ?");
        $stmtPrecio->execute([$item["id"]]);
        $precio_unitario = $stmtPrecio->fetchColumn();

        $stmtDetalle->execute([$id_pedido, $item["id"], $item["cantidad"], $precio_unitario]);
        $stmtUpdateStock->execute([$item["cantidad"], $item["id"]]);
    }

    // Confirmar transacción
    $db->commit();

    // Respuesta exitosa
    http_response_code(201);
    echo json_encode([
        "status" => "success",
        "mensaje" => "Pedido creado correctamente",
        "id_pedido" => (int)$id_pedido,
        "subtotal" => $subtotal,
        "impuesto" => $impuesto,
        "total" => $total,
        "id_repartidor" => $id_repartidor
    ]);

} catch (Exception $e) {
    // Revertir cambios en caso de error
    if ($db->inTransaction()) {
        $db->rollBack();
    }
    http_response_code(500);
    echo json_encode([
        "status" => "error",
        "mensaje" => $e->getMessage()
    ]);
}
?>

