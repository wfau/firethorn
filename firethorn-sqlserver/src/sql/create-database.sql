/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

    /*
     * Create our database.
     *
     */
    USE master
    CREATE DATABASE [{databasename}] ON  PRIMARY 
        (
        NAME = N'{databasename}',
        FILENAME = N'{datapath}\{databasename}.mdf',
        SIZE = 3072KB ,
        FILEGROWTH = 1024KB
        )
    LOG ON 
        (
        NAME = N'{databasename}_log',
        FILENAME = N'{datapath}\{databasename}_log.ldf',
        SIZE = 4096KB,
        FILEGROWTH = 10%
        )
    go
    
    /*
     * Configure the database.
     *
     */
    ALTER DATABASE [{databasename}] SET ANSI_NULL_DEFAULT OFF
    ALTER DATABASE [{databasename}] SET ANSI_NULLS OFF 
    ALTER DATABASE [{databasename}] SET ANSI_PADDING OFF 
    ALTER DATABASE [{databasename}] SET ANSI_WARNINGS OFF 
    ALTER DATABASE [{databasename}] SET ARITHABORT OFF 
    ALTER DATABASE [{databasename}] SET AUTO_CLOSE OFF 
    ALTER DATABASE [{databasename}] SET AUTO_CREATE_STATISTICS ON 
    ALTER DATABASE [{databasename}] SET AUTO_SHRINK OFF 
    ALTER DATABASE [{databasename}] SET AUTO_UPDATE_STATISTICS ON 
    ALTER DATABASE [{databasename}] SET CURSOR_CLOSE_ON_COMMIT OFF 
    ALTER DATABASE [{databasename}] SET CURSOR_DEFAULT  GLOBAL 
    ALTER DATABASE [{databasename}] SET CONCAT_NULL_YIELDS_NULL OFF 
    ALTER DATABASE [{databasename}] SET NUMERIC_ROUNDABORT OFF 
    ALTER DATABASE [{databasename}] SET QUOTED_IDENTIFIER OFF 
    ALTER DATABASE [{databasename}] SET RECURSIVE_TRIGGERS OFF 
    ALTER DATABASE [{databasename}] SET DISABLE_BROKER 
    ALTER DATABASE [{databasename}] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
    ALTER DATABASE [{databasename}] SET DATE_CORRELATION_OPTIMIZATION OFF 
    ALTER DATABASE [{databasename}] SET PARAMETERIZATION SIMPLE 
    ALTER DATABASE [{databasename}] SET READ_WRITE 
    ALTER DATABASE [{databasename}] SET RECOVERY FULL 
    ALTER DATABASE [{databasename}] SET MULTI_USER 
    ALTER DATABASE [{databasename}] SET PAGE_VERIFY CHECKSUM  
    go
    

