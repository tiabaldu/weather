package weatherStorePack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReadCitiesFromFile {
	private List<CityRecord> cities = null;
	
	public boolean readCitiesFromFile() {
		String strCities;
		// try to read cities from file
		File file = new File("cities.txt"); 
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			strCities = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// try to parse cities
		try {
            JSONObject jsonObject = new JSONObject(strCities);
            JSONArray citiesArray = jsonObject.getJSONArray(JsonStrings.JSON_TAG_CITIES);
            cities = new LinkedList<CityRecord>();
            for (int i = 0; i < citiesArray.length(); i++) {
            	JSONObject jsonWithCity = citiesArray.getJSONObject(i);
                CityRecord city = cityFromJsonInternal(jsonWithCity);
                cities.add(city);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	// parse one city from json
	private CityRecord cityFromJsonInternal(JSONObject jsonObject) throws JSONException {
		CityRecord city = new CityRecord();
        city.name = jsonObject.getString(JsonStrings.JSON_TAG_CITY_NAME);
        city.country = jsonObject.getString(JsonStrings.JSON_TAG_COUNTRY);
        return city;
	}
	
	public List<CityRecord> getCities() {
		if (cities != null && !cities.isEmpty()) {
			return cities;
		}
		return null;
	}
}
