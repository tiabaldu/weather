package weatherStorePack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class WorkWithDB {
	private static final String LEFT_BR = "(";
	private static final String RIGHT_BR = ")";
	private static final String COMMA = ",";
	private static final String STRING_BR = "'";
	private static final String DB_NAME = "WEATHER_TABLE";
	private static final String DB_VALUES = "(ID, DAY, NAME, TEMPERATURE, CLOUDS)";
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy/MM/dd");

	private Connection connectToDatabase() {
		Connection con = null;
		String url = "jdbc:postgresql://localhost:5432/testdb";
	    String user = "userDB";
	    String password = "weather";
	    try {
	    	DriverManager.registerDriver(new org.postgresql.Driver());
	        con = DriverManager.getConnection(url, user, password);
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	    }
	    if (con == null) {
	    	System.out.println("connection error");
	    }
	    return con;
	}
	
	//TODO change to private
	public void createTable() {
		try {
			Connection con = connectToDatabase();
			if (con != null) {
				System.out.println("Opened database successfully");
				
				Statement st = null;
				try {
					st = con.createStatement();
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        }
				// TODO: rm deleting table
				String sql1 =  "DROP TABLE IF EXISTS " + DB_NAME;
				st.executeUpdate(sql1);
				String sql = "CREATE TABLE " + DB_NAME 
							+ " (ID INT NOT NULL,"
							+ "DAY TIMESTAMP NOT NULL,"
							+ "NAME TEXT NOT NULL,"
							+ "TEMPERATURE REAL NOT NULL,"
							+ "CLOUDS REAL NOT NULL)";
				st.executeUpdate(sql);
				st.close();
				con.close();
			} else {
				System.out.println("connection error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
	    }
		System.out.println("Table Created Successfully");		
	}
	
	public void insertDataIntoDB(List<CityWeatherRecord> cities) {
		try {
			Connection con = connectToDatabase();
			if (con != null) {
				System.out.println("Opened database successfully");
				
				Statement st = null;
				try {
					st = con.createStatement();
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        }
				String req = constructInsertFromCitiesList(cities);
				System.out.println(req);
				st.executeUpdate(req);
				st.close();
				con.close();
			} else {
				System.out.println("connection error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
	    }
		System.out.println("add info Successfully");
	}
	
	private String constructDataToInsert(String value) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(DB_NAME).append(" ").append(DB_VALUES).append(" VALUES ").append(value);
		return sb.toString();
	}
	
	private String constructOneCityRecord(CityWeatherRecord city) {
		StringBuilder sb = new StringBuilder(LEFT_BR);
		sb.append(Long.toString(city.getId())).append(COMMA);
		//sb.append(Long.toString(city.getWeatherDate().getTime())).append(COMMA);
		sb.append("to_date('").append(dateFormat.format(city.getWeatherDate().getTime()))
				.append("', 'yyyy/mm/dd')").append(COMMA);
		sb.append(STRING_BR).append(city.getName()).append(STRING_BR).append(COMMA);
		sb.append(Double.toString(city.getTemperature())).append(COMMA);
		sb.append(Double.toString(city.getClouds())).append(RIGHT_BR);
		
		//TODO rm print
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	private String constructRequestFromCities(List<CityWeatherRecord> cities) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cities.size() - 1; i++) {
			sb.append(constructOneCityRecord(cities.get(i))).append(COMMA);
		}
		sb.append(constructOneCityRecord(cities.get(cities.size() - 1))).append(";");
		return sb.toString();
	}
	
	private String constructInsertFromCitiesList(List<CityWeatherRecord> cities) {
		return constructDataToInsert(constructRequestFromCities(cities));
	}
}
