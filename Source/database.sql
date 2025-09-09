-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: readbook
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `author` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `death_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (1,'Tần Minh','1981-01-01',NULL),(2,'Aoyama Gōshō','1963-06-21',NULL),(3,'J.K. Rowling','1965-07-31',NULL),(4,'Nam Cao','1915-11-29','1951-11-30'),(5,'Leo Tolstoy','1828-09-09','1910-11-20'),(6,' Dale Carnegie','1888-11-24','1955-11-01'),(7,'Sir Arthur Conan Doyle','1859-05-22','1930-07-07'),(8,'Nguyễn Minh Hiển2','1985-05-04',NULL),(9,'Không xác định',NULL,NULL);
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text,
  `cover_image` varchar(255) DEFAULT NULL,
  `type_file` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `active` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (2,'Chim Cổ Đỏ','Cuốn tiểu thuyết kịch tính “Chim Cổ Đỏ” của nhà văn Jo Nesbo không chỉ là một cuộc hành trình đến tội ác và bí ẩn mà còn là một vẻ nhìn sâu sắc vào tâm lý con người, qua chân dung của thanh tra cảnh sát tài năng – Harry Hole','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/sherlock_home.jpg','ePub','Hoàn thành','2024-06-12 12:35:19.000000',0),(3,'Harry Potter và Hòn đá phù thủy','Harry Potter và Hòn đá phù thủy mở cửa vào một thế giới mà không có giới hạn, nơi phép thuật và kỳ diệu trở thành hiện thực. Cuốn tiểu thuyết này là một hành trình đầy kỳ diệu của sự phát triển và khám phá của một cậu bé mồ côi, Harry Potter.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/potter_harry.jpg','PDF','Hoàn thành','2024-06-14 12:30:19.000000',0),(4,'Thép đã tôi thế đấy!','“Thép đã tôi thế đấy!” – những từ này không chỉ là một tên cuốn tiểu thuyết, mà còn là những lời thách thức, ánh sáng hy vọng được bày tỏ một cách chân thành qua từng trang sách. Nhà văn Xô Viết Nikolai A. Ostrovsky đã đan xen tri thức và cảm xúc để xây dựng lên một tác phẩm vĩ đại, đánh dấu sự thăng hoa của văn học Nga vào những năm 1934.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/10465-lao-hac-1.jpg','ePub','Hoàn thành','2024-06-18 12:30:19.000000',0),(5,'Chiến tranh và hòa bình','Tác phẩm kinh điển của nhà văn Nga Leo Tolstoy, miêu tả cuộc sống của nhiều tầng lớp xã hội khác nhau trong thời kỳ chiến tranh Napoleon.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/chien_tranh_va_hoa_binh.jpg','PDF','Hoàn thành','2024-06-20 12:30:19.000000',0),(6,'Đắc nhân tâm','Cuốn sách nổi tiếng của Dale Carnegie, hướng dẫn cách giao tiếp và đối xử với người khác để thành công trong cuộc sống.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/dac-nhan-tam.jpg','ePub','Hoàn thành','2024-06-24 12:30:19.000000',1),(7,'Sherlock Holmes: Tập hợp truyện ngắn','Tập hợp các câu chuyện ngắn về thám tử nổi tiếng Sherlock Holmes, được viết bởi Sir Arthur Conan Doyle.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/sherlock_home.jpg','PDF','Hoàn thành','2024-07-03 12:30:19.000000',0),(8,'Cuốn sách của tôi','Một cuốn sách đầy cảm hứng viết bởi một tác giả nổi tiếng.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/temp_image586371.jpg','ePub','Hoàn thành','2024-07-10 12:30:19.000000',0),(9,'Tết ở làng Địa ngục','Trong cuốn sách Tết ở làng Địa Ngục, tác giả Thảo Trang đưa chúng ta vào một thế giới huyền bí nằm sâu trong một ngôi làng xa xôi trên đỉnh ngọn núi hoang vu. Trong không khí của ngày Tết, làng Địa Ngục đón chào một lễ hội đầy kinh hãi, đánh dấu sự bắt đầu của một chuỗi bi kịch đen tối.','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/28081b0aac321d411d35c33db36b7064.jpg','ePub','Đang ra','2024-06-12 12:30:19.000000',1),(53,'Yêu thích','A','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/28081b0aac321d411d35c33db36b7064.jpg','PDF','Hoàn thành','2024-08-11 14:41:56.113577',0),(90,'Hoạt động','B','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/temp_image586371.jpg','ePub','Hoàn thành','2024-12-22 01:16:41.176000',0),(91,'Tết kinh dị','','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/image452253.jpg','PDF','Đang ra','2024-12-22 14:59:21.603203',0),(92,'Eu','Eu','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/image653995.jpg','PDF','Đang ra','2024-12-22 15:06:41.680900',0);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_category`
--

DROP TABLE IF EXISTS `book_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_category` (
  `book_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`category_id`),
  KEY `FKam8llderp40mvbbwceqpu6l2s` (`category_id`),
  CONSTRAINT `book_category_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKam8llderp40mvbbwceqpu6l2s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_category`
--

LOCK TABLES `book_category` WRITE;
/*!40000 ALTER TABLE `book_category` DISABLE KEYS */;
INSERT INTO `book_category` VALUES (2,1),(9,1),(53,1),(91,1),(5,2),(9,2),(3,3),(6,3),(9,3),(90,3),(2,4),(6,4),(9,4),(2,5),(9,5),(7,6),(9,6),(4,7),(9,7),(53,7),(4,8),(9,8),(92,8),(9,9),(9,10);
/*!40000 ALTER TABLE `book_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookmarks`
--

DROP TABLE IF EXISTS `bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `position` int DEFAULT NULL,
  `progress_percentage` varchar(255) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  `book_id` int NOT NULL,
  `note_id` int DEFAULT NULL,
  `chapternumber` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK89oo76tye1yp2gnlt0lhfxx96` (`note_id`),
  KEY `FKsmahp4oyhjokycdn4oogvsm8o` (`book_id`),
  KEY `FK4l9xx7gr3tn8ioperpyq6wjdh` (`username`),
  CONSTRAINT `FK4l9xx7gr3tn8ioperpyq6wjdh` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  CONSTRAINT `FKkc3lyhtotjt4n2lfiwdemq379` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`),
  CONSTRAINT `FKsmahp4oyhjokycdn4oogvsm8o` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks`
--

LOCK TABLES `bookmarks` WRITE;
/*!40000 ALTER TABLE `bookmarks` DISABLE KEYS */;
INSERT INTO `bookmarks` VALUES (2,'2024-07-03 05:32:50',40988,'7.7','2','lanh123',9,4,1),(3,'2024-07-04 03:30:59',40001,'7.5','2','lanh123',2,5,1),(5,'2024-07-04 03:54:59',50965,'9.5','1','lanh123',3,NULL,1),(10,'2024-07-31 06:33:18',1020,'2.5','1','lanh123',9,NULL,1),(11,'2024-07-31 06:39:21',40977,'7.4','2','lanh123',9,6,1),(12,'2024-07-31 17:34:52',68617,'12.9','1','lanh123',9,NULL,1),(14,'2024-08-01 06:52:57',40965,'7.4','2','lanh123',9,30,1),(15,'2024-08-01 07:01:26',82804,'14.8','2','lanh123',9,31,1),(16,'2024-08-01 07:06:45',562,'1.9','1','lanh123',7,NULL,1),(17,'2024-08-01 07:20:22',123,'0.6','2','lanh123',7,32,1),(18,'2024-08-01 07:29:04',874,'2.6','2','lanh123',7,33,1),(19,'2024-08-02 01:35:55',899,'3.2','2','lanh123',7,34,1),(20,'2024-08-02 03:23:21',46254,'8.4','1','lanh123',8,NULL,1),(21,'2024-08-02 03:31:11',46254,'8.4','1','lanh123',8,NULL,1),(22,'2024-08-03 16:43:55',9084,'1,5','2','lanh123',8,35,1),(26,'2024-08-03 17:52:30',452,'1.6','1','lanh123',7,NULL,1),(35,'2024-08-06 11:45:16',87679,'14.9','2','lanh123',8,38,1),(41,'2024-08-11 07:44:10',5048,'6.966506','1','lanh123',53,NULL,1),(42,'2024-08-11 07:44:29',4292,'5.9231863','2','lanh123',53,44,1),(43,'2024-08-13 08:58:14',1585,'0.27646908','1','lanh123',8,NULL,1),(44,'2024-08-13 08:59:27',14650,'1.5087321','1','lanh123',8,NULL,1),(45,'2024-12-13 00:55:22',111,'18.064516','1','lanh123',3,NULL,1),(46,'2024-12-13 01:18:27',3,'0.6451613','2','lanh123',7,49,1),(47,'2024-12-13 02:29:31',2,'0.48387098','2','lanh123',7,50,1),(53,'2024-12-15 06:58:58',0,'0.16129032','1','lanh123',7,NULL,1),(60,'2024-12-15 07:43:18',11,'1.9354839','1','lanh123',7,NULL,1),(61,'2024-12-15 07:43:33',22,'3.7096772','1','lanh123',7,NULL,1),(63,'2024-12-15 07:48:28',3,'0.6451613','1','lanh123',7,NULL,1),(80,'2024-12-15 09:10:16',1,'1.7361528','1','lanh123',2,NULL,1),(89,'2024-12-15 18:37:25',40001,'4.5178146','1','lanh123',2,NULL,1),(96,'2024-12-16 06:03:51',48308,'9.355905','1','minh123',2,NULL,2),(98,'2024-12-16 07:06:53',65929,'9.405274','1','minh123',2,NULL,2),(99,'2024-12-17 01:05:15',11855,'1.4976754','1','minh123',6,NULL,1),(100,'2024-12-17 01:05:25',11855,'1.4976754','2','minh123',6,70,1),(101,'2024-12-17 04:15:40',64146,'8.1037445','2','minh123',2,71,1),(102,'2024-12-17 06:41:51',8771,'8.039266','2','minh123',2,72,2),(103,'2024-12-17 18:13:04',8514,'1.4789209','2','lanh123',2,74,1),(104,'2024-12-20 14:21:35',117,'19.032257','2','lanh123',3,77,1);
/*!40000 ALTER TABLE `bookmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Điều tra','Sách điều tra'),(2,'Lịch sử','Sách lịch sử cung cấp thông tin về các sự kiện, nhân vật, và thời kỳ trong quá khứ. Nó nhằm mục đích giúp người đọc hiểu biết về các biến cố lịch sử và cách chúng đã ảnh hưởng đến thế giới hiện tại.'),(3,'Phát triển bản thân','Sách phát triển bản thân tập trung vào việc cải thiện kỹ năng, kiến thức, và thói quen cá nhân để đạt được thành công và sự hài lòng trong cuộc sống. Nó thường cung cấp các chiến lược và phương pháp để đạt được mục tiêu cá nhân và nghề nghiệp.'),(4,'Self-help','Sách giúp độc giả giải quyết những vấn đề cá nhân.'),(5,'Tình cảm','Sách tình cảm. Lãng mạn'),(6,'Trinh thám','Sách trinh thám'),(7,'Tự truyện','Sách tự truyện là loại sách mà tác giả viết về chính cuộc đời của mình. Nó thường bao gồm các sự kiện quan trọng, những trải nghiệm cá nhân, và quá trình trưởng thành của tác giả.'),(8,'Tiểu thuyết','Tiểu thuyết là một loại hình văn học mà trong đó câu chuyện là sản phẩm của trí tưởng tượng. Nó có thể bao gồm các nhân vật, tình tiết, và bối cảnh hoàn toàn hư cấu.'),(9,'Văn hóa - xã hội','Sách văn hóa, xã hội'),(10,'Văn học hiện thực','Văn học hiện thực phản ánh các tình huống và vấn đề đời thực một cách chân thực và sống động. Các câu chuyện trong thể loại này thường mô tả các tình huống xã hội, tâm lý, và cảm xúc của nhân vật, mà không cần đến yếu tố hư cấu.'),(19,'Tuong lai','Sach vien tuong tuong lai');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chapter`
--

DROP TABLE IF EXISTS `chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chapter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `chapter_number` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `file_path` varchar(255) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfxaijiug52tyrl5ifextmcfqb` (`book_id`),
  CONSTRAINT `FKfxaijiug52tyrl5ifextmcfqb` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chapter`
--

LOCK TABLES `chapter` WRITE;
/*!40000 ALTER TABLE `chapter` DISABLE KEYS */;
INSERT INTO `chapter` VALUES (1,1,'2024-07-03 05:30:19','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/ePub/Tet%20o%20lang%20Dia%20Nguc%20-%20Thao%20Trang.epub','Nguy hiểm',9),(4,1,'2024-07-13 15:07:54','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/Noli-Me-Tangere.pdf','Nguyên hiểm',3),(5,1,'2024-07-13 15:07:54','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/Noli-Me-Tangere.pdf',NULL,4),(6,1,'2024-07-13 15:07:54','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/Noli-Me-Tangere.pdf',NULL,5),(7,1,'2024-07-13 15:07:54','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/ePub/Tet%20o%20lang%20Dia%20Nguc%20-%20Thao%20Trang.epub',NULL,6),(8,1,'2024-07-13 15:07:54','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/Noli-Me-Tangere.pdf',NULL,7),(9,1,'2024-07-13 15:07:54','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/ePub/Tet%20o%20lang%20Dia%20Nguc%20-%20Thao%20Trang.epub',NULL,8),(12,2,'2024-07-30 07:06:25','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/KEY%2010%20TEST%20ETS%202022-KEY%20READING%20.pdf',NULL,3),(31,1,'2024-08-11 07:41:56','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Files/file127815.epub','Moi',53),(57,1,'2024-11-21 22:08:06','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/ePub/Tet%20o%20lang%20Dia%20Nguc%20-%20Thao%20Trang.epub','Vết đạn',2),(58,2,'2024-11-21 22:08:06','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Book/ePub/Thao%20Tung%20Tam%20Ly%20-%20Shannon%20Thomas.epub','Người bí ẩn',2),(61,1,'2024-12-22 07:59:22','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Files/file440839.pdf','Tết',91),(62,1,'2024-12-22 08:06:42','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Files/Tet%20o%20lang%20Dia%20Nguc%20-%20Thao%20Trang_511484.pdf','E',92);
/*!40000 ALTER TABLE `chapter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail_author`
--

DROP TABLE IF EXISTS `detail_author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_author` (
  `book_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`author_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `detail_author_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `detail_author_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_author`
--

LOCK TABLES `detail_author` WRITE;
/*!40000 ALTER TABLE `detail_author` DISABLE KEYS */;
INSERT INTO `detail_author` VALUES (9,1),(53,1),(91,1),(90,2),(3,3),(9,3),(2,4),(4,4),(53,4),(5,5),(2,6),(6,6),(8,6),(92,6),(2,7),(7,7),(92,7),(8,8);
/*!40000 ALTER TABLE `detail_author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `id` int NOT NULL AUTO_INCREMENT,
  `number` int DEFAULT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK273xtfi5rey9ay0bek4b6mey5` (`book_id`),
  CONSTRAINT `FK273xtfi5rey9ay0bek4b6mey5` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
INSERT INTO `favorites` VALUES (1,1,9),(3,5,2),(4,1,3),(5,2,4),(6,1,5),(7,1,6),(8,1,7),(9,2,8),(45,2,53),(51,0,90),(52,0,91),(53,0,92);
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `book_id` int NOT NULL,
  `username` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`book_id`,`username`),
  KEY `FKp84dn6e375y6p37eb2ao0j6i1` (`username`),
  CONSTRAINT `FK1i5rjsjrgrt9gg2mgv9uja3ge` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKp84dn6e375y6p37eb2ao0j6i1` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (2,'hung123','2024-12-12 12:35:19.000000'),(2,'john_doe777','2024-12-20 12:35:19.000000'),(2,'lanh123','2024-12-23 19:00:35.100217'),(2,'minh123','2024-12-12 12:35:19.000000'),(2,'triet123','2024-11-12 12:35:19.000000'),(3,'john_doe777','2024-12-14 12:35:19.000000'),(4,'john_doe777','2024-11-13 12:35:19.000000'),(4,'minh123','2024-12-19 12:35:19.000000'),(5,'minh123','2024-11-20 12:35:19.000000'),(6,'john_doe777','2024-12-21 12:35:19.000000'),(7,'lanh123','2024-12-23 19:00:59.599155'),(8,'minh123','2024-12-06 12:35:19.000000'),(9,'john_doe777','2024-11-05 12:35:19.000000'),(53,'hung123','2024-12-04 12:35:19.000000'),(53,'lanh123','2024-12-23 19:01:04.899648');
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `note` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(10) NOT NULL,
  `username` varchar(100) NOT NULL,
  `book_id` int DEFAULT NULL,
  `chapternumber` int DEFAULT NULL,
  `status` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrussm9y4vwyp0x6gl8n288ovv` (`book_id`),
  KEY `FKku5urlntfa4bs6rgh1ejytmbk` (`username`),
  CONSTRAINT `FKku5urlntfa4bs6rgh1ejytmbk` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  CONSTRAINT `FKrussm9y4vwyp0x6gl8n288ovv` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` VALUES (4,'Hay','2024-07-03 05:30:23','2','lanh123',9,1,0,NULL),(5,'Nhân vật mới','2024-07-04 03:46:11','2','lanh123',2,1,0,NULL),(6,'Sách hay','2024-07-13 08:35:45','2','lanh123',9,1,0,NULL),(17,'Sách hay','2024-07-17 10:13:14','1','lanh123',9,1,1,'Nội dung không phù hợp'),(19,'Được học ở trường rồi!','2024-07-17 10:46:45','1','lanh123',9,1,1,'Nội dung không phù hợp'),(21,'Hay','2024-07-18 16:10:13','1','lanh123',2,1,1,'Vi phạm thuần phong mỹ tục'),(23,'Sách hay','2024-07-18 16:11:42','1','lanh123',2,1,1,'Nội dung không phù hợp'),(29,'Hay','2024-08-01 06:41:26','1','lanh123',9,1,1,'Vi phạm chuẩn mực đạo đức'),(30,'Hay qua','2024-08-01 06:52:57','2','lanh123',9,1,0,NULL),(31,'Hay!!','2024-08-01 07:01:26','2','lanh123',9,1,0,NULL),(32,'Hay','2024-08-01 07:20:22','2','lanh123',7,1,0,NULL),(33,'Ghi','2024-08-01 07:29:04','2','lanh123',7,1,0,NULL),(34,'Ghi chu','2024-08-02 01:35:55','2','lanh123',7,1,0,NULL),(35,'Nhân vật ông Thập','2024-08-03 16:43:55','2','lanh123',9,1,0,NULL),(38,'Hoi ba','2024-08-06 11:45:16','2','lanh123',8,1,0,NULL),(42,'Rất hay!','2024-08-11 07:43:44','1','john_doe11',53,NULL,1,'Nội dung không phù hợp'),(44,'Hello','2024-08-11 07:44:29','2','lanh123',53,1,0,NULL),(45,'Hay quá!','2024-11-28 07:21:49','1','john_doe11',2,NULL,1,'Nội dung không phù hợp'),(46,'Hay!','2024-11-28 07:22:11','1','john_doe11',2,1,0,''),(47,'Hay quá!','2024-12-09 10:20:07','2','lanh123',9,1,0,''),(48,'Tôi rất yêu thích chuyện này, tôi có 3 cuốn sách 3 phần nhưng đọc trên điện thoại thích hơn!','2024-12-13 01:04:05','1','john_doe11',2,1,0,''),(49,'Thích quá!','2024-12-13 01:18:27','2','lanh123',7,1,0,''),(50,'Rất thích','2024-12-13 02:29:31','2','lanh123',7,1,0,''),(53,'Hay!','2024-12-15 07:12:38','1','lanh123',7,1,0,''),(54,'lanh','2024-12-15 07:27:19','1','lanh123',7,1,0,''),(56,'Iu sach qua','2024-12-15 07:33:57','1','lanh123',7,1,0,''),(70,'Hồi thứ 1','2024-12-17 01:05:25','2','minh123',6,1,0,''),(71,'hồi thứ 2','2024-12-17 04:15:40','2','minh123',2,1,0,''),(72,'Lời giới thiệu ','2024-12-17 06:41:51','2','minh123',2,2,0,''),(73,'Hãy quá đi!','2024-12-17 07:38:11','1','minh123',2,1,0,''),(74,'Táo quân báo mộng','2024-12-17 18:13:04','2','lanh123',2,1,0,''),(75,'','2024-12-17 19:00:01','1','lanh123',2,NULL,0,''),(76,'','2024-12-17 19:32:42','1','lanh123',2,NULL,0,''),(77,'XVI','2024-12-20 14:21:34','2','lanh123',3,1,0,''),(80,'Ok','2024-12-22 02:47:09','1','lanh123',2,1,1,'Nội dung không phù hợp'),(81,'Tim','2024-12-22 10:14:12','1','lanh123',2,1,0,'');
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,9,'Sách pháp y Tần Minh vừa mới được đăng tải','Add','2024-06-28 14:40:37'),(2,9,'Sách pháp y Tần Minh vừa mới thêm chap','Update','2024-07-03 05:27:04'),(3,2,'Sách Thám tử lừng danh Conan được đăng tải','Add','2024-07-21 14:35:12'),(28,3,'SáchHarry Potter và Hòn đá phù thủy vừa mới được đăng tải','Add','2024-06-14 05:30:19'),(29,4,'Sách Lão Hạc vừa mới được đăng tải','Add','2024-06-18 05:30:19'),(30,5,'Sách Chiến tranh và hòa bình vừa mới được đăng tải','Add','2024-06-20 05:30:19'),(31,6,'Sách Đắc nhân tâm vừa mới được đăng tải','Add','2024-06-24 05:30:19'),(32,7,'Sách Sherlock Holmes: Tập hợp truyện ngắn vừa mới được đăng tải','Add','2024-07-03 05:30:19'),(33,8,'Sách Cuốn sách của tôi vừa mới được đăng tải','Add','2024-07-10 05:30:19'),(36,53,'Sách Mua he khac nghiet vừa đăng tải!','Add','2024-08-11 07:41:56'),(39,2,'SáchChim Cổ Đỏ đã được cập nhật chương!','Update','2024-11-21 21:46:17'),(40,2,'SáchChim Cổ Đỏ đã được cập nhật chương!','Update','2024-11-21 21:52:32'),(41,2,'SáchChim Cổ Đỏ đã được cập nhật chương!','Update','2024-11-21 22:08:06'),(46,90,'EEEEEE đã được đăng tải','Add','2024-12-21 18:16:41'),(47,91,'Sách Tết kinh dị vừa đăng tải!','Add','2024-12-22 07:59:22'),(48,92,'Sách Eu vừa đăng tải!','Add','2024-12-22 08:06:42');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reading_progress`
--

DROP TABLE IF EXISTS `reading_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reading_progress` (
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `progress_path` varchar(255) DEFAULT NULL,
  `progress_percentage` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `book_id` int NOT NULL,
  `chapter_id` int NOT NULL,
  PRIMARY KEY (`book_id`,`username`,`chapter_id`),
  KEY `FKpwfsr3hos0lerv6xn2omhufkd` (`username`),
  KEY `FKqwjvcqqxwnc40it4yjqkdwt86` (`chapter_id`),
  CONSTRAINT `FKbehf5wq97fe6rmpulhp02emgo` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `FKpwfsr3hos0lerv6xn2omhufkd` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  CONSTRAINT `FKqwjvcqqxwnc40it4yjqkdwt86` FOREIGN KEY (`chapter_id`) REFERENCES `chapter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reading_progress`
--

LOCK TABLES `reading_progress` WRITE;
/*!40000 ALTER TABLE `reading_progress` DISABLE KEYS */;
INSERT INTO `reading_progress` VALUES ('2024-12-17 06:19:16','6669','1.4855554','Unread','minh123',2,57),('2024-07-30 07:15:46','123','20.0','Unread','lanh123',3,4),('2024-08-05 06:34:21','0','0.16129032','Unread','lanh123',5,6),('2024-12-22 15:50:52','2','0.48387098','Unread','lanh123',7,8),('2024-08-03 17:08:55','5066','0,6','Unread','triet123',8,1),('2024-08-14 15:13:18','146','0.015801618','Unread','lanh123',9,1);
/*!40000 ALTER TABLE `reading_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `replies`
--

DROP TABLE IF EXISTS `replies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `replies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(100) NOT NULL,
  `note_id` int NOT NULL,
  PRIMARY KEY (`id`,`username`,`note_id`),
  KEY `FK1ik2lc6ove53204vskjjufgie` (`note_id`),
  KEY `FKc9g7hq49bpekqs1b6aqyohh68` (`username`),
  CONSTRAINT `FK1ik2lc6ove53204vskjjufgie` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`),
  CONSTRAINT `FKc9g7hq49bpekqs1b6aqyohh68` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `replies`
--

LOCK TABLES `replies` WRITE;
/*!40000 ALTER TABLE `replies` DISABLE KEYS */;
INSERT INTO `replies` VALUES (43,'Phản hồi mới','2024-11-27 20:42:01','hien123',42),(78,'Ê','2024-12-08 03:53:44','hien123',45),(91,'cm','2024-12-08 05:12:54','hien123',21),(92,'AAAAA','2024-12-08 05:16:30','hien123',19),(94,'Cảm ơn bạn nhé!','2024-12-08 05:22:45','hien123',46),(96,'Cảm ơn bạn!','2024-12-09 19:14:06','hien123',17),(100,'Iu bạn quá <tim>!','2024-12-14 13:25:11','hien123',46),(103,'Iu','2024-12-14 13:42:28','hien123',48),(106,'Cảm ơn nhé!','2024-12-14 14:22:03','hien123',48),(112,'Hehe vui thật!','2024-12-14 14:41:36','hien123',48),(113,'Cảm ơn bạn!','2024-12-18 09:52:19','hien123',56),(114,'cam ơn ban','2024-12-22 02:54:12','hien123',80);
/*!40000 ALTER TABLE `replies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searches`
--

DROP TABLE IF EXISTS `searches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `searches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) NOT NULL,
  `type` varchar(10) NOT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2ycl2bcbu4ny0dntpw1x8v0nm` (`username`),
  CONSTRAINT `FK2ycl2bcbu4ny0dntpw1x8v0nm` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searches`
--

LOCK TABLES `searches` WRITE;
/*!40000 ALTER TABLE `searches` DISABLE KEYS */;
INSERT INTO `searches` VALUES (7,'phap y','sach','lanh123'),(8,'nam cao','tacgia','lanh123'),(9,'trinh tham','theloai','lanh123'),(10,'ký ức','sach','lanh123'),(11,'chiến tranh','sach','john_doe11'),(13,'dac nhan tam','sach','lanh123'),(14,'t','sach','lanh123'),(15,'tan minh','tacgia','lanh123'),(16,'tan minh','sach','lanh123'),(17,'dieu tra','theloai','lanh123'),(18,'minh','tacgia','lanh123');
/*!40000 ALTER TABLE `searches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `font` varchar(100) DEFAULT NULL,
  `font_size` int DEFAULT NULL,
  `reading_mode` varchar(10) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `UK7mdq5u0339l0dipn6ddmfq28s` (`username`),
  CONSTRAINT `FKmc3yube9s4rbyjc60ynq2a4t` FOREIGN KEY (`username`) REFERENCES `user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
INSERT INTO `setting` VALUES (1,'Roboto',15,'Ban ngày','lanh123'),(3,'Roboto',15,'Ban ngày','john_doe11'),(4,'Roboto',15,'Ban ngày','hung123'),(5,'Roboto',15,'Ban ngày','lien123'),(6,'Roboto',13,'Ban đêm','minh123');
/*!40000 ALTER TABLE `setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `role` int DEFAULT NULL,
  `phone` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('fdfd','fdfddfd@gmail.com','blob:http://localhost:3000/1d3342aa-36cc-4a4d-aeea-09cf2a9f54dd','fdfd','fdfddfd',1,NULL,0),('hien123','hien123@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleGirl.jpg','Nguyễn Thị Thu Hiền','12345',0,366256344,0),('hung123','nguyentthien2801@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','Hung','hung123',2,231546524,0),('john_doe11','john.doe@example124.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','John Doe_DDD','securePassword123',1,1234567890,0),('john_doe45','john.doe@example144.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','John Doess8','john_doe45',1,1234567890,1),('john_doe77','john.doe@example174.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','John Doess8','john_doe77',1,1234567877,0),('john_doe777','john.doe@example18.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','John Doe','john_doe777',2,1234567877,0),('lanh123','toibiet20001@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/img_girl2.jpg','Lanh Nguyen','lanh123',2,366256344,0),('lien123','lien123@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','Lien','lien123',1,366256344,0),('linh123','linh123@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleGirl.jpg','Linh','12345',1,456458745,0),('minh123','minh123@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg','Minh Nguyen','minh123',2,366356344,0),('RRRR','hhhhhhhhhhh@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/1731313600766-Ellipse%201.png','RRRRR','rrr',1,542365785,0),('triet123','toibiet2000@gmail.com','https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/img_boy2.jpg','Triet','12345',2,245678546,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-23 19:06:56
