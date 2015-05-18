
create database gcm_db;
grant all on gcm_db.* to 'gcm_dbuser'@'localhost' identified by '{PASSWORD}';
create user gcm_dbuser;

CREATE TABLE IF NOT EXISTS `gcm_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL DEFAULT '',
  `gcm_regid` text,
  `email` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) 
