package jcats.model;

import java.io.File;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TickFileSystem {
	private static DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
	private static DateTimeFormatter dateFormatter =  DateTimeFormatter.ISO_LOCAL_DATE; 
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static String basePath;

	public static DateTimeFormatter getTimeFormatter() {
		return timeFormatter;
	}
	public static DateTimeFormatter getDateFormatter() {
		return dateFormatter;
	}
	public static void setBasePath(String basePath) {
		TickFileSystem.basePath = basePath;
		new File(basePath).mkdirs();
	}
	public static String getBasePath() {
		return TickFileSystem.basePath;
	}
	public static String getPath(String ticker, LocalDate date, String basePath) {
		if (basePath == null || basePath.isEmpty()) basePath = getBasePath();
		String path = basePath + monthFormatter.format(date);
		new File(path).mkdirs();
		return path + "/" + ticker + "_" + dateFormatter.format(date) + ".txt";
	}
}
