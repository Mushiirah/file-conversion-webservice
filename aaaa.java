// Convert TIF to PNG

package helloWorld;
import java.io.File;
import java.util.Scanner; 

public class aaaa {

	

	public static void main(String[] args) throws Exception {


			File pathToExecutable = new File( "C:/TEOTYS TOOLS/ImageMagick-7.0.5-Q16/convert.exe" ); // convert.exe replaces convert in the command "convert -verbose marbles.tif outtestfromjava.png"
			ProcessBuilder builder = new ProcessBuilder( pathToExecutable.getAbsolutePath(), "-verbose", "marbles.tif", "outtestfromjava.png");
			builder.directory( new File( "C:/Users/User/Desktop/Conversion" ).getAbsoluteFile() ); // this is where you set the root folder for the executable to run with
			builder.redirectErrorStream(true);
			Process process =  builder.start();

			Scanner s = new Scanner(process.getInputStream());
			StringBuilder text = new StringBuilder();
			while (s.hasNextLine()) {
			  text.append(s.nextLine());
			  text.append("\n");
			}
			s.close();

			int result = process.waitFor();

			System.out.printf( "Process exited with result %d and output %s%n", result, text );

			

	}

}
