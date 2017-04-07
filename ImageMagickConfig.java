/**The MIT License (MIT)

Copyright (c) 2014 Max Planck Digital Library

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.**/

package war;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

public class ImageMagickConfig {
	
	public static final String SERVICE_NAME = "conversion-service";
	private static final String PROPERTIES_FILENAME = "conversion-service.properties";
	private Properties properties = new Properties();

	public ImageMagickConfig () {
		load();
	}

	public String getServiceUrl() {
		if (properties.containsKey("service.url"))
			return (String) properties.get("service.url");
		return "http://localhost:8080/" + SERVICE_NAME;
	}

	/**
	 * Return the home directory for imagemagick (important for windows)
	 * 
	 * @return
	 */
	public String getMagickConvertBin() {
		if (properties.containsKey("imagemagick.convert.bin"))
			return (String) properties.get("imagemagick.convert.bin");
		return null;
	}

	private void load() {
		if (getPropertyFileLocation() != null) {
			try {
				properties.load(new FileInputStream(new File(
						getPropertyFileLocation())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Return the location of the property file according to the server*/
	
	private String getPropertyFileLocation() {
		String loc = "";
		if (System.getProperty("jboss.server.config.dir") != null) {
			loc = System.getProperty("jboss.server.config.dir");
		} else if (System.getProperty("catalina.home") != null) {
			loc = System.getProperty("catalina.home") + "/conf";
		}
		return FilenameUtils.concat(loc, PROPERTIES_FILENAME);
	}

}
