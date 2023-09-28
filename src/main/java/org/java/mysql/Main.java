package org.java.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
		final String url = "jdbc:mysql://localhost:3306/db-nations";
		final String user = "root";
		final String password = "root";
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			final String query = " SELECT c.country_id , c.name 'country_name' , r.name 'region_name' , cont.name 'continent_name'  "
					 + " FROM countries c  "
					 + " JOIN regions r ON r.region_id = c.region_id "
					 + " JOIN continents cont ON cont.continent_id = r.continent_id "
					 + "ORDER BY c.name ";
			

			try {
				PreparedStatement ps = con.prepareStatement(query);			
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
					int id = rs.getInt("country_id");
					String name = rs.getString("country_name");
					String regionName = rs.getString("region_name");
					String continentName = rs.getString("continent_name");
					
					System.out.println("ID: " + id + " - Country name: " + name 
							+ " - Region name: " + regionName + " - Continent name: " + continentName );
					
					
				}
			} catch (Exception e) {
				
				System.out.println(e.getMessage());
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}}}

