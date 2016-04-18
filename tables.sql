create database netspider;
use netspider;

create table MyUrl
(
  uid int primary key auto_increment,
  url TEXT,
  time datetime,
  readed int default 0
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;