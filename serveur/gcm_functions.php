<?php
/**
* Fonctions permettant l'envoi de notifications auprès de GCM
*/


/**
	$reg_ids : array Of RegId with id User as Key
	$message : array
	$debug   : show debug trace
*/
function sendMessages($reg_ids,$message,$debug){

	$chunks = array_chunk($reg_ids,1000);
	$status = send_notification($chunks,$message,$debug);

	$result =array();
	
	$canonical_ids=0;
	$failure=0;
	$success=0;
	$failureList=array();
	$canonical_idsList=array();
	$successList=array();

	$errorType="";
	$newgcmid="";
	
	
	if(!empty($status) && is_array($status)){
		foreach($status as $key=>$row){
			$hasFailure=false;
			$hasCanonicalId=false;
			$hasSuccess =false;
			if($row->{'success'}){
				$success 		+= $row->{'success'};
				$hasSuccess 	=  $row->{'success'}>0;
			}
			if($row->{'canonical_ids'}){
				$canonical_ids 	+= $row->{'canonical_ids'};
				$hasCanonicalId =  $row->{'canonical_ids'}>0;
			}
			if ($row->{'failure'}){
				$failure		+= $row->{'failure'};
				$hasFailure		=  $row->{'failure'}>0;
			}
			if($row->{'results'} && !empty($row->{'results'}) && is_array($row->{'results'})){
				foreach($row->{'results'} as $k=>$v){
					$userid = array_search($chunks[$key][$k], $reg_ids);
					if($hasCanonicalId && $v->{'registration_id'}){
						$newgcmid  = $v->{'registration_id'};
						$canonical_idsList[$userid]=$newgcmid;
					}
					else if($hasFailure && $v->{'error'}){
						$errorType  = $v->{'error'};
						$failureList[]=$userid;
					}
					else if ($hasSuccess){
						$successList[]=$userid;
					}
				}
			}
		}
		$result =array("success"=>$success,"successList"=>$successList, "failure"=>$failure,"failureList"=>$failureList, "updatedRegisteredId"=>$canonical_ids,"updatedRegisteredListId"=>$canonical_idsList );
	}else{
		$result =array("error"=>"No response or Error from gcm function");
	}
	
	return $result;

}



/*
* Envoi auprès de GCM une notification
* $registration_ids : Array de regId à notifier
* $message          : message au format Json à envoyer aux RegId
* $debug            : option d'affichage de trace
*/
function send_notification($registration_ids, $message, $debug) {
	include_once 'config.php';
	$status =array();
	// Set POST variables
	$url = 'https://android.googleapis.com/gcm/send';
	
	foreach($registration_ids as $reg_ids)
	{
		$fields = array(
			'registration_ids' => array_values($reg_ids),
			'data' => $message,
		);
		$headers = array(
			'Authorization: key=' . GOOGLE_API_KEY,
			'Content-Type: application/json;charset=\"utf-8\"'
		);
		// Open connection
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		// Disabling SSL Certificate support temporarly
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
		// Execute post
		$result = curl_exec($ch);

		if ($result === FALSE) {
		echo  curl_error($ch);
			die('Curl failed: ' . curl_error($ch));
		}
		// Close connection
		curl_close($ch);
		if ($debug)
		{
			print_r($result);
		}
		$data=json_decode($result);
		$status[]=$data;
	}
	return $status;
}

?>
