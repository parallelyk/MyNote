package com.example.mynote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;














import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends Activity {
	/** Called when the activity is first created. */

	ArrayList<HashMap<String,String>> itemsList;
	private DragDelListView mListView;
	private ProgressDialog pDialog;
	JSONParser jParser=new JSONParser();
	
	private String url_get_item = "http://192.168.1.222:80/mynote/get_item.php";
	private String url_del_item = "http://192.168.1.222:80/mynote/del_item.php";
	JSONArray items=null;
	
	Button delButton;
	TextView idTextView,itemnameTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		itemsList = new ArrayList<HashMap<String, String>>();

        new LoadAllProducts().execute();
        
	}
	


	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		 MenuItem add=menu.add(0,1,0,"添加");

	        //绑定到ActionBar 
	        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);


		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if(item.getItemId()==1) {
	    	Intent intent = new Intent(ListViewActivity.this,AddItemActivity.class);
			startActivity(intent);
			finish();
	    }
	    else{
	        //对没有处理的事件，交给父类来处理
	return super.onOptionsItemSelected(item);
	    }
	    //返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
	return true;
	}

	

		class MyAdapter extends BaseAdapter {
			private ArrayList<HashMap<String,String>> mItemsList;
			public MyAdapter(ArrayList<HashMap<String,String>> itemsList)
			{
				mItemsList=itemsList;
			}
			@Override
			public int getCount() {
				return mItemsList.size();
			}
//			public void notifyDataSetChanged()
//			{
//				
//			}
			@Override
			public HashMap<String,String> getItem(int position) {
				return mItemsList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				final int loc=position;
				ViewHolder holder=null;
				View menuView=null;
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(),
							R.layout.list_item, null);
					menuView = View.inflate(getApplicationContext(),
							R.layout.swipemenu, null);
					//合成内容与菜单
					convertView = new DragDelItem(convertView,menuView);;
					holder=new ViewHolder(convertView);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
					HashMap<String, String> item = getItem(position);
					final String id = item.get("id");
					final String itemname = item.get("itemname");
					holder.tv_title.setText(id);
					holder.tv_note.setText(itemname);
					holder.tv_edit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							String editinfo[] = new String[2];
							editinfo[0] = id;
							editinfo[1] = itemname;
							
							Intent intent = new Intent(ListViewActivity.this,EditItemActivity.class);
							intent.putExtra("editinfo", editinfo);
							startActivity(intent);
						}
					});
					holder.tv_del.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Toast.makeText(ListViewActivity.this, "delete:"+loc, Toast.LENGTH_SHORT).show();
							
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("id", id));
							params.add(new BasicNameValuePair("itemname", itemname));
							try {
								HttpPost httpRequest = new HttpPost(url_del_item);
								UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(  
					                    params); 
								//HttpEntity httpEntity = new UrlEncodedFormEntity(params,"UTF-8");
								httpRequest.setEntity(formEntiry);
								HttpClient httpClient = new DefaultHttpClient();
								HttpResponse httpResponse = httpClient.execute(httpRequest);
								
								if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
									
					                String result = EntityUtils.toString(httpResponse.getEntity());  
					                //Toast.makeText(ListViewActivity.this,result,1000).show();
					                Intent intent = new Intent(ListViewActivity.this,ListViewActivity.class);
									startActivity(intent);
									finish();
					            }else{  
					            	Toast.makeText(ListViewActivity.this, "error!", 1000).show();  
					            }  
							  
					        } catch (ClientProtocolException e) {  
					            // TODO Auto-generated catch block  
					            e.printStackTrace();  
					        } catch (IOException e) {  
					            // TODO Auto-generated catch block  
					            e.printStackTrace();  
					        } 
							
						}
					});
					
					holder.tv_note.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(ListViewActivity.this)
							.setTitle("详细笔记"+id)
							.setMessage(itemname)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							})
							.show();
						}
					});
				return convertView;
			}
			
			class ViewHolder {
				TextView tv_title,tv_note;
				TextView tv_edit,tv_del;
				public ViewHolder(View view) {
					tv_title = (TextView) view.findViewById(R.id.title);
					tv_note = (TextView) view.findViewById(R.id.info);
					tv_edit=(TextView)view.findViewById(R.id.tv_open);
					tv_del=(TextView)view.findViewById(R.id.tv_del);
					view.setTag(this);
				}
			}
		}
	

	
	
	
	
	@SuppressLint("NewApi")
	class LoadAllProducts extends AsyncTask<String,String,String>
    {

        @TargetApi(Build.VERSION_CODES.CUPCAKE)
		@SuppressLint("NewApi")
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListViewActivity.this);
            pDialog.setMessage("正在努力加载中...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(url_get_item, "GET", params);

            Log.d("All Products: ", json.toString());
            try{

                int success = json.getInt("success");
                if(success == 1)
                 {
// products found
// Getting Array of Products
                    items = json.getJSONArray("items");
// looping through All Products
                    for(int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);
// Storing each json item in variable
                        String id = c.getString("id");
                        String itemname = c.getString("itemname");
// creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
// adding each child node to HashMap key => value
                        
                        map.put("id", id);
                        map.put("itemname", itemname);
// adding HashList to ArrayList
                        itemsList.add(map);
                    }
                } else{


                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //updating parsed JSON data into ListView
                	MyAdapter adapter=new MyAdapter(itemsList);
            		mListView = (DragDelListView) findViewById(R.id.listView);
            		mListView.setAdapter(adapter);
                }
            });
        }
    }
    
}

	
	
	
	
	
	
