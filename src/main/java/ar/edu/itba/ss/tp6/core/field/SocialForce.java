
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
		protected final double drain;
		protected final Vector space;

		public SocialForce(final Configuration configuration) {
			this.cache = NeighbourCache.ofDeep(2)
					.space(configuration.getWidth(), configuration.getHeight())
					.interactionRadius(configuration.getRadius()[1] + configuration.getBreakRange())
					.build();
			this.A = configuration.getA();
			this.B = configuration.getB();
			this.space = Vector.of(
				configuration.getWidth(),
				configuration.getHeight()
			);
			this.drain = configuration.getDrain();
		}

		@Override
		public Vector apply(final List<T> state, final T body) {
			final double leftξ0 = leftξ0(body);
			final double rightξ0 = rightξ0(body);
			final double floorξ0 = floorξ0(body);
			return Vector.of(A * Math.exp(leftξ0/B), 0.0)
					.add(Vector.of(-A * Math.exp(rightξ0/B), 0.0))
					.add(Vector.of(0.0, A * Math.exp(floorξ0/B)))
					.add(cache.neighbours(state)
					.get(body)
					.stream()
					.map(p -> {
						final double ξ0 = -body.distance(p);
						return body.normal(p).multiplyBy(-A * Math.exp(ξ0/B));
					})
					.reduce(Vector.ZERO, (F1, F2) -> F1.add(F2)));
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

		protected double leftξ0(final T body) {
			return body.getRadius() - Math.abs(body.getX());
		}

		protected double rightξ0(final T body) {
			return body.getRadius() - Math.abs(space.getX() - body.getX());
		}

		protected double floorξ0(final T body) {
			final double base = 0.1 * space.getY();
			final double drain0 = 0.5 * (space.getX() - drain);
			if (drain0 < body.getX() && body.getX() < drain0 + drain)
				return Double.NEGATIVE_INFINITY;
			else if (body.getY() < (base - body.getRadius()))
				return Double.NEGATIVE_INFINITY;
			else return body.getRadius() - Math.abs(base - body.getY());
		}
	}
