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

import java.awt.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class ImageMagickFront {
	
	private static String CONVERT_COMMAND = "convert";
	private Runtime runtime;
	private static final String DEFAULT_FORMAT = "jpg";
	private static final String COMMAMD_SEPARATOR = "XXX_SEPARATOR_XXX";


	
	public enum Priority {
		CROP, RESIZE;
		
		public static Priority nonNullValueOf(String value) {
			try {
				return valueOf(value.toUpperCase());
			} catch (Exception e) {
				return RESIZE;
			}
		}
	}//end of enum priority

	//initialize the runtime
	public ImageMagickFront() {
		runtime = Runtime.getRuntime();
		ImageMagickConfig config = new ImageMagickConfig();
		if (config.getMagickConvertBin() != null)
			CONVERT_COMMAND = config.getMagickConvertBin();
	}

	/*
	 * Do convert from an inputstream to an outputstream
	 * @param in
	 * @param out
	 * @param name
	 * @param format
	 * @param size
	 * @param params
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void convert(InputStream in, OutputStream out, String name,
			String format, String size, String crop, Priority priority,
			String params1, String params2) throws IOException,
			InterruptedException {
		File temp = File.createTempFile("magick",
				"".equals(FilenameUtils.getExtension(name)) ? ".tmp" : "."
						+ FilenameUtils.getExtension(name));
		IOUtils.copy(in, new FileOutputStream(temp));
		IOUtils.copy(
				new FileInputStream(convert(temp, format, size, crop, priority,
						params1, params2)), out);
	}

	/*
	 * Convert a File to the specified format with the specified size
	 * @param input
	 * @param format
	 * @param size
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File convert(File input, String format, String size, String crop,
			Priority priority, String params1, String params2)
			throws IOException, InterruptedException {
		File output = createOutputFile(format);
		String cmd = generateCommand(input, output, format, size, crop,
				priority, params1, params2);
		Process p = runtime.exec(cmd.replace(COMMAMD_SEPARATOR, " "));
		int res = p.waitFor();
		if (res != 0) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(p.getErrorStream(), out);
		}
		return output;
	}

	/*
	 * Convert {@link FileItem} {@link List}
	 * @param items
	 * @param out
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void convert(List<FileItem> items, OutputStream out)
			throws IOException, InterruptedException {
		convert(getUploadedFileItem(items).getInputStream(), out,
				getFieldValue(items, "name"), getFieldValue(items, "format"),
				getFieldValue(items, "size"), getFieldValue(items, "crop"),
				Priority.nonNullValueOf(getFieldValue(items, "priority")),
				getFieldValue(items, "params1"),
				getFieldValue(items, "params1"));
	}
	
	private File createOutputFile(String format) throws IOException {
		if (format == null || "".equals(format))
			format = DEFAULT_FORMAT;
		return File.createTempFile("result", "-" + format);
	}

	/*
	 * Generate an Imagemagick command Line following the template:<br/>
	 * convert -define jpeg:size=SIZE PARAMS1 INPUT PARAMS2 OUTPUT
	 * 
	 * @param input
	 * @param output
	 * @param format
	 * @param size
	 * @param cmd
	 * @return
	 */
	private String generateCommand(File input, File output, String format,
			String size, Priority priority, String params1,
			String params2) {

		params1 = params1 != null ? params1 : "";
		params2 = params2 != null ? params2 : "";
		String resize = priority == Priority.RESIZE ? getSizeAsParam(size)
				+ COMMAMD_SEPARATOR 
				: " " + getSizeAsParam(size);
		return (CONVERT_COMMAND + COMMAMD_SEPARATOR + params1 + COMMAMD_SEPARATOR
				+ input.getAbsolutePath() + COMMAMD_SEPARATOR + resize
				+ COMMAMD_SEPARATOR + params2 + COMMAMD_SEPARATOR + output
					.getAbsolutePath()).trim();
	}

	private String getSizeAsParam(String size) {
		if (size == null || "".equals(size))
			return "";
		return "-resize" + COMMAMD_SEPARATOR + size;
	}

	
}
