package Browser;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.mail.util.MailSSLSocketFactory;

public class Function implements ActionListener{

//--------------------------------------------------log
	static String logRoot= "./Cache/";
	public static void logger(String mess) {
		
		try {
			FileWriter fw= new FileWriter(logRoot+ "log.log", true);  //将日志写入文件
			fw.write(mess);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
//--------------------------------------------------log
	
//--------------------------------------------------HttpAndHttps
    public static String charsetName= "GBK";
    public static String fileRoot= "./Cache/";
    
    public static String getPage(URLConnection connect, String url) throws IOException {  //打开网页
   	 
 		  URL ur= new URL(url);
		  String file= ur.getHost();
		  file= url.replaceAll("[/:|]", "");
		  file= fileRoot+ file+ ".html";

		  File fl= new File(file);
		  if(fl.exists()) {
		  		return file;
		  }
		
		  FileWriter fw= new FileWriter(fl);		
   	   	  BufferedReader br= new BufferedReader(new InputStreamReader(connect.getInputStream(), charsetName));
          String line= null;
          StringBuffer result = new StringBuffer();
          while((line= br.readLine())!= null) {  //将网页缓存到本地         
	       	  result.append(line+ "\r\n");
	       	  fw.write(line+ "\r\n");
          }
  		  fw.close();         

         return file;
    }
    
    public static String getPage(Socket socket, String url) throws IOException {  //HTTPS打开网页
 		
   	 	 String file= null;
 		 file= url.replaceAll("[/:|.]", "");
 		 file= fileRoot+ file+ ".html";
 		
        String st[]= url.split("/");
        String request= st[st.length- 1];
        if(request.contains(":"))
       	 request= "";
        
        PrintWriter out= new PrintWriter(socket.getOutputStream(), true);
        out.println("GET /"+ request+ " HTTPS/1.1");
 		FileWriter fw= new FileWriter(new File(file));		
   	    BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
        String line= null;
        StringBuffer result = new StringBuffer();
        while((line= br.readLine())!= null) {
       	  result.append(line+ "\r\n");
       	  fw.write(line+ "\r\n");
        }
        fw.close();

        return file;
    }
    
    public static String getPicture(URLConnection connect, URL ur, String url) throws IOException {  //打开图片
   	 	 System.out.println();

		 String str[]= url.split("/");
		 String fileName= str[str.length- 1];
		 fileName= fileRoot+fileName;

         int line;
         InputStream in= connect.getInputStream();
         FileOutputStream fw= new FileOutputStream(new File(fileName));
         while ((line = in.read()) !=-1) {
             fw.write(line);
         }
         fw.close();		

   	    return fileName;
   	 
    }
    
    public static String getPicture(Socket socket, String url) throws IOException {  //HTTPS打开图片
		 String str[]= url.split("/");
		 String fileName= str[str.length- 1];
		 fileName= fileRoot+fileName;
		 
		 File file= new File(fileName);
		 if(file.exists())
			 return fileName;
		 
         String st[]= url.split("/");
         String request= st[st.length- 1];
         if(request.contains(":"))
       	  	request= "";
         PrintWriter out= new PrintWriter(socket.getOutputStream(), true);
         out.println("GET /"+ request+ " HTTPS/1.1");  //向服务器发送请求头

         int line;
         InputStream in= socket.getInputStream();
         FileOutputStream fw= new FileOutputStream(file);
         while ((line = in.read()) !=-1) {  //将图片缓存到本地
        	 fw.write(line);
         }
         fw.close();

   	 	 return fileName;
   	 
    }
    
    @SuppressWarnings("resource")
	public static  String getCookie(String url) {  //获取相应url的Cookie
		File fl= new File(fileRoot+ "cookie.txt");
		
		try {
			if(!fl.exists()) {
				fl.createNewFile();
			}	
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(fl)));
			String rf= null;
			while((rf= br.readLine())!= null) {
				if(rf.contains(url)) {
				    String st[]= rf.split(" ");
				    return st[1];
				}
			}
				
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	    return null;
   	 
    }
    public static String getHttp(String url){  //获取HTTP内容
    	
   	 	HttpURLConnection connect= null;
   	 	URL ur= null;
   	 	try {
   		 
	        if(url.contains(" ")) {
	        	String post[]= url.split(" ");
	        	url= post[0];

	       		ur= new URL(url);
	       		
	    		connect= (HttpURLConnection) ur.openConnection();   
	    		//设置请求属性
				connect.setRequestProperty("accept", "*/*");
		        connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
				connect.setConnectTimeout(6000);
				connect.setReadTimeout(6000);
		        connect.setDoOutput(true);
				connect.setDoInput(true);
				
				PrintWriter out = new PrintWriter(connect.getOutputStream());
				out.print(post[1]+ "\n");
				//flush输出流的缓冲
				out.flush();
	        }  
	        else {
	     		ur= new URL(url);
	    		connect= (HttpURLConnection) ur.openConnection();   
	    		
	    		//设置请求属性
				connect.setRequestProperty("accept", "*/*");
		        connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
				connect.setConnectTimeout(6000);
				connect.setReadTimeout(6000);
				
		        if(getCookie(url)!= null) {  //如果该url有Cookie则将其写入请求头中
		        	connect.setRequestProperty("cookie", getCookie(url));
		        }
		        connect.connect();	        	
	        }

			if(connect.getResponseCode()== 200) {
				if(connect.getHeaderField("content-type").contains("html")) {  //如果请求的是网页
					return getPage(connect, url);
				}
				else
					return getPicture(connect, ur, url);  //如果请求的是图片
   	    }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(connect!= null)
				connect.disconnect();
		}
   	 
		return null;

    }
    
    public static String getHttps(String url) throws Exception{

   	 	//设置HTTPS请求认证
        String clientKeyStoreFile = "./lib/foxclient.keystore";
        String clientKeyStorePwd = "foxclientks";
        String foxclientKeyPwd = "foxclient";
        String clientTrustKeyStoreFile = "./lib/foxclienttrust.keystore";
        String clientTrustKeyStorePwd = "foxclienttrustks";
 
        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        clientKeyStore.load(new FileInputStream(clientKeyStoreFile), clientKeyStorePwd.toCharArray());
 
        KeyStore clientTrustKeyStore = KeyStore.getInstance("JKS");
        clientTrustKeyStore.load(new FileInputStream(clientTrustKeyStoreFile), clientTrustKeyStorePwd.toCharArray());
 
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientKeyStore, foxclientKeyPwd.toCharArray());
 
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(clientTrustKeyStore);
 
        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        
        URL ur= new URL(url);
        int uPort= ur.getPort();  //获取网址的端口
        if(uPort== -1)
       	 	uPort= 80;
        Socket socket = socketFactory.createSocket(ur.getHost(), uPort);
        
		 if(url.contains(".jpg")||url.contains(".png")) {  //如果请求的是图片
			 return getPicture(socket, url);
		 }else
		 	 return getPage(socket, url);  //如果请求的是网页

   	 
    }
    public static String chooseProtocol(String url) {  //选择协议
   	 
   	 	if(url.startsWith("https")) {  //如果请求HTTPS
   	 		try {
				return getHttps(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   	 	}    
   	 
   		if(url.startsWith("http")) {  //如果请求HTTP
   			return getHttp(url);
   		}
   	 

		return null;
    }
    
    public static String  getWebSource(String url) {  //获取网页信息
   	 	return chooseProtocol(url);
    }
//--------------------------------------------------HttpAndHttps
	
//--------------------------------------------------DotDownload    
    static long startIndex = 0;  //下载开始位置
    
    static long downloadSize = 0;
    
    static boolean downloadFinish = false;  //是否下载完全
    
    static long totleSize = 0;
    
    static String downLoadRoot= "./Cache/";  //下载路径
    
    public static void download(String url, int downloadTimeout) throws IOException{
        
        RandomAccessFile raf = null;
        InputStream input = null;
        String filePath= getPath(url);
        HttpURLConnection connect= null;
        try {
            URL file_url = new URL(url);
            connect = (HttpURLConnection)file_url.openConnection();
            connect.setConnectTimeout(downloadTimeout);
            connect.setRequestMethod("GET");
            File file = new File(filePath);

            if(file.exists() && file.isFile()){  //判断文件是否已经存在
                downloadSize = file.length();
                startIndex = downloadSize;
            }
            System.out.println("Start with:"+ startIndex);
            
            connect.setRequestProperty("Range", "bytes=" + startIndex + "-");  //如果文件以=已存在就从文件尾部开始请求
            
            int status = connect.getResponseCode();
            totleSize = downloadSize + connect.getContentLength();
            if(status== 200 || status == 206 ){  //如果请求成功就写入文件
                raf = new RandomAccessFile(file, "rw");
                raf.seek(startIndex);
                input = connect.getInputStream();
                
                byte[] buffer = new byte[1024];
                int size = 0;
                while((size=input.read(buffer)) !=-1 ){
                    raf.write(buffer, 0, size);
                    downloadSize += size;
                }
                raf.close();
                input.close();
            }
            
            if(totleSize<= downloadSize) {
            	downloadFinish= true;
            }
        } catch (Throwable e) {
        	e.printStackTrace();
        }finally {
        	if(downloadFinish)
        		connect.disconnect();
        }
    }

    public static String getPath(String ur) throws IOException { //获取文件路径并且获取文件类型

    	String[] type= ur.split("/");
    	
	   	 return downLoadRoot+ type[type.length- 1];
    }
    
    public static void downloadMain(String url, int timeout) throws IOException {
    	while(!downloadFinish) {
    		download(url, timeout);
    	}
    }
    
 //-----------------------------------------DotDownload
 
 //-----------------------------------------eMail
    
    JFrame frame= null;  
    JTextField sourMail= null;  //源邮箱
    JComboBox<String> combo= null;  //可以发送的邮箱种类
    JPanel panel1= null;
    JPanel panel2= null;
    JPanel panel3= null;
    JTextArea textF= null;  //邮件内容
    JLabel sendLabel= null;  
    JLabel recieveLabel= null;
    JTextField destMail= null;  //目的邮箱
    JPasswordField password= null;  //源邮箱的密码
    JLabel passLabel= null;
    JPanel panel= null;
    JButton sent= null;
    String choice[]= {"@qq.com", "@gmail.com"};
    
    String server= null;  //源邮箱的服务器
    int port= 0;
    String message= null;  //内容
    String dMail= null;  //目的邮箱
    String sMail= null;  //源邮箱
    String passw= null;   //密码
    boolean hasin= false;  //判断是否进行了邮箱类型的挑选
     
    public  void constructFrame() {  //设置界面
    	frame= new JFrame();
    	frame.setTitle("邮件发送");
    	//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setBounds(600, 100, 400, 400);	 //设置窗体的大小
    	
 
    	recieveLabel= new JLabel("Recipient:");
   	    destMail= new JTextField(19);    	
    	sendLabel= new JLabel("Sender:");
    	sourMail= new JTextField(20);
    	passLabel= new JLabel("Password:");
    	password= new JPasswordField(18);
        combo= new JComboBox<String>(choice);
    	textF= new JTextArea();
    	textF.setEditable(true);    	
    	sent= new JButton("Send");
    	destMail.setText("2949588484");
     	sourMail.setText("2949588484@qq.com");   
    	password.setText("kugrsjrshbovdgdf");    	
    	textF.setText("谭文彪 2017153009 计算机科学与技术");   
    	
 
    	panel= new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  	
    	panel1= new JPanel();
    	panel1.setLayout(new FlowLayout(FlowLayout.LEFT));        
    	panel2= new JPanel();
    	panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
    	panel3= new JPanel();
    	panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

	
        combo.addActionListener(this);  //设置监听器
        sent.addActionListener(this);
        panel1.add(recieveLabel);
        panel1.add(destMail);
        panel1.add(combo);
        panel2.add(sendLabel);
    	panel2.add(sourMail);
        panel3.add(passLabel);
        panel3.add(password);
        panel3.add(sent);
        
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(textF);
        frame.add(panel);
    	frame.setVisible(true);
    	
    }
    public static void eMail() throws Exception {  
		Function fun= new Function();
    	fun.constructFrame();
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        Object bj= e.getSource();
        if(bj== combo) {  //如果选择了下拉框
        	String str= new String(combo.getSelectedItem().toString());
        	String type= "";
        	if(str.equals("@qq.com")) {
        		server= "smtp.qq.com";
        		type= "@qq.com";
        		port= 25;
        	}
        	if(str.equals("@gmail.com")) {
        		server= "smtp.gmail.com";
        		type= "@gmail.com";
        		port= 587;
        	}

        	dMail= destMail.getText()+type;
        	hasin= true;
        }
        if(bj== sent) {  //如果点击了发送按钮
        	if(!hasin) {
        		server= "smtp.qq.com";
            	dMail= destMail.getText() +"@qq.com";    
            	hasin= false;
            	port= 25;
        	}
        	message= textF.getText();
        	sMail= sourMail.getText();
        	passw= new String(password.getPassword());

				try {
					Send(server,message, dMail, sMail, passw);
					//Send.send("smtp.qq.com", 25, message, "T2949588484@163.com", sMail, passw);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  //调用发送模块
        }
	}  
	
    public static void Send(String ser, String mes, String dMail, String sMail, String pass) throws MessagingException, GeneralSecurityException {
    	  
    	String server= ser;  //服务器
        String message= mes;  //内容
        String destMail= dMail;  //目的邮箱
        
        String sourMail= sMail;  //源邮箱
        String password= pass;   //密码
        System.out.println(server);
        System.out.println(dMail);
        System.out.println(sMail);
    	server= "smtp.qq.com";
       
    	
        Properties props = new Properties();

        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", server);
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();  //进行加密传输
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {  
                 protected PasswordAuthentication getPasswordAuthentication() {  //验证
                      return new PasswordAuthentication(sourMail,  password);  
                 }  
        });

        MimeMessage mess = new MimeMessage(session);
        mess.setSubject("Mail from Tan");  //发送的主题

         mess.setText(message); 
         mess.setFrom(new InternetAddress(sourMail));  //设置发送者
         mess.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destMail));  //设置接收者
         Transport.send(mess);  //发送
         System.out.println("Send over");
    }

 //------------------------------------------------------eMail
    
}
