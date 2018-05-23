
	package ar.edu.itba.ss.tp6.mode;

	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.List;

	import ar.edu.itba.ss.tp4.core.MassiveParticleFactory;
	import ar.edu.itba.ss.tp4.core.TimeDrivenSimulation;
	import ar.edu.itba.ss.tp4.integrators.BeemanIntegrator;
	import ar.edu.itba.ss.tp4.integrators.GearIntegrator;
	import ar.edu.itba.ss.tp4.integrators.VelocityVerlet;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;
	import ar.edu.itba.ss.tp5.core.Generator;
	import ar.edu.itba.ss.tp5.core.GranularParticle;
	import ar.edu.itba.ss.tp5.core.GranularParticleFactory;
	import ar.edu.itba.ss.tp5.core.ParticleAggregator;
	import ar.edu.itba.ss.tp6.core.field.CrowdForce;
	import ar.edu.itba.ss.tp6.interfaces.Configuration;
	import ar.edu.itba.ss.tp6.interfaces.Mode;
	import ar.edu.itba.ss.tp6.io.DrainFile;
	import ar.edu.itba.ss.tp6.io.StateFile;
	import ar.edu.itba.ss.tp6.io.StaticFile;

	public class Simulate implements Mode {

		protected final ParticleAggregator aggregator
			= ParticleAggregator.getInstance();

		protected PrintWriter pressure;

		@Override
		public void run(final Configuration configuration) {
			System.out.println("Simulation...");
			StateFile.in(configuration)
				.ifPresent(output -> {
					DrainFile.in(configuration).ifPresent(flow -> {
						try {
							final ar.edu.itba.ss.tp6.config.Configuration config
								= (ar.edu.itba.ss.tp6.config.Configuration) configuration;
							final double Δt = config.getDelta();
							final double Δs = 1.0 / config.getSamplesPerSecond();
							final long Δ = Δs < Δt? 1 : Math.round(Δs/Δt);
							this.pressure = new PrintWriter(
									new FileWriter(config.getOutput() + ".pressure"));
							TimeDrivenSimulation.of(
									buildIntegrator(
										config,
										new GranularParticleFactory<GranularParticle>(),
										new CrowdForce<GranularParticle>(config),
										Generator.with(config.getGenerator(), config.getN())
											.in(config.getWidth(), config.getHeight())
											.withDrain(config.getDrain())
											.mass(config.getMass())
											.minRadius(config.getRadius()[0])
											.maxRadius(config.getRadius()[1])
												.build()
											.getParticles()))
								.reportEnergy(config.getReportEnergy())
								.reportTime(config.getReportTime())
								.maxTime(config.getTime())
								.by(Δt)
								.spy((time, state) -> {
									if (Math.round(time/Δt) % Δ == 0) {
										output.write(time, state);
										flow.write(time, state);
										savePressure(state);
									}
								})
								.build()
								.run();
							this.pressure.close();
							System.out.println(
									"\tLas presiones se almacenaron con éxito.");
						}
						catch (final ClassNotFoundException exception) {
							System.out.println(
								"El integrador es desconocido. Especifique otro.");
						}
						catch (final IOException exception) {
							System.out.println(
								"No se puede generar el archivo de presiones.");
						}
						System.out.println(
							"\tLos eventos de salida se almacenaron con éxito.");
						flow.close();
					});
					System.out.println(
						"\tLa simulación fue almacenada con éxito.");
					output.close();
				});
		}

		protected <T extends GranularParticle> void savePressure(final List<T> state) {
			final int particles = state.size() - 2; // Siempre hay drenaje!
			final double [] pressures = aggregator.getAggregation("pressure");
			for (int i = 0; i < particles; ++i) {
				pressure.println(pressures[i]);
			}
		}

		protected static <T extends GranularParticle> Integrator<T> buildIntegrator(
				final ar.edu.itba.ss.tp6.config.Configuration configuration,
				final MassiveParticleFactory<T> factory,
				final ForceField<T> force,
				final List<T> state)
					throws ClassNotFoundException {

			StaticFile.in(configuration)
				.ifPresent(output -> {
					output.write(state);
					output.close();
					System.out.println(
						"\tLas propiedades estáticas se almacenaron con éxito.");
				});

			switch (configuration.getIntegrator()) {
				case "VelocityVerlet" : {
					return VelocityVerlet.of(force)
							.withInitial(state)
							.factory(factory)
							.build();
				}
				case "BeemanIntegrator" : {
					return BeemanIntegrator.of(force)
							.withInitial(state)
							.factory(factory)
							.build();
				}
				case "GearIntegrator" : {
					return GearIntegrator.of(force)
							.Δt(configuration.getDelta())
							.withInitial(state)
							.factory(factory)
							.build();
				}
				default : {
					throw new ClassNotFoundException();
				}
			}
		}
	}
