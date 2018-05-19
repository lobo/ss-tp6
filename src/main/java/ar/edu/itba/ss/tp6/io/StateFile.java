
	package ar.edu.itba.ss.tp6.io;

	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.List;
	import java.util.Optional;

	import ar.edu.itba.ss.tp2.core.MobileParticle;
	import ar.edu.itba.ss.tp6.interfaces.Configuration;

	public class StateFile {

		protected final PrintWriter output;
		protected final boolean hasDrain;

		protected StateFile(final Configuration configuration)
				throws IOException {
			final ar.edu.itba.ss.tp6.config.Configuration config
				= (ar.edu.itba.ss.tp6.config.Configuration) configuration;
			this.output = new PrintWriter(
					new FileWriter(config.getOutput() + ".state"));
			this.hasDrain = 0.0 < config.getDrain();
		}

		public static Optional<StateFile> in(
				final Configuration configuration) {
			try {
				return Optional.of(new StateFile(configuration));
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo de simulación.");
				return Optional.empty();
			}
		}

		public <T extends MobileParticle> StateFile write(
				final double time, final List<T> state) {
			final int particles = state.size() - (hasDrain? 2 : 0);
			for (int i = 0; i < particles; ++i) {
				final T p = state.get(i);
				output.println(
					p.getX() + " " + p.getY() + " " +
					p.getVx() + " " + p.getVy());
			}
			return this;
		}

		public void close() {
			output.close();
		}
	}
