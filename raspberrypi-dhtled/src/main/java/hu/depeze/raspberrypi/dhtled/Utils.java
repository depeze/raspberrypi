package hu.depeze.raspberrypi.dhtled;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

public class Utils {

	public static void log(String message) {
		System.out.println(getDateTime() + " " + message);
	}

	public static String getDateTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Budapest"));
		return simpleDateFormat.format(new Date());
	}

	private static void writeToFile(String filePath, String content) {
		try {
			File file = new File(filePath);
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static String getDataLine(float value) {
		return System.currentTimeMillis() + "," + value + System.getProperty("line.separator");
	}

	public static synchronized void writeTemperature(String directory, float value) {
		writeToFile(directory + File.separator + "temperature.csv", getDataLine(value));
	}

	public static synchronized void writeHumidity(String directory, float value) {
		writeToFile(directory + File.separator + "humidity.csv", getDataLine(value));
	}

}