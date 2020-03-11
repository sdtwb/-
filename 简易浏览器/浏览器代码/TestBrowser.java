package Browser;

public class TestBrowser {
	static int co= 0;
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
        int count= 0;
        
        while(count< 10000) {
        	count++;
        	co++;
        	System.out.println("µÚ"+co+"¸öä¯ÀÀÆ÷\n");
        	Thread.sleep(3);;
        	testThread te= new testThread("http://localhost:8081/");
        	te.start();
        }
	}

}
