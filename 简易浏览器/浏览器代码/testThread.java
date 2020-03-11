package Browser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class testThread extends Thread{
	String urlName= null;
    public testThread(String url){
    	urlName= url;
    }
	public  String sendGet(String url, String param){  //发送GET请求
		String result = "";
		String urlName = url;
		try{
			URL realUrl = new URL(urlName);
			//打开和URL之间的连接
			HttpURLConnection connect = (HttpURLConnection) realUrl.openConnection();
			//设置通用的请求属性
			connect.setRequestProperty("accept", "*/*");
			connect.setRequestProperty("connection", "Keep-Alive");
			connect.setRequestProperty("user-agent",
                    " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
			connect.setRequestProperty("cookie", "1234");
			//建立实际的连接
			connect.connect();


			if(url.contains(".png")||url.contains(".jpg")) {
				System.out.println("read file");
				String str[]= url.split("/");
				String fileName= str[str.length- 1];
				InputStream in= realUrl.openStream();
	            int line;
	            FileOutputStream fw= new FileOutputStream(new File(fileName));
	            while ((line = in.read()) !=-1) {
	                result += line;
	                fw.write(line);
	                //System.out.println(line);
	            }
	            fw.close();
	            System.out.println("over");		
	            
			}
			else {	            
				//System.out.println("read file");
				BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
	            String line;	            
	            while ((line = in.readLine()) !=null) {
		                result += line+"\r\n";   
	            }
	           // System.out.println("over");	
	            
	            FileWriter fw= new FileWriter("./Page.html");
	            fw.write(result);
	            fw.close();
	            System.out.println(result);
			}

            connect.disconnect();
            
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常" + e);
			e.printStackTrace();
		}

		return result;
		
		
	}
	public synchronized void run() {
		// TODO Auto-generated method stub
        sendGet(urlName, null);

	}
}
