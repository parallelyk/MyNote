package com.example.mynote;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends Activity {
	private EditText noteEditText ;
	private Button addButton,cancelButton;
	private String add_item_url = "http://192.168.1.222:80/mynote/add_item.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		noteEditText = (EditText) findViewById(R.id.inputDesc);
		addButton = (Button) findViewById(R.id.add_btn);
		cancelButton = (Button) findViewById(R.id.cancel_btn);
		addButton.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String itemname = noteEditText.getText().toString();
				if(!itemname.isEmpty())
				{

					
					List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

					paramsList.add(new BasicNameValuePair("itemname", itemname));
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpRequest = new HttpPost(add_item_url);
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramsList);
						httpRequest.setEntity(entity);
						HttpResponse httpResponse = httpClient.execute(httpRequest);
						if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
							Toast.makeText(AddItemActivity.this, "添加成功", 500).show();
							Intent intent = new Intent(AddItemActivity.this,ListViewActivity.class);
							startActivity(intent);
							finish();
						}
						else {
							Toast.makeText(AddItemActivity.this, "添加出错", 1000).show();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				else {
					Toast.makeText(AddItemActivity.this, "请输入内容", 1000).show();
				}
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}

}
