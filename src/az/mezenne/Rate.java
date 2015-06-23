package az.mezenne;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import az.mezenne.database.Divs;
import az.mezenne.database.MySQLiteHelper;

public class Rate extends Activity {

	WebView webView;
	private CheckBox valuta;
	private CheckBox trade;
	private CheckBox petrol;
	private RelativeLayout arrow;
	private String valuta_text;
	private String trade_text;
	private String petrol_text;
	ImageView imageView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
		setContentView(R.layout.rate);

		valuta = (CheckBox) findViewById(R.id.valuta);
		trade = (CheckBox) findViewById(R.id.trade);
		petrol = (CheckBox) findViewById(R.id.petrol);
		webView = (WebView) findViewById(R.id.webView);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");

		MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
		List<Divs> divs = db.getAllDivs();
		for (Divs div : divs) {
			valuta_text = div.getValuta();
			trade_text = div.getTrade();
			petrol_text = div.getPetrol();
		}

		webView.setWebViewClient(new MyWebViewClient());

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				Rate.this.finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
		});

		valuta.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {

					Log.i(" information", valuta_text);

					webView.loadDataWithBaseURL("file:///android_asset/",
							valuta_text, "text/html", "UTF-8", null);

					trade.setChecked(false);
					petrol.setChecked(false);
					valuta.setEnabled(false);
					trade.setEnabled(true);
					petrol.setEnabled(true);
					RelativeLayout layout = (RelativeLayout) findViewById(R.id.upperWebView);
					layout.setVisibility(RelativeLayout.INVISIBLE);

				}

			}
		});

		trade.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Log.i("information", trade_text);
					webView.loadDataWithBaseURL("file:///android_asset/",
							trade_text, "text/html", "UTF-8", null);

					valuta.setChecked(false);
					trade.setEnabled(false);
					valuta.setEnabled(true);
					petrol.setChecked(false);
					petrol.setEnabled(true);
				}

			}
		});

		petrol.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Log.i("information", petrol_text);
					webView.loadDataWithBaseURL("file:///android_asset/",
							petrol_text, "text/html", "UTF-8", null);
					valuta.setChecked(false);
					trade.setChecked(false);
					petrol.setEnabled(false);
					valuta.setEnabled(true);
					trade.setEnabled(true);
				}

			}
		});

		valuta.setChecked(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);

			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			animate(view);
			super.onPageFinished(view, url);
		}

		private void animate(final WebView view) {
			Animation anim = AnimationUtils.loadAnimation(getBaseContext(),
					android.R.anim.slide_in_left);
			view.startAnimation(anim);
		}
	}

}