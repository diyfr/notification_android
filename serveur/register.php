<?php

/**
 * Service d'Enregistrement d'un device pour un utilisateur
 */
header('Content-type: application/json');
$data = json_decode(file_get_contents('php://input'));

	
$resultpost='{"registered":false}';
if ($data) {
    $uuid = $data->{'uuid'};
    $gcm_regid = $data->{'registered_id'}; // GCM Registration ID
	$email = $data->{'email'};


    include_once 'db_functions.php';

    $db = new DB_Functions();

    $res = $db->createOrUpdate($uuid, $gcm_regid,$email);
    $reg_id = array($gcm_regid);
	$type="1";
	/*
	* Send first registration 
	*/
    $message = array("content" => "Votre application a bien été associée à votre compte", "title"=>"Enregistrement", "subtitle"=>"Première notification", "level"=>0,"type"=>$type,"emis"=> date('Y-m-d H:i:s'));

    include_once 'functions.php';

    $result = sends($email, $message,false);
	if ($result && !array_key_exists("error",$result) && $result['success']>0){
		$resultpost= '{"registered":true}';
	}
}
echo $resultpost;
?>
