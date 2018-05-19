
	package ar.edu.itba.ss.tp6.config;

	import java.io.File;
	import java.io.IOException;

	import com.fasterxml.jackson.core.JsonParseException;
	import com.fasterxml.jackson.databind.JsonMappingException;
	import com.fasterxml.jackson.databind.ObjectMapper;

		/**
		* <p>Se encarga de obtener la configuración global en formato
		* <i>JSON</i>, y de almacenarla en un objeto accesible.</p>
		*/

	public final class Configurator {

		// Configuración global:
		protected Configuration configuration
			= new Configuration();

		// Parser de JSON:
		protected static final ObjectMapper mapper
			= new ObjectMapper();

		protected Configurator(final Configuration configuration) {
			this.configuration = configuration;
		}

		/**
		* <p>Devuelve la configuración actual.</p>
		*
		* @return Un objeto que contiene la configuración actual.
		*/
		public Configuration getConfiguration() {
			return configuration;
		}

		/**
		* <p>Se encarga de leer la configuracón, de parsear la misma, y de
		* generar un objeto válido que la contenga, el cual será accesible de
		* forma global por toda la aplicación.</p>
		*
		* @throws JsonParseException
		*	En caso de que el archivo de configuración esté malformado, es
		*	decir, que no se encuentre en formato <i>JSON</i>.
		* @throws JsonMappingException
		*	Si durante el parsing de la configuración se detecta una propiedad
		*	para la cual no existe un mapeo válido en el objeto contenedor.
		* @throws IOException
		*	Si hubo un error durante la lectura del archivo de configuración.
		*/
		public static Configurator load()
				throws JsonParseException,
					JsonMappingException,
					IOException {

			final File file = new File(Configuration.CONFIGURATION_FILENAME);
			return new Configurator(mapper.readValue(file, Configuration.class));
		}
	}
