package weatherStorePack;

import java.util.Date;

// contains data about weather in city at some date
public class CityWeatherRecord {
	private long id;
	private CityRecord city;
	private Date date;
	private float cloudsInPercents;
	private double temperature;
	private RetCodes whatToUpdate = RetCodes.CMP_RETCODE_EQUAL; //nothing to update
	
	public enum RetCodes {
		CMP_RETCODE_ERROR,	// if trying to compare different cities, or different date
		CMP_RETCODE_EQUAL,	// if weather is equal
		CMP_RETCODE_TEMP,	// if temperature is not equal
		CMP_RETCODE_RAIN,	// if rain is not equal 
		CMP_RETCODE_TEMP_RAIN,	// if temperature and rain are not equal
				
	};
	
	public long getId() {
		return id;
	}
	public String getName() {
		return city.name;
	}
	public Date getWeatherDate() {
		return date;
	}
	public float getClouds() {
		return cloudsInPercents;
	}
	public double getTemperature() {
		return temperature;
	}
	public RetCodes getUpdateNecessity() {
		return whatToUpdate;
	}
	
	public void setId(long newid) {
		id = newid;
	}
	public void setCity(CityRecord newcity) {
		city = newcity;
	}
	public void setDate(Date newdate) {
		date = newdate;
	}
	public void setClouds(float rain) {
		cloudsInPercents = rain;
	}
	public void setTemperature(double temp) {
		temperature = temp;
	}
	
	public void setUpdateNecessity(RetCodes code) {
		whatToUpdate = code;
	}
	
	/* CityWeatherRecord comparator
	 * returns CMP_RETCODE_ERROR if we trying to compare weather in different cities 
	 * or weather at different days
	 * CMP_RETCODE_EQUAL if weather in city not changed
	 * CMP_RETCODE_TEMP if temperature at this date changed
	 * CMP_RETCODE_RAIN if rainy changed 
	 * CMP_RETCODE_TEMP_RAIN if temperature and rainy at this day changed
	 */
	public RetCodes compareWeather(CityWeatherRecord rec) {
		if ((id != rec.getId()) 
				|| (city.name.compareTo(rec.getName()) != 0)
				|| (id == rec.getId() && !compareDate(rec.getWeatherDate()))) {
			return RetCodes.CMP_RETCODE_ERROR;
		} else {
			if (temperature == rec.getTemperature() && cloudsInPercents == rec.getClouds()) {
				return RetCodes.CMP_RETCODE_EQUAL;
			} else if (temperature != rec.getTemperature() && cloudsInPercents == rec.getClouds()) {
				return RetCodes.CMP_RETCODE_TEMP;
			} else if (temperature == rec.getTemperature() && cloudsInPercents != rec.getClouds()) {
				return RetCodes.CMP_RETCODE_RAIN;
			} else if (temperature != rec.getTemperature() && cloudsInPercents != rec.getClouds()) {
				return RetCodes.CMP_RETCODE_TEMP_RAIN;
			}
		}
		
		return RetCodes.CMP_RETCODE_ERROR;
	}
	
	@SuppressWarnings("deprecation")
	private boolean compareDate(Date d) {
		if (date.getDay() == d.getDay() && date.getYear() == d.getYear()) {
			return true;
		}
		return false;
	}

	public void printCity() {
		System.out.println("City: " + city.name);
		System.out.println("Date: "	+ date.toString());
		System.out.println("Date: "	+ date.getTime());
		System.out.println("code: " + id);
		System.out.println("temp: " + temperature);
		System.out.println("clouds: " + cloudsInPercents);
	}
}