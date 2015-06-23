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
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint({ "SetJavaScriptEnabled", "ShowToast",
		"ClickableViewAccessibility" })
public class Oil extends Activity {

	WebView webView;
	WebView webView1;
	ImageView reload;
	private RelativeLayout arrow;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.oil);
		super.onCreate(savedInstanceState);

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				Oil.this.finish();
				// overridePendingTransition(R.anim.push_up_in,
				// R.anim.push_up_out);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
		});

		// implementing reload button
		reload = (ImageView) findViewById(R.id.reload1);
		reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Oil.this, Oil.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(),
						"Internet bağlantılarınızı bir daha yoxlayın...",
						Toast.LENGTH_LONG).show();

			}
		});

		final String oil_text = "<html> "
				+ "<head>"
				+ "<script src=\"file:///android_asset/removeLinks.js\" type=\"text/javascript\"> </script>"
				+

				"</head>"
				+ "<body>"
				+ "<center> <script   type=\"text/javascript\" src=\"http://www.oil-price.net/TABLE2/gen.php?lang=en\"> </script><noscript> To get the <a href=\"http://www.gold-quote.net\">gold price</a>, please enable Javascript. </noscript></center>"
				+ "</body>" + "</html>";
		// initializing the first WebView and changing settings
		webView = (WebView) findViewById(R.id.webView_oil);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.setEnabled(false);
		webView.setClickable(false);
		webView.setWebViewClient(new MyWebViewClient());

		webView.loadDataWithBaseURL("file:///android_asset/", oil_text,
				"text/html", "UTF-8", null);

		String oil_text1 = "<html> "
				+ "<head>"
				+ "<script src=\"file:///android_asset/removeLinks.js\" type=\"text/javascript\"> </script>"
				+

				"</head>"
				+ "<body>"
				+ "<center> <script   type=\"text/javascript\" src=\"http://www.oil-price.net/widgets/brent_crude_price_large/gen.php?lang=en\"> </script><noscript> To get the <a href=\"http://www.gold-quote.net\">gold price</a>, please enable Javascript. </noscript></center>"
				+ "</body>" + "</html>";

		// initializing the second WebView and changing settings
		webView1 = (WebView) findViewById(R.id.webView_oil1);
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.getSettings().setDefaultTextEncodingName("utf-8");
		webView1.setBackgroundColor(Color.TRANSPARENT);
		webView1.loadDataWithBaseURL("file:///android_asset/", oil_text1,
				"text/html", "UTF-8", null);

		webView1.setEnabled(false);
		webView1.setClickable(false);

		webView1.setWebViewClient(new MyWebViewClient());
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
			mProgressDialog = new ProgressDialog(Oil.this, R.style.MyTheme);
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
