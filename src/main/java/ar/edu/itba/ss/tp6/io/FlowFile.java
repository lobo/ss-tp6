
	package ar.edu.itba.ss.tp6.io;

	import static java.util.stream.Collectors.groupingBy;

	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.nio.file.Files;
	import java.nio.file.Paths;
	import java.util.Stack;

	import ar.edu.itba.ss.tp6.interfaces.Configuration;

	public class FlowFile {
		public static boolean generate(final Configuration configuration) {
			final ar.edu.itba.ss.tp6.config.Configuration config
				= (ar.edu.itba.ss.tp6.config.Configuration) configuration;
			final String filename = config.getOutput() + ".drain";
			final double maxTime = config.getTime();
			final double window = config.getFlowRate();
			try (final PrintWriter output = new PrintWriter(
					new FileWriter(config.getOutput() + ".flow"))) {
				final Stack<Integer> lastStep = new Stack<>();
				lastStep.push(0);
				Files.lines(Paths.get(filename))
					.map(line -> Double.parseDouble(line.split("\\s")[0]))
					.collect(groupingBy(time -> (int) Math.ceil((time/window))))
					.entrySet()
					.stream()
					.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
					.forEachOrdered(entry -> {
						final int step = entry.getKey();
						final double time = step * window;
						for (int i = lastStep.pop(); i < step; ++i) {
							output.write((i * window) + " " + 0 + "\n");
						}
						output.write(time + " " +
								(entry.getValue().size()/window) + "\n");
						lastStep.push(step + 1);
					});
				final int step = (int) Math.ceil((maxTime/window));
				for (int i = lastStep.pop(); i <= step; ++i) {
					output.write((i * window) + " " + 0 + "\n");
				}
				return true;
			}
			catch (final FileNotFoundException exception) {
				System.out.println(
					"El archivo de drenaje no existe.");
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo de flujo.");
			}
			return false;
		}
	}
