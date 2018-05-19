
	package ar.edu.itba.ss.tp6.core.field;

	import java.util.List;

	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp5.core.GranularParticle;
	import ar.edu.itba.ss.tp5.core.NeighbourCache;
	import ar.edu.itba.ss.tp5.core.ParticleAggregator;
	import ar.edu.itba.ss.tp6.config.Configuration;

	public class ContactForce<T extends GranularParticle>
		implements ForceField<T> {

		protected final ParticleAggregator aggregator;
		protected final NeighbourCache cache;
		protected final Vector space;
		protected final double drain;
		protected final double k;
		protected final double γs;
		protected final double γ;

		public ContactForce(
				final Configuration configuration,
				final NeighbourCache cache) {
			this.aggregator = ParticleAggregator.getInstance();
			this.space = Vector.of(
				configuration.getWidth(),
				configuration.getHeight()
			);
			this.cache = cache;
			this.drain = configuration.getDrain();
			this.k = configuration.getElasticNormal();
			this.γ = configuration.getViscousDamping();
			this.γs = configuration.getSiloDamping();
		}

		@Override
		public Vector apply(final List<T> state, final T body) {
			final double leftξ0 = body.leftξ0();
			final double rightξ0 = body.rightξ0(space.getX());
			final double floorξ0 = body.floorξ0(space, drain);
			final double leftξ1 = body.leftξ1(leftξ0);
			final double rightξ1 = body.rightξ1(rightξ0, space.getX());
			final double floorξ1 = body.floorξ1(floorξ0);
			final Vector contact = Vector.of(k * leftξ0 + γs * leftξ1, 0.0)
				.add(Vector.of(-k * rightξ0 - γs * rightξ1, 0.0))
				.add(Vector.of(0.0, k * floorξ0 + γs * floorξ1))
				.add(cache.neighbours(state)
						.get(body)
						.stream()
						.map(p -> {
							final double ξ0 = body.ξ0(p);
							final double ξ1 = body.ξ1(ξ0, (GranularParticle) p);
							return body.normal(p).multiplyBy(-k * ξ0 - γ * ξ1);
						})
						.reduce(Vector.ZERO, (F1, F2) -> F1.add(F2)));
			aggregator.update("pressure", contact.magnitude() / body.perimeter());
			return contact;
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
