package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

public class Libros {
	
	private int libroid;
	private String title;
	private int idautor;
	private String autor;
	private String language;
	private String edition;
	private long dateCreation;
	private long dateImpresion;
	private String Editorial;
	private List<Link> links;
	public int getLibroid() {
		return libroid;
	}
	public void setLibroid(int libroid) {
		this.libroid = libroid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIdautor() {
		return idautor;
	}
	public void setIdautor(int idautor) {
		this.idautor = idautor;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public long getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(long dateCreation) {
		this.dateCreation = dateCreation;
	}
	public long getDateImpresion() {
		return dateImpresion;
	}
	public void setDateImpresion(long dateImpresion) {
		this.dateImpresion = dateImpresion;
	}
	public String getEditorial() {
		return Editorial;
	}
	public void setEditorial(String editorial) {
		Editorial = editorial;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}




	
}
