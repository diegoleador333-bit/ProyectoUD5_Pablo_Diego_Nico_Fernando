CREATE DATABASE if not exists ProyectoUD5;

CREATE USER 'usuario_ud5'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON ProyectoUD5.* TO 'usuario_ud5'@'localhost';
FLUSH PRIVILEGES;

USE ProyectoUD5;
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
    precio DECIMAL(10,2) NOT NULL,

    temporada ENUM(
        '2000-2005',
        '2006-2011',
        '2012-2017',
        '2018-2022',
        '2023-2026'
    ) NOT NULL,

    liga ENUM(
        'LaLiga',
        'Premier League',
        'Serie A',
        'Bundesliga',
        'Selecciones'
    ) NOT NULL,

    parche TINYINT(1),
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
	 tallaSeleccionada ENUM(
	 'S',
	 'M',
	 'L',
	 'XL'
	 )NOT NULL,
	 nombrePersonalizado VARCHAR(100),
	 numeroPersonalizado INT,
	 llevaParche TINYINT,
    FOREIGN KEY (carrito_Id) REFERENCES Carrito(id) ON DELETE CASCADE,
    FOREIGN KEY (camiseta_Id) REFERENCES Camisetas(id)
);

CREATE TABLE Pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_Id INT NOT NULL,
    fechaPedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    precioTotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (usuario_Id) REFERENCES Usuarios(id)
);

CREATE TABLE DetallePedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_Id INT NOT NULL,
    camiseta_Id INT NOT NULL,
    cantidad INT NOT NULL,
    talla ENUM('S', 'M', 'L', 'XL') NOT NULL,
    nombrePersonalizado VARCHAR(100),
    numeroPersonalizado INT,
    llevaParche TINYINT,
    FOREIGN KEY (pedido_Id) REFERENCES Pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (camiseta_Id) REFERENCES Camisetas(id)
);



--inserts--
INSERT INTO Usuarios (DNI, nombre, apellido, correo, pwd) VALUES
('00000001A', 'Admin', '90T', 'admin@90t.com', 'admin123'),
('00000002B', 'Quique', 'Martin', 'quique@90t.com', 'q1234'),
('00000003C', 'Mercy', 'Perez', 'mercy@90t.com', 'm1234');

-- =========================
-- 2) CAMISETAS (tu JSON)
-- Nota: parche=0 y nombreDorsal/numeroDorsal en NULL
-- =========================
INSERT INTO Camisetas (equipo, imagen, precio, temporada, liga, parche, nombreDorsal, numeroDorsal) VALUES
('Real Madrid', 'realmadrid_2000_2005.png', 85.00, '2000-2005', 'LaLiga', 0, NULL, NULL),
('FC Barcelona', 'barcelona_2000_2005.png', 85.00, '2000-2005', 'LaLiga', 0, NULL, NULL),
('Manchester United', 'manutd_2000_2005.png', 88.00, '2000-2005', 'Premier League', 0, NULL, NULL),
('Juventus', 'juventus_2000_2005.png', 82.00, '2000-2005', 'Serie A', 0, NULL, NULL),
('Bayern Munich', 'bayern_2000_2005.png', 87.00, '2000-2005', 'Bundesliga', 0, NULL, NULL),
('Brasil', 'brasil_2000_2005.png', 80.00, '2000-2005', 'Selecciones', 0, NULL, NULL),

('Real Madrid', 'realmadrid_2006_2011.png', 90.00, '2006-2011', 'LaLiga', 0, NULL, NULL),
('FC Barcelona', 'barcelona_2006_2011.png', 92.00, '2006-2011', 'LaLiga', 0, NULL, NULL),
('Chelsea', 'chelsea_2006_2011.png', 90.00, '2006-2011', 'Premier League', 0, NULL, NULL),
('AC Milan', 'milan_2006_2011.png', 88.00, '2006-2011', 'Serie A', 0, NULL, NULL),
('Bayern Munich', 'bayern_2006_2011.png', 89.00, '2006-2011', 'Bundesliga', 0, NULL, NULL),
('España', 'espana_2006_2011.png', 85.00, '2006-2011', 'Selecciones', 0, NULL, NULL),

('FC Barcelona', 'barcelona_2012_2017.png', 95.00, '2012-2017', 'LaLiga', 0, NULL, NULL),
('Real Madrid', 'realmadrid_2012_2017.png', 95.00, '2012-2017', 'LaLiga', 0, NULL, NULL),
('Manchester City', 'mancity_2012_2017.png', 94.00, '2012-2017', 'Premier League', 0, NULL, NULL),
('Juventus', 'juventus_2012_2017.png', 90.00, '2012-2017', 'Serie A', 0, NULL, NULL),
('Borussia Dortmund', 'dortmund_2012_2017.png', 88.00, '2012-2017', 'Bundesliga', 0, NULL, NULL),
('Alemania', 'alemania_2012_2017.png', 90.00, '2012-2017', 'Selecciones', 0, NULL, NULL),

('Real Madrid', 'realmadrid_2018_2022.png', 100.00, '2018-2022', 'LaLiga', 0, NULL, NULL),
('Liverpool', 'liverpool_2018_2022.png', 98.00, '2018-2022', 'Premier League', 0, NULL, NULL),
('Inter', 'inter_2018_2022.png', 95.00, '2018-2022', 'Serie A', 0, NULL, NULL),
('Bayern Munich', 'bayern_2018_2022.png', 98.00, '2018-2022', 'Bundesliga', 0, NULL, NULL),
('Francia', 'francia_2018_2022.png', 95.00, '2018-2022', 'Selecciones', 0, NULL, NULL),

('Real Madrid', 'realmadrid_2023_2026.png', 110.00, '2023-2026', 'LaLiga', 0, NULL, NULL),
('Manchester City', 'mancity_2023_2026.png', 110.00, '2023-2026', 'Premier League', 0, NULL, NULL),
('Napoli', 'napoli_2023_2026.png', 105.00, '2023-2026', 'Serie A', 0, NULL, NULL),
('Bayern Munich', 'bayern_2023_2026.png', 108.00, '2023-2026', 'Bundesliga', 0, NULL, NULL),
('Argentina', 'argentina_2023_2026.png', 110.00, '2023-2026', 'Selecciones', 0, NULL, NULL);

-- =========================
-- 3) STOCK POR TALLA
-- (valores de ejemplo; ajustadlos como queráis)
-- Insertamos usando SELECT del id para no depender de autoincrement exacto.
-- =========================

INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Real Madrid' AND temporada='2000-2005' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 5, 9, 7, 3 FROM Camisetas WHERE equipo='FC Barcelona' AND temporada='2000-2005' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 4, 8, 6, 2 FROM Camisetas WHERE equipo='Manchester United' AND temporada='2000-2005' AND liga='Premier League';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 7, 11, 9, 5 FROM Camisetas WHERE equipo='Juventus' AND temporada='2000-2005' AND liga='Serie A';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Bayern Munich' AND temporada='2000-2005' AND liga='Bundesliga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 8, 12, 10, 6 FROM Camisetas WHERE equipo='Brasil' AND temporada='2000-2005' AND liga='Selecciones';

INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 5, 9, 7, 3 FROM Camisetas WHERE equipo='Real Madrid' AND temporada='2006-2011' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 4, 8, 6, 2 FROM Camisetas WHERE equipo='FC Barcelona' AND temporada='2006-2011' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Chelsea' AND temporada='2006-2011' AND liga='Premier League';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 7, 11, 9, 5 FROM Camisetas WHERE equipo='AC Milan' AND temporada='2006-2011' AND liga='Serie A';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Bayern Munich' AND temporada='2006-2011' AND liga='Bundesliga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 8, 12, 10, 6 FROM Camisetas WHERE equipo='España' AND temporada='2006-2011' AND liga='Selecciones';

INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 4, 8, 6, 2 FROM Camisetas WHERE equipo='FC Barcelona' AND temporada='2012-2017' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 5, 9, 7, 3 FROM Camisetas WHERE equipo='Real Madrid' AND temporada='2012-2017' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Manchester City' AND temporada='2012-2017' AND liga='Premier League';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 7, 11, 9, 5 FROM Camisetas WHERE equipo='Juventus' AND temporada='2012-2017' AND liga='Serie A';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Borussia Dortmund' AND temporada='2012-2017' AND liga='Bundesliga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 8, 12, 10, 6 FROM Camisetas WHERE equipo='Alemania' AND temporada='2012-2017' AND liga='Selecciones';

INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 5, 9, 7, 3 FROM Camisetas WHERE equipo='Real Madrid' AND temporada='2018-2022' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Liverpool' AND temporada='2018-2022' AND liga='Premier League';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 7, 11, 9, 5 FROM Camisetas WHERE equipo='Inter' AND temporada='2018-2022' AND liga='Serie A';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 10, 8, 4 FROM Camisetas WHERE equipo='Bayern Munich' AND temporada='2018-2022' AND liga='Bundesliga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 8, 12, 10, 6 FROM Camisetas WHERE equipo='Francia' AND temporada='2018-2022' AND liga='Selecciones';

INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 4, 7, 6, 2 FROM Camisetas WHERE equipo='Real Madrid' AND temporada='2023-2026' AND liga='LaLiga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 4, 7, 6, 2 FROM Camisetas WHERE equipo='Manchester City' AND temporada='2023-2026' AND liga='Premier League';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 5, 8, 7, 3 FROM Camisetas WHERE equipo='Napoli' AND temporada='2023-2026' AND liga='Serie A';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 4, 7, 6, 2 FROM Camisetas WHERE equipo='Bayern Munich' AND temporada='2023-2026' AND liga='Bundesliga';
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL)
SELECT id, 6, 9, 8, 4 FROM Camisetas WHERE equipo='Argentina' AND temporada='2023-2026' AND liga='Selecciones';


