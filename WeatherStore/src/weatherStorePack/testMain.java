package weatherStorePack;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class testMain {

	public static void main(String[] args) throws IOException, JSONException {
		System.out.println("Hello, world");
		/*ReadCitiesFromFile reader = new ReadCitiesFromFile();
		WorkWithDB db = new WorkWithDB();
		List<List<CityWeatherRecord>> citiesWeather = new LinkedList<List<CityWeatherRecord>>();
		if (reader.readCitiesFromFile()) {
			List<CityRecord> crds = reader.getCities();

			WeatherRetreiver weathers = new WeatherRetreiver();
			citiesWeather = weathers.checkForUpdates(crds);

		}

		db.createTable();
		db.insertDataIntoDB(citiesWeather);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		try {
			date = dateFormat.parse("2015/03/09");
			db.retreiveWeatherForCodeDate(3451190, date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.updateWeatherDB(citiesWeather.get(0).get(0));*/

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("UpdateWeatherTrigger", "group1")
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInMinutes(1).repeatForever())
				.build();
		try {
			WeatherUpdaterJob updateJob = new WeatherUpdaterJob();
			updateJob.init();
			JobDetail job = JobBuilder.newJob(updateJob.getClass())
					.withIdentity("WeatherJob", "group1").build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}	

	}
}
