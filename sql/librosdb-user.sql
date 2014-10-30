drop user 'libro'@'localhost';
create user 'libro'@'localhost' identified by 'libro';
grant all privileges on librosdb.* to 'libro'@'localhost';
flush privileges;