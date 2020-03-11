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

	public void getPage(String url) throws IOException {  //根据url请求文件

		if(url.contains(".jpg")||url.contains(".png")) {
			String file= Function.getWebSource(url);
			browser.setUrl(new File(file).toURL().toString());
			return;
		}
		
		browser.setUrl(new File(Function.getWebSource(url)).toURL().toString());
		

	}
	public void setCookie() {  //设置Cookie
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
	
	public void openPage(String url) throws IOException {  //根据url打开网页
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

		if(url.contains("http")&&(url.contains("127.0.0.1")||url.contains("172.29.21.156"))) {  //如果请求的网址是本人搭建的则调用本人写的方法
			getPage(url);
			return;
		}

		browser.setUrl(url);
		
	}
	
	public void remind(String title, String mess){  //弹出提醒窗口

		JFrame frame = new JFrame(title);
		JEditorPane pane= new JEditorPane();
		
		frame.setBounds(300, 300, 200, 200);
        pane.setText(mess);
        Font font= new Font("楷体", 9, 50);
        pane.setFont(font);
        pane.setEditable(false);
        
        frame.add(pane);
        frame.setVisible(true);
	}

	public void initBrowser(Shell shell) throws MalformedURLException {  //浏览器窗口的初始
		shell.setLayout(new FormLayout());

		Composite statusBar= new Composite(shell,SWT.NONE);  //状态栏
		statusBar.setLayout(new GridLayout(7, false));
		FormData formdata= new FormData();
		formdata.top= new FormAttachment(0,0);
		formdata.left= new FormAttachment(0,0);
		formdata.right= new FormAttachment(100, 0);
		statusBar.setLayoutData(formdata);
		
		Label bottom= new Label(shell, SWT.NONE);  //底部的状态栏
		formdata= new FormData();
		formdata.left= new FormAttachment(0,0);
		formdata.right= new FormAttachment(100, 0);
		formdata.bottom= new FormAttachment(100, 0);
		bottom.setLayoutData(formdata);

		
		ArrayList<String> urlList= new ArrayList<String>();
		browser= new Browser(shell, SWT.BORDER);  //浏览器
		
		browser.addOpenWindowListener(new OpenWindowListener() {  //监听新打开的窗口
			@Override
			public void open(WindowEvent event) {
				// TODO Auto-generated method stub
				final Shell she= new Shell(shell);
				final Browser brow= new Browser(she, SWT.NONE);
				event.browser= brow;
				
				event.display.asyncExec(new Runnable() {  //将事件用我的Browser打开
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
	
		Button buttonBack= new Button(statusBar, SWT.PUSH);  //返回按钮
		Image iconBack= new Image(null, "./Icon/back.png");
		buttonBack.setImage(iconBack);
		buttonBack.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				browser.back();
			}
		});
		
		Button buttonGo= new Button(statusBar, SWT.PUSH);  //前进按钮
		Image iconForward= new Image(null, "./Icon/forward.png");
		buttonGo.setImage(iconForward);
		buttonGo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				browser.forward();

			}
		});
		
		Button buttonRefresh= new Button(statusBar, SWT.PUSH);  //刷新按钮
		Image iconRefresh= new Image(null, "./Icon/refresh.png");
		buttonRefresh.setImage(iconRefresh);
		buttonRefresh.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				browser.refresh();

			}
		});
		
		urlCombo= new Combo(statusBar, SWT.BORDER);  //地址下拉栏
		urlCombo.setLayoutData(new GridData(300, SWT.DEFAULT));
		urlCombo.setFocus();
		
		Button favorite= new Button(statusBar, SWT.PUSH);  //添加收藏的按钮
		Image iconFavorite= new Image(null, "./Icon/favorite.png");
		favorite.setImage(iconFavorite);
		favorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				urlFavorites.add(urlCombo.getText());
			}
		});
		
		
		Button enter= new Button(statusBar, SWT.PUSH);  //Enter按钮
		enter.setText("Enter");
		enter.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent event) {
				String urlName= urlCombo.getText();
				boolean hasin= false;				
				for(String str: urlList) {  //判断当前搜索的地址是否已经在之前已经添加过到地址下拉栏中
					if(urlName.equals(str))
						hasin= true;
				}
				
				if(!hasin) {  //如果之前没有添加过则现在添加
					urlList.add(urlName);
					urlCombo.add(urlName);
					urlCombo.setText(urlName);
				}
				
				try {
					openPage(urlName);  //打开网址
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		shell.setDefaultButton(enter);  //设置默认的按钮为Enter按钮
		
		Button buttonTools = new Button(statusBar, SWT.PUSH);  //工具栏按钮
		buttonTools.setText("Tools");
		buttonTools.addSelectionListener(new mySelectinAdapter(buttonTools));
		
		browser.addCloseWindowListener(new myCloseWindowListener());
		browser.addLocationListener(new myLocationListener(urlCombo));
		browser.addStatusTextListener(new myStatusTextListener(bottom));
		browser.addMouseListener(new myMouseListener());
		
		//browser.setUrl("http://127.0.0.1:8081/");

		new CallJava(browser , "test");  //监听网页中是否调用了js中的test函数

	}

	public void run() throws MalformedURLException {  //启动浏览器
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
	
	public void openFavorites(Button button) {  //打开收藏夹窗口
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
	
	public void sendMail() throws Exception {  //调用发送邮件模块
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
        	user= (String)arg0[0]+" "+ browser.evaluate("return document.getElementById('username').value");  //返回网页中用户名
            try {
				openPage(user);  //打开网页
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return super.function(arg0);  
        }  
  
    }  
	
	class myMouseListener implements MouseListener{  //鼠标监听

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
					openPage(location);	//打开网页			
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	class myCloseWindowListener implements CloseWindowListener{  //关闭窗口监听

		@Override
		public void close(WindowEvent event) {
			// TODO Auto-generated method stub
			
			((Browser) event.widget).getShell().close();;
		}
		
	}

	class myLocationListener implements LocationListener{  //当前位置监听
		Combo com;
        myLocationListener(Combo urlList){
        	com= urlList;
        }
		@Override
		public void changed(LocationEvent event) {
			// TODO Auto-generated method stub
			com.setText(event.location);  //设置改变后的文本
		}

		@Override
		public void changing(LocationEvent event) {
			// TODO Auto-generated method stub
			com.setText("load "+event.location+ "...");  //设置正在更改的文本
			
		}
		
	}
	
	class myStatusTextListener implements StatusTextListener{  //设置状态监听
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
	
	class mySelectinAdapter extends SelectionAdapter{  //组件选择监听
		
		private Button tool;
		private Menu menu;
		
		public mySelectinAdapter(Button dropdown) {
			this.tool = dropdown;
			menu = new Menu(dropdown.getParent().getShell());
			
			add("收藏夹");
			add("发送邮件");			
			
		}

		public void add(String item) {
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setText(item);
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					// 选中子项的处理方法
					MenuItem item = (MenuItem) event.widget;
					if(item.getText().equals("收藏夹")) {  //如果选择的是收藏夹
						openFavorites(tool);
					}
					if(item.getText().equals("发送邮件")) {  //如果选择的是发送邮件
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

		public void widgetSelected(SelectionEvent event) {  //设置窗口弹出的位置
			
			Button btn = (Button) event.widget;
			Rectangle rect = btn.getBounds();
			Point pt = btn.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);

		}
	}
}
