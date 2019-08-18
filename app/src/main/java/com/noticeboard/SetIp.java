package com.noticeboard;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetIp extends Activity {
Button b;
EditText ed;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_ip);
		
		b=(Button)findViewById(R.id.button1);
		ed=(EditText)findViewById(R.id.editText1);
		
		b.setOnClickListener(new View.OnClickListener() 
		{
		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String ip=ed.getText().toString();
				if(ip.equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(), "Enter IP address", Toast.LENGTH_SHORT).show();
				}else{
					String[] spl=ip.split("\\.");
					if(spl.length==4)
					{
						//globally saving the value of ip into ipad
						SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						Editor ed=sh.edit();
						ed.putString("ipad", ip);
						ed.commit();
						
						Intent i=new Intent(getApplicationContext(), NoticeBoard.class);
						startActivity(i);						
					}else{
						Toast.makeText(getApplicationContext(), "invalid IP", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
