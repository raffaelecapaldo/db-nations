package org.java.mysql;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws SQLException {
		final String url = "jdbc:mysql://localhost:3306/db-nations";
		final String user = "root";
		final String password = "root";
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Inserisci filtro per cercare tra le nazioni: ");
		String filter = "%"+sc.nextLine()+"%";

		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			final String query = " SELECT c.country_id , c.name 'country_name' , r.name 'region_name' , cont.name 'continent_name'  "
					 + " FROM countries c  "
					 + " JOIN regions r ON r.region_id = c.region_id "
					 + " JOIN continents cont ON cont.continent_id = r.continent_id "
					 + "WHERE c.name LIKE ? " 
					 + "ORDER BY c.name; ";
			
			final String queryStats = " SELECT DISTINCT  c.name , l.`language`, cs.population , cs.gdp, cs.`year` "
					+ " FROM countries c "
					+ " JOIN country_languages cl ON c.country_id = cl.country_id "
					+ " JOIN languages l ON l.language_id = cl.language_id "
					+ " JOIN country_stats cs ON cs.country_id = c.country_id "
					+ " WHERE (cs.country_id, cs.`year`) IN"
					+ " (SELECT country_id, MAX(`year`) AS latest_year "
					+ " FROM country_stats"
					+ " GROUP BY country_id)"
					+ " AND c.country_id = ? ;";
			

			try {
				PreparedStatement ps = con.prepareStatement(query);		
				
				ps.setString(1, filter);
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
					int id = rs.getInt("country_id");
					String name = rs.getString("country_name");
					String regionName = rs.getString("region_name");
					String continentName = rs.getString("continent_name");
					
					System.out.println("ID: " + id + " - Country name: " + name 
							+ " - Region name: " + regionName + " - Continent name: " + continentName );
					System.out.println();
					
					
				}
				
				PreparedStatement ps2 = con.prepareStatement(queryStats);		
				System.out.println("Inserisci l'ID di una nazione: ");
				int countryID = Integer.valueOf(sc.nextLine());
				ps2.setInt(1, countryID);
				ResultSet rs2 = ps2.executeQuery();

				rs2.next();
				String name = rs2.getString("name");
				String languages = rs2.getString("language");
				long population = rs2.getLong("population");
				long gdp = rs2.getLong("gdp");
				int year = rs2.getInt("year");
				
				while(rs2.next()) {
					languages+= ", "+rs2.getString("language");
				}
				
				System.out.println();
				System.out.println("Nome nazione: " + name);
				System.out.println("Lingue parlate: " + languages);
				System.out.println("Informazioni più recenti");
				System.out.println("Anno informazioni: " + year);
				System.out.println("Popolazione: " + population);
				System.out.println("GDP: "+ gdp);
				

				
			} catch (Exception e) {
				
				System.out.println(e.getMessage());
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}}}

