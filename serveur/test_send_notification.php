<?php
/*
* Exemple de requête GET permettant de tester l'envoi de notification
* Il est déconseillé de l'utiliser tel quel , et de sécuriser à minima l'accès à ce script (POST, KEYCODE ..)
*/
include_once './functions.php';
$debug=true;

if (isset($_GET["content"])&& isset($_GET["subtitle"]) && isset($_GET["title"]) && isset($_GET["level"])) {
	$content = $_GET["content"];
	$title = $_GET["title"];
	$subtitle = $_GET["subtitle"];
	$level = $_GET["level"];
	$type="1";
	$email=null;
	if (isset($_GET["email"])){
		$email = $_GET["email"];
	}
	$message = array("title"=>$title, "subtitle"=>$subtitle, "content" => $content, "type"=>$type, "level"=>$level,"emis"=> date('Y-m-d H:i:s'));
	$response = sends($email,$message,$debug);
	print_r($response);
}
?>
