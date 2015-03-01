package weatherStorePack;

public class CityWeatherRecord {
	private int id;
	private CityRecord city;
	private int date;
	private int rainInPercents;
	private double temperature;
	
	public enum RetCodes {
		CMP_RETCODE_ERROR,	// if trying to compare different cities, or different date
		CMP_RETCODE_EQUAL,	// if weather is equal
		CMP_RETCODE_TEMP,	// if temperature is not equal
		CMP_RETCODE_RAIN,	// if rain is not equal 
		CMP_RETCODE_TEMP_RAIN,	// if temperature and rain are not equal
				
	};
	
	public int getId() {
		return id;
	}
	public String getName() {
		return city.name;
	}
	public int getWeatherDate() {
		return date;
	}
	public int getRain() {
		return rainInPercents;
	}
	public double getTemperature() {
		return temperature;
	}
	
	public void setId(int newid) {
		id = newid;
	}
	public void setCity(CityRecord newcity) {
		city = newcity;
	}
	public void setDate(int newdate) {
		date = newdate;
	}
	public void setRain(int rain) {
		rainInPercents = rain;
	}
	public void setTemperature(double temp) {
		temperature = temp;
	}
	
	public RetCodes compareWeather(CityWeatherRecord rec) {
		if ((id != rec.getId()) 
				|| (city.name.compareTo(rec.getName()) != 0)
				|| (id == rec.getId() && date != rec.getWeatherDate())) {
			return RetCodes.CMP_RETCODE_ERROR;
		} else {
			if (temperature == rec.getTemperature() && rainInPercents == rec.getRain()) {
				return RetCodes.CMP_RETCODE_EQUAL;
			} else if (temperature != rec.getTemperature() && rainInPercents == rec.getRain()) {
				return RetCodes.CMP_RETCODE_TEMP;
			} else if (temperature == rec.getTemperature() && rainInPercents != rec.getRain()) {
				return RetCodes.CMP_RETCODE_RAIN;
			} else if (temperature != rec.getTemperature() && rainInPercents != rec.getRain()) {
				return RetCodes.CMP_RETCODE_TEMP_RAIN;
			}
		}
		
		return RetCodes.CMP_RETCODE_ERROR;
	}
}