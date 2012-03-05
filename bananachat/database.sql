/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema bananachat
--

CREATE DATABASE IF NOT EXISTS bananachat;
USE bananachat;

--
-- Definition of table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `password` varchar(40) NOT NULL,
  `rank` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `gender` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `kisses` int(10) unsigned NOT NULL DEFAULT '0',
  `knuddels` int(10) unsigned NOT NULL DEFAULT '0',
  `onlineTime` int(10) unsigned NOT NULL DEFAULT '0',
  `registration` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ipAddress` varchar(15) DEFAULT '0.0.0.0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `accounts`
--

/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`id`,`name`,`password`,`rank`,`gender`,`kisses`,`knuddels`,`onlineTime`,`registration`,`ipAddress`) VALUES 
 (1,'Banana','',3,0,0,0,0,'2000-01-01 00:00:00','0.0.0.0');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;


--
-- Definition of table `channels`
--

DROP TABLE IF EXISTS `channels`;
CREATE TABLE `channels` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(26) NOT NULL,
  `size` smallint(5) unsigned NOT NULL DEFAULT '25',
  `topic` varchar(250) DEFAULT NULL,
  `game` enum('MAFIA') DEFAULT NULL,
  `backgroundImage` varchar(45) NOT NULL DEFAULT 'pics/-',
  `backgroundPosition` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `style` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`name`),
  KEY `INDEX` (`style`),
  CONSTRAINT `FK_STYLE` FOREIGN KEY (`style`) REFERENCES `channelstyles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `channels`
--

/*!40000 ALTER TABLE `channels` DISABLE KEYS */;
INSERT INTO `channels` (`id`,`name`,`size`,`topic`,`game`,`backgroundImage`,`backgroundPosition`,`style`) VALUES 
 (1,'Lobby',25,NULL,NULL,'pics/kuecke2.gif',36,1),
 (2,'Mafia',14,'Hier kann das spannende und packende Spiel Mafia gespielt werden. Anleitung unbedingt vorher mit °BB>/help mafia|\"<r° lesen. Bis zum nächsten Spielbeginn können nur Mitspieler öffentlich und privat reden.','MAFIA','pics/mjoe.gif',36,2);
/*!40000 ALTER TABLE `channels` ENABLE KEYS */;


--
-- Definition of table `channelstyles`
--

DROP TABLE IF EXISTS `channelstyles`;
CREATE TABLE `channelstyles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fontSize` tinyint(3) unsigned NOT NULL DEFAULT '15',
  `lineSpace` tinyint(3) unsigned NOT NULL DEFAULT '10',
  `foreground` varchar(11) NOT NULL DEFAULT '0,0,0',
  `background` varchar(11) NOT NULL DEFAULT '255,255,255',
  `red` varchar(13) NOT NULL DEFAULT 'R',
  `blue` varchar(13) NOT NULL DEFAULT 'B',
  `administrator` varchar(11) NOT NULL DEFAULT '255,0,0',
  `moderator` varchar(11) NOT NULL DEFAULT '0,0,140',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `channelstyles`
--

/*!40000 ALTER TABLE `channelstyles` DISABLE KEYS */;
INSERT INTO `channelstyles` (`id`,`fontSize`,`lineSpace`,`foreground`,`background`,`red`,`blue`,`administrator`,`moderator`) VALUES 
 (1,15,11,'255,255,255','123,90,66','[215,80,120]','[80,180,220]','215,80,120','20,180,90'),
 (2,14,12,'255,255,255','100,100,100','R','[135,135,255]','255,0,0','0,255,0');
/*!40000 ALTER TABLE `channelstyles` ENABLE KEYS */;


--
-- Definition of table `smileys`
--

DROP TABLE IF EXISTS `smileys`;
CREATE TABLE `smileys` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(7) NOT NULL,
  `replacement` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `smileys`
--

/*!40000 ALTER TABLE `smileys` DISABLE KEYS */;
/*!40000 ALTER TABLE `smileys` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
