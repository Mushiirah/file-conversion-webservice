package war;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;


public class ConversionService {
	
	//TODO: Customise this according to your laptop
	private static final String CONVERTER_EXECUTABLE = "C:/tools/ImageMagick-7.0.5-Q16/convert.exe"; 
			
	
	public File convertFile (File sourceFile, String targetType) throws IOException, InterruptedException {
		
		File pathToExecutable = new File(CONVERTER_EXECUTABLE); // convert.exe replaces convert in the command "convert -verbose marbles.tif outtestfromjava.png"
		
		String sourceAbsolutePath = sourceFile.getAbsolutePath();
		String targetAbsolutePath = ConversionRestEndpoint.TARGET_FOLDER + "/" + FilenameUtils.getBaseName(sourceFile.getName() + "." + targetType);
		
		System.out.println("Converting " + sourceAbsolutePath + " to " + targetAbsolutePath);
		
		ProcessBuilder builder = new ProcessBuilder( pathToExecutable.getAbsolutePath(), "-verbose", sourceAbsolutePath, targetAbsolutePath);
		builder.directory( new File(ConversionRestEndpoint.TARGET_FOLDER).getAbsoluteFile() ); // this is where you set the root folder for the executable to run with
		builder.redirectErrorStream(true);
		Process process =  builder.start();

		Scanner s = new Scanner(process.getInputStream());
		StringBuilder text = new StringBuilder();
		while (s.hasNextLine()) 
		{
		  text.append(s.nextLine());
		  text.append("\n");
		}// end while
		s.close();

		int result = process.waitFor();

		System.out.printf( "Process exited with result %d and output %s%n", result, text );
		return new File(targetAbsolutePath);
		

	}// end of method

}
