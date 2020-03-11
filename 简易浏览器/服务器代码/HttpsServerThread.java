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


public class HttpsServerThread implements Runnable{
    Socket socket= null;
	String root= "./HttpServer Data/Page";
	File rootDirect= new File("./HttpServer Data/Page");
	
    public HttpsServerThread(Socket soc){
    	this.socket= soc;
    	
    }

    public void pictureTrans(String req) throws IOException {  //传输图片
    	String filePath= root+ req;
		File file= new File(filePath);
//		InputStreamReader input= new InputStreamReader(new FileInputStream(file));
//		Scanner sc= new Scanner(input);
//		String st= null;
//		String web = null;
		OutputStream raw= new BufferedOutputStream(socket.getOutputStream());
		byte data[]= Files.readAllBytes(file.toPath());

		raw.write(data);
		raw.flush();	    

		socket.close();
    }
    public void sendHeader(Writer out, String response, String contentType, int length) throws IOException {  //发送头部
    	out.write(response+ "\r\n");
        Date date= new Date();
        out.write("Date:"+ date+ "\r\n");
    	out.write("Server: JHTTP 2.0\r\n");
    	out.write("Content-length:"+ length+ "\r\n");
    	out.write("Content-type:"+ contentType+ "\r\n\r\n");

    	out.flush();
    }
    public void htmlFile() throws IOException {  //接受客户端的连接并进行相应的返回
    	
 		byte[] buf = new byte[1024*1024];
		InputStream inn = socket.getInputStream();
		int byteRead = inn.read(buf, 0, 1024*1024);
		String  get = new String(buf, 0, byteRead);  //读取请求头
		System.out.println(get);   	
    	
    	OutputStream raw= new BufferedOutputStream(this.socket.getOutputStream());
    	Writer out= new OutputStreamWriter(raw);

    	String token[]= get.split("\\s+");
    	String method= token[0];
    	String version= "";
    	String firstLine= get.split("\n")[0];
    	if(firstLine.contains("png")||firstLine.contains("jpg")) {  //如果请求的是图片
    		pictureTrans(token[1]);
    		return;
    	}
    	    	
    	if("HEAD".equals(method)) {  //如果请求方法是HEAD
    		sendHeader(out, "HTTP/1.1 200 OK", "text/html", 0);
    	}
    	

    	if("GET".equals(method)||"POST".equals(method)) {  //如果请求方法是GET或POST
    		String fileName= token[1];

    		if(get.contains("cookie: ")) { 	//如果请求头中含有Cookie
    			String cook= get.substring(get.indexOf("cookie: ")).split("\r\n")[0];
    			File fl= new File("./cookie.txt");
    			if(!fl.exists()) {
    				fl.createNewFile();
    			}
    			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(fl)));
    			String rf= null;
    			boolean hasin= false;  //用来判断现在的Cookie是否在之前已经记录过了
    			while((rf= br.readLine())!= null) {
    				if(cook.equals(rf)) {
    					fileName+= "PostPage.html";
    					hasin= true;
    					break;
    				}
    			}
    			if(!hasin) {  //如果之前没有记录过该Cookie
    				FileWriter fw= new FileWriter("./cookie.txt", true);
    				fw.write(cook+"\r\n");
    				fw.close();
    			}
    		}    		
    		
    		if(fileName.endsWith("/")) {  //如果请求的文件是以/结尾则进行自动补充

    			if("POST".equals(method)) {
     				fileName+= "/PostPage.html";   		
     				
     				String st[]= get.split("\r\n");
     				int len= st.length;

     				FileWriter fw = new FileWriter("./cookie.txt", true);
     				fw.write("cookie: "+ st[len- 1]);
     				fw.close();
    			}
    			else
    				fileName+="MyPage.html";
    		}

    		String contentType= URLConnection.getFileNameMap().getContentTypeFor(fileName);  //判断文件的类型
    		File file= new File(rootDirect, fileName.substring(1, fileName.length()));
    		
    		if(file.canRead()) {
	     		byte[] data= Files.readAllBytes(file.toPath());
	    		
	    		if(token.length> 2)
	    			version= token[2];
	    		
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
