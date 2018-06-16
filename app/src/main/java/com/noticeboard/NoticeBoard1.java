package com.noticeboard;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class NoticeBoard1 extends Activity implements OnItemClickListener{
	 public static String NAMESPACE = "http://tempuri.org/";
	 private static String SOAP_ACTION1 = "http://tempuri.org/getnotice";
	 private static String METHOD_NAME1 ="getnotice";//method used to retrieve subjects
	 public String URL="";//"http://192.168.173.234/NoticeBoard/WebService.asmx";
	 ListView lst;
	 
	 String[] nt_id,nt_sub;//notice id and subjects
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_board1);
		
		if (android.os.Build.VERSION.SDK_INT > 9) 
    	{
    		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		StrictMode.setThreadPolicy(policy);
    	}
		
		lst=(ListView)findViewById(R.id.listView1);		
		String s=getIntent().getStringExtra("cats");//save the subjects into s.
		
		//retrieving the value of ipad, previously saved.
		SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String ip=sh.getString("ipad", "");
		URL="http://"+ip+"/NoticeBoard/WebService.asmx";
		
		Editor ed=sh.edit();
		ed.putString("url", URL);
		ed.commit();
		
		getits(s);
		
		lst.setOnItemClickListener(NoticeBoard1.this);
	}
		private void getits(String sb) {
		Object result;
		String re="na";
		try
        {        	 	
				SoapObject r = new SoapObject(NAMESPACE, METHOD_NAME1);
				r.addProperty("category",sb);					
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.encodingStyle = SoapSerializationEnvelope.ENC;
	            envelope.setOutputSoapObject(r);
	            envelope.dotNet=true;	            
	            
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.call(SOAP_ACTION1, envelope);
	            
	            result= (Object) envelope.getResponse();
	            if(result!=null)
	            {
		        re=result.toString();
		        Log.d("onlineusers",re);
		        
		        if(!(re.equalsIgnoreCase("na")))
		        {
		        	String itar[]=re.split("\\@");
		        	
		        	nt_id=new String[itar.length];
		        	nt_sub=new String[itar.length];
		        
		        	for(int i=0;i<itar.length;i++)
		        	{
		        		String itr[]=itar[i].split("\\#");
		        		nt_id[i]=itr[0];
		        		nt_sub[i]=itr[1];
		        	}
		        	ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,nt_sub);
		        	lst.setAdapter(ad);
		          }
	            }
        }
        catch(Exception e)
        {
        	Toast.makeText(getApplicationContext(),"exception="+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent p= new Intent(getApplicationContext(),NoticeBoard2.class);
		p.putExtra("not_id", nt_id[arg2]);
		p.putExtra("not_sub", nt_sub[arg2]);
		startActivity(p);		
	}
}