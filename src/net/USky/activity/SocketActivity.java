package net.USky.activity;

import java.util.ArrayList;
import java.util.List;
import net.USky.adapter.InfoAdapter;
import net.USky.entity.InfoEntity;
import net.USky.socket.ClientSocket;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.lasttest.R;

public class SocketActivity extends Activity {
	EditText input_text;
	Button send;
	ListView lv;
	List<InfoEntity> list;
	private InfoAdapter adapter;
	private ClientSocket socket;
	private String tt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		socket = new ClientSocket();
		socket.start();
		setContentView(R.layout.show_listview);
		initView();
		list = new ArrayList<InfoEntity>();
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tt = input_text.getText().toString().trim();
				socket.sendMessage(tt);
			}
		});

		// adapter = new InfoAdapter(list, SocketActivity.this);
		// String data = socket.getMessage();
		// adapter.notifyDataSetChanged();
		//
		// if (data != null && data != "") {
		// InfoEntity entity = new InfoEntity();
		// entity.setData(data);
		// list.add(entity);
		// lv.setAdapter(adapter);
		// }
	}

	// 组件初始化
	private void initView() {
		// TODO Auto-generated method stub
		input_text = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send_btn);
		lv = (ListView) findViewById(R.id.listView1);

	}

}
