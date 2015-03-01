package weatherStorePack;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.DailyForecast.Forecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import org.json.JSONException;

public class WeatherRetreiver {
	private OpenWeatherMap owm = null;

    /*public static void main(String[] args)
            throws IOException, MalformedURLException, JSONException {

        // declaring object of "OpenWeatherMap" class
        OpenWeatherMap owm = new OpenWeatherMap("9229fc57d217e84684dfb717867b67f5");

        // getting current weather data for the "London" city
        CurrentWeather cwd = owm.currentWeatherByCityName("London");
        

        // checking data retrieval was successful or not
        if (cwd.isValid()) {

            // checking if city name is available
            if (cwd.hasCityName()) {
                //printing city name from the retrieved data
                System.out.println("City: " + cwd.getCityName());
            }

            // checking if max. temp. and min. temp. is available
            if (cwd.getMainInstance().hasMaxTemperature() && cwd.getMainInstance().hasMinTemperature()) {
                // printing the max./min. temperature
                System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
                            + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");
            }
        }
    }*/
	private void init() {
		// declaring object of "OpenWeatherMap" class
		owm = new OpenWeatherMap("9229fc57d217e84684dfb717867b67f5");
	}
	
	private double getCelcium(double temp) {
		return Math.round((5 * (temp - 32.0))/9);
		//return Math.floor(((5 * (temp - 32.0))/9)*100)/100; round to 2 decimals after point
	}
		
	public CityWeatherRecord getCurrentWeatherForCity(CityRecord city) {
		if (owm == null) {
			return null;
		}

        try {
	        // getting current weather data for the city
	        CurrentWeather cwd = owm.currentWeatherByCityName(city.name);
	        
	        // checking data retrieval was successful or not
	        if (cwd.isValid()) {
	        	CityWeatherRecord cityRec = new CityWeatherRecord();
	        	cityRec.setCity(city);
	            // checking if city name is available
	            if (cwd.hasCityName()) {
	                //printing city name from the retrieved data
	                System.out.println("City: " + cwd.getCityName());                
	            }
	            if (cwd.hasCityCode()) {
	                System.out.println("City code: " + cwd.getCityCode());	
	                cityRec.setId(cwd.getCityCode());
	            }

	            if (cwd.getMainInstance().hasTemperature()) {
	            	System.out.println("Temperature: " + cwd.getMainInstance().getTemperature() + "\'F");
	            	System.out.println("Temperature: " + getCelcium(cwd.getMainInstance().getTemperature()) + "\'C");
	            	cityRec.setTemperature(getCelcium(cwd.getMainInstance().getTemperature()));
	            }
	            
	            if (cwd.getCloudsInstance().hasPercentageOfClouds()) {
	            	System.out.println("Clouds: " + cwd.getCloudsInstance().getPercentageOfClouds());  
	            	cityRec.setClouds(cwd.getCloudsInstance().getPercentageOfClouds());
	            }
	            return cityRec;
	        } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	public void retreiveTodayCitiesWeather(List<CityRecord> cities) {
		if (owm == null) {
			init();
		}
		for (int i = 0; i < cities.size(); i++) {
			getCurrentWeatherForCity(cities.get(i));
		}
	}
	
	public void getForecast() throws IOException, JSONException {
		if (owm == null) {
			init();
		}
		DailyForecast fcst = owm.dailyForecastByCityName("Moscow", "RU", (byte) 10);
		if (fcst.isValid()) {
			System.out.println("forecast: " + fcst.getForecastCount());
			System.out.println("forecast: " + fcst.getForecastInstance(0).getTemperatureInstance().getDayTemperature());
			System.out.println("forecast: " + fcst.getForecastInstance(1).getTemperatureInstance().getDayTemperature());
			System.out.println("forecast: " + fcst.getForecastInstance(2).getTemperatureInstance().getDayTemperature());
			System.out.println("forecast: " + fcst.getForecastInstance(3).getTemperatureInstance().getDayTemperature());
			System.out.println("forecast: " + fcst.getForecastInstance(9).getTemperatureInstance().getDayTemperature());
		} else {
			System.out.println("forecast invalid ");
		}
	}
}