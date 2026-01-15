TABLAS BASE DE DATOS:
--Usuarios:
  Id (PK),
  DNI,
  Nombre,
  Apellido,
  Correo,
  Contraseña
--Camisetas:
  Id (PK),
  Equipo/Nombre,
  Imagen,
  Precio,
  Temporada,
  Liga,
  Parche,
  NombreDorsal
--StockPorTalla
  Id (PK),
  StockS.
  StockM,
  StockL,
  StockXL,
  CF Camiseta
--Carrito:
  Id (PK),
  CF Usuario,
  Precio,
  FechaCreacion
--Carrito Contenido:
  Id (PK),
  CF CarritoId,
  CF CamisetaId,
  Cantidad
