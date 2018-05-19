
	package ar.edu.itba.ss.tp6.io;

	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.List;
	import java.util.Optional;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp6.interfaces.Configuration;

	public class StaticFile {

		protected final PrintWriter output;
		protected final boolean hasDrain;

		protected StaticFile(final Configuration configuration)
				throws IOException {
			final ar.edu.itba.ss.tp6.config.Configuration config
				= (ar.edu.itba.ss.tp6.config.Configuration) configuration;
			this.output = new PrintWriter(
					new FileWriter(config.getOutput() + ".static"));
			this.hasDrain = 0.0 < config.getDrain();
		}

		public static Optional<StaticFile> in(
				final Configuration configuration) {
			try {
				return Optional.of(new StaticFile(configuration));
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo estático.");
				return Optional.empty();
			}
		}

		public <T extends MassiveParticle> StaticFile write(final List<T> state) {
			final int particles = state.size() - (hasDrain? 2 : 0);
			for (int i = 0; i < particles; ++i) {
				final T p = state.get(i);
				output.println(p.getRadius() + " " + p.getMass());
			}
			return this;
		}

		public void close() {
			output.close();
		}
	}
