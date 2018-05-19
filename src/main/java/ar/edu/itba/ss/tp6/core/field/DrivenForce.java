
	package ar.edu.itba.ss.tp6.core.field;

	import java.util.List;

	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp5.core.GranularParticle;
	import ar.edu.itba.ss.tp6.config.Configuration;

	public class DrivenForce<T extends GranularParticle>
		implements ForceField<T> {

		protected final Vector target;
		protected final double desiredSpeed;
		protected final double τ;
		protected final double exit;

		public DrivenForce(final Configuration configuration) {
			this.target = configuration.getTarget();
			this.desiredSpeed = configuration.getDesiredSpeed();
			this.τ = configuration.getTau();
			this.exit = 0.1 * configuration.getHeight();
		}

		@Override
		public Vector apply(final List<T> state, final T body) {
			if (0.0 < body.getRadius()) {
				final Vector dynamicTarget = exit < body.getY()?
						target : Vector.of(body.getX(), -Math.abs(body.getY()) - 1.0);
				return dynamicTarget
						.subtract(Vector.of(body.getX(), body.getY()))
						.versor()
						.multiplyBy(desiredSpeed)
						.subtract(Vector.of(body.getVx(), body.getVy()))
						.multiplyBy(body.getMass()/τ);
			}
			else return Vector.ZERO;
		}

		@Override
		public boolean isVelocityDependent() {
			return true;
		}

		@Override
		public boolean isConservative() {
			return true;
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
