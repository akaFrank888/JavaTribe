CREATE DATABASE blog;
USE blog;

CREATE TABLE `user` (

                         `id` INT AUTO_INCREMENT,

                         `account` varchar(20) NOT NULL,

                         `password` varchar(20) NOT NULL,

                         `nickname` varchar(20) NOT NULL,

                         PRIMARY KEY (`id`),

                         UNIQUE KEY (`id`)

);

CREATE TABLE `userInfo` (

                        `id` INT AUTO_INCREMENT,

                        `name` varchar(20) NOT NULL,

                        `gender` varchar(100) NOT NULL,

                        `age` INT NOT NULL,

                        `stuID` varchar(20) NOT NULL,

                        `master` varchar(20) NOT NULL,

                        `mailBox` varchar(20) NOT NULL,

                        `headImg` BLOB ,

                        `account` varchar(20) NOT NULL,

                        PRIMARY KEY (`id`),

                        UNIQUE KEY  (`id`)

);

CREATE TABLE `essay` (

                        `id` INT AUTO_INCREMENT,

                        `title` varchar(20) NOT NULL,

                        `content` varchar(10000) NOT NULL,

                        `date` datetime NOT NULL,

                        `account` varchar(20) NOT NULL,

                        PRIMARY KEY (`id`),

                        UNIQUE KEY (`id`)

);