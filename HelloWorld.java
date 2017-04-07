package war;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;



@Path("/rest")
public class HelloWorld {
	
	private static final String SOURCE_FOLDER = "C:/projects/conversion-service/conversions/source";
	private static final String TARGET_FOLDER = "C:/projects/conversion-service/conversions/target";

	
	@SuppressWarnings("resource")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/convert")
	public Response getImage(@Multipart(value = "sourceType") String sourceType, 
							 @Multipart(value = "targetType") String targetType,
							 MultipartBody multipartBody) throws IOException {

		List<Attachment> attachments = multipartBody.getAllAttachments();
		Attachment attachment = attachments.get(2); //the last attachment is the actual attached file
		MultivaluedMap<String, String> map = attachment.getHeaders();
		
		
		String[] contentDispositions = map.get("Content-Disposition").get(0).split(";");
		String sourceFileName = StringUtils.remove(contentDispositions[1].split("=")[1], "\"");
		System.out.println("File name is: " + sourceFileName);
		
		
		this.createWorkingFolders();
		 
		InputStream inputStream = attachment.getDataHandler().getInputStream();
		File sourceFile = new File(SOURCE_FOLDER, sourceFileName);
		FileUtils.copyInputStreamToFile(inputStream, sourceFile);
		System.out.println("File copied to " + sourceFile.getAbsolutePath());
		
		//calling conversion software-process builder example
		ProcessBuilder pb = new ProcessBuilder("convert","-density","label: newImageFormatisjpg",
						"output.jpg");   
				 pb.redirectErrorStream(true);   
				 try {   
				   Process p = pb.start();   
				   BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));   
				   String line = null;   
				   while((line=br.readLine())!=null){   
				       System.out.println(line);   
				   }   
				   System.out.println(p.waitFor());   
				  } catch(Exception e) { } 
				 //end of conversion software call
		
		ConversionInfo conversionInfo = new ConversionInfo();
		conversionInfo.setSourceFileName(sourceFile.getAbsolutePath());
		
		return Response.ok(conversionInfo).build();
	}
	
	//download converted file
	@GET
	@Path("/response")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response downloadFile(String targetFileName) {
	    File targetFile  = new File(TARGET_FOLDER, targetFileName);
	    ResponseBuilder response = Response.ok((Object) targetFile);
	    response.header("Content-Disposition", "attachment; filename=\"" + targetFile + "\"");
	    return response.build();
	}//end of download converted file
	
	private void createWorkingFolders() {
		File sourceFolder = new File(SOURCE_FOLDER);
		File targetFolder = new File(TARGET_FOLDER);	
		if (!sourceFolder.exists()) {
			sourceFolder.mkdirs(); //creates directory named by this abstract pathname
		}
		if (!targetFolder.exists()) {
			targetFolder.mkdirs();
		}
	}
	
 
  
}

