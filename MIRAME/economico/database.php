<?php
class Database {
    private $host = "34.31.145.38";
    private $db_name = "el_economico_db";
    private $username = "root";
    private $password = "@lexNain1234";
    public $conn;

    public function getConnection() {
        $this->conn = null;
        try {
            $this->conn = new PDO(
                "mysql:host={$this->host};dbname={$this->db_name};charset=utf8mb4",
                $this->username,
                $this->password
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch(PDOException $exception){
            echo json_encode(["mensaje" => "Error de conexión: " . $exception->getMessage()]);
            exit;
        }
        return $this->conn;
    }
}
?>

