package hu.depeze.raspberrypi.dhtled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class DHTThread implements Runnable {

	private String dhtCommand;
	private int sleep;
	private int badHumidityLevel;
	private float errorPercent;
	private String dataDirectory;

	private volatile boolean running = true;

	public DHTThread(String dhtCommand, int sleep, int badHumidityLevel, float errorPercent, String dataDirectory) {
		this.dhtCommand = dhtCommand;
		this.sleep = sleep;
		this.badHumidityLevel = badHumidityLevel;
		this.errorPercent = errorPercent;
		this.dataDirectory = dataDirectory;
	}

	public void run() {
		Utils.log("DHT thread starting");
		Utils.log("-------------------------------------------");

		Float previousTemperature = null;
		Float previousHumidity = null;

		while (running) {
			PumpStreamHandler pumpStreamHandler = null;
			ByteArrayOutputStream byteArrayOutputStream = null;

			try {
				DefaultExecutor executor = new DefaultExecutor();

				byteArrayOutputStream = new ByteArrayOutputStream(256);
				pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream, byteArrayOutputStream);
				executor.setStreamHandler(pumpStreamHandler);

				ExecuteWatchdog watchdog = new ExecuteWatchdog(5000);
				executor.setWatchdog(watchdog);

				CommandLine cmdLine = CommandLine.parse(dhtCommand);
				executor.execute(cmdLine);
				String dhtOutput = byteArrayOutputStream.toString("ISO-8859-1");

				if (dhtOutput.contains("Temp") && dhtOutput.contains("Hum")) {
					int tempIndex = dhtOutput.indexOf("Temp = ");
					String tempString = dhtOutput.substring(tempIndex + 8, tempIndex + 12);
					float temperature = Float.valueOf(tempString);
					Utils.log("Temperature: " + temperature + " C");

					if (previousTemperature != null && (temperature > previousTemperature * (1 + errorPercent / 100) || temperature < previousTemperature * (1 - errorPercent / 100))) {
						Utils.log("Temperature skipped");
					} else {
						previousTemperature = temperature;
						Utils.writeTemperature(dataDirectory, temperature);
					}

					int humIndex = dhtOutput.indexOf("Hum = ");
					String humString = dhtOutput.substring(humIndex + 6, humIndex + 10);
					float humidity = Float.valueOf(humString);
					Utils.log("Humidity: " + humidity + " %");

					if (previousHumidity != null && (humidity > previousHumidity * (1 + errorPercent / 100) || humidity < previousHumidity * (1 - errorPercent / 100))) {
						Utils.log("Humidity skipped");
					} else {
						previousHumidity = humidity;
						Utils.writeHumidity(dataDirectory, humidity);
					}

					if (humidity > badHumidityLevel) {
						flashLED();
					}
				} else {
					Utils.log("no data received");
				}

				Utils.log("-------------------------------------------");
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (byteArrayOutputStream != null) {
					try {
						byteArrayOutputStream.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

			sleep();
		}

		Utils.log("DHT thread ended");
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	private void sleep() {
		try {
			Thread.sleep(sleep * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private static void flashLED() {
		GpioController gpio = null;
		GpioPinDigitalOutput pin = null;

		try {
			gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED", PinState.HIGH);

			pin.low();
			for (int i = 0; i < 10; i++) {
				pin.toggle();
				Thread.sleep(50);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pin != null) {
				pin.low();
			}

			if (gpio != null) {
				gpio.unprovisionPin(pin);
				gpio.shutdown();
			}
		}
	}

}