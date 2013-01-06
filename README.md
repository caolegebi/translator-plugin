**Google translator maven plugin**

This maven plugin takes properties file and javascript files and internationalizes them. Following is the configuration:

	<plugin>
		<groupId>google.translate</groupId>
		<artifactId>translator-plugin</artifactId>
		<version>1.0-SNAPSHOT</version>
		<executions>
			<execution>
				<phase>compile</phase>
				<goals>
					<goal>i18n</goal>
				</goals>
			</execution>
		</executions>
		<configuration>
			<inputPropertyFileLocations>
				<inputPropertyFileLocation>${basedir}/src/main/resources/META-INF/labels-indv-bundle</inputPropertyFileLocation>
			</inputPropertyFileLocations>
			<propertyFileName>messages</propertyFileName>
			<inputJsFileLocations>
				<inputPropertyFileLocation>${basedir}/src/main/webapp/js/locale</inputPropertyFileLocation>
			</inputJsFileLocations>
			<javascriptFileName>messages</javascriptFileName>
			<locales>
				<locale>es</locale>
				<locale>tl</locale>
			</locales>
		</configuration>
	</plugin>

Javascript files with the following notation will be parsed:

	var messages = {
		"Please enter stuff": "Please enter stuff"
	};