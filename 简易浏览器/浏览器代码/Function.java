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
			FileWriter fw= new FileWriter(logRoot+ "log.log", true);  //����־д���ļ�
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
    
    public static String getPage(URLConnection connect, String url) throws IOException {  //����ҳ
   	 
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
          while((line= br.readLine())!= null) {  //����ҳ���浽����         
	       	  result.append(line+ "\r\n");
	       	  fw.write(line+ "\r\n");
          }
  		  fw.close();         

         return file;
    }
    
    public static String getPage(Socket socket, String url) throws IOException {  //HTTPS����ҳ
 		
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
    
    public static String getPicture(URLConnection connect, URL ur, String url) throws IOException {  //��ͼƬ
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
    
    public static String getPicture(Socket socket, String url) throws IOException {  //HTTPS��ͼƬ
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
         out.println("GET /"+ request+ " HTTPS/1.1");  //���������������ͷ

         int line;
         InputStream in= socket.getInputStream();
         FileOutputStream fw= new FileOutputStream(file);
         while ((line = in.read()) !=-1) {  //��ͼƬ���浽����
        	 fw.write(line);
         }
         fw.close();

   	 	 return fileName;
   	 
    }
    
    @SuppressWarnings("resource")
	public static  String getCookie(String url) {  //��ȡ��Ӧurl��Cookie
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
    public static String getHttp(String url){  //��ȡHTTP����
    	
   	 	HttpURLConnection connect= null;
   	 	URL ur= null;
   	 	try {
   		 
	        if(url.contains(" ")) {
	        	String post[]= url.split(" ");
	        	url= post[0];

	       		ur= new URL(url);
	       		
	    		connect= (HttpURLConnection) ur.openConnection();   
	    		//������������
				connect.setRequestProperty("accept", "*/*");
		        connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
				connect.setConnectTimeout(6000);
				connect.setReadTimeout(6000);
		        connect.setDoOutput(true);
				connect.setDoInput(true);
				
				PrintWriter out = new PrintWriter(connect.getOutputStream());
				out.print(post[1]+ "\n");
				//flush������Ļ���
				out.flush();
	        }  
	        else {
	     		ur= new URL(url);
	    		connect= (HttpURLConnection) ur.openConnection();   
	    		
	    		//������������
				connect.setRequestProperty("accept", "*/*");
		        connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36");
				connect.setConnectTimeout(6000);
				connect.setReadTimeout(6000);
				
		        if(getCookie(url)!= null) {  //�����url��Cookie����д������ͷ��
		        	connect.setRequestProperty("cookie", getCookie(url));
		        }
		        connect.connect();	        	
	        }

			if(connect.getResponseCode()== 200) {
				if(connect.getHeaderField("content-type").contains("html")) {  //������������ҳ
					return getPage(connect, url);
				}
				else
					return getPicture(connect, ur, url);  //����������ͼƬ
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

   	 	//����HTTPS������֤
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
        int uPort= ur.getPort();  //��ȡ��ַ�Ķ˿�
        if(uPort== -1)
       	 	uPort= 80;
        Socket socket = socketFactory.createSocket(ur.getHost(), uPort);
        
		 if(url.contains(".jpg")||url.contains(".png")) {  //����������ͼƬ
			 return getPicture(socket, url);
		 }else
		 	 return getPage(socket, url);  //������������ҳ

   	 
    }
    public static String chooseProtocol(String url) {  //ѡ��Э��
   	 
   	 	if(url.startsWith("https")) {  //�������HTTPS
   	 		try {
				return getHttps(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   	 	}    
   	 
   		if(url.startsWith("http")) {  //�������HTTP
   			return getHttp(url);
   		}
   	 

		return null;
    }
    
    public static String  getWebSource(String url) {  //��ȡ��ҳ��Ϣ
   	 	return chooseProtocol(url);
    }
//--------------------------------------------------HttpAndHttps
	
//--------------------------------------------------DotDownload    
    static long startIndex = 0;  //���ؿ�ʼλ��
    
    static long downloadSize = 0;
    
    static boolean downloadFinish = false;  //�Ƿ�������ȫ
    
    static long totleSize = 0;
    
    static String downLoadRoot= "./Cache/";  //����·��
    
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

            if(file.exists() && file.isFile()){  //�ж��ļ��Ƿ��Ѿ�����
                downloadSize = file.length();
                startIndex = downloadSize;
            }
            System.out.println("Start with:"+ startIndex);
            
            connect.setRequestProperty("Range", "bytes=" + startIndex + "-");  //����ļ���=�Ѵ��ھʹ��ļ�β����ʼ����
            
            int status = connect.getResponseCode();
            totleSize = downloadSize + connect.getContentLength();
            if(status== 200 || status == 206 ){  //�������ɹ���д���ļ�
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

    public static String getPath(String ur) throws IOException { //��ȡ�ļ�·�����һ�ȡ�ļ�����

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
    JTextField sourMail= null;  //Դ����
    JComboBox<String> combo= null;  //���Է��͵���������
    JPanel panel1= null;
    JPanel panel2= null;
    JPanel panel3= null;
    JTextArea textF= null;  //�ʼ�����
    JLabel sendLabel= null;  
    JLabel recieveLabel= null;
    JTextField destMail= null;  //Ŀ������
    JPasswordField password= null;  //Դ���������
    JLabel passLabel= null;
    JPanel panel= null;
    JButton sent= null;
    String choice[]= {"@qq.com", "@gmail.com"};
    
    String server= null;  //Դ����ķ�����
    int port= 0;
    String message= null;  //����
    String dMail= null;  //Ŀ������
    String sMail= null;  //Դ����
    String passw= null;   //����
    boolean hasin= false;  //�ж��Ƿ�������������͵���ѡ
     
    public  void constructFrame() {  //���ý���
    	frame= new JFrame();
    	frame.setTitle("�ʼ�����");
    	//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setBounds(600, 100, 400, 400);	 //���ô���Ĵ�С
    	
 
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
    	textF.setText("̷�ı� 2017153009 �������ѧ�뼼��");   
    	
 
    	panel= new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  	
    	panel1= new JPanel();
    	panel1.setLayout(new FlowLayout(FlowLayout.LEFT));        
    	panel2= new JPanel();
    	panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
    	panel3= new JPanel();
    	panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

	
        combo.addActionListener(this);  //���ü�����
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
        if(bj== combo) {  //���ѡ����������
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
        if(bj== sent) {  //�������˷��Ͱ�ť
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
				}  //���÷���ģ��
        }
	}  
	
    public static void Send(String ser, String mes, String dMail, String sMail, String pass) throws MessagingException, GeneralSecurityException {
    	  
    	String server= ser;  //������
        String message= mes;  //����
        String destMail= dMail;  //Ŀ������
        
        String sourMail= sMail;  //Դ����
        String password= pass;   //����
        System.out.println(server);
        System.out.println(dMail);
        System.out.println(sMail);
    	server= "smtp.qq.com";
       
    	
        Properties props = new Properties();

        // ���ͷ�������Ҫ�����֤
        props.setProperty("mail.smtp.auth", "true");
        // �����ʼ�������������
        props.setProperty("mail.host", server);
        // �����ʼ�Э������
        props.setProperty("mail.transport.protocol", "smtp");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();  //���м��ܴ���
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {  
                 protected PasswordAuthentication getPasswordAuthentication() {  //��֤
                      return new PasswordAuthentication(sourMail,  password);  
                 }  
        });

        MimeMessage mess = new MimeMessage(session);
        mess.setSubject("Mail from Tan");  //���͵�����

         mess.setText(message); 
         mess.setFrom(new InternetAddress(sourMail));  //���÷�����
         mess.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destMail));  //���ý�����
         Transport.send(mess);  //����
         System.out.println("Send over");
    }

 //------------------------------------------------------eMail
    
}
