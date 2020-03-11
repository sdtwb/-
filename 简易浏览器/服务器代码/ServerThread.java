package HttpAndHttps;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.Scanner;

public class ServerThread implements Runnable{
    Socket socket= null;
	String root= "./HttpServer Data/Page";
	File rootDirect= new File("./HttpServer Data/Page");
	String cookie= null;
	
    public ServerThread(Socket soc){
    	this.socket= soc;
    	
    }

    public void pictureTrans(String req) throws IOException {  //����ͼƬ
    	String filePath= root+ req;
		File file= new File(filePath);
	    //System.out.println("has in picture  "+filePath);
		InputStreamReader input= new InputStreamReader(new FileInputStream(file));
		Scanner sc= new Scanner(input);
		String st= null;
		String web = null;
		OutputStream raw= new BufferedOutputStream(socket.getOutputStream());
		
		Writer bw= new OutputStreamWriter(raw);
		byte data[]= Files.readAllBytes(file.toPath());
		
		web= "HTTP/1.1 200 ok \r\n"
		+"Content-Type: image/png \r\n"
		+"Content-Length:" + file.length() + "\r\n\r\n";
        //�������ȥ������
        
		bw.write(web);
	    bw.flush();

		raw.write(data);
		raw.flush();	    
	
		socket.close();
    }

    public void sendHeader(Writer out, String response, String contentType, int length) throws IOException {  //����ͷ��
    	out.write(response+ "\r\n");
        Date date= new Date();
        out.write("Date:"+ date+ "\r\n");
    	out.write("Server: JHTTP 2.0\r\n");
    	out.write("Content-length:"+ length+ "\r\n");
     	if(cookie!= null) {
    		out.write("Set-Cookie: "+ cookie+ "\r\n");
    		cookie= null;
    	}   	
    	out.write("Content-type:"+ contentType+ "\r\n\r\n");


    	out.flush();
    }
    public void htmlFile() throws IOException {  //���ܿͻ��˵����Ӳ�������Ӧ�ķ���
    	
 		byte[] buf = new byte[1024*1024];
		InputStream inn = socket.getInputStream();
		int byteRead = inn.read(buf, 0, 1024*1024);
		if(byteRead< 0)
			return;
		
		String  get = new String(buf, 0, byteRead);
		System.out.println(get);   	
   	
    	OutputStream raw= new BufferedOutputStream(this.socket.getOutputStream());
    	Writer out= new OutputStreamWriter(raw);

    	String token[]= get.split("\\s+");
    	String method= token[0];
    	String version= "";
   	
    	if("HEAD".equals(method)) {  //�����HEAD����
    		sendHeader(out, "HTTP/1.1 200 OK", "text/html", 0);
    		return;
    	}
    	

    	if("GET".equals(method)||"POST".equals(method)) {  //�����GET��POST����
    		String fileName= token[1];
    		
			if("POST".equals(method)) {  //�����POST����
 				String st[]= get.split("\r\n");
 				int len= st.length;
	
 				cookie= st[len- 1];
 				FileWriter fw = new FileWriter("./cookie.txt", true);
 				fw.write("cookie: "+ cookie);
 				fw.close();
 				pictureTrans(token[1]);
 				return;
			}
			
    		if(get.contains("cookie: ")) { 	//�������ͷ�а���cookie����
    			String cook= fileName+ " "+ get.substring(get.indexOf("cookie: ")).split("\r\n")[0];  //��ȡ���е�Cookie
    			
    			File fl= new File("./cookie.txt");
    			if(!fl.exists()) {
    				fl.createNewFile();
    			}
    			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(fl)));
    			String rf= null;
    			boolean hasin= false;  //�����ж����ڵ�Cookie�Ƿ���֮ǰ�Ѿ���¼����
    			while((rf= br.readLine())!= null) {
    				if(cook.equals(rf)) {

    					hasin= true;
    					break;
    				}
    			}
    			if(!hasin) {  //���֮ǰû�м�¼����Cookie����д��Cookie�ļ���
    				FileWriter fw= new FileWriter("./cookie.txt", true);
    				fw.write(cook+"\r\n");
    				fw.close();
    			}
    		}  
    		else if(fileName.contains(".jpg")||fileName.contains(".png")) {
    			fileName= "/PostPage.html";
    		}
    		
    		if(fileName.endsWith("/"))
    			fileName+= "MyPage.html";

    		String contentType= URLConnection.getFileNameMap().getContentTypeFor(fileName);  //�ж��ļ�������
    		File file= new File(rootDirect, fileName.substring(1, fileName.length()));
    		
    		if(file.canRead()) {
	     		byte[] data= Files.readAllBytes(file.toPath());
	    		
	    		if(token.length> 2)
	    			version= token[2];
	    		if(version.startsWith("HTTP/")) {
	    			sendHeader(out, "HTTP/1.1 200 OK", contentType, data.length);
	    		}

		     	raw.write(data);
		    	raw.flush();    			
    		}
    		else {
        		String body= new StringBuffer("<HTML>\r\n")
        				.append("<HEAD><TITLE> Not Implemented</TITLE>\r\n")
        				.append("</HEAD>\r\n")
        				.append("<BODY>")
        				.append("<H1>HTTP Error 404</H1>\r\n")
        				.append("</BODY></HTML>\r\n").toString();
        		if(version.startsWith("HTTP/")){
        		     sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset= utf-8", body.length());
        		}
        		out.write(body);
        		out.flush();   			
    		}
  		
    	}
    	else {
    		String body= new StringBuffer("<HTML>\r\n")
    				.append("<HEAD><TITLE> Not Implemented</TITLE>\r\n")
    				.append("</HEAD>\r\n")
    				.append("<BODY>")
    				.append("<H1>HTTP Error 501</H1>\r\n")
    				.append("</BODY></HTML>\r\n").toString();
    		if(version.startsWith("HTTP/")){
    		     sendHeader(out, "HTTP/1.0 501 Not Implemented", "text/html; charset= utf-8", body.length());
    		}
    		out.write(body);
    		out.flush();
    	}
    	
    	socket.close();

    
    }

	public synchronized void run() {
		
		try {
			htmlFile();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
