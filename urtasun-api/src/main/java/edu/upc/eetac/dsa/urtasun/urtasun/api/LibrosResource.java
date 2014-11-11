package edu.upc.eetac.dsa.urtasun.urtasun.api;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.urtasun.urtasun.api.model.Author;
import edu.upc.eetac.dsa.urtasun.urtasun.api.model.Libros;
import edu.upc.eetac.dsa.urtasun.urtasun.api.model.LibrosCollection;
import edu.upc.eetac.dsa.urtasun.urtasun.api.model.Review;

@Path("/libros")
public class LibrosResource {
	
	@Context
	private SecurityContext security;
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String GET_LIBROS_QUERY = "select lib.*, aut.name from libros lib, autor aut where lib.idAuthor=aut.id order by libroid";

	@GET
	@Produces(MediaType.URTASUN_API_LIBROS_COLLECTION)
	public LibrosCollection getLibros() {
		LibrosCollection libros = new LibrosCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt =conn.prepareStatement(GET_LIBROS_QUERY);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Libros libro = new Libros();
				libro.setLibroid(rs.getInt("libroid"));
				libro.setAutor(rs.getString("name"));
				libro.setIdautor(rs.getInt("idAuthor"));
				libro.setDateCreation(rs.getLong("DateCreation"));
				libro.setDateImpresion(rs.getLong("DateImpresion"));
				libro.setEdition(rs.getString("edition"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setLanguage(rs.getString("language"));
				libro.setTitle(rs.getString("title"));
				
				libros.addLibros(libro);
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return libros;
	}
	
	private String GET_LIBRO_QUERY = "select lib.*,aut.name from libros lib, autor aut where lib.idAuthor=aut.id and libroid=?";
	
	
	@GET
	@Path("/{libroid}")
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Libros getLibro(@PathParam("libroid") String libroid) {
		Libros libro = new Libros();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt =conn.prepareStatement(GET_LIBRO_QUERY);
			stmt.setString(1, libroid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				libro.setAutor(rs.getString("name"));
				libro.setLibroid(rs.getInt("libroid"));
				libro.setIdautor(rs.getInt("idAuthor"));
				libro.setDateCreation(rs.getLong("DateCreation"));
				libro.setDateImpresion(rs.getLong("DateImpresion"));
				libro.setEdition(rs.getString("edition"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setLanguage(rs.getString("language"));
				libro.setTitle(rs.getString("title"));
				
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return libro;
		
	}
	
	private String INSERT_AUTHOR_QUERY = "insert into libros (title, idauthor, language, edition, editorial) values (?,?,?,?,?)";

	@POST
	@Consumes(MediaType.URTASUN_API_LIBROS)
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Libros createLibro(Libros libro) { // CREATE
		validateAdmin();
		System.out.println("Creando Libro....");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_AUTHOR_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, libro.getTitle());
			stmt.setInt(2, libro.getIdautor());
			stmt.setString(3, libro.getLanguage());
			stmt.setString(4, libro.getEdition());
			stmt.setString(5, libro.getEditorial());
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int idlibro = rs.getInt(1);
				libro = getLibroFromDatabase(Integer.toString(idlibro));
				System.out.println("idautor del nuevo libro: "+ libro.getIdautor());
				System.out.println("Titulo del nuevo libro: "+ libro.getTitle());
				System.out.println("Idioma del nuevo libro: "+ libro.getLanguage());
				System.out.println("Edicion del nuevo libro: "+ libro.getEdition());
				System.out.println("Editorial del nuevo libro: "+ libro.getEditorial());
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return libro;
	}
	
	
	private String UPDATE_LIBRO_QUERY = "update libros set language=ifnull(?, language), edition=ifnull(?, edition), editorial=ifnull(?, editorial) where libroid=?";

	@PUT
	@Path("/{libroid}")
	@Consumes(MediaType.URTASUN_API_LIBROS)
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Libros updateLibro(@PathParam("libroid") String idlibro, Libros libro) { // UPDATE
		validateAdmin();
		System.out.println("Actualizando Libro....");

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_LIBRO_QUERY);
			stmt.setString(1, libro.getLanguage());
			stmt.setString(2, libro.getEdition());
			stmt.setString(3, libro.getEditorial());
			stmt.setString(4, idlibro);

			int rows = stmt.executeUpdate();
			if (rows == 1){
				libro = getLibroFromDatabase(idlibro);
				System.out.println("Libro con titulo: "+ libro.getTitle() +" en: "+libro.getLanguage()+" editado por: "+libro.getEditorial()+" en su "+libro.getEdition()+" edition, ha sido actualizado.");
			}
			
			else {
				throw new NotFoundException("No existe ningun libro con id="
						+ idlibro);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return libro;
	}
	
	
	private String DELETE_LIBROS_QUERY = "delete from libros where libroid=?";

	@DELETE
	@Path("/{libroid}")
	public void deleteLibro(@PathParam("libroid") String idlibro) { // DELETE
		validateAdmin();
		System.out.println("Borrando Libro....");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		Libros libro = new Libros ();
		libro = getLibroFromDatabase(idlibro);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_LIBROS_QUERY);
			stmt.setInt(1, Integer.valueOf(idlibro));

			int rows = stmt.executeUpdate();
			
			System.out.println("Libro con nombre: "+ libro.getTitle() +" y id: "+libro.getLibroid()+" borrados.");
			
			if (rows == 0)
				;// Deleting inexistent sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	
	private String GET_LIBRO_BY_ID_QUERY = "select lib.*,aut.name from libros lib, autor aut where lib.idAuthor=aut.id and libroid=?";

	private Libros getLibroFromDatabase(String idlibro) { // GET AUTHOR DATABASE

		Libros libro = new Libros();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_LIBRO_BY_ID_QUERY);
			stmt.setInt(1, Integer.valueOf(idlibro));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				libro.setAutor(rs.getString("name"));
				libro.setLibroid(rs.getInt("libroid"));
				libro.setIdautor(rs.getInt("idAuthor"));
				libro.setDateCreation(rs.getLong("DateCreation"));
				libro.setDateImpresion(rs.getLong("DateImpresion"));
				libro.setEdition(rs.getString("edition"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setLanguage(rs.getString("language"));
				libro.setTitle(rs.getString("title"));
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return libro;

	}
	
	
	private void validateAdmin() { // VALIDATE ADMIN
		if (!security.isUserInRole("administrator"))
			throw new ForbiddenException("This function is only for admins.");

	}
	
	
	
	/*
	 * RESEÑASSS
	 * -------------------------------------------
	 */
	
	String INSERT_REVIEW_QUERY = "insert into review(idlibro,username,name,reviewtext) values (?,?,?,?)";
	
	@POST
	@Path("/review/{libroid}")
	@Consumes(MediaType.URTASUN_API_LIBROS)
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Review createReview( @PathParam ("libroid") String idlibro, Review review) { // CREATE REVIEW
		System.out.println("Creando reseña....");
		System.out.println("libroid..."+idlibro);
		System.out.println("username "+review.getUsernameReviewer());
		System.out.println("name "+review.getNameReviewer());
		System.out.println("reseña "+review.getReviewtext());
		validateUserOfBook(idlibro);
		boolean turn = validateOnetimePerUser(review.getUsernameReviewer(), review.getIdLibro());
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		
			try {
				if(turn==true){
					stmt = conn.prepareStatement(INSERT_REVIEW_QUERY,
							Statement.RETURN_GENERATED_KEYS);
		
					stmt.setString(1, idlibro);
					stmt.setString(2, review.getUsernameReviewer());
					stmt.setString(3, review.getNameReviewer());
					stmt.setString(4, review.getReviewtext());
					
					stmt.executeUpdate();
					ResultSet rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						
						review = getReviewFromDatabase(idlibro);
					}
				}
			
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return review;
	}
	
	private String UPDATE_REVIEW_QUERY = "update review set reviewtext=ifnull(?, reviewtext) where idreview=?";

	@PUT
	@Path("/review/{libroid}/{reviewid}")
	@Consumes(MediaType.URTASUN_API_LIBROS)
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Review updatereview(@PathParam("libroid") String idlibro, @PathParam("reviewid") String idReview, Review Review) { // UPDATE
		System.out.println("Actualizando Review....");
		validateUserOfBook(idlibro);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_REVIEW_QUERY);
			stmt.setString(1, Review.getReviewtext());
			stmt.setInt(2, Integer.valueOf(idReview));

			

			int rows = stmt.executeUpdate();
			if (rows == 1){
				Review = getReviewFromDatabase(idReview);
				System.out.println("Reseña con id: "+ Review.getIdReview() +" ha sido actualizada.");
			}
			
			else {
				throw new NotFoundException("No existe ningun libro con id="
						+ idlibro);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return Review;
	}
	
	
	private String DELETE_REVIEW_QUERY = "delete from review where idreview=?";

	@DELETE
	@Path("/review/{libroid}/{reviewid}")
	public void deleteReview(@PathParam("libroid") String idlibro, @PathParam("reviewid") String idReview) { // DELETE
		System.out.println("Borrando Libro....");
		
		validateUserOfBook(idlibro);
			
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		Review review = new Review ();
		review = getReviewFromDatabase(idlibro);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_REVIEW_QUERY);
			stmt.setInt(1, Integer.valueOf(idlibro));

			int rows = stmt.executeUpdate();
			
			System.out.println("Review/s con el id del libro: "+ review.getIdReview()+" borrados.");
			
			if (rows == 0)
				;// Deleting inexistent sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private String GET_REVIEW_BY_IDLIBRO = "select * from review where idlibro=?";

	private Review getReviewFromDatabase(String idReview) { // GET AUTHOR DATABASE

		Review review = new Review();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_REVIEW_BY_IDLIBRO);
			stmt.setInt(1, Integer.valueOf(idReview));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				review.setIdReview(rs.getInt("idreview"));
				review.setIdLibro(rs.getInt("idlibro"));
				review.setUsernameReviewer(rs.getString("username"));
				review.setNameReviewer(rs.getString("name"));
				review.setReviewtext(rs.getString("reviewtext"));
				review.setLastModified(rs.getTimestamp("lastmodified").getTime());
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return review;

	}
	
	private String GET_NUM_OF_TIMES_USED = "select * from review where username=? and idlibro=?";

	
	private Boolean validateOnetimePerUser(String username, int idlibro){
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		int count = 0;
		boolean turn;
		try {
			stmt = conn.prepareStatement(GET_NUM_OF_TIMES_USED);
			stmt.setString(1, username);
			stmt.setInt(2, idlibro);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count++;
			}
			if(count<2){
				turn = true;
			}
			else{
				turn = false;
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return turn;		
	}

	private void validateUserOfBook(String idLibro){
		Libros libro = getLibroFromDatabase(idLibro);
	    String autorlibro = libro.getAutor();
	    System.out.println("id -->"+idLibro+"comparo "+ autorlibro +" y "+ security.getUserPrincipal().getName());
	    		if (!security.getUserPrincipal().getName()
				.equals(autorlibro))
			throw new ForbiddenException(
					"No eres el autor de este libro.");
		
	}
}