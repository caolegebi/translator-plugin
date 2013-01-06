package google.translate;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which translates files
 *
 */
@Mojo( name = "i18n")
public class GoogleTranslatorMojo
    extends AbstractMojo
{
	@Parameter
    private List<String> inputPropertyFileLocations;
    
	@Parameter
    private List<String> inputJsFileLocations;
    
    @Parameter
    private String propertyFileName;
    
    @Parameter
    private String javascriptFileName;
    
    @Parameter
    private List<String> locales;
    

    public void execute()
        throws MojoExecutionException
    {
    	//localize property files
    	for(String inputPropertyFileLocation : inputPropertyFileLocations){
    		try {
				InputStream is = new FileInputStream(inputPropertyFileLocation + File.separator + propertyFileName + "_en.properties");
				Properties props = new Properties();
				props.load(is);
				is.close();
				for(String locale : locales){
					getLog().debug("starting translation for " + locale);
					Writer writer = new BufferedWriter(new FileWriter(inputPropertyFileLocation + File.separator + propertyFileName + "_"+locale +".properties"));
					Set<Entry<Object, Object>> entrySet = props.entrySet();
					for(Entry<Object, Object> entry : entrySet){
						String key = String.valueOf(entry.getKey());
						String value = String.valueOf(entry.getValue());
						if(value == null || value.length() == 0 || URLEncoder.encode(value, "UTF-8").length() == 0){
							getLog().debug(key);
						}
						else{
							getLog().debug("translating " + value);
							String translatedValue = Translate.translate(URLEncoder.encode(value, "UTF-8"), "en", locale);
							writer.write(key + "="+ translatedValue + "\n");
						}
					}
					writer.flush();
					writer.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	//localize js files
    	for(String inputJsFileLocation : inputJsFileLocations){
    		try {
    			BufferedReader reader = new BufferedReader(new FileReader(inputJsFileLocation + File.separator + "en" + File.separator + javascriptFileName + ".js"));
				List<String> inputFileContent = new ArrayList<String>();
				String line = null;
				while(( line = reader.readLine() ) != null){
					inputFileContent.add(line);
				}
				reader.close();
				Pattern keyValuePattern = Pattern.compile("[\"'](.*)[\"'\\s]:[\"'\\s]+(.*)[\"']");
				for(String locale : locales){
					getLog().debug("starting translation for " + locale);
					File file = new File(inputJsFileLocation + File.separator + locale + File.separator + javascriptFileName + ".js");
					file.getParentFile().mkdirs();
					Writer writer = new BufferedWriter(new FileWriter(file));
					for(String input : inputFileContent){
						Matcher m = keyValuePattern.matcher(input);
						if(m.find()){
							String key = m.group(1);
							String value = m.group(2);
							getLog().debug("translating " + value);
							String translatedValue = Translate.translate(URLEncoder.encode(value, "UTF-8"), "en", locale);
							input = input.replaceFirst(":([\"'\\s]+)"+value, ":$1"+translatedValue);
						}
						writer.write(input + "\n");
					}
					writer.flush();
					writer.close();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
