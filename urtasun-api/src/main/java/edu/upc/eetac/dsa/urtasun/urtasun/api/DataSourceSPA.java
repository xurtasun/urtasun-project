package edu.upc.eetac.dsa.urtasun.urtasun.api;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceSPA {
    private DataSource dataSource; //referencia al datasource
	private static DataSourceSPA instance; //referencia al Singleton

	private DataSourceSPA() {
		super();
		Context envContext = null;
		try {
			envContext = new InitialContext();
			Context initContext = (Context) envContext.lookup("java:/comp/env");
			dataSource = (DataSource) initContext.lookup("jdbc/librosdb"); 
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}

	public final static DataSourceSPA getInstance() { //se obtiene la referencia al datasource
		if (instance == null)
			instance = new DataSourceSPA();
		return instance;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}