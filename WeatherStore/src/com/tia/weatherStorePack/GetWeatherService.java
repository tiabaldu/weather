package com.tia.weatherStorePack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GetWeatherService {
	//@Path("/getweather")
	public class MessageRestService {
	 
		@GET
		@Path("/getweather")
		@Produces(MediaType.TEXT_PLAIN)
		public Response printMessage(@FormParam("code") String strcode, @FormParam("date") String strdate) {
	 
			long code = Long.parseLong(strcode);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			WorkWithDB db = new WorkWithDB();
			try {
				Date date = format.parse(strdate);
				CityWeatherRecord weather = db.retreiveWeatherForCodeDate(code, date);
			} catch (ParseException e) {
				e.printStackTrace();
			} 			
			
			String result = "Restful example : ";
	 
			return Response.status(200).entity(result).build();
	 
		}
	 
	}
}
