package com.example.mynote;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ResponseCache;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText name,password;
	private Button login,register;	
	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		name = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register=(Button)findViewById(R.id.register);
		login.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String username = name.getText().toString();
				String psd = password.getText().toString();
				
				if(username.isEmpty()||psd.isEmpty())
					Toast.makeText(MainActivity.this, "请正确输入", 1000).show();
				
				//把数据传给数据库进行比较
				String urlAddress = "http://192.168.1.222:80/mynote/login.php";
				String url = urlAddress + "?username=" + username + "&psd="
				+ psd;
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode() ==HttpStatus.SC_OK)
					{
						HttpEntity httpEntity = httpResponse.getEntity();
						String result = EntityUtils.toString(httpEntity);
						int tag =result.length();
						System.out.println(result);
						System.out.println(tag);
						//Toast.makeText(MainActivity.this, tag, 1000).show();
						//String tag = "success";
						if(tag == 5)
						{
							//登录成功 跳转到listview
							Toast.makeText(MainActivity.this, "登录成功", 1000).show();
							Intent intent = new Intent(MainActivity.this,ListViewActivity.class);
							startActivity(intent);
							finish();
						}
						else {
							Toast.makeText(MainActivity.this, "账号或密码错误", 1000).show();
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
		});
		register.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String username = name.getText().toString();
				String psd = password.getText().toString();
				
				
				if(username.isEmpty()||psd.isEmpty())
				{
					Toast.makeText(MainActivity.this, "请正确输入", 1000).show();
					
				}
				//判断是否符合规则 放入数据库
				
				else
				{

				
				
				String url = "http://192.168.1.222:80/mynote/register.php";
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("psd", psd));
				try {
					HttpPost httpRequest = new HttpPost(url);
					UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(  
		                    params); 
					//HttpEntity httpEntity = new UrlEncodedFormEntity(params,"UTF-8");
					httpRequest.setEntity(formEntiry);
					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					
					if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						if(params.isEmpty()) Toast.makeText(MainActivity.this,"eeeempty", 5000).show();
		                String result = EntityUtils.toString(httpResponse.getEntity());  
		                Toast.makeText(MainActivity.this,result, 5000).show();  
		            }else{  
		            	Toast.makeText(MainActivity.this, "注册失败!", 500).show();  
		            }  
				} catch (UnsupportedEncodingException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        } catch (ClientProtocolException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	private String inStream2String(InputStream is) throws Exception {  
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//        byte[] buf = new byte[1024];  
//        int len = -1;  
//        while ((len = is.read(buf)) != -1) {  
//            baos.write(buf, 0, len);  
//        }  
//        return new String(baos.toByteArray());  
//    }  
}



