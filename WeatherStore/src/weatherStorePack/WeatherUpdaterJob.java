package weatherStorePack;

import java.util.LinkedList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class WeatherUpdaterJob implements Job{
	private WorkWithDB db = null;
	private List<List<CityWeatherRecord>> citiesWeather = null;
	private List<CityRecord> crds = null;
	private WeatherRetreiver weatherRetriever = null;
	
	public void init() {
		db = new WorkWithDB();
		db.createTable();
		
		ReadCitiesFromFile reader = new ReadCitiesFromFile();
		weatherRetriever = new WeatherRetreiver();
		
		citiesWeather = new LinkedList<List<CityWeatherRecord>>();
		if (reader.readCitiesFromFile()) {
			 crds = reader.getCities();	
			 citiesWeather = weatherRetriever.checkForUpdates(crds);
			 db.insertDataIntoDB(citiesWeather);
		}
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Hello, world");
		if (crds != null && weatherRetriever != null) {
			citiesWeather = weatherRetriever.checkForUpdates(crds);
		}
		
	}

}
