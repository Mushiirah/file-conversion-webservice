package war;

public class TestClient {
	
	/*public static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://localhost:8080/");
        FileBody bin = new FileBody(new File(TestClient.class.getResource("/project_summary.xml").getFile()));
        StringBody comment = new StringBody("Project summary.");
 
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("project", bin);
        reqEntity.addPart("comment", comment);
 
        httppost.setEntity(reqEntity);
 
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
 
        System.out.println("-");
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
            System.out.println("Response content type: " + resEntity.getContentType().getValue());
            System.out.println("Project Rank : " + MultipartResource.getProjectFromInputStream(resEntity.getContent()).getRank());
        }
        if (resEntity != null) {           
            resEntity.consumeContent();
        }
    }*/
	}


