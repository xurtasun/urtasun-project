package edu.upc.eetac.dsa.urtasun.urtasun.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.urtasun.urtasun.api.model.Author;
import edu.upc.eetac.dsa.urtasun.urtasun.api.MediaType;
import edu.upc.eetac.dsa.urtasun.urtasun.api.DataSourceSPA;


@Path("/author")
public class AuthorResource {
	
	@Context
	private SecurityContext security;
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	private String INSERT_AUTHOR_QUERY = "insert into autor (name) value (?)";
	
	@POST
	@Consumes(MediaType.URTASUN_API_AUTHOR)
	@Produces(MediaType.URTASUN_API_AUTHOR)
	public Author createAuthor(Author author) {  //CREATE

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		validateAdmin();
		
		PreparedStatement stmt = null;
		try {
				stmt = conn.prepareStatement(INSERT_AUTHOR_QUERY,
						Statement.RETURN_GENERATED_KEYS);
	
				stmt.setString(1, author.getName());
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()){
					String autint = rs.getString(Integer.valueOf(1));
					author = getAutFromDatabase (autint);
					System.out.println("nombre autor registrado"+ author.getName());
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
		return author;
	}
	
	private String UPDATE_AUTHOR_QUERY = "update autor set name=ifnull(?, name) where id=?";
	
	@PUT
	@Path("/{authorid}")
	@Consumes(MediaType.URTASUN_API_AUTHOR)
	@Produces(MediaType.URTASUN_API_AUTHOR)
	public Author updateSting(@PathParam("authorid") String autid, Author author) {  //UPDATE
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		validateAdmin();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_AUTHOR_QUERY);
			stmt.setString(1, author.getName());
			stmt.setInt(2, Integer.valueOf(autid));

			int rows = stmt.executeUpdate();
			if (rows == 1)
				author = getAutFromDatabase(autid);
			else {
				throw new NotFoundException("There's no sting with stingid="
						+ autid);
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

		return author;
		}
	
	
	
	private String DELETE_AUTHOR_QUERY = "delete from author where id=?";

	@DELETE
	@Path("/{authorid}")
	public void deleteAuthor(@PathParam("authorid") String autid) { //DELETE
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		validateAdmin();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_AUTHOR_QUERY);
			stmt.setInt(1, Integer.valueOf(autid));

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
	
	private String GET_AUT_BY_ID_QUERY = "select * from autor where id=?";
	
	private Author getAutFromDatabase (String autint){  //GET AUTHOR DATABASE
		
		Author author = new Author();
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_AUT_BY_ID_QUERY);
			stmt.setInt(1, Integer.valueOf(autint));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				author.setIdauthor(Integer.valueOf("id"));
				author.setName("name");
			}
		}
		catch (SQLException e) {
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
		return author;
		
	}

	
	String GET_USERNAME_OF_ADMIN_BY_QUERY_AUTHOR = "select ur.username from user_roles ur where ur.rolename='administrator'";
	
	private String getUsernameOfAdminFromAutorDatabase (){
		String userName;
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_USERNAME_OF_ADMIN_BY_QUERY_AUTHOR);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userName = rs.getString("username");
				System.out.println(userName);
			} else {
				throw new NotFoundException("No hay administradores registrados en la BD.");
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		
		
		return userName;
	}
	
	private void validateAdmin (){  // VALIDATE ADMIN
		
		String Autname = getUsernameOfAdminFromAutorDatabase();
		System.out.println("Nombre administrador bd: "+Autname);
		System.out.println("Nombre intento registro postman: "+ security.getUserPrincipal().getName());
		if (!security.getUserPrincipal().getName()
				.equals(Autname))
			throw new ForbiddenException(
					"No tienes permiso para crear un autor nuevo.");
		
		
	}


	
	

}
