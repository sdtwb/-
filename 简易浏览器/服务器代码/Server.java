package HttpAndHttps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{

	public  void run() {
		// TODO Auto-generated method stub
        ServerSocket server;
		try {
			server = new ServerSocket(8081);  //服务器监听8081端口
	        Socket socket= null;
	        
	        ExecutorService execute= Executors.newFixedThreadPool(300);
	        System.out.println("Start http server.");
	        while(true) {
	        	socket= server.accept();  //阻塞等待客户端连接
	        	execute.execute(new ServerThread(socket));
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
