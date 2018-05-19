
	package ar.edu.itba.ss.tp6.core.field;

	import java.util.List;

	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp5.core.GranularParticle;
	import ar.edu.itba.ss.tp5.core.NeighbourCache;
	import ar.edu.itba.ss.tp6.config.Configuration;

	public class SocialForce<T extends GranularParticle>
		implements ForceField<T> {

		protected final NeighbourCache cache;
		protected final double A;
		protected final double B;

		public SocialForce(final Configuration configuration) {
			this.cache = NeighbourCache.ofDeep(2)
					.space(configuration.getWidth(), configuration.getHeight())
					.interactionRadius(configuration.getRadius()[1] + configuration.getBreakRange())
					.build();
			this.A = configuration.getA();
			this.B = configuration.getB();
		}

		@Override
		public Vector apply(final List<T> state, final T body) {
			// Contra las paredes también?
			return cache.neighbours(state)
					.get(body)
					.stream()
					.map(p -> {
						final double ξ0 = -body.distance(p);
						return body.normal(p).multiplyBy(-A * Math.exp(ξ0/B));
					})
					.reduce(Vector.ZERO, (F1, F2) -> F1.add(F2));
		}

		@Override
		public boolean isVelocityDependent() {
			return false;
		}

		@Override
		public boolean isConservative() {
			return false;
		}

		@Override
		public Vector derivative1(final List<T> state, final T body) {
			return Vector.ZERO;
		}

		@Override
		public Vector derivative2(final List<T> state, final T body) {
			return Vector.ZERO;
		}

		@Override
		public Vector derivative3(final List<T> state, final T body) {
			return Vector.ZERO;
		}

		@Override
		public double energyLoss(final double time) {
			return 0.0;
		}

		@Override
		public double potentialEnergy(final T body) {
			return 0.0;
		}
	}
