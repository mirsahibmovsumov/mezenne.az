package az.mezenne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class Gold extends Activity {

	WebView webView;
	WebView webView1;
	private RelativeLayout arrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.gold);
		super.onCreate(savedInstanceState);

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				Gold.this.finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
		});

		String gold_text = "<html> "
				+ "<head>"
				+ "<script src=\"file:///android_asset/removeLinks.js\" type=\"text/javascript\"> </script>"
				+

				"</head>"
				+ "<body>"
				+ "<center> <script   type=\"text/javascript\" src=\"http://www.gold-quote.net/TABLE2/gen.php?lang=en\"> </script><noscript> To get the <a href=\"http://www.gold-quote.net\">gold price</a>, please enable Javascript. </noscript></center>"
				+ "</body>" + "</html>";

		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.loadDataWithBaseURL("file:///android_asset/", gold_text,
				"text/html", "UTF-8", null);
		webView.setWebViewClient(new MyWebViewClient());

		String gold_text1 = "<html> "
				+ "<head>"
				+ "<script src=\"file:///android_asset/removeLinks.js\" type=\"text/javascript\"> </script>"
				+

				"</head>"
				+ "<body>"
				+ "<center> <script   type=\"text/javascript\" src=\"http://www.gold-quote.net/widgets/gold-price-estimator/gen.php?lang=en\"> </script><noscript> To get the <a href=\"http://www.gold-quote.net\">gold price</a>, please enable Javascript. </noscript></center>"
				+ "</body>" + "</html>";

		webView1 = (WebView) findViewById(R.id.webView1);
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.getSettings().setDefaultTextEncodingName("utf-8");
		webView1.setBackgroundColor(Color.TRANSPARENT);
		webView1.loadDataWithBaseURL("file:///android_asset/", gold_text1,
				"text/html", "UTF-8", null);
		webView1.setWebViewClient(new MyWebViewClient());

		ImageView reload = (ImageView) findViewById(R.id.reload2);

		reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Gold.this, Gold.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(),
						"Internet bağlantılarınızı bir daha yoxlayın...",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	private class MyWebViewClient extends WebViewClient {

		private ProgressDialog mProgressDialog;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);

			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			mProgressDialog = new ProgressDialog(Gold.this, R.style.MyTheme);
			mProgressDialog.setCancelable(true);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
			mProgressDialog.show();

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			animate(view);
			mProgressDialog.dismiss();
			super.onPageFinished(view, url);
		}

		private void animate(final WebView view) {
			Animation anim = AnimationUtils.loadAnimation(getBaseContext(),
					android.R.anim.slide_in_left);
			view.startAnimation(anim);
		}
	}
}
