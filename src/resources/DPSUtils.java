package resources;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;
import application.JSONHandler;
import application.Main;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import entities.AllJsonData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import threads.DiscordConnection;

public class DPSUtils {
	private static String currentDirectory = null;
	private static DiscordConnection disCon;
	private static String version = "v1.5.0";
	private static Boolean running = false;
	private static Integer pokeCatchCounter = 0;
	private static Label fullCounter;
	private static Button btn;
	private static TextField token;
	private static Integer pokestopsRobed = 0;
	private static TextField waitingTime;
	private static TextField amountToCatch;

	public static double formatCoords(double coords) {
		DecimalFormat df = new DecimalFormat("###.#####");
		return Double.parseDouble(df.format(coords).replace(',', '.'));
	}

	public static void log(String logMessage) {
		log(logMessage, MyColors.defaultMessage);
	}

	public static void log(String logMessage, String color) {
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		System.out.println("[" + time.format(formatter) + "] " + logMessage);
		DPSUtils.updateLogArea("[" + time.format(formatter) + "] " + logMessage, color);
	}

	public static String getToken() {
		return AllJsonData.getToken();
	}

	public static void loadSnipingPokemon() {
		JSONHandler.PokeList();
	}

	public static void updateLogArea(String str, String color) {
		Label lab = new Label(str);
		lab.setMinWidth(Region.USE_PREF_SIZE);
		RowConstraints row = new RowConstraints();
		row.setMaxHeight(30);
		row.setMinHeight(30);
		lab.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AllJsonData.getLog().getChildren().add(lab);
				AllJsonData.getScrollForLog().setVvalue(1.0);
			}
		});

	}

	public static void disableLoggers() {
		Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.setLevel(Level.ERROR);
	}

	public static void startBot(Button btn, TextField token) {
		DPSUtils.btn = (btn);
		DPSUtils.token = token;
		disableLoggers();
		setCurrentDirectoryLocation();
		DPSUtils.log("Starting Program ");
		disCon = new DiscordConnection(btn, token);
		disCon.start();
	}

	public static void startBot() {
		disableLoggers();
		setCurrentDirectoryLocation();
		DPSUtils.log("Starting Program ");
		disCon = new DiscordConnection(DPSUtils.btn, DPSUtils.token);
		disCon.start();
	}

	public static void stopBot() {
		if (disCon != null)
		disCon.terminate();
	}

	public static void stopBot(String str) {
		DPSUtils.log("Stoping Bot, Reason: " + str, MyColors.error);
		if (disCon != null)
			disCon.terminate();
	}

	public static void forceStopBot(String str) {
		DPSUtils.log("Stoping Bot, Reason: " + str, MyColors.hardError);
		if (disCon != null)
		disCon.forceTerminate();
	}

	public static void setCurrentDirectoryLocation() {
		try {
			CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			currentDirectory = jarFile.getParentFile().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentDirectory() {
		return currentDirectory;
	}

	public static String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		DPSUtils.version = version;
	}

	public static Boolean getRunning() {
		return running;
	}

	public static void setRunning(Boolean running) {
		DPSUtils.running = running;
	}

	public static Integer getPokeCatchCounter() {
		return pokeCatchCounter;
	}

	public static void setPokeCatchCounter() {
		DPSUtils.pokeCatchCounter++;
		if (pokeCatchCounter >= AllJsonData.getAmountToCatch()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DPSUtils.stopBot("Total number of " + pokeCatchCounter + " pokemons was caught!");
		}
	}

	public static void setPokeCatchCounter(Integer toAdd) {
		DPSUtils.pokeCatchCounter += toAdd;
	}

	public static Label getFullCounter() {
		return fullCounter;
	}

	public static void setFullCounter(Label fullCounter) {
		DPSUtils.fullCounter = fullCounter;
	}

	public static Button getBtn() {
		return btn;
	}

	public static void setBtn(Button btn) {
		DPSUtils.btn = btn;
	}

	public static Integer getPokestopsRobed() {
		return pokestopsRobed;
	}

	public static void setPokestopsRobed() {
		DPSUtils.pokestopsRobed++;
	}

	public static TextField getWaitingTime() {
		return waitingTime;
	}

	public static void setWaitingTime(TextField waitingTime) {
		DPSUtils.waitingTime = waitingTime;
	}

	public static TextField getAmountToCatch() {
		return amountToCatch;
	}

	public static void setAmountToCatch(TextField amountToCatch) {
		DPSUtils.amountToCatch = amountToCatch;
	}

}
