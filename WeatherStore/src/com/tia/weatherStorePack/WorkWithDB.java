package com.tia.weatherStorePack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

public class WorkWithDB {
	private static final String LEFT_BR = "(";
	private static final String RIGHT_BR = ")";
	private static final String COMMA = ",";
	private static final String STRING_BR = "'";
	private static final String TABLE_NAME = "WEATHER_TABLE";
	private static final String TABLE_VALUES = "(ID, DAY, NAME, TEMPERATURE, CLOUDS)";
	private static final String SELECT_VALUES = "NAME, TEMPERATURE, CLOUDS";
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	private Connection connectToDatabase() {
		Connection con = null;
		String url = "jdbc:postgresql://localhost:5432/testdb";
	    String user = "postgres";
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
				String sql1 =  "DROP TABLE IF EXISTS " + TABLE_NAME;
				st.executeUpdate(sql1);
				String sql = "CREATE TABLE " + TABLE_NAME 
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
	
	public void insertDataIntoDB(List<List<CityWeatherRecord>> cities) {
		try {
			Connection con = connectToDatabase();
			if (con != null) {
				System.out.println("Opened database successfully");
				
				PreparedStatement st = null;
				try {
					String req = constructInsertFromCitiesList(cities);
					System.out.println(req);
					st = con.prepareStatement(req);
					st.executeUpdate();
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        }
				
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
	
	public void insertDataIntoDB(CityWeatherRecord city) {
		try {
			Connection con = connectToDatabase();
			if (con != null) {
				System.out.println("Opened database successfully");
				
				PreparedStatement st = null;
				try {
					String req = constructInsertFromCity(city);
					System.out.println(req);
					st = con.prepareStatement(req);
					st.executeUpdate();
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        }
				
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
	
	public CityWeatherRecord retreiveWeatherForCodeDate(long code, Date date) {
		CityWeatherRecord city = null;
		try {
			Connection con = connectToDatabase();
			if (con != null) {
				System.out.println("Opened database successfully");
				
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					String req = constructSelectRequest(code, date);
					System.out.println(req);
					
					st = con.prepareStatement(req);
					rs = st.executeQuery();
					
					if (rs.next()) {							
						CityRecord cr = new CityRecord();
						cr.name = rs.getString("NAME");
						city = new CityWeatherRecord(code, cr, date,
													rs.getFloat("CLOUDS"),
													rs.getDouble("TEMPERATURE"));
						//TODO rm print
						city.printCity();
		            }
		        } catch (SQLException e) {
		        	e.printStackTrace();
		        }
				
				st.close();
				rs.close();
				con.close();
			} else {
				System.out.println("connection error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
	    }
		return city;
	}
	
	public void updateWeatherDB(List<List<CityWeatherRecord>> citiesWeather) {
		for (int i = 0; i < citiesWeather.size(); i++) {
			for (int j = 0; j < citiesWeather.get(i).size(); j++) {
				updateCityWeatherDB(citiesWeather.get(i).get(j));
			}
		}
	}
	
	public void updateCityWeatherDB(CityWeatherRecord city) {
		if (retreiveWeatherForCodeDate(city.getId(), city.getWeatherDate()) != null) {
			//update data
			System.out.println("update");
			try {
				Connection con = connectToDatabase();
				if (con != null) {
					System.out.println("Opened database successfully");
					
					Statement st = null;
					try {						
						st = con.createStatement();
						con.setAutoCommit(false);
						st.executeUpdate(constructUpdateTempRequest(city));
						st.executeUpdate(constructUpdateCloudsRequest(city));
						con.commit();
												
			        } catch (SQLException e) {
			        	if (con != null) {
			                try {
			                    con.rollback();
			                } catch (SQLException ex1) {
			                	ex1.printStackTrace();
			                }
			            }
			        	e.printStackTrace();
			        }
					
					st.close();
					con.close();
				} else {
					System.out.println("connection error");
				}
			} catch (SQLException e) {
				e.printStackTrace();
		    }
			System.out.println("update info Successfully");
		} else {
			//write
			System.out.println("write");
			insertDataIntoDB(city);
		}
	}
	
	private String constructSelectRequest(long code, Date date) {
		StringBuilder sb = new StringBuilder("SELECT ");
		sb.append(SELECT_VALUES).append(" FROM ").append(TABLE_NAME);
		sb.append(" WHERE id=").append(Long.toString(code));
		sb.append(" AND day=").append("to_date('").append(dateFormat.format(date.getTime()))
								.append("', '").append(DATE_FORMAT).append("');");
		return sb.toString();
	}
	
	private String constructUpdateTempRequest(CityWeatherRecord city) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(TABLE_NAME).append(" SET TEMPERATURE=").append(Double.toString(city.getTemperature()));
		sb.append(" WHERE id=").append(Long.toString(city.getId()));
		sb.append(" AND day=").append("to_date('").append(dateFormat.format(city.getWeatherDate().getTime()))
								.append("', '").append(DATE_FORMAT).append("');");
		return sb.toString();
	}
	
	private String constructUpdateCloudsRequest(CityWeatherRecord city) {
		StringBuilder sb = new StringBuilder("UPDATE ");
		sb.append(TABLE_NAME).append(" SET CLOUDS=").append(Float.toString(city.getClouds()));
		sb.append(" WHERE id=").append(Long.toString(city.getId()));
		sb.append(" AND day=").append("to_date('").append(dateFormat.format(city.getWeatherDate().getTime()))
								.append("', '").append(DATE_FORMAT).append("');");
		return sb.toString();
	}
	
	private String constructDataToInsert(String value) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(TABLE_NAME).append(" ").append(TABLE_VALUES).append(" VALUES ").append(value);
		return sb.toString();
	}
	
	private String constructOneCityRecord(CityWeatherRecord city) {
		StringBuilder sb = new StringBuilder(LEFT_BR);
		sb.append(Long.toString(city.getId())).append(COMMA);
		sb.append("to_date('").append(dateFormat.format(city.getWeatherDate().getTime()))
								.append("', '").append(DATE_FORMAT).append("')").append(COMMA);
		sb.append(STRING_BR).append(city.getName()).append(STRING_BR).append(COMMA);
		sb.append(Double.toString(city.getTemperature())).append(COMMA);
		sb.append(Float.toString(city.getClouds())).append(RIGHT_BR);

		return sb.toString();
	}
	
	private String constructRequestFromCities(List<List<CityWeatherRecord>> cities) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cities.size() - 1; i++) {
			for (int j = 0; j < cities.get(i).size(); j++) {
				sb.append(constructOneCityRecord(cities.get(i).get(j))).append(COMMA);
			}			
		}
		sb.replace(sb.length()-1, sb.length(), ";");
		return sb.toString();
	}
	
	private String constructInsertFromCitiesList(List<List<CityWeatherRecord>> cities) {
		return constructDataToInsert(constructRequestFromCities(cities));
	}
	
	private String constructInsertFromCity(CityWeatherRecord city) {
		return constructDataToInsert(constructOneCityRecord(city));
	}
}
