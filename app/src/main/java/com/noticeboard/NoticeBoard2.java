package com.noticeboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NoticeBoard2 extends Activity {
	private static String SOAP_ACTION = "http://tempuri.org/GetImg";
	private static String METHOD_NAME ="GetImg";
	 ImageView img;
	 RelativeLayout rl;
	 ZoomControls zoom;
	 String ntid="",ntsub="",im="",URL="";
	Button b;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_board2);

		if (android.os.Build.VERSION.SDK_INT > 9) 
    	{
    		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		StrictMode.setThreadPolicy(policy);
    	}
		
		rl = (RelativeLayout) findViewById(R.id.r);
		img=(ImageView)findViewById(R.id.imageView1);		
		b=(Button)findViewById(R.id.button1);
		zoom = new ZoomControls(NoticeBoard2.this);
		
		ntid=getIntent().getStringExtra("not_id");
		ntsub=getIntent().getStringExtra("not_sub");
		
		SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		URL=sh.getString("url", "");
		
		
		im=get_img(ntid);
		if(im.equalsIgnoreCase(" "))
	    {
	    }
	    else
	    {	    	
	       byte[] bt=android.util.Base64.decode(im, android.util.Base64.DEFAULT);
	       Log.d("imag.....", bt.toString());
	       Bitmap bmp=BitmapFactory.decodeByteArray(bt, 0, bt.length);
    		if(bmp!=null)
    		{     		
				img.setImageBitmap(bmp);
	    	 }
	    	     else
	    	     {
	    	    	 Log.d("bitmap.....", "nulllllllllllll");
	    	     }      		
	        }
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) LayoutParams.WRAP_CONTENT, (int) LayoutParams.WRAP_CONTENT);
        
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = 40;
        
        zoom.setLayoutParams(params);
        
        rl.addView(zoom);

        zoom.setOnZoomInClickListener(new OnClickListener() {
			
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			float x = img.getScaleX();
    			float y = img.getScaleY();
    			
    			img.setScaleX((float) (x+0.01));
    			img.setScaleY((float) (y+0.01));
    		}
    	});
            
            zoom.setOnZoomOutClickListener(new View.OnClickListener() {
    			
    		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			float x = img.getScaleX();
    			float y = img.getScaleY();
    			
    			img.setScaleX((float) (x-0.01));
    			img.setScaleY((float) (y-0.01));
    		}
    	});

		
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!im.equalsIgnoreCase(""))
				{
					File fil = new File(Environment.getExternalStorageDirectory(),"/NoticeBoard");
					fil.mkdirs();
					if(!fil.exists()){
						try {
							fil.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				File file = new File(Environment.getExternalStorageDirectory(),"/NoticeBoard/" +ntsub+ntid+".jpg");
	            FileOutputStream fos;
	            byte[] data1 = android.util.Base64.decode(im, android.util.Base64.DEFAULT);
	            try {
	                fos = new FileOutputStream(file);
	                fos.write(data1);
	                fos.flush();
	                fos.close();
	                Toast.makeText(getApplicationContext(), "Downloaded",Toast.LENGTH_LONG).show();
	            } catch (FileNotFoundException e) {
	                // handle exception
	            	Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
	            } catch (IOException e) {
	                // handle exception
	            	Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
	            }
			  }
				else{
					Toast.makeText(getApplicationContext(), "No Images to download",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	private String get_img(String ntid) {
		// TODO Auto-generated method stub
		String re="na";
		
		SoapObject request=new SoapObject(NoticeBoard1.NAMESPACE, METHOD_NAME);
 	   	request.addProperty("nid", ntid);  

 	   	SoapSerializationEnvelope env=new SoapSerializationEnvelope(SoapEnvelope.VER11);
 	    env.setOutputSoapObject(request);
 	    env.dotNet=true;
 	    try {
 	        	HttpTransportSE se=new HttpTransportSE(URL);
 	        	se.call(SOAP_ACTION, env);

 	        	Object res=(Object)env.getResponse();
 	        	if(res!=null)
 	        	{
 	        		String detail= res.toString();
 	        		if(detail.equalsIgnoreCase("na")){
 	        			Toast.makeText(getApplicationContext(),"no itemss....", Toast.LENGTH_LONG).show();
 	        		}
 	        		else
 	        		{
 	        			re=detail;
 	        		}
 	        		}
 	        	else 
 	        	{
 					Toast.makeText(getApplicationContext(),"no response", Toast.LENGTH_LONG).show();
 				}					
 			} 
 	    catch (Exception e) 
 			{
 	    	Toast.makeText(getApplicationContext(),""+e.getMessage(), Toast.LENGTH_LONG).show();
 				e.printStackTrace();
 			}      
 	    return re;
    }
}