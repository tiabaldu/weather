package weatherStorePack;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import weatherStorePack.CityWeatherRecord.RetCodes;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.DailyForecast.Forecast;
import net.aksingh.owmjapis.OpenWeatherMap;

public class WeatherRetreiver {
	private OpenWeatherMap owm = null;
	private final static int DAYS_FOR_FORECAST = 10;
	private List<CityWeatherRecord> lastUpdatedWeatherList = null;

	private void init() {
		// declaring object of "OpenWeatherMap" class
		owm = new OpenWeatherMap("9229fc57d217e84684dfb717867b67f5");
	}

	private double toCelcium(double temp) {
		return Math.round((5 * (temp - 32.0)) / 9);
		// return Math.floor(((5 * (temp - 32.0))/9)*100)/100; round to 2
		// decimals after point
	}

	public CityWeatherRecord currentWeatherForCity(CityRecord city) {
		if (owm == null) {
			return null;
		}

		try {
			// getting current weather data for the city
			CurrentWeather cwd = owm.currentWeatherByCityName(city.name);

			// checking data retrieval was successful or not
			if (cwd.isValid()) {
				Date date = Calendar.getInstance().getTime();
				CityWeatherRecord cityRec = new CityWeatherRecord(cwd.getCityCode(),
												city, 
												date, 
												cwd.getCloudsInstance().getPercentageOfClouds(),
												toCelcium(cwd.getMainInstance().getTemperature()));
				
				//TODO rm print
				cityRec.printCity();
				
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
			currentWeatherForCity(cities.get(i));
		}
	}

	// returns weather forecast for one city
	public List<CityWeatherRecord> forecastForCity(CityRecord city) {
		if (owm == null) {
			init();
		}
		try {
			DailyForecast fcst = owm.dailyForecastByCityName(city.name,
					city.country, (byte) DAYS_FOR_FORECAST);
			if (fcst.isValid()) {
				List<CityWeatherRecord> weatherList = new LinkedList<CityWeatherRecord>();
				for (int i = 0; i < DAYS_FOR_FORECAST; i++) {
					Forecast daily = fcst.getForecastInstance(i);
					CityWeatherRecord cityWeather = new CityWeatherRecord(fcst.getCityInstance().getCityCode(),
													city,
													daily.getDateTime(),
													daily.getPercentageOfClouds(),
													toCelcium(daily.getTemperatureInstance().getDayTemperature()));

					//TODO rm print
					cityWeather.printCity();
					weatherList.add(cityWeather);

				}
				return weatherList;
			} else {
				System.out.println("forecast invalid ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CityWeatherRecord> dailyForecastsForAllCities(List<CityRecord> cities) {
		if (owm == null) {
			init();
		}
		List<CityWeatherRecord> weatherList = new LinkedList<CityWeatherRecord>();
		for (int i = 0; i < cities.size(); i++) {
			weatherList.addAll(forecastForCity(cities.get(i)));
		}
		return weatherList;
	}
	
	// return updated cities weather list 
	public List<CityWeatherRecord> checkForUpdates(List<CityRecord> cities) {
		if (lastUpdatedWeatherList == null) {
			lastUpdatedWeatherList = dailyForecastsForAllCities(cities);
			return lastUpdatedWeatherList;
		} else {
			List<CityWeatherRecord> weatherList = new LinkedList<CityWeatherRecord>();
			List<CityWeatherRecord> updatesList = new LinkedList<CityWeatherRecord>();
			weatherList = dailyForecastsForAllCities(cities);
			for (int i = 0; i < weatherList.size(); i++) {
				RetCodes code = weatherList.get(i).compareWeather(lastUpdatedWeatherList.get(i));
				if ((code != RetCodes.CMP_RETCODE_ERROR) && (code != RetCodes.CMP_RETCODE_EQUAL)) {
					CityWeatherRecord city = weatherList.get(i);
					city.setUpdateNecessity(code);
					updatesList.add(city);
				}
			}
			lastUpdatedWeatherList = weatherList;
			return updatesList;
		}
	}
	
}