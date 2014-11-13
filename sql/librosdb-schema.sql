drop database if exists librosdb;
create database librosdb;

use librosdb;

create table users (
	username	varchar(20) not null primary key,
	userpass	char(32) not null,
	name		varchar(70) not null,
	email		varchar(255) not null
);

create table user_roles (
	id					int not null auto_increment primary key,
	username			varchar(20) not null,
	rolename 			varchar(20) not null,
	foreign key(username) references users(username) on delete cascade
);
create table autor (
	id 		int not null auto_increment primary key,
	name	varchar(70) not null,
	foreign key(name) 	references users(username)
);

create table libros (
	libroid 			int not null auto_increment primary key,
	title 				varchar(50) not null,
	idAuthor			int not null,
	language			varchar(50) not null,
	edition				varchar(10) not null,
	editorial			varchar(50) not null,
	dateImpresion		timestamp default current_timestamp ON UPDATE CURRENT_TIMESTAMP,
	dateCreation		datetime not null default current_timestamp,
	foreign key(idauthor) 	references autor(id)
);

create table review (
	idreview		int not null auto_increment primary key,
	idlibro			int not null,
	username		varchar(20),
	name			varchar(70),
	lastmodified	timestamp default current_timestamp ON UPDATE CURRENT_TIMESTAMP,
	reviewtext			varchar(5000),
	foreign key(idreview) 	references libros(libroid)
);