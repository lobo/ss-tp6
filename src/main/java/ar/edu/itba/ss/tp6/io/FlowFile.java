
	package ar.edu.itba.ss.tp6.io;

	import static java.util.stream.Collectors.toList;

	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.nio.file.Files;
	import java.nio.file.Paths;
	import java.util.List;
	import java.util.stream.IntStream;

	import ar.edu.itba.ss.tp6.interfaces.Configuration;

	public class FlowFile {
		public static boolean generate(final Configuration configuration) {
			final ar.edu.itba.ss.tp6.config.Configuration config
				= (ar.edu.itba.ss.tp6.config.Configuration) configuration;
			final String filename = config.getOutput() + ".drain";
			final double maxTime = config.getTime();
			try (final PrintWriter output = new PrintWriter(
					new FileWriter(config.getOutput() + ".flow"))) {
				final double window = config.getWindow();
				final double rate = config.getFlowRate();
				final List<Double> events = Files.lines(Paths.get(filename))
					.map(line -> Double.parseDouble(line.split("\\s")[0]))
					.collect(toList());
				IntStream.rangeClosed(0, (int) Math.ceil((maxTime - window)/rate))
					.forEachOrdered(k -> {
						final double Δ = k * rate;
						final double time = window + Δ;
						final long discharges = events.stream()
							.filter(t -> Δ <= t && t < time)
							.mapToDouble(t -> t)
							.count();
						output.write(time + " " + (discharges/window) + "\n");
					});
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
