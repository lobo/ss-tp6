
	package ar.edu.itba.ss.tp6;

	import java.io.IOException;

	import com.fasterxml.jackson.core.JsonParseException;
	import com.fasterxml.jackson.databind.JsonMappingException;

	import ar.edu.itba.ss.tp6.config.Configurator;
	import ar.edu.itba.ss.tp6.interfaces.Configuration;
	import ar.edu.itba.ss.tp6.interfaces.Mode;
	import ar.edu.itba.ss.tp5.support.ClassBuilder;

		/**
		* <p>Punto de entrada principal de la simulación. Se encarga de
		* configurar los parámetros de operación y de desplegar el
		* sistema requerido.</p>
		*/

	public final class Main {

		public static void main(final String [] arguments) {

			try {
				final Configuration config = Configurator.load().getConfiguration();
				System.out.println("A crowd simulation...");
				System.out.println(config);
				getMode(arguments[0]).run(config);
			}
			catch (final ClassNotFoundException exception) {
				System.out.println(
					"Modo desconocido. Lea el README.");
			}
			catch (final ArrayIndexOutOfBoundsException exception) {
				System.out.println(
					"Argumentos insuficientes. Especifique el modo.");
			}
			catch (final JsonParseException exception) {
				System.out.println(
					"La configuración no se encuentra en formato JSON.");
			}
			catch (final JsonMappingException exception) {
				System.out.println(
					"El mapeo de objetos de configuración es inválido.");
			}
			catch (final IOException exception) {
				System.out.println(
					"No se pudo leer la configuración.");
			}
		}

		protected static Mode getMode(final String mode)
				throws ClassNotFoundException {
			final String name = mode.substring(0, 1).toUpperCase() + mode.substring(1);
			return (Mode) ClassBuilder.getInstance("ar.edu.itba.ss.tp6.mode." + name);
		}
	}
