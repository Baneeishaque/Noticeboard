package com.noticeboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NoticeBoard extends Activity {
ListView Categ;
String cat[]={"General","Hostel","CSE","IT","EEE","ECE","AEI","ME","CE","ARCH"};

int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_board);
		
		Categ=(ListView)findViewById(R.id.listView1);

		ArrayAdapter<String> adptr=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,cat);
		Categ.setAdapter(adptr);
		
		Categ.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Toast.makeText(getApplicationContext(),cat[arg2], Toast.LENGTH_SHORT).show();
				Intent p= new Intent(getApplicationContext(),NoticeBoard1.class);
//				p.putExtra("id", (arg2+1)+"");
				p.putExtra("cats", cat[arg2]);
				startActivity(p);
			}
		});
		
//			
		
	}

}
