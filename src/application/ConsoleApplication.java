package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import preparer.Configuration;
import preparer.Dimension;
import preparer.Preparer;
import preparer.Resolutions;

public class ConsoleApplication {

	//---- Constants and Definitions ----
	private interface ExecutionMode {
		public void extractCommand(final String[] args, final Map<Integer, Object> map);
		public void execute();
	}

	private class ConsoleApplicationException extends RuntimeException {
		private ConsoleApplicationException() {
			super();
		}

		private ConsoleApplicationException(final String msg) {
			super(msg);
		}
	}

	private final Integer COMMAND = 0;
	private final Integer FILE_PATH = 1;
	private final Integer RESOLUTION = 2;
	private final Integer SCREEN_WIDTH = 3;
	private final Integer SCREEN_HEIGHT = 4;
	private final Integer NO_CENTER_HORIZONTALLY = 5;
	private final Integer NO_CENTER_VERTICALLY = 6;
	private final Integer NO_FULLSCREEN_MODE = 7;
	private final Integer NO_REMOVE_STYLES = 8;

	private final String EXIT_COMMAND = "exit";
	private final String HELP_COMMAND = "help";
	private final String PREPARE_COMMAND = "prepare";
	private final String SCREEN_WIDTH_ARG = "-w";
	private final String SCREEN_HEIGHT_ARG = "-h";
	private final String NO_CENTER_HORIZONTALLY_ARG = "-ncenterHorizontally";
	private final String NO_CENTER_VERTICALLY_ARG = "-ncenterVertically";
	private final String NO_FULLSCREEN_MODE_ARG = "-nfullScreenMode";
	private final String KEEP_STYLES_ARG = "-keepStyles";

	//---- Atributtes ----
	private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private ExecutionMode executionMode;

	private boolean exit = false;
	private Map<Integer, Object> args = new HashMap<>();

	//---- Constructor ----
	private ConsoleApplication() {}

	//---- Methods ----
	private void setup(final String[] args) {
		this.executionMode = args.length > 0 ? new OnceExecutionMode(args, this.args) : new LoopExecutionMode();
	}

	private void start() {
		if(executionMode == null) {
			throw new ConsoleApplicationException("Something went wrong. The application haven't been setup before execute it.");
		}
		executionMode.execute();
	}

	private void print(final String msg) {
		System.out.print(msg);
	}

	private void println(final String msg) {
		System.out.println(msg);
	}

	/**
	 * <p>Made with: {@linkplain https://www.askapache.com/online-tools/figlet-ascii/}</p>
	 */
	private void welcome() {
		String appName = ""
				+ " _____                               _____                _ _ _____           _        _                    \r\n"
				+ "|  __ \\                             |  __ \\              (_) |  __ \\         | |      | |                   \r\n"
				+ "| |__) | __ ___ _ __   __ _ _ __ ___| |__) |__ _ __   ___ _| | |__) | __ ___ | |_ ___ | |_ _   _ _ __   ___ \r\n"
				+ "|  ___/ '__/ _ \\ '_ \\ / _` | '__/ _ \\  ___/ _ \\ '_ \\ / __| | |  ___/ '__/ _ \\| __/ _ \\| __| | | | '_ \\ / _ \\\r\n"
				+ "| |   | | |  __/ |_) | (_| | | |  __/ |  |  __/ | | | (__| | | |   | | | (_) | || (_) | |_| |_| | |_) |  __/\r\n"
				+ "|_|   |_|  \\___| .__/ \\__,_|_|  \\___|_|   \\___|_| |_|\\___|_|_|_|   |_|  \\___/ \\__\\___/ \\__|\\__, | .__/ \\___|\r\n"
				+ "               | |                                                                          __/ | |         \r\n"
				+ "               |_|                                                                         |___/|_|         \r\n";

		String autor = "By Sergio Fernández";

		StringBuilder welcome = new StringBuilder();
		welcome.append(appName).append("\n");
		for(int i = 0; i < 11; ++i) {
			welcome.append("\t");
		}
		welcome.append(autor).append("\n\n");

		println(welcome.toString());
	}

	private void readFromConsole() {
		println("");
		print(">> ");

		try {
			String line = reader.readLine();
			String[] args = line.split(" ");

			executionMode.extractCommand(args, this.args);
		} catch(Exception ex) {
			throw new ConsoleApplicationException(ex.getMessage());
		}
	}

	private void extractArguments(final String[] args, final Map<Integer, Object> map) {
		int i = 0; // args[0] and usually args[1] will be discard
		while(i < args.length) {
			String arg = args[i];
			if(SCREEN_HEIGHT_ARG.equals(arg)) {
				map.put(SCREEN_HEIGHT, Integer.parseInt(args[i+1]));
				++i;
			} else if(SCREEN_WIDTH_ARG.equals(arg)) {
				map.put(SCREEN_WIDTH, Integer.parseInt(args[i+1]));
				++i;
			} else if(Resolutions.names().contains(arg.replace("-", ""))) {
				map.put(RESOLUTION, Resolutions.getResolutionByName(arg.replace("-", "")));
			} else if(NO_CENTER_HORIZONTALLY_ARG.equals(arg)) {
				map.put(NO_CENTER_HORIZONTALLY, Boolean.TRUE);
			} else if(NO_CENTER_VERTICALLY_ARG.equals(arg)) {
				map.put(NO_CENTER_VERTICALLY, Boolean.TRUE);
			} else if(NO_FULLSCREEN_MODE_ARG.equals(arg)) {
				map.put(NO_FULLSCREEN_MODE, Boolean.TRUE);
			} else if(KEEP_STYLES_ARG.equals(arg)) {
				map.put(NO_REMOVE_STYLES, Boolean.TRUE);
			}

			++i;
		}
	}

	private void performTask() {
		String command = (String) args.get(COMMAND);

		if(command == null) {
			throw new ConsoleApplicationException("There is no command");
		}

		if(command.equals(EXIT_COMMAND)) {
			exit();
		} if(command.equals(HELP_COMMAND)) {
			help();
		} else if(command.equals(PREPARE_COMMAND)) {
			preparePrototype();
		}
	}

	private void exit() {
		exit = true;
	}

	private void help() {
		println("How to use:");
		println("\n");
		println("- Commands:");
		println("\t* help: shows list of commands and possible arguments.");
		println("\t* prepare \"filepath\" ARGs: prepares a pencil project prototype.");
		println("\t* exit: ends the execution.");
		println("\n");
		println("- Possible arguments:");
		println("\t* " + SCREEN_WIDTH_ARG + " -> Specify the screen width in px where the prototype will be display.");
		println("\t* " + SCREEN_HEIGHT_ARG + " -> Specify the screen height in px where the prototype will be display.");
		println("\t* Screen resolution directly -> Possible values: -4K, -2K, -FHD, -HD");
		println("\t* " + NO_CENTER_HORIZONTALLY_ARG + " -> The pictures won't be center horizontally.");
		println("\t* " + NO_CENTER_VERTICALLY_ARG + " -> The pictures won't be center vertically.");
		println("\t* " + NO_FULLSCREEN_MODE_ARG + " -> The prototype won't be preprared for browser's fullscreen mode.");
		println("\t* " + KEEP_STYLES_ARG + " -> The styles applied to the pictures won't be removed.");
		println("\n");
		println("Examples:");
		println("- Directly from outside: java -jar *.jar \"filepath\" -HD " + KEEP_STYLES_ARG);
		println("- From inside: >> prepare \"filepath\" " + SCREEN_HEIGHT_ARG + " 500 " + SCREEN_WIDTH_ARG + " 450 " + NO_FULLSCREEN_MODE_ARG);
	}

	private void preparePrototype() {
		Configuration config = buildConfigurationFromArgs();
		Dimension dimension = getScreenResolutionFromArgs();

		Preparer preparer = new Preparer(dimension.getWidth(), dimension.getHeight(), config);
		String outputFile = preparer.prepare((String) args.get(FILE_PATH));
		println("Prototype prepared with success. Output file: \"" + outputFile + "\"");
	}

	private Configuration buildConfigurationFromArgs() {
		Configuration.Builder builder = new Configuration.Builder();

		if(args.get(NO_CENTER_HORIZONTALLY) != null && (Boolean) args.get(NO_CENTER_HORIZONTALLY)) {
			builder.noCenterHorizontally();
		}

		if(args.get(NO_CENTER_VERTICALLY) != null && (Boolean) args.get(NO_CENTER_VERTICALLY)) {
			builder.noCenterVertically();
		}

		if(args.get(NO_FULLSCREEN_MODE) != null && (Boolean) args.get(NO_FULLSCREEN_MODE)) {
			builder.noFullScreenMode();
		}

		if(args.get(NO_REMOVE_STYLES) != null && (Boolean) args.get(NO_REMOVE_STYLES)) {
			builder.noRemoveStyles();
		}

		return builder.build();
	}

	private Dimension getScreenResolutionFromArgs() {
		Resolutions resolution = (Resolutions) args.get(RESOLUTION);
		if(resolution != null) {
			return resolution.getDimension();
		}

		Resolutions defaultResolution = Resolutions.FHD;
		Integer width = (Integer) args.get(SCREEN_WIDTH);
		Integer height = (Integer) args.get(SCREEN_HEIGHT);
		if(width != null && height != null) {
			try {
				return new Dimension(width, height);
			} catch(IllegalArgumentException ex) {
				println(ex.getMessage());
				println(defaultResolution.getName() + " resolution is applied instead.");
			}
		}

		return defaultResolution.getDimension();
	}

	/* **************************************************
	 * 				Once Execution Mode
	 * **************************************************/
	private class OnceExecutionMode implements ExecutionMode {

		public OnceExecutionMode(final String[] args, final Map<Integer, Object> map) {
			extractCommand(args, map);
		}

		@Override
		public void execute() {
			performTask();
		}

		@Override
		public void extractCommand(final String[] args, final Map<Integer, Object> map) {
			map.clear();

			if(EXIT_COMMAND.equals(args[0])) {
				map.put(COMMAND, EXIT_COMMAND);
				return;
			} else if(HELP_COMMAND.equals(args[0])) {
				map.put(COMMAND, HELP_COMMAND);
				return;
			} else if(PREPARE_COMMAND.equals(args[0])) {
				map.put(COMMAND, PREPARE_COMMAND);
				map.put(FILE_PATH, args[1].replaceAll("\\\"", ""));
			} else {
				map.put(COMMAND, PREPARE_COMMAND);
				map.put(FILE_PATH, args[0].replaceAll("\\\"", ""));
			}

			extractArguments(args, map);
		}

	}


	/* **************************************************
	 * 				Loop Execution Mode
	 * **************************************************/
	private class LoopExecutionMode implements ExecutionMode {

		@Override
		public void execute() {
			welcome();
			while(!exit) {
				try {
					readFromConsole();
					performTask();
				} catch(Exception ex) {
					println(ex.getMessage());
				}
			}

		}

		@Override
		public void extractCommand(final String[] args, final Map<Integer, Object> map) {
			map.clear();

			if(EXIT_COMMAND.equals(args[0])) {
				map.put(COMMAND, EXIT_COMMAND);
				return;
			} else if(HELP_COMMAND.equals(args[0])) {
				map.put(COMMAND, HELP_COMMAND);
				return;
			} else if(PREPARE_COMMAND.equals(args[0]) && args.length > 1) {
				map.put(COMMAND, PREPARE_COMMAND);
				map.put(FILE_PATH, args[1].replaceAll("\\\"", ""));
			} else {
				throw new ConsoleApplicationException("Unrecognized comand. Use \"help\".");
			}

			extractArguments(args, map);
		}

	}


	//---- Main ----
	public static void main(final String[] args) throws IOException {
		ConsoleApplication app = new ConsoleApplication();
		try {
			app.setup(args);
			app.start();
		} catch(Exception ex) {
			app.println(ex.getMessage());
		} finally {
			app.reader.close();
		}
	}

}
