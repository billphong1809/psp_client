package phong.android.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class ActivityWellcomeSplashScreen extends Activity {
	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent i = new Intent(ActivityWellcomeSplashScreen.this,
						ActivityFirstLogin.class);
				startActivity(i);

				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
