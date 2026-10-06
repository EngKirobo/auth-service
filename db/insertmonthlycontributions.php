<?php

/*
|--------------------------------------------------------------------------
| Database Configuration
|--------------------------------------------------------------------------
*/

$host = "localhost";
$dbname = "atcmaafa";
$username = "root";
$password = "root";


/*
|--------------------------------------------------------------------------
| Connect to MySQL
|--------------------------------------------------------------------------
*/

try {

    $pdo = new PDO(
        "mysql:host=$host;dbname=$dbname;charset=utf8mb4",
        $username,
        $password
    );

    $pdo->setAttribute(
        PDO::ATTR_ERRMODE,
        PDO::ERRMODE_EXCEPTION
    );

} catch (PDOException $e) {

    die(
        "Database connection failed: "
        . $e->getMessage()
    );
}


/*
|--------------------------------------------------------------------------
| Insert Transactions
|--------------------------------------------------------------------------
*/

$sql = "
    INSERT INTO transactions (
        user_id,
        collect_id
    )
    SELECT
        id,
        3
    FROM users
    WHERE status = 1
";


try {

    $stmt = $pdo->prepare($sql);

    $stmt->execute();

    $inserted = $stmt->rowCount();

    echo "Successfully inserted "
        . $inserted
        . " transaction(s).";

} catch (PDOException $e) {

    echo "Insert failed: "
        . $e->getMessage();
}