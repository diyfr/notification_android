
create database gcm_db;
grant all on gcm_db.* to 'gcm_admin'@'localhost' identified by '{PASSWORD}';
create user gcm_admin;

CREATE TABLE IF NOT EXISTS `gcm_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL DEFAULT '',
  `gcm_regid` text,
  `email` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) 
