package phong.android.com;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityRegistration extends Activity {
	private EditText edUsername;
	private EditText edPassWord;
	public EditText edCFpass;
	private EditText edEmail;
	private Button btnDone;
	public String URL_SERVER_REGISTER = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		edUsername = (EditText) findViewById(R.id.re_username);
		edPassWord = (EditText) findViewById(R.id.re_pass);
		edCFpass = (EditText) findViewById(R.id.re_confirm);
		edEmail = (EditText) findViewById(R.id.re_email);
		btnDone = (Button) findViewById(R.id.btnDone);

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String username = edUsername.getText().toString();
				String password = edPassWord.getText().toString();
				String cfPassword = edPassWord.getText().toString();
				String email = edEmail.getText().toString();

				if (cfPassword == password) {
					URL_SERVER_REGISTER += "?username=" + username
							+ "&password=" + password + "&email=" + email;
					makeJsonObjectRequestSendWitdParameter();

				} else {
					Toast.makeText(getApplicationContext(),
							"Check your pass and confirm !", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	private void makeJsonObjectRequestSendWitdParameter() {

	}

}
