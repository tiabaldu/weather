package weatherStorePack;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

public class testMain {

	public static void main(String[] args)
            throws IOException, JSONException {
		 System.out.println("Hello, world");
		 ReadCitiesFromFile reader = new ReadCitiesFromFile();
		 WorkWithDB db = new WorkWithDB();
		 List<CityWeatherRecord> citiesWeather = new LinkedList<CityWeatherRecord>();
		 if (reader.readCitiesFromFile()) {
			 List<CityRecord> crds = reader.getCities();
			 WeatherRetreiver weathers = new WeatherRetreiver();
			 citiesWeather = weathers.checkForUpdates(crds);
			 
		 }		 		 
		 
		 db.createTable();
		 db.insertDataIntoDB(citiesWeather);
	}
}
 