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



USE ProyectoUD5;

-- ==========================================
-- 1. Insertar Usuarios
-- ==========================================
INSERT INTO Usuarios (DNI, nombre, apellido, correo, pwd) VALUES 
('12345678A', 'Juan', 'Pérez', 'juan.perez@email.com', 'pass1234'),
('87654321B', 'María', 'García', 'maria.garcia@email.com', 'segura5678'),
('11223344C', 'Carlos', 'López', 'carlos.lopez@email.com', 'admin9999');

-- ==========================================
-- 2. Insertar Camisetas (Variedad de Ligas y Temporadas)
-- ==========================================
INSERT INTO Camisetas
(equipo, imagen, precio, temporada, liga, parche, nombreDorsal, numeroDorsal)
VALUES
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

-- ==========================================
-- 3. Insertar StockPorTalla (Vinculado a Camisetas)
-- ==========================================
INSERT INTO StockPorTalla (camiseta_Id, stockS, stockM, stockL, stockXL) VALUES 
(1, 10, 20, 15, 5),  -- Real Madrid: Buen stock
(2, 5, 5, 0, 2),     -- Man City: Sin stock en L
(3, 1, 1, 1, 0),     -- Milan Retro: Stock muy bajo (exclusivo)
(4, 50, 50, 50, 50), -- España: Mucho stock
(5, 10, 10, 10, 10); -- Bayern: Stock normal

-- ==========================================
-- 4. Insertar Carritos (Carrito activo)
-- ==========================================
-- Juan (ID 1) tiene un carrito pendiente
INSERT INTO Carrito (usuario_Id, precioTotal, fechaCreacion) VALUES 
(1, 165.50, '2023-10-25 10:00:00');

-- ==========================================
-- 5. Insertar Contenido del Carrito
-- ==========================================
-- El carrito de Juan tiene 2 items
INSERT INTO CarritoContenido (carrito_Id, camiseta_Id, cantidad, tallaSeleccionada, nombrePersonalizado, numeroPersonalizado, llevaParche) VALUES 
-- Camiseta Real Madrid (ID 1), Talla M
(1, 1, 1, 'M', NULL, NULL, 1), 
-- Camiseta Bayern (ID 5), Talla L, Personalizada con su nombre
(1, 5, 1, 'L', 'JUANITO', 10, 0);

-- ==========================================
-- 6. Insertar Pedidos (Histórico de compras)
-- ==========================================
-- María (ID 2) ya realizó un pedido la semana pasada
INSERT INTO Pedidos (usuario_Id, fechaPedido, precioTotal) VALUES 
(2, '2023-10-20 15:30:00', 120.00);

-- ==========================================
-- 7. Insertar Detalle de Pedidos
-- ==========================================
-- El pedido de María fue la camiseta retro del Milan
INSERT INTO DetallePedidos (pedido_Id, camiseta_Id, cantidad, talla, nombrePersonalizado, numeroPersonalizado, llevaParche) VALUES 
(1, 3, 1, 'S', 'KAKA', 22, 0);