
	package ar.edu.itba.ss.tp6.core.field;

	import java.util.List;

	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp5.core.GranularParticle;
	import ar.edu.itba.ss.tp5.core.NeighbourCache;
	import ar.edu.itba.ss.tp6.config.Configuration;

	public class DryFrictionForce<T extends GranularParticle>
		implements ForceField<T> {

		protected final NeighbourCache cache;
		protected final Vector space;
		protected final double drain;
		protected final double k;

		public DryFrictionForce(
				final Configuration configuration,
				final NeighbourCache cache) {
			this.space = Vector.of(
				configuration.getWidth(),
				configuration.getHeight()
			);
			this.cache = cache;
			this.drain = configuration.getDrain();
			this.k = configuration.getElasticTangent();
		}

		@Override
		public Vector apply(final List<T> state, final T body) {
			final double leftξ0 = body.leftξ0();
			final double rightξ0 = body.rightξ0(space.getX());
			final double floorξ0 = body.floorξ0(space, drain);
			return Vector.of(0.0, -k * leftξ0 * body.getVy())
					.add(Vector.of(0.0, -k * rightξ0 * body.getVy()))
					.add(Vector.of(-k * floorξ0 * body.getVx(), 0.0))
					.add(cache.neighbours(state)
						.get(body)
						.stream()
						.map(p -> {
							final GranularParticle gp = (GranularParticle) p;
							final Vector t = body.normal(p).tangent();
							final double ξ0 = body.ξ0(p);
							return t.multiplyBy(-k * ξ0 * body.relativeVelocity(gp).dot(t));
						})
						.reduce(Vector.ZERO, (F1, F2) -> F1.add(F2)));
		}

		@Override
		public boolean isVelocityDependent() {
			return true;
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
