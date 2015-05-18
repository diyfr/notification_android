<?php 
/*
* Fonction réalisant le lien entre la base de données et l'envoi de notification
* Elle traite aussi le résultat d'envoi des notifications
* $email    : une adresse email, si null on prend touts les utilisateurs
* $message  : tableau de valeur correspondant au message à envoyer
* $debug    : Affichage des traces
*/

function sends($email,$message,$debug){
	include_once 'db_functions.php';
	include_once 'gcm_functions.php';
	$reg_ids =array();
	$result=array();
	$db = new DB_Functions();
	if ($email)
	{
		$reg_ids = $db->getAllRegIdForEmailUser($email);
	}else{
		$reg_ids = $db->getAllRegId();
	}
	if ($reg_ids && count($reg_ids)>0)
	{
		$result = sendMessages($reg_ids,$message,$debug);
		if ($debug)
		{
			var_dump($result);
		}
		if ($result && !array_key_exists("error",$result)){
			if (array_key_exists("failure",$result)  && $result["failure"]>0){
				foreach($result["failureList"] as $userId)
				{
					$db->removeGCMId($userId);
				}
				
			}
			if (array_key_exists("updatedRegisteredId",$result)  && $result["updatedRegisteredId"]>0){
				foreach($result["updatedRegisteredListId"] as $userId=>$newRegId)
				{
					$db->updateGCMId($userid,$newRegId);
				}
			}
		}
		unset($result["failureList"]);
		unset($result["updatedRegisteredListId"]);
		unset($result["successList"]);
	}
	else
	{
		$result = array("error"=>"no User");
	}
	return $result;
}

?>
