package com.example.mynote;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mynote.ListViewActivity.MyAdapter;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends Activity {
	private EditText editText ;
	private Button editButton,cancelButton;
	private String edit_item_url = "http://192.168.1.222:80/mynote/edit_item.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		editText = (EditText) findViewById(R.id.edit);
		editButton = (Button) findViewById(R.id.edit_btn);
		cancelButton = (Button) findViewById(R.id.edit_cancel_btn);
		Intent intent = getIntent();
		
		final String id = (intent.getStringArrayExtra("editinfo"))[0];
		final String iteminfo = (intent.getStringArrayExtra("editinfo"))[1];
		editText.setText(iteminfo);
		
		editButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String newiteminfo = editText.getText().toString();
				if(!newiteminfo.equals(iteminfo)){
					List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
					paramsList.add(new BasicNameValuePair("id", id));
					paramsList.add(new BasicNameValuePair("itemname", newiteminfo));
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpRequest = new HttpPost(edit_item_url);
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramsList);
						httpRequest.setEntity(entity);
						HttpResponse httpResponse = httpClient.execute(httpRequest);
						if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
							Toast.makeText(EditItemActivity.this, "ÐÞ¸Ä³É¹¦", 500).show();
							Intent intent = new Intent(EditItemActivity.this,ListViewActivity.class);
							startActivity(intent);
							finish();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				else {
					finish();
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
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

}
