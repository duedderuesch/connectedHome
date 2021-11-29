<?php
//database constants
//http://10.3.141.140/dbaccess/upload.php?db=Pollen&sql=INSERT@~INTO@~`user`@~(`partregion_id`,@~`value`)@~VALUES@~(%2792%27,@~%271%27); as example

define('DB_HOST', 'localhost');
define('DB_USER', 'placed_app');
define('DB_PASS', 'nassePflanzenerde');


$database = $_GET['db'];
$request = $_GET['sql'];

define('DB_NAME', $database);


//connecting to database and getting the connection object
$conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

//Checking if any error occured while connecting
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: ". mysqli_connect_error() ;
    die();
}

$request = str_replace("@~", " ", $request);

$stmt = $conn->prepare($request);

$stmt->execute();

echo $stmt->sqlstate;



?>