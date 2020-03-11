package HttpAndHttps;

public class serverMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new HttpsServer().start();  //开启HTTPS服务器
		new Server().start();  //开启HTTP服务器
		
	}

}
