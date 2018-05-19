
	package ar.edu.itba.ss.tp6.config;

	import ar.edu.itba.ss.tp4.core.Vector;

		/**
		* <p>Esta clase representa la estructura del archivo de configuración,
		* la cual será automáticamente completada durante el inicio del
		* sistema. La clase debe ser completamente mapeable con respecto al
		* archivo origen.</p>
		*/

	public class Configuration
		implements ar.edu.itba.ss.tp6.interfaces.Configuration {

		// Archivo de configuración origen:
		public static final String CONFIGURATION_FILENAME
			= "config.json";

		protected String output = "res/data/output";
		protected double delta = 0.00001;
		protected double time = 5.0;
		protected double playbackSpeed = 1.0;
		protected int fps = 50;
		protected int samplesPerSecond = 100;

		protected String integrator = "BeemanIntegrator";
		protected boolean reportEnergy = false;
		protected boolean reportTime = true;
		protected double [] radius = {0.01, 0.015};
		protected double mass = 0.01;

		protected double elasticNormal = 1.0E+3;
		protected double elasticTangent = 2.0E+3;
		protected double viscousDamping = 1.0E+2;
		protected double siloDamping = 20.0;

		protected long generator = System.nanoTime();
		protected int n = 100;
		protected double height = 1.0;
		protected double width = 0.25;
		protected double drain = 0.15;

		protected double window = 1.0;
		protected double flowRate = 0.1;

		protected double a = 2000.0;
		protected double b = 0.08;
		protected double tau = 0.5;
		protected double desiredSpeed = 0.8;
		protected double breakRange = 2.5;
		protected double [] target = {10.0, -10.0};

		public String getOutput() {
			return output;
		}

		public double getDelta() {
			return delta;
		}

		public double getTime() {
			return time;
		}

		public int getFPS() {
			return fps;
		}

		public double getPlaybackSpeed() {
			return playbackSpeed;
		}

		public int getSamplesPerSecond() {
			return samplesPerSecond;
		}

		public String getIntegrator() {
			return integrator;
		}

		public boolean getReportEnergy() {
			return reportEnergy;
		}

		public boolean getReportTime() {
			return reportTime;
		}

		public double [] getRadius() {
			return radius;
		}

		public double getMass() {
			return mass;
		}

		public double getElasticNormal() {
			return elasticNormal;
		}

		public double getElasticTangent() {
			return elasticTangent;
		}

		public double getViscousDamping() {
			return viscousDamping;
		}

		public double getSiloDamping() {
			return siloDamping;
		}

		public double getA() {
			return a;
		}

		public double getB() {
			return b;
		}

		public double getTau() {
			return tau;
		}

		public double getDesiredSpeed() {
			return desiredSpeed;
		}

		public double getBreakRange() {
			return breakRange;
		}

		public long getGenerator() {
			return generator;
		}

		public int getN() {
			return n;
		}

		public double getHeight() {
			return height;
		}

		public double getWidth() {
			return width;
		}

		public double getDrain() {
			return drain;
		}

		public double getWindow() {
			return window;
		}

		public double getFlowRate() {
			return flowRate;
		}

		public Vector getTarget() {
			return Vector.of(target[0], 0.1 * height + target[1]);
		}

		@Override
		public String toString() {
			return new StringBuilder(768)
					.append("\tOutput: " + output + "\n")
					.append("\tDelta: " + delta + " [s]\n")
					.append("\tTime: " + time + " [s]\n")
					.append("\tFPS: " + fps + "\n")
					.append("\tSamples per Second: " + samplesPerSecond + "\n")
					.append("\tPlayback Speed: x" + playbackSpeed + "\n")
					.append("\tIntegrator: " + integrator + "\n")
					.append("\tReport Energy?: " + reportEnergy + "\n")
					.append("\tReport Time?: " + reportTime + "\n")
					.append("\tRadius: (" + radius[0] + ", " + radius[1] + ") [m]\n")
					.append("\tMass: " + mass + " [kg]\n")
					.append("\tElastic (normal): " + elasticNormal + " [N/m]\n")
					.append("\tElastic (tangent): " + elasticTangent + " [N/m]\n")
					.append("\tFluid Gamma: " + viscousDamping + " [kg/s]\n")
					.append("\tSilo Fluid Gamma: " + siloDamping + " [kg/s]\n")
					.append("\tA: " + a + " [N]\n")
					.append("\tB: " + b + " [m]\n")
					.append("\tTau: " + tau + " [s]\n")
					.append("\tDesired Speed: " + desiredSpeed + " [m/s]\n")
					.append("\tSocial Force Range: " + breakRange + " [m]\n")
					.append("\tTarget: (" + getTarget().getX() + ", " + target[1] + ") [m]\n")
					.append("\tGenerator Seed: " + generator + "\n")
					.append("\tN: " + n + " particles\n")
					.append("\tHeight: " + height + " [m]\n")
					.append("\tWidth: " + width + " [m]\n")
					.append("\tDrain Gate: " + drain + " [m]\n")
					.append("\tFlow Window: " + window + " [s]\n")
					.append("\tFlow Rate Sliding: " + flowRate + " [s]\n")
					.toString();
		}
	}
