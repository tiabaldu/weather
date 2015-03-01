package weatherStorePack;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

public class testMain {

	public static void main(String[] args)
            throws IOException, JSONException {
		 System.out.println("Hello, world");
		 ReadCitiesFromFile reader = new ReadCitiesFromFile();
		 if (reader.readCitiesFromFile()) {
			 List<CityRecord> crds = reader.getCities();
			 for (int i = 0; i < crds.size(); i++) {
				 System.out.println(crds.get(i).name);
			 }
			 WeatherRetreiver weathers = new WeatherRetreiver();
			 weathers.retreiveTodayCitiesWeather(crds);
			 
			 weathers.getForecast();
		 }		 		 
	}
}
 