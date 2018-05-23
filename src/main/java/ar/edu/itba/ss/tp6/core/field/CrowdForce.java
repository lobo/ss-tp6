
	package ar.edu.itba.ss.tp6.core.field;

	import java.util.List;

	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp5.core.GranularParticle;
	import ar.edu.itba.ss.tp5.core.NeighbourCache;
	import ar.edu.itba.ss.tp5.core.ParticleAggregator;
	import ar.edu.itba.ss.tp6.core.field.ContactForce;
	import ar.edu.itba.ss.tp6.core.field.DryFrictionForce;
	import ar.edu.itba.ss.tp6.config.Configuration;

	public class CrowdForce<T extends GranularParticle>
		implements ForceField<T> {

		protected final ParticleAggregator aggregator;
		protected final NeighbourCache cache;
		protected final DrivenForce<T> driven;
		protected final ContactForce<T> contact;
		protected final DryFrictionForce<T> friction;
		protected final SocialForce<T> social;

		public CrowdForce(final Configuration configuration) {
			this.aggregator = ParticleAggregator.getInstance();
			this.cache = NeighbourCache.ofDeep(2)
				.space(configuration.getWidth(), configuration.getHeight())
				.interactionRadius(configuration.getRadius()[1])
				.build();
			this.social = new SocialForce<>(configuration);
			this.driven = new DrivenForce<>(configuration);
			this.contact = new ContactForce<>(configuration, cache);
			this.friction = new DryFrictionForce<>(configuration, cache);
		}

		@Override
		public Vector apply(final List<T> state, final T body) {
			final Vector socialForce = social.apply(state, body);
			final Vector contactForce = contact.apply(state, body);
			aggregator.update("pressure",
					(socialForce.add(contactForce).magnitude())
					/ body.perimeter());
			return driven.apply(state, body)
					.add(friction.apply(state, body))
					.add(socialForce)
					.add(contactForce);
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
		public Vector derivative1(
				final List<T> state,
				final T body) {
			return driven.derivative1(state, body)
					.add(social.derivative1(state, body))
					.add(contact.derivative1(state, body))
					.add(friction.derivative1(state, body));
		}

		@Override
		public Vector derivative2(
				final List<T> state,
				final T body) {
			return driven.derivative2(state, body)
					.add(social.derivative2(state, body))
					.add(contact.derivative2(state, body))
					.add(friction.derivative2(state, body));
		}

		@Override
		public Vector derivative3(
				final List<T> state,
				final T body) {
			return driven.derivative3(state, body)
					.add(social.derivative3(state, body))
					.add(contact.derivative3(state, body))
					.add(friction.derivative3(state, body));
		}

		@Override
		public double energyLoss(final double time) {
			return driven.energyLoss(time) +
					social.energyLoss(time) +
					contact.energyLoss(time) +
					friction.energyLoss(time);
		}

		@Override
		public double potentialEnergy(final T body) {
			return driven.potentialEnergy(body) +
					social.potentialEnergy(body) +
					contact.potentialEnergy(body) +
					friction.potentialEnergy(body);
		}
	}
