package phong.android.com;

import java.util.ArrayList;

import phong.android.com.entity.MessageWithIP;
import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.DataSingleton;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActivityChatMain extends Activity {

	private Button btnSend;
	private EditText inputMsg;
	private TextView tvMessage;
	public UserLogin me;
	public UserLogin friend;
	private BroadcastReceiver broadcastReceived;
	public ArrayList<MessageWithIP> arrMessage;
	private static final String TAG = ActivityChatMain.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chat);
		btnSend = (Button) findViewById(R.id.btnSend);
		inputMsg = (EditText) findViewById(R.id.inputMsg);
		tvMessage = (TextView) findViewById(R.id.tvChat);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		final String received = bundle.getString("username");

		me = DataSingleton.getMe();

		friend = new UserLogin(received, null, null, null, null);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(received);

		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = inputMsg.getText().toString();
				MessageWithIP msgIP = new MessageWithIP(msg, me, friend, "msg");
				String msgToServer = getGson().toJson(msgIP);
				Intent intent = new Intent("SEND_MESSAGE_TO_SERVER");
				intent.putExtra("msg", msgToServer);
				sendBroadcast(intent);
				tvMessage.append("\n " + me.getUsersName() + " : " + msg);
				inputMsg.setText("");
			}
		});
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("RECEIVED_FROM_SERVER");
		broadcastReceived = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub

				if (intent.getAction().equals("RECEIVED_FROM_SERVER")) {

					Bundle bundle = intent.getExtras();
					String msg = bundle.getString("msg");
					String sender = bundle.getString("sender");
					String received = bundle.getString("received");
					Log.e(TAG, msg + sender + received);
					if (me.getUsersName().equals(received)) {
						tvMessage.append("\n " + sender + " : " + msg);
					}

				}

			}
		};

		registerReceiver(broadcastReceived, intentFilter);

	}

	public synchronized void update() {
		ArrayList<MessageWithIP> arr = DataSingleton.getMessageList(friend);
		for (int i = arrMessage.size(); i < arr.size(); i++) {
			MessageWithIP msg = arr.get(i);
			arrMessage.add(msg);

			addMessageFromServer(arr.get(i));

		}
	}

	public void addMessageFromServer(MessageWithIP msgIP) {

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// unregisterReceiver(reserver);
	}

	public Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson;
	}

}
