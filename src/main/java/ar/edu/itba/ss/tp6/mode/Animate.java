
	package ar.edu.itba.ss.tp6.mode;

	import ar.edu.itba.ss.tp6.interfaces.Configuration;
	import ar.edu.itba.ss.tp6.interfaces.Mode;
	import ar.edu.itba.ss.tp6.io.OvitoFile;

	public class Animate implements Mode {

		@Override
		public void run(final Configuration configuration) {
			System.out.println("Animation...");
			if (OvitoFile.generate(configuration)) {
				System.out.println(
					"\tLa animación fue generada con éxito.");
			}
		}
	}
