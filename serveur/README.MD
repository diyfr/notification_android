todo
## Partie serveur de l'envoi de notification  
GCM nécessite de déclarer l'adresse IP du serveur emmettant la notification, donc il est possible d'héberger cette partie serveur à domicile à condition d'avoir une IP Fixe  

configGCM.php  
  Fichier à modifier en y renseignant les paramètres de connexion à votre base de données et de l'API Key générée sur la console dev google.  
  
register.php  
  C'est le script appelé par l'application Android pour enregister votre device dans la base de données
