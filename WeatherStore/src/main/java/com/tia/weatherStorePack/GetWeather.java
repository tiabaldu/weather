package com.tia.weatherStorePack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetWeather {

	/* Find weather helper
	 * search and return weather data in DB
	 * @param code - city code
	 * @param date - forecast date 
	 */
    private CityWeatherRecord findWeather(long code, String strdate) {
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	CityWeatherRecord weather = null;
		WorkWithDB db = new WorkWithDB();
		try {
			Date date = format.parse(strdate);
			weather = db.retreiveWeatherForCodeDate(code, date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return weather;
    }

    /*
     * Returns JSON with weather info on GET request
     */
    @RequestMapping("/GetJSON")
    public CityWeatherRecord getJSON(@RequestParam(value="date", defaultValue="16/03/2015") String date,
    								@RequestParam(value="code", defaultValue="498817") long code) {
    	
    	CityWeatherRecord weather = findWeather(code, date);
		if (weather != null) {
			return weather;
		} else {
			return null;
		}
    }
    
    /*
     * Returns initial view of http://localhost:8080/index.html
     */
    @RequestMapping("/index.html")
    public String getHtml() {  	    	
        return new ViewCreator().createBaseView();
    }
    
    /*
     * Returns weather info on GET request
     */
    @RequestMapping("/GetWithView")
    public String getWeather(@RequestParam(value="date", defaultValue="16/03/2015") String date,
    								@RequestParam(value="code", defaultValue="498817") long code) {
    	
    	CityWeatherRecord weather = findWeather(code, date);
    	return new ViewCreator().createViewWithWeather(weather);
    }
}
