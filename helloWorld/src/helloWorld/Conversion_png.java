package helloWorld;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Conversion_png 
{
	public static void main(String[] args) throws Exception
	{
		
	ProcessBuilder pb = new ProcessBuilder(
			
			"C:/TEOTYS_TOOLS/ImageMagick-7.0.5-Q16/convert.exe",// Change directory according to your computer
			"-verbose",
			"C:/Users/User/Desktop/Conversion/stonehenge.png",// Change directory according to your computer
			"C:/Users/User/Desktop/Conversion/outputtest.jpg");// Change directory according to your computer
	
		Process p1;
	    try {
	        p1 = pb.start();
	         BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream()));
	            String line = null;
	            while((line=br.readLine())!=null){
	                System.out.println(line);
	            }
	            System.out.println("2"+p1.waitFor());
	
	    } catch (Exception e) {
	        
	        e.printStackTrace();
	    }
		
	}// end of method
	
	
}// end class
