
	package ar.edu.itba.ss.tp6.io;

	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.Arrays;
	import java.util.List;
	import java.util.Optional;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp5.core.ParticleAggregator;
	import ar.edu.itba.ss.tp6.config.Configuration;

	public class DrainFile {

		protected final ParticleAggregator aggregator;
		protected final PrintWriter output;
		protected final boolean hasDrain;
		protected final double base;

		protected DrainFile(final ar.edu.itba.ss.tp6.interfaces.Configuration configuration)
				throws IOException {
			final Configuration config = (Configuration) configuration;
			this.aggregator = ParticleAggregator.getInstance();
			this.aggregator.addAggregation("drain", config.getN());
			this.output = new PrintWriter(
					new FileWriter(config.getOutput() + ".drain"));
			this.hasDrain = 0.0 < config.getDrain();
			this.base = 0.1 * config.getHeight();
			Arrays.fill(this.aggregator.getAggregation("drain"), base + 1.0);
		}

		public static Optional<DrainFile> in(
				final ar.edu.itba.ss.tp6.interfaces.Configuration configuration) {
			try {
				return Optional.of(new DrainFile(configuration));
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo crear el archivo de flujo.");
				return Optional.empty();
			}
		}

		public <T extends MassiveParticle> DrainFile write(
				final double time, final List<T> state) {
			final int particles = state.size() - (hasDrain? 2 : 0);
			final double [] flows = aggregator.getAggregation("drain");
			for (int i = 0; i < particles; ++i) {
				final T p = state.get(i);
				if (flows[i] < base) {}
				else if (p.getY() < base) {
					output.println(time + " " + i);
					flows[i] = p.getY();
				}
			}
			return this;
		}

		public void close() {
			output.close();
		}
	}
