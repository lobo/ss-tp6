
	package ar.edu.itba.ss.tp6.io;

	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Scanner;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp6.interfaces.Configuration;

	public class OvitoFile {

		public static boolean generate(final Configuration configuration) {
			final ar.edu.itba.ss.tp6.config.Configuration config
				= (ar.edu.itba.ss.tp6.config.Configuration) configuration;
			final String filename = config.getOutput();
			final int FPS = config.getFPS();
			final int N = config.getN();
			final double Δt = 1.0 / config.getSamplesPerSecond();
			final double playbackSpeed = config.getPlaybackSpeed();
			try (
				final Scanner properties = new Scanner(new File(filename + ".static"));
				final Scanner input = new Scanner(new File(filename + ".state"));
				final PrintWriter output = new PrintWriter(new FileWriter(filename + ".xyz"));
			) {
				final List<MassiveParticle> state = getState(properties);
				if (config.getSamplesPerSecond() < FPS)
					throw new IllegalArgumentException();
				final int jump = (int) (playbackSpeed / (Δt * FPS));
				int line = 0;
				int chunk = 0;
				while (input.hasNext()) {

					if (chunk % jump == 0) {
						// Header...
						if (line % N == 0) {
							output.println(N);
							output.println(chunk * Δt);
						}
						// Chunk...
						for (int i = 0; i < N; ++i) {
							output.write(getRow(state.get(i), input.nextLine()) + "\n");
						}
					}
					else {
						// Ignore...
						for (int i = 0; i < N; ++i) {
							input.nextLine();
						}
					}
					line += N;
					++chunk;
				}
				return true;
			}
			catch (final ArithmeticException exception) {
				System.out.println(
					"Se necesitan más muestras. Ajuste 'playbackSpeed' o simule con mayor SPS.");
			}
			catch (final IllegalArgumentException exception) {
				System.out.println(
					"Se necesitan más muestras. Verifique que FPS <= SPS.");
			}
			catch (final FileNotFoundException exception) {
				System.out.println(
					"El archivo de simulación no existe.");
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo de animación.");
			}
			return false;
		}

		protected static String getRow(
				final MassiveParticle state, final String line) {
			final String [] tokens = line.split("\\s");
			final double speed = Math.hypot(
					Double.parseDouble(tokens[2]),
					Double.parseDouble(tokens[3]));
			return new StringBuilder()
					.append(tokens[0])
					.append(" ")
					.append(tokens[1])
					.append(" ")
					.append(state.getRadius())
					.append(" ")
					.append(speed)
					.toString();
		}

		protected static List<MassiveParticle> getState(final Scanner properties) {
			final List<MassiveParticle> state = new ArrayList<>();
			while (properties.hasNext()) {
				final double radius = Double.parseDouble(properties.next());
				final double mass = Double.parseDouble(properties.next());
				state.add(new MassiveParticle(0.0, 0.0, radius, 0.0, 0.0, mass));
			}
			return state;
		}
	}
