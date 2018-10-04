/*******************************************************************************
 * Copyright (C) 2017  TeamDank
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package uib.teamdank.common.util;

import java.io.InputStream;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Imports the open source data from YR to provide the current weather situation
 * for every game. The weather is real time weather forecast.
 */
public class WeatherData {

	private static final long WEATHER_FETCH_LIMIT_SECONDS = (60 * 12);

	/**
	 * YR's XML-format for weather data requires four parameters: country,
	 * county, municipality and place name. The following string can be
	 * formatted with those parameters in that order to get the weather data
	 * XML-file for a specific location.
	 * 
	 * @see http://om.yr.no/verdata/xml/
	 */
	private static final String XML_URL_FORMAT = "http://www.yr.no/place/%s/%s/%s/%s/forecast.xml";

	private WeatherData() {
		// Hide constructor
	}

	/**
	 * Represents different weather conditions.
	 * 
	 * @see http://om.yr.no/forklaring/symbol/
	 */
	public enum WeatherType {

		CLOUD(3, 4, 42, 7, 26, 20, 15), 
		SNOW(44, 8, 45, 28, 21, 29, 47, 12, 48, 31, 23, 32, 49, 13, 50, 33, 14, 34), 
		SUN(1, 2), 
		RAIN(40, 5, 41, 24, 6, 25, 43, 27, 46, 9, 10, 30, 22, 11);

		private static WeatherType getFor(int symbol) {
			for (WeatherType type : WeatherType.values()) {
				if (type.symbols.contains(symbol)) {
					return type;
				}
			}
			return null;
		}

		private final List<Integer> symbols = new ArrayList<>();

		WeatherType(int... symbols) {
			for (int symbol : symbols) {
				this.symbols.add(symbol);
			}
		}
	}

	private WeatherType previousWeatherType;
	private long previousPullTime;

	private WeatherType parseWeatherDocument(Document document) {
		NodeList weatherNodes = document.getElementsByTagName("symbol");
		Element currentWeatherNode = (Element) weatherNodes.item(0);
		int weatherSymbol = Integer.parseInt(currentWeatherNode.getAttribute("number"));
		return WeatherType.getFor(weatherSymbol);
	}

	private WeatherType pickRandomType() {
		WeatherType[] types = WeatherType.values();
		return types[new Random().nextInt(types.length)];
	}

	/**
	 * Pulls the current weather conditions in the specified location in
	 * (English) and returns it as a {@link WeatherType}.
	 * 
	 * If for some reason the weather data could not be pulled, a random weather
	 * type will be picked. Weather will only be refreshed every
	 * {@value #WEATHER_FETCH_LIMIT_SECONDS} seconds. However, this method
	 * caches the previous value and be called unlimited times.
	 * 
	 * @param country
	 *            the country ("land" in Norwegian)
	 * @param county
	 *            the county ("fylke" in Norwegian)
	 * @param municipality
	 *            the municipality ("kommune" in Norwegian)
	 * @param placeName
	 *            the place name ("stedsnavn" in Norwegian)
	 * @return the current weather type in the specified location
	 */
	public WeatherType pullWeather(String country, String county, String municipality, String placeName) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		if (System.currentTimeMillis() - previousPullTime > WEATHER_FETCH_LIMIT_SECONDS * 1000) {
			try {
				Future<InputStream> f = service.submit(() -> new URL(String.format(XML_URL_FORMAT, country, county, municipality, placeName)).openStream()); 

				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(f.get(7, TimeUnit.SECONDS));
				previousWeatherType = parseWeatherDocument(document);

			} catch (Exception e) {
				Logger logger = LoggerFactory.getLogger(WeatherData.class);
				logger.error(e.getMessage());

				// Could not fetch weather data, pick a random one
				previousWeatherType = pickRandomType();
			}
			previousPullTime = System.currentTimeMillis();
			this.saveAsJson();
		}

		service.shutdown();
		return previousWeatherType;
	}

	private void saveAsJson() {

		FileHandle handle = Gdx.files.external("TeamDank/WeatherData/data.json");
		Gson gson = new GsonBuilder().create();
		handle.writeString(gson.toJson(this), false);
	}

	public static WeatherData create() {

		try {
			FileHandle handle = Gdx.files.external("TeamDank/WeatherData/data.json");
			if (handle.exists()) {
				Gson gson = new GsonBuilder().create();
				return gson.fromJson(handle.readString(), WeatherData.class);
			}
			return new WeatherData();
		} catch (Exception e) {
			Logger logger = LoggerFactory.getLogger(WeatherData.class);
			logger.error(e.getMessage());
			
			// Return a new type if exception
			return new WeatherData();
		}
	}
}
