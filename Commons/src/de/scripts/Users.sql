CREATE TABLE Customer(
   id_customer INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
   name TEXT NOT NULL,
   email TEXT NOT NULL,
   birthday TEXT,
   address TEXT NOT NULL,
   tax_num TEXT NOT NULL,
   id_register INT NOT NULL,
   FOREIGN KEY(id_register) REFERENCES Register(id_register)
);

CREATE TABLE Employee(
	id_employee INT PRIMARY KEY NOT NULL,
	rank TEXT NOT NULL,
	FOREIGN KEY(id_employee) REFERENCES Customer(id_customer)
);

CREATE TABLE Register (
	id_register INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	id_employee_registered_by INT,
	id_employee_approved_by INT
	,FOREIGN KEY(id_employee_registered_by) REFERENCES Employee(id_employee),
	FOREIGN KEY(id_employee_approved_by) REFERENCES Employee(id_employee)
);

insert into Register(id_employee_registered_by, id_employee_approved_by) values(NULL,NULL);
insert into Customer values(1, "Rafael Campos", "example@gmail.com", NULL, "Rua da Colher", "456789456", 1);