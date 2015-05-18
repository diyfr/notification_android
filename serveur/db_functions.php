<?php

class DB_Functions {
  /*
  * Classe d'interaction avec la base de données, centrée autour de l'utilisateur.
  * Un utilisateur peut avoir plusieurs UUID pour une adresse Email.
  */

    private $db;

    // constructor
    function __construct() {
        include_once 'db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * creation ou mise à jour d'un utilisateurs
     * returns user details
     */
    public function createOrUpdate($uuid, $gcm_regid,$email) {
		$result = mysql_query("DELETE FROM gcm_users where gcm_regid ='$gcm_regid'");
        // insert user into database
        $result = mysql_query("INSERT INTO gcm_users(uuid, gcm_regid,email, created_at) VALUES('$uuid', '$gcm_regid','$email', NOW()) ON DUPLICATE KEY UPDATE gcm_regid='$gcm_regid',email='$email', created_at=NOW()");
		if ($result) {
            // get user details
            $id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM gcm_users WHERE uuid = '$uuid'") or die(mysql_error());
            // return user details
            if (mysql_num_rows($result) > 0) {
                return mysql_fetch_array($result);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
	
	public function updateGCMId($userid,$newgcmid){
		$result = mysql_query("select id, gcm_regid FROM gcm_users where gcm_regid like '".$newgcmid."';");
		if ($result) {
			if (mysql_num_rows($result) > 0) {
				while ($row = mysql_fetch_assoc($result)) {
					if($row['id'] != $userid)
					{
						$delete = mysql_query("DELETE FROM gcm_users where id =".$row['id'].";");
					}
				}
			}
		}
		$result = mysql_query("UPDATE gcm_users set gcm_regid= '".$newgcmid."' where id =$userid ;");
	}
	
	public function removeGCMId($userid){
		// On supprime le registered_id
		$result = mysql_query("UPDATE gcm_users set gcm_regid= '' where id =$userid ;");
		// On récupère les userId pour les comptes ayant la même adresse email.
		$result = mysql_query("SELECT id FROM gcm_users WHERE email IN (SELECT email from gcm_users where id =".$userid." );");
		
		if ($result && mysql_num_rows($result) > 1) {
			// Ce compte est superflu car plus de registered id et meme email utilisé pour un autre utilisateur id
			$delete = mysql_query("DELETE FROM gcm_users where id =".$userid.";");
		}
	
	}
	
	  /**
     * Tableau de regId pour un utilisateur
     */
    public function getAllRegIdForEmailUser($email) {
		$response = array();
        $result = mysql_query("select id, gcm_regid FROM gcm_users where gcm_regid NOT LIKE '' AND email like '".$email."';");
		if ($result)
		{
			while ($row = mysql_fetch_assoc($result)) {
				$response[$row['id']]= $row['gcm_regid'];
			}
		}
        return $response;
    }
	
    /**
     * Tableau de regId Tous les utilisateurs
     */
    public function getAllRegId() {
		$response = array();
        $result = mysql_query("select id, gcm_regid FROM gcm_users where gcm_regid NOT LIKE '';");
		if (!$result)
		{
			die('Pas de données:'.mysql_error());
		}
		else
		{
			while ($row = mysql_fetch_assoc($result)) {
				$response[$row['id']]= $row['gcm_regid'];
			}
		}
        return $response;
    }

}

?>
