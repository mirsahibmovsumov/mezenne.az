package article.htmlparsing;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import az.mezenne.R;

public class SingleItemView extends Activity {
	// Declare Variables

	WebView webView;
	int position;
	private RelativeLayout arrow;
	private Object name;
	private TextView articleName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleitemview);

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(SingleItemView.this, Article.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		webView = (WebView) findViewById(R.id.webView_article);

		// Retrieve data from MainActivity on listview item click
		Intent i = getIntent();
		// Get a single position
		position = i.getExtras().getInt("id");
		name = i.getExtras().get("name");

		articleName = (TextView) findViewById(R.id.article_name);
		articleName.setText((CharSequence) name);
		Log.i("#####  ######", String.valueOf(position));
		new DownloadArticle()
				.execute("http://mezenne.az/?act=meqale&article_id="
						+ String.valueOf(position));

	}

	class DownloadArticle extends AsyncTask<String, Integer, String> {

		private Dialog mProgressDialog;
		private String article_text;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(SingleItemView.this,
					R.style.MyTheme);
			mProgressDialog.setCancelable(true);
			((ProgressDialog) mProgressDialog)
					.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			Document doc;

			try {
				doc = Jsoup.connect(params[0]).get();
				article_text = doc.select("div[style=font-size:13px;]").first()
						.toString();
				if (position >= 13) {
					article_text = article_text.replace("/mezenne.az/img",
							"http://www.mezenne.az/mezenne.az/img");
				}

			} catch (SocketTimeoutException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String args) {

			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setDefaultTextEncodingName("utf-8");
			try {

				Log.i("Article Text", article_text);
				webView.loadDataWithBaseURL("file:///android_asset/",
						article_text, "text/html", "UTF-8", null);
			} catch (Exception e) {
				// TODO: handle exception
			}
			mProgressDialog.dismiss();
		}

	}
}