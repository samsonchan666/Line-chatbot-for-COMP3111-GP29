package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;
import java.lang.*;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	private Connection connection;
	String text = null;

	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		this.text = text;
		this.connection = this.getConnection();

		result = searchRes();
		if (result != null)
			return result;

		result = searchTour();
		if (result != null)
			return result;

		connection.close();
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

	private String searchRes() throws Exception{
		String result = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT keyword, response FROM restable where STRPOS(LOWER(?),LOWER(keyword))>0"
			);
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString(2);
			}
			rs.close();
			stmt.close();

		}
		catch (Exception e){
			System.out.println("searchRes()" + e);
		}
		return result;
	}

	private String searchTour() throws Exception{
		StringBuilder str = new StringBuilder();
		String result = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT id, name FROM tour where STRPOS(LOWER(name), LOWER(?))>0  or STRPOS(LOWER(attraction), LOWER(?))>0"
			);
			stmt.setString(1, text);
			stmt.setString(2, text);
			ResultSet rs = stmt.executeQuery();
			int count = 1;
			if (rs.next()) {
				str.append(count + ".  " +
						rs.getString("id") + "\t" +
						rs.getString("name") + "\n");
				result = str.toString();

				count++;
			}
			rs.close();
			stmt.close();

		}
		catch (Exception e){
			System.out.println("searchTour()" + e);
		}
		return result;
	}

}
