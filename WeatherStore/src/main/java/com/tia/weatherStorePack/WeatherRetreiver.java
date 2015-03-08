package com.tia.weatherStorePack;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.tia.weatherStorePack.CityWeatherRecord.RetCodes;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.DailyForecast.Forecast;
import net.aksingh.owmjapis.OpenWeatherMap;

public class WeatherRetreiver {
	private OpenWeatherMap owm = null;
	private final static int DAYS_FOR_FORECAST = 10;
	private List<List<CityWeatherRecord>> lastUpdatedWeatherList = null;
	private int amountOfCities = 0;

	private void init() {
		// declaring object of "OpenWeatherMap" class
		owm = new OpenWeatherMap("9229fc57d217e84684dfb717867b67f5");
	}

	// convert Farenheit to Celcium
	private double toCelcium(double temp) {
		return Math.round((5 * (temp - 32.0)) / 9);
	}

	//retreives current (now) weather for city from openweathermap.org
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
				
				return cityRec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//retreives current (now) weather for list of cities from openweathermap.org
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

	// returns weather forecast for list of cities
	public List<List<CityWeatherRecord>> dailyForecastsForAllCities(List<CityRecord> cities) {
		if (owm == null) {
			init();
		}
		amountOfCities = cities.size();
		List<List<CityWeatherRecord>> weatherList = new LinkedList<List<CityWeatherRecord>>();
		for (int i = 0; i < amountOfCities; i++) {
			List<CityWeatherRecord> list = new LinkedList<CityWeatherRecord>();
			list.addAll(forecastForCity(cities.get(i)));
			weatherList.add(list);
		}
		return weatherList;
	}
	
	// return updated cities weather list 
	public List<List<CityWeatherRecord>> checkForUpdates(List<CityRecord> cities) {
		if (lastUpdatedWeatherList == null) {
			lastUpdatedWeatherList = dailyForecastsForAllCities(cities);
			return lastUpdatedWeatherList;
		} else {
			List<List<CityWeatherRecord>> weatherList = new LinkedList<List<CityWeatherRecord>>();
			List<List<CityWeatherRecord>> updatesList = new LinkedList<List<CityWeatherRecord>>();
			weatherList = dailyForecastsForAllCities(cities);
			for (int i = 0; i < amountOfCities; i++) {
				List<CityWeatherRecord> list = new LinkedList<CityWeatherRecord>();
				List<CityWeatherRecord> listUpdates = new LinkedList<CityWeatherRecord>();
				list = weatherList.get(i);
				for (int j = 0; j < list.size(); j++) {
					//search for updates for one city and day
					CityWeatherRecord found = searchElemByDate(lastUpdatedWeatherList.get(i), list.get(j).getWeatherDate());
					if (found == null) {
						// if not found - new daily forecast
						listUpdates.add(list.get(j));
					} else {
						RetCodes code = list.get(j).compareWeather(found);
						if ((code != RetCodes.CMP_RETCODE_ERROR) && (code != RetCodes.CMP_RETCODE_EQUAL)) {
							listUpdates.add(list.get(j));
						}
					}					
				}
				if (!listUpdates.isEmpty()) {
					updatesList.add(listUpdates);
				}
			}
			lastUpdatedWeatherList = weatherList;
			return updatesList;
		}
	}
	
	@SuppressWarnings("deprecation")
	//searching weather forecast by city code and date
	private CityWeatherRecord searchElemByDate(List<CityWeatherRecord> cities, Date date) {
		CityWeatherRecord found = null;
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i).getWeatherDate().getDay() == date.getDay() &&
					cities.get(i).getWeatherDate().getYear() == date.getYear()) {
				found = cities.get(i);
			}
		}
		return found;
	}
	
}