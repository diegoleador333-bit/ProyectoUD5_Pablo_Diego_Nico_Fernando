CREATE DATABASE if not exists ProyectoUD5;
-- 1. Tabla Usuarios
CREATE TABLE Usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    DNI VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    pwd VARCHAR(255) NOT NULL
);

-- 2. Tabla Camisetas
CREATE TABLE Camisetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    equipo VARCHAR(100) NOT NULL, 
	 imagen VARCHAR(255),
    precio DECIMAL(10, 2) NOT NULL,
    temporada ENUM,
    liga ENUM,
    parche boolean,         
    nombreDorsal VARCHAR(100),
	 numeroDorsal INT 
);

-- 3. Tabla StockPorTalla
-- Relación 1:1 o 1:N con Camisetas
CREATE TABLE StockPorTalla (
    id INT AUTO_INCREMENT PRIMARY KEY,
    camiseta_Id INT NOT NULL,
    stockS INT,
    stockM INT,
    stockL INT,
    stockXL INT,
    FOREIGN KEY (camiseta_Id) REFERENCES Camisetas(id) ON DELETE CASCADE
);

-- 4. Tabla Carrito
CREATE TABLE Carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_Id INT NOT NULL,  
	 precioTotal DECIMAL(10, 2),
    fechaCreacion DATETIME,
    FOREIGN KEY (usuario_Id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- 5. Tabla CarritoContenido (Detalle del carrito)
CREATE TABLE CarritoContenido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carrito_Id INT NOT NULL,
    camiseta_Id INT NOT NULL,
	 cantidad INT,
    FOREIGN KEY (carrito_Id) REFERENCES Carrito(id) ON DELETE CASCADE,
    FOREIGN KEY (camiseta_Id) REFERENCES Camisetas(id)
);

