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
	public Libros updateSting(@PathParam("libroid") String idlibro, Libros libro) { // UPDATE
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
	public void deleteAuthor(@PathParam("libroid") String idlibro) { // DELETE
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
	
	
	private String GET_LIBRO_BY_ID_QUERY = "select * from libros where libroid=?";

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
			throw new ForbiddenException("This fucntion is only for admins.");

	}
	
	
	/*
	 * RESEÑASSS
	 * -------------------------------------------
	 */
	
	String INSERT_REVIEW_QUERY = "insert into review(idlibro,username,name,review) values (?,?,?,?)";
	
	@POST
	@Path("/{libroid}/review")
	@Consumes(MediaType.URTASUN_API_LIBROS)
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Review createReview( @PathParam ("libroid") String idlibro, Review review) { // CREATE REVIEW
		System.out.println("Creando reseña....");
		System.out.println("libroid..."+idlibro);
		System.out.println("username "+review.getUsernameReviewer());
		System.out.println("name "+review.getNameReviewer());
		System.out.println("reseña "+review.getReview());
		
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
					stmt.setString(4, review.getReview());
					
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
	
	
	
	
	private String UPDATE_REVIEW_QUERY = "update review set review=ifnull(?, review) where idreview=?";

	@PUT
	@Path("/{libroid}/review")
	@Consumes(MediaType.URTASUN_API_LIBROS)
	@Produces(MediaType.URTASUN_API_LIBROS)
	public Review updatereview(@PathParam("libroid") String idlibro, Review review) { // UPDATE
		System.out.println("Actualizando Review....");

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
			stmt.setString(1, review.getReview());
			stmt.setInt(2, review.getIdReview());

			

			int rows = stmt.executeUpdate();
			if (rows == 1){
				review = getReviewFromDatabase(idlibro);
				System.out.println("Reseña con id: "+ review.getIdReview() +" ha sido actualizada.");
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

		return review;
	}
	
	
	private String DELETE_REVIEW_QUERY = "delete from review where idreview=?";

	@DELETE
	@Path("/{libroid}/review")
	public void deleteReview(@PathParam("libroid") String idlibro) { // DELETE
		System.out.println("Borrando Libro....");
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

	private Review getReviewFromDatabase(String idlibro) { // GET AUTHOR DATABASE

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
			stmt.setInt(1, Integer.valueOf(idlibro));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				review.setIdReview(rs.getInt("idreview"));
				review.setIdLibro(rs.getInt("idlibro"));
				review.setUsernameReviewer(rs.getString("username"));
				review.setNameReviewer(rs.getString("name"));
				review.setReview(rs.getString("review"));
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
	
	
	/*
	private String GET_STINGS_QUERY_SUBJECT = " select s.*, u.name from stings s, users u where u.username=s.username and s.subject=? and s.content=?";
	
	@GET
	@Path("/search")
	@Produces(MediaType.BEETER_API_STING_COLLECTION)
	public StingCollection getStingsParametros(@QueryParam("subject") String subject,
			@QueryParam("content") String content, @QueryParam ("lenght") int length) {
		StingCollection stings = new StingCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		System.out.println(subject);
		System.out.println("creamos statment");
		PreparedStatement stmt = null;
		
		try{
			if (subject!= null){
				try {
					
					stmt = conn.prepareStatement(GET_STINGS_QUERY_SUBJECT);
					
					stmt.setString(1, subject);
					stmt.setString(2, content);
					System.out.println("ejecutando Query--->" + stmt);
					ResultSet rs = stmt.executeQuery();
					boolean first = true;
					long oldestTimestamp = 0;
					int i=0;
					while(rs.next() && i< length){
						
						Sting sting = new Sting();
						
						sting.setStingid(rs.getInt("stingid"));
						System.out.println(sting.getStingid());
						sting.setUsername(rs.getString("username"));
						sting.setAuthor(rs.getString("name"));
						sting.setSubject(rs.getString("subject"));
						oldestTimestamp = rs.getTimestamp("last_modified").getTime();
						sting.setLastModified(oldestTimestamp);
						if (first) {
							first = false;
							stings.setNewestTimestamp(sting.getLastModified());
						}
						stings.addSting(sting);
						i++;
					}
							
					
				}
				catch (SQLException e) {
					throw new ServerErrorException(e.getMessage(),
							Response.Status.INTERNAL_SERVER_ERROR);
				} 
			}
		}finally {
					try {
						if (stmt != null)
							stmt.close();
						conn.close();
					} catch (SQLException e) {
					}
				}
		

		return stings;
	}
		

	
	
	
	private String GET_STING_BY_ID_QUERY = "select s.*, u.name from stings s, users u where u.username=s.username and s.stingid=?";

	private Sting getStingFromDatabase(String stingid) {
		Sting sting = new Sting();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_STING_BY_ID_QUERY);
			stmt.setInt(1, Integer.valueOf(stingid));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				sting.setStingid(rs.getInt("stingid"));
				sting.setUsername(rs.getString("username"));
				sting.setAuthor(rs.getString("name"));
				sting.setSubject(rs.getString("subject"));
				sting.setContent(rs.getString("content"));
				sting.setLastModified(rs.getTimestamp("last_modified")
						.getTime());
				sting.setCreationTimestamp(rs
						.getTimestamp("creation_timestamp").getTime());
			} else {
			throw new NotFoundException("There's no sting with stingid="
			+ stingid);
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return sting;
	}
	
	@GET
	@Path("/{stingid}")
	@Produces(MediaType.BEETER_API_STING)
	public Response getSting(@PathParam("stingid") String stingid, @Context Request request) {
		// Create CacheControl
		CacheControl cc = new CacheControl();

		Sting sting = getStingFromDatabase(stingid);

		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(sting.getLastModified()));

		// Verify if it matched with etag available in http request
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		rb = Response.ok(sting).cacheControl(cc).tag(eTag);

		return rb.build();
	}
	
	
	private String INSERT_STING_QUERY = "insert into stings (username, subject, content) value (?, ?, ?)";

	@POST
	@Consumes(MediaType.BEETER_API_STING)
	@Produces(MediaType.BEETER_API_STING)
	public Sting createSting(Sting sting) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		
		validateSting(sting);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_STING_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			//stmt.setString(1, sting.getUsername());
			stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(2, sting.getSubject());
			stmt.setString(3, sting.getContent());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int stingid = rs.getInt(1);

				sting = getStingFromDatabase(Integer.toString(stingid));
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return sting;
	}
	
	private String DELETE_STING_QUERY = "delete from stings where stingid=?";

	@DELETE
	@Path("/{stingid}")
	public void deleteSting(@PathParam("stingid") String stingid) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		validateUser(stingid);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_STING_QUERY);
			stmt.setInt(1, Integer.valueOf(stingid));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				;// Deleting inexistent sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private String UPDATE_STING_QUERY = "update stings set subject=ifnull(?, subject), content=ifnull(?, content) where stingid=?";

	@PUT
	@Path("/{stingid}")
	@Consumes(MediaType.BEETER_API_STING)
	@Produces(MediaType.BEETER_API_STING)
	public Sting updateSting(@PathParam("stingid") String stingid, Sting sting) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		validateUpdateSting(sting);
		validateUser(stingid);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_STING_QUERY);
			stmt.setString(1, sting.getSubject());
			stmt.setString(2, sting.getContent());
			stmt.setInt(3, Integer.valueOf(stingid));

			int rows = stmt.executeUpdate();
			if (rows == 1)
				sting = getStingFromDatabase(stingid);
			else {
				throw new NotFoundException("There's no sting with stingid="
						+ stingid);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return sting;
		}
	
	@POST
	@Consumes(MediaType.BEETER_API_STING)
	@Produces(MediaType.BEETER_API_STING)
	private void validateSting(Sting sting) {
		if (sting.getSubject() == null)
			throw new BadRequestException("Subject can't be null.");
		if (sting.getContent() == null)
			throw new BadRequestException("Content can't be null.");
		if (sting.getSubject().length() > 100)
			throw new BadRequestException("Subject can't be greater than 100 characters.");
		if (sting.getContent().length() > 500)
			throw new BadRequestException("Content can't be greater than 500 characters.");
	}
	
	private void validateUpdateSting(Sting sting) {
		if (sting.getSubject() != null && sting.getSubject().length() > 100)
			throw new BadRequestException(
					"Subject can't be greater than 100 characters.");
		if (sting.getContent() != null && sting.getContent().length() > 500)
			throw new BadRequestException(
					"Content can't be greater than 500 characters.");
	}
	
	private void validateUser(String stingid) {
	    Sting sting = getStingFromDatabase(stingid);
	    String username = sting.getUsername();
		if (!security.getUserPrincipal().getName()
				.equals(username))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}
	*/
}



