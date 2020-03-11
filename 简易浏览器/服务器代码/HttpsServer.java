package HttpAndHttps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class HttpsServer extends Thread{

	public void run() {

        String serverKeyStoreFile = "./lib/catserver.keystore";
        String serverKeyStorePwd = "catserverks";
        String catServerKeyPwd = "catserver";
        String serverTrustKeyStoreFile = "./lib/catservertrust.keystore";
        String serverTrustKeyStorePwd = "catservertrustks";
 
        KeyStore serverKeyStore;
		try {
			//设置HTTPS认证
			serverKeyStore = KeyStore.getInstance("JKS");
	        serverKeyStore.load(new FileInputStream(serverKeyStoreFile), serverKeyStorePwd.toCharArray());
	        
	        KeyStore serverTrustKeyStore = KeyStore.getInstance("JKS");
	        serverTrustKeyStore.load(new FileInputStream(serverTrustKeyStoreFile), serverTrustKeyStorePwd.toCharArray());
	 
	        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        kmf.init(serverKeyStore, catServerKeyPwd.toCharArray());
	 
	        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        tmf.init(serverTrustKeyStore);
	 
	        SSLContext sslContext = SSLContext.getInstance("TLSv1");
	        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
	 
	        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
	        SSLServerSocket server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8080);
	        server.setNeedClientAuth(true);

	        Socket socket= null;
	        
	        ExecutorService execute= Executors.newFixedThreadPool(300);
	        System.out.println("Start Https server.");
	        while(true) {
	        	socket= server.accept();  //阻塞等待浏览器连接
	        	execute.execute(new HttpsServerThread(socket));
	        }
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
}
