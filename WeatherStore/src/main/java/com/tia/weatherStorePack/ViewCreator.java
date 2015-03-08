package com.tia.weatherStorePack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ViewCreator {
	private static final String FILE_VIEW = "WebContent/index.html";
	private static final String END_DOC = "</fieldset>";
	private String view = null;
			
	public String createViewWithWeather(CityWeatherRecord city) {
		if (view == null) {
			createBaseView();
		}
		StringBuilder sb = new StringBuilder(view);
		int enddoc = sb.lastIndexOf(END_DOC);
		StringBuilder insertsb = new StringBuilder();
		if (city == null) {
			System.out.println("weather not found");
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			insertsb.append("<p>").append("Unfortunatelly weather with entered city code and date not found in DB. ")
					.append("Please check city code and date format: dd/MM/yyyy").append("</p>");
		} else {
			System.out.println("weather found");
			insertsb.append("<p><p><b>Weather forecast:</b></p>")
					.append("<p><table>")
					.append("<tr><td>City name: </td><td>").append(city.getName()).append("</td></tr>")
					.append("<tr><td>Date: </td><td>").append(city.getWeatherDate().toString()).append("</td></tr>")
					.append("<tr><td>Temperature: </td><td>").append(Double.toString(city.getTemperature())).append("C</td></tr>")
					.append("<tr><td>Cloudness: </td><td>").append(Float.toString(city.getClouds()))
					.append("%</td></tr>").append("</table></p>");
		}
		sb.insert(enddoc, insertsb.toString());
		view = sb.toString();
		return view;
	}
	
	public String createBaseView() {
		File file = new File(FILE_VIEW); 
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			view = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return view;
	}
}
