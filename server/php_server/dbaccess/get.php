<?php

//ulr-example: http://10.3.141.140/dbaccess/get.php?table=plant_object&keys=1

//database constants
define('DB_HOST', 'localhost');
define('DB_USER', 'placed_app');
define('DB_PASS', 'nassePflanzenerde');
define('DB_NAME', 'placed');

$table = $_GET['table'];
$getkeys = $_GET['keys'];
$where = $_GET['where'];
$limit = $_GET['limit'];

//connecting to database and getting the connection object
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

//Checking if any error occured while connecting
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
    die();
}
if($where != null && $limit != null){
    $where = str_replace("@~", " ", $where);
    if($table == "data"){
        $sql = "SELECT * FROM " . $table . " WHERE " . $where . " ORDER BY id DESC LIMIT " . $limit;
    } else {
        $sql = "SELECT * FROM " . $table . " WHERE " . $where . " LIMIT " . $limit;
    }
} elseif ($where == null && $limit != null){
    if($table == "data"){
        $sql = "SELECT * FROM " . $table . " ORDER BY id DESC LIMIT " . $limit;
    } else {
        $sql = "SELECT * FROM " . $table . " LIMIT " . $limit;
    }
} else {
    $sql = "SELECT * FROM " . $table;
}
$result = $conn->query($sql);
if($getkeys == 0){
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
    echo json_encode($emparray);
    } else {
        $keys = array();
        foreach($result as $sub) {
            $keys = array_merge($keys, $sub);
        }
        echo json_encode(array_keys($keys));
    }
?>

