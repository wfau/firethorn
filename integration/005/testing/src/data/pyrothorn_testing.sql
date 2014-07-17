-- Host: localhost
-- Generation Time: Jul 17, 2014 at 01:35 PM
-- Server version: 5.5.37-0ubuntu0.13.10.1
-- PHP Version: 5.5.3-1ubuntu2.6


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pyrothorn_testing`
--

-- --------------------------------------------------------

--
-- Table structure for table `queries`
--

CREATE TABLE IF NOT EXISTS `queries` (
  `queryid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryrunID` text NOT NULL,
  `query_timestamp` varchar(120) NOT NULL,
  `query` text NOT NULL,
  `direct_sql_rows` int(11) NOT NULL,
  `firethorn_sql_rows` int(11) NOT NULL,
  `firethorn_duration` varchar(60) NOT NULL,
  `sql_duration` varchar(60) NOT NULL,
  `test_passed` tinyint(1) NOT NULL,
  `firethorn_version` varchar(60) NOT NULL,
  `error_message` varchar(60) NOT NULL,
  PRIMARY KEY (`queryid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
