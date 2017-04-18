package war;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;


@Path("/rest")
public class ConversionRestEndpoint {
	
	ConversionService conversionservice = new ConversionService();//ConversionService class instance
	
	//IMPORTANT: set this as required
	public static final String PARENT_DIRECTORY = "c:/projects/conversion-service/conversions";
	public static final String SOURCE_FOLDER =  PARENT_DIRECTORY + "/source";
	public static final String TARGET_FOLDER = PARENT_DIRECTORY + "/target";
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test")
	public Response testMe(ConversionInfo conversionInfo) {
		System.out.println("Test successful");
		return Response.ok(conversionInfo).build();
	}

	@POST
	@Path("/convert")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImage(@Multipart(value = "sourceType") String sourceType, 
							 @Multipart(value = "targetType") String targetType,
							 MultipartBody multipartBody) throws IOException, InterruptedException {

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
		
		File targetFile = conversionservice.convertFile(sourceFile, targetType);
		
		ConversionInfo conversionInfo = new ConversionInfo();
		conversionInfo.setSourceFileName(sourceFile.getAbsolutePath());
		conversionInfo.setTargetFilePath(targetFile.getAbsolutePath());
		conversionInfo.setTargetFileFormat(targetType);
		
		return Response.ok(conversionInfo).build();
		
	}
	
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

