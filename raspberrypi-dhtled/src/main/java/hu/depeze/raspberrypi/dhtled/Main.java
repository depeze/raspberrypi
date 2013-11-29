package hu.depeze.raspberrypi.dhtled;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class Main {

	public static void main(String[] args) {
		Options options = new Options();

		Option dhtCommandOption = new Option("dhtCommand", true, "DHT command (required)");
		dhtCommandOption.setRequired(true);
		options.addOption(dhtCommandOption);

		Option dataDirectoryOption = new Option("dataDirectory", true, "Directory path for data files (required)");
		dataDirectoryOption.setRequired(true);
		options.addOption(dataDirectoryOption);

		options.addOption("highHumidityLevel", true, "High humidity level to flash the LED (default = 70)");
		options.addOption("sampleFrequencySeconds", true, "Sample frequency in seconds (default = 5)");
		options.addOption("allowedErrorPercent", true, "Allowed error percent (default = 70)");

		try {
			CommandLineParser commandLineParser = new PosixParser();
			CommandLine commandLine = commandLineParser.parse(options, args);

			String dhtCommand = commandLine.getOptionValue("dhtCommand");
			int highHumidityLevel = Integer.valueOf(commandLine.getOptionValue("highHumidityLevel", "70"));
			int sampleIntervalSeconds = Integer.valueOf(commandLine.getOptionValue("sampleFrequencySeconds", "5"));
			float allowedErrorPercent = Float.valueOf(commandLine.getOptionValue("allowedErrorPercent", "70"));
			String dataDirectory = commandLine.getOptionValue("dataDirectory");

			Runnable runnable = new DHTThread(dhtCommand, sampleIntervalSeconds, highHumidityLevel, allowedErrorPercent, dataDirectory);
			Thread dhtThread = new Thread(runnable);
			dhtThread.start();
		} catch (Exception ex) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setLeftPadding(3);
			formatter.printHelp("DHTLED", options);
			System.exit(-1);
		}
	}

}