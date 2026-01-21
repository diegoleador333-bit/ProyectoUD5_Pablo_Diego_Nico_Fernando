package com.example.demo.Endpoints;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Camisetas.CamisetasMapper;
import com.example.demo.Carrito.Carrito;
import com.example.demo.Carrito.CarritoMapper;
import com.example.demo.CarritoContenido.CarritoContenido;
import com.example.demo.CarritoContenido.contenidoMapper;
import com.example.demo.Usuarios.Usuarios;

import jakarta.servlet.http.HttpSession;

// Todos los endpoints tendra una variable del carrito del usuario y la declaracion de un usuario gracias al Http Session
// Los necesitan modificar un producto en especifico recogeran el id del producto y su talla
// Las variables que pillan de valor un select tendran que usar la clase propia 

@CrossOrigin(origins = { "http://127.0.0.1:5500", "http://localhost:5500" }, allowCredentials = "true")
@RestController
@RequestMapping("/carrito")
public class CarritoEndpoints {

	private final JdbcTemplate jdbcTemplate;

	public CarritoEndpoints(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// Enpoint para lista los productos del carrito
	@GetMapping("/productos")
	public Map<String, Object> verCarrito(HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return Map.of("error", "No has iniciado sesión");
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		List<CarritoContenido> contenido = jdbcTemplate.query("SELECT * FROM CarritoContenido WHERE carrito_Id = ?",
				new contenidoMapper(), carrito.getId());

		// Despues de conseguir el carrito y la lista de los items en lista gracias al
		// usuario lo devolvemos en forma de mapa
		return Map.of("items", contenido, "precioTotal", carrito.getPrecioTotal());
	}

	// Enpoint para vaciar el carrito entero
	@GetMapping("/vaciar")
	public String vaciar(HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		// Hacemos dos consultas para borrar todos los producto y resetear el precio
		// total
		jdbcTemplate.update("DELETE FROM CarritoContenido WHERE carrito_Id = ?", carrito.getId());

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = 0 WHERE id = ?", carrito.getId());

		return "Carrito vaciado";
	}

	// Endpoints para borrar una camiseta del carrito
	@PostMapping("/eliminar/{camiseta_Id}/{tallaSeleccionada}")
	public String eliminarDelCarrito(@PathVariable("camiseta_Id") int idCamiseta,
			@PathVariable("tallaSeleccionada") String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		// Se comprueba si tiene el parche o no
		Boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND tallaSeleccionada = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		// Calculamos la cantidad de camisetas para saber cuanto se restara y el precio
		// de cada unidad
		Integer cantidad = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		Double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		// Se calcula cuanto se restara y tambien la cantidad en caso de parche
		double totalResta = precioUnidad * cantidad;
		if (parche != null && parche) {
			totalResta += 5.00 * cantidad;
		}

		// Finalmente quitamos el producto del carrito y se resta la cantidad de precio
		// total
		jdbcTemplate.update(
				"DELETE FROM CarritoContenido WHERE camiseta_Id = ? AND carrito_Id = ? AND tallaSeleccionada = ?",
				idCamiseta, carrito.getId(), talla);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", totalResta,
				carrito.getId());

		return "Producto eliminado";
	}

	// Endpoint para relizar la compra con todo lo que conlleva
	@PostMapping("/comprar")
	// Transactional por si existe algun problema durante las multiples consultas
	// que afectan a la base de datos
	@Transactional
	public String comprar(HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		List<CarritoContenido> contenido = jdbcTemplate.query("SELECT * FROM CarritoContenido WHERE carrito_Id = ?",
				new contenidoMapper(), carrito.getId());

		// Primero se verifica que el carrito no este vacio
		if (contenido.isEmpty()) {
			return "El carrito esta vacio";
		}

		// Con un for revisamos el stock de cada productod del carrito
		for (CarritoContenido item : contenido) {
			String columnaStock = "stock" + item.getTallaSeleccionada();

			Integer stockDisponible = jdbcTemplate.queryForObject(
					"SELECT " + columnaStock + " FROM StockPorTalla WHERE camiseta_Id = ?", Integer.class,
					item.getCamiseta());

			// En caso de que no haya stock se pillan los datos de la camiseta y devulve un
			// mensaje de error
			if (item.getCantidad() > stockDisponible) {
				Camisetas sinStock = jdbcTemplate.queryForObject("SELECT * FROM Camisetas WHERE id = ?",
						new CamisetasMapper(), item.getCamiseta());
				return "Hubo un error al realizar la compra. La camiseta del " + sinStock.getEquipo() + " de "
						+ sinStock.getTemporada() + " no tiene suficiente stock";
			}
		}

		// Una vez visto que hay stock se hace el update del stock para restarlo
		for (CarritoContenido item : contenido) {
			String columnaStock = "stock" + item.getTallaSeleccionada();
			jdbcTemplate.update(
					"UPDATE StockPorTalla SET " + columnaStock + " = " + columnaStock + " - ? WHERE camiseta_Id = ?",
					item.getCantidad(), item.getCamiseta());
		}

		// Creamos un nuevo pedido para el usuario
		jdbcTemplate.update("INSERT INTO Pedidos (usuario_Id, precioTotal) VALUES (?, ?)", carrito.getUsuario(),
				carrito.getPrecioTotal());

		// Conseguimos el id del nuevo pedido y rellenamos todos los productos en la
		// tabla detallae pedido
		Integer idPedido = jdbcTemplate.queryForObject("SELECT MAX(id) FROM Pedidos", Integer.class);

		for (CarritoContenido item : contenido) {
			jdbcTemplate.update(
					"INSERT INTO DetallePedidos (pedido_Id, camiseta_Id, cantidad, talla, nombrePersonalizado, numeroPersonalizado, llevaParche) VALUES (?, ?, ?, ?, ?, ?, ?)",
					idPedido, item.getCamiseta(), item.getCantidad(), item.getTallaSeleccionada(),
					item.getNombrePersonalizado(), item.getNumeroPersonalizado(), item.isLlevaParche() ? 1 : 0);
		}

		// Finalmente vaciamos el carrito y dejamos el precioTotal a 0
		jdbcTemplate.update("DELETE FROM CarritoContenido WHERE carrito_Id = ?", carrito.getId());

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = 0 WHERE id = ?", carrito.getId());

		return "Compra realizada correctamente";
	}

	// Enpoint para el boton de sumar cantidad
	@PostMapping("/sumar/{camiseta_Id}/{tallaSeleccionada}")
	public String aumentarCantidad(@PathVariable("camiseta_Id") int idCamiseta,
			@PathVariable("tallaSeleccionada") String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		// Revisamos que tenga o no parche
		Boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND tallaSeleccionada = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		// Confirmamos el stock disponible y la cantidad actual del carrito
		String columnaStock = "stock" + talla;

		Integer stockDisponible = jdbcTemplate.queryForObject(
				"SELECT " + columnaStock + " FROM StockPorTalla WHERE camiseta_Id = ?", Integer.class, idCamiseta);

		Integer cantidadCarrito = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		// En caso de que no haya suficiente no se podra sumar
		if (cantidadCarrito >= stockDisponible) {
			return "No hay mas stock disponible para la talla " + talla;
		}

		// En caso de que si conseguimos el precio de la unidad y le sumamos el dinero
		// del caso de tener parche
		Double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		if (parche != null && parche) {
			precioUnidad += 5.00;
		}

		// Finalmente actulizamos el campo e cantidad y precio total
		jdbcTemplate.update(
				"UPDATE CarritoContenido SET cantidad = cantidad + 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
				carrito.getId(), idCamiseta, talla);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal + ? WHERE id = ?", precioUnidad,
				carrito.getId());

		return "Cantidad aumentada";
	}

	// Endpoint para el boton de restar en cantidad
	@PostMapping("/restar/{camiseta_Id}/{tallaSeleccionada}")
	public String disminuirCantidad(@PathVariable("camiseta_Id") int idCamiseta,
			@PathVariable("tallaSeleccionada") String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		// Confirmamos el stock disponible y la cantidad actual del carrito
		Boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND tallaSeleccionada = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		// En caso de que si conseguimos el precio de la unidad y le sumamos el dinero
		// del caso de tener parche
		Double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		if (parche != null && parche) {
			precioUnidad += 5.00;
		}

		// Finalmente actulizamos el campo e cantidad y precio total
		jdbcTemplate.update(
				"UPDATE CarritoContenido SET cantidad = cantidad - 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ? AND cantidad > 1",
				carrito.getId(), idCamiseta, talla);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", precioUnidad,
				carrito.getId());

		return "Cantidad disminuida";
	}

	// Enpoint para cambiar el parche del producto
	@PostMapping("/parche/{camiseta_Id}/{tallaSeleccionada}")
	public String cambioParche(@PathVariable("camiseta_Id") int idCamiseta,
			@PathVariable("tallaSeleccionada") String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		// Confirmamos el stock disponible y la cantidad actual del carrito
		Boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND tallaSeleccionada = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		// Calculamos al cantidad para ver cuanto afectara al precio total y el precio
		// total
		Integer cantidadCarrito = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		double precioFinal = 5.00 * cantidadCarrito;

		// En caso de tener parche se añade y en contrario se quita, ademas actulizamos
		// el precio total
		if (parche != null && parche) {
			jdbcTemplate.update(
					"UPDATE CarritoContenido SET llevaParche = 0 WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
					carrito.getId(), idCamiseta, talla);
			jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", precioFinal,
					carrito.getId());
			return "Parche quitado";
		} else {
			jdbcTemplate.update(
					"UPDATE CarritoContenido SET llevaParche = 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
					carrito.getId(), idCamiseta, talla);
			jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal + ? WHERE id = ?", precioFinal,
					carrito.getId());
			return "Parche añadido";
		}
	}

	// Endpoint para cambiar los datos de la camiseta desde el carrito
	@PostMapping("/datos/{camiseta_Id}/{tallaSeleccionada}")
	public String cambioNombre(@RequestParam("nombrePersonalizado") String nombrePersonalizado,
			@RequestParam("numeroPersonalizado") String numeroPersonalizado,
			@PathVariable("camiseta_Id") int idCamiseta, @PathVariable("tallaSeleccionada") String talla,
			HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		// Recojemos el nombre y numero para con ellos hacer un update y actualizar los
		// datos
		jdbcTemplate.update(
				"UPDATE CarritoContenido SET nombrePersonalizado = ?, numeroPersonalizado = ? WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
				nombrePersonalizado, numeroPersonalizado, carrito.getId(), idCamiseta, talla);

		return "Datos cambiados";
	}
}
