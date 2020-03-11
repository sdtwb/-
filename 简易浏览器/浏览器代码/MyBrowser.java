package Browser;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;



public class MyBrowser {

	static boolean pageOrPicture= false;
	String allFileRoot= "./Cache/";
	Browser browser;
	Combo urlCombo;
	String location;
	String user= null;
	ArrayList<String> urlFavorites= new ArrayList<String>();

	public void getPage(String url) throws IOException {  //����url�����ļ�

		if(url.contains(".jpg")||url.contains(".png")) {
			String file= Function.getWebSource(url);
			browser.setUrl(new File(file).toURL().toString());
			return;
		}
		
		browser.setUrl(new File(Function.getWebSource(url)).toURL().toString());
		

	}
	public void setCookie() {  //����Cookie
		try {
			File fl= new File(allFileRoot+"cookie.txt");
			if(!fl.exists()) {
				fl.createNewFile();
			}
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(fl)));
			String rf= null;
			boolean hasin= false;
			while((rf= br.readLine())!= null) {
				if(user.equals(rf)) {
					hasin= true;
					break;
				}
			}
			if(!hasin) {
				FileWriter fw= new FileWriter(allFileRoot+ "cookie.txt", true);
				fw.write(user+"\r\n");
				fw.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			user= null;
			
		}
		
	}
	
	public void openPage(String url) throws IOException {  //����url����ҳ
		System.out.println();
		System.out.println("openPage: "+url);
		Function.logger("openPage: "+url+ "\r\n");
		
		if(user!= null) {
			setCookie();
		}
		
		if(url.contains(".exe")||url.contains(".zip")) {
			Function.downloadMain(url, 6000);
			System.out.println("Download has done");
			return;
		}

		if(url.contains("http")&&(url.contains("127.0.0.1")||url.contains("172.29.21.156"))) {  //����������ַ�Ǳ��˴������ñ���д�ķ���
			getPage(url);
			return;
		}

		browser.setUrl(url);
		
	}
	
	public void remind(String title, String mess){  //�������Ѵ���

		JFrame frame = new JFrame(title);
		JEditorPane pane= new JEditorPane();
		
		frame.setBounds(300, 300, 200, 200);
        pane.setText(mess);
        Font font= new Font("����", 9, 50);
        pane.setFont(font);
        pane.setEditable(false);
        
        frame.add(pane);
        frame.setVisible(true);
	}

	public void initBrowser(Shell shell) throws MalformedURLException {  //��������ڵĳ�ʼ
		shell.setLayout(new FormLayout());

		Composite statusBar= new Composite(shell,SWT.NONE);  //״̬��
		statusBar.setLayout(new GridLayout(7, false));
		FormData formdata= new FormData();
		formdata.top= new FormAttachment(0,0);
		formdata.left= new FormAttachment(0,0);
		formdata.right= new FormAttachment(100, 0);
		statusBar.setLayoutData(formdata);
		
		Label bottom= new Label(shell, SWT.NONE);  //�ײ���״̬��
		formdata= new FormData();
		formdata.left= new FormAttachment(0,0);
		formdata.right= new FormAttachment(100, 0);
		formdata.bottom= new FormAttachment(100, 0);
		bottom.setLayoutData(formdata);

		
		ArrayList<String> urlList= new ArrayList<String>();
		browser= new Browser(shell, SWT.BORDER);  //�����
		
		browser.addOpenWindowListener(new OpenWindowListener() {  //�����´򿪵Ĵ���
			@Override
			public void open(WindowEvent event) {
				// TODO Auto-generated method stub
				final Shell she= new Shell(shell);
				final Browser brow= new Browser(she, SWT.NONE);
				event.browser= brow;
				
				event.display.asyncExec(new Runnable() {  //���¼����ҵ�Browser��
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String ur= brow.getUrl();
						try {

							openPage(ur);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						she.close();
						
					}
					
				});
				
			}
			
		});
		formdata= new FormData();
		formdata.top= new FormAttachment(statusBar);
		formdata.bottom= new FormAttachment(bottom);
		formdata.left= new FormAttachment(0,0);
		formdata.right= new FormAttachment(100, 0);
		browser.setLayoutData(formdata);
	
		Button buttonBack= new Button(statusBar, SWT.PUSH);  //���ذ�ť
		Image iconBack= new Image(null, "./Icon/back.png");
		buttonBack.setImage(iconBack);
		buttonBack.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				browser.back();
			}
		});
		
		Button buttonGo= new Button(statusBar, SWT.PUSH);  //ǰ����ť
		Image iconForward= new Image(null, "./Icon/forward.png");
		buttonGo.setImage(iconForward);
		buttonGo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				browser.forward();

			}
		});
		
		Button buttonRefresh= new Button(statusBar, SWT.PUSH);  //ˢ�°�ť
		Image iconRefresh= new Image(null, "./Icon/refresh.png");
		buttonRefresh.setImage(iconRefresh);
		buttonRefresh.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				browser.refresh();

			}
		});
		
		urlCombo= new Combo(statusBar, SWT.BORDER);  //��ַ������
		urlCombo.setLayoutData(new GridData(300, SWT.DEFAULT));
		urlCombo.setFocus();
		
		Button favorite= new Button(statusBar, SWT.PUSH);  //����ղصİ�ť
		Image iconFavorite= new Image(null, "./Icon/favorite.png");
		favorite.setImage(iconFavorite);
		favorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				urlFavorites.add(urlCombo.getText());
			}
		});
		
		
		Button enter= new Button(statusBar, SWT.PUSH);  //Enter��ť
		enter.setText("Enter");
		enter.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent event) {
				String urlName= urlCombo.getText();
				boolean hasin= false;				
				for(String str: urlList) {  //�жϵ�ǰ�����ĵ�ַ�Ƿ��Ѿ���֮ǰ�Ѿ���ӹ�����ַ��������
					if(urlName.equals(str))
						hasin= true;
				}
				
				if(!hasin) {  //���֮ǰû����ӹ����������
					urlList.add(urlName);
					urlCombo.add(urlName);
					urlCombo.setText(urlName);
				}
				
				try {
					openPage(urlName);  //����ַ
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		shell.setDefaultButton(enter);  //����Ĭ�ϵİ�ťΪEnter��ť
		
		Button buttonTools = new Button(statusBar, SWT.PUSH);  //��������ť
		buttonTools.setText("Tools");
		buttonTools.addSelectionListener(new mySelectinAdapter(buttonTools));
		
		browser.addCloseWindowListener(new myCloseWindowListener());
		browser.addLocationListener(new myLocationListener(urlCombo));
		browser.addStatusTextListener(new myStatusTextListener(bottom));
		browser.addMouseListener(new myMouseListener());
		
		//browser.setUrl("http://127.0.0.1:8081/");

		new CallJava(browser , "test");  //������ҳ���Ƿ������js�е�test����

	}

	public void run() throws MalformedURLException {  //���������
		Display display= new Display();
		Shell shell= new Shell(display);
		
		shell.setText("MyBrowser");
		initBrowser(shell);
		
		shell.open();
		while(!shell.isDisposed()) {
			if(display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public void openFavorites(Button button) {  //���ղؼд���
		Menu menu= new Menu(button.getParent().getShell());
		
		for(String str: urlFavorites) {
			MenuItem item= new MenuItem(menu, SWT.NONE);
			item.setText(str);
			item.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
	                   MenuItem it= (MenuItem) event.widget;
	                   
	                   try {
	                	   openPage(it.getText());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                   
				}
			});
		}
		
		Rectangle rect= button.getBounds();
		Point point= button.getParent().toDisplay(new Point(rect.x, rect.y));
		menu.setLocation(point.x, point.y+ rect.height);
		menu.setVisible(true);
		
	}
	
	public void sendMail() throws Exception {  //���÷����ʼ�ģ��
		Function.eMail();
	}
	
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
        MyBrowser mybrowser= new MyBrowser();
        mybrowser.run();
	}
	
	
	class CallJava extends BrowserFunction {   

        public CallJava (Browser arg0, String arg1) { 
            super(arg0, arg1);  
        }  

        public Object function(Object[] arg0) {  
        	user= (String)arg0[0]+" "+ browser.evaluate("return document.getElementById('username').value");  //������ҳ���û���
            try {
				openPage(user);  //����ҳ
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return super.function(arg0);  
        }  
  
    }  
	
	class myMouseListener implements MouseListener{  //������

		@Override
		public void mouseDoubleClick(MouseEvent arg0) {
			// TODO Auto-generated method stub		
		}

		@Override
		public void mouseDown(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseUp(MouseEvent arg0) {
			// TODO Auto-generated method stub
			try {
				if(location!= null) {
					openPage(location);	//����ҳ			
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	class myCloseWindowListener implements CloseWindowListener{  //�رմ��ڼ���

		@Override
		public void close(WindowEvent event) {
			// TODO Auto-generated method stub
			
			((Browser) event.widget).getShell().close();;
		}
		
	}

	class myLocationListener implements LocationListener{  //��ǰλ�ü���
		Combo com;
        myLocationListener(Combo urlList){
        	com= urlList;
        }
		@Override
		public void changed(LocationEvent event) {
			// TODO Auto-generated method stub
			com.setText(event.location);  //���øı����ı�
		}

		@Override
		public void changing(LocationEvent event) {
			// TODO Auto-generated method stub
			com.setText("load "+event.location+ "...");  //�������ڸ��ĵ��ı�
			
		}
		
	}
	
	class myStatusTextListener implements StatusTextListener{  //����״̬����
		Label label;
		myStatusTextListener(Label url){
			label= url;
		}
		@Override
		public void changed(StatusTextEvent event) {
			// TODO Auto-generated method stub
			label.setText(event.text);
			if(event.text.startsWith("http"))
				location= event.text;		
		}
		
	}
	
	class mySelectinAdapter extends SelectionAdapter{  //���ѡ�����
		
		private Button tool;
		private Menu menu;
		
		public mySelectinAdapter(Button dropdown) {
			this.tool = dropdown;
			menu = new Menu(dropdown.getParent().getShell());
			
			add("�ղؼ�");
			add("�����ʼ�");			
			
		}

		public void add(String item) {
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText(item);
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					// ѡ������Ĵ�����
					MenuItem item = (MenuItem) event.widget;
					if(item.getText().equals("�ղؼ�")) {  //���ѡ������ղؼ�
						openFavorites(tool);
					}
					if(item.getText().equals("�����ʼ�")) {  //���ѡ����Ƿ����ʼ�
						try {
							sendMail();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}

		public void widgetSelected(SelectionEvent event) {  //���ô��ڵ�����λ��
			
			Button btn = (Button) event.widget;
			Rectangle rect = btn.getBounds();
			Point pt = btn.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);

		}
	}
}
