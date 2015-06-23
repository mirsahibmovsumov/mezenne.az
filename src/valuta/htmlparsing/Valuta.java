package valuta.htmlparsing;

import az.mezenne.R;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Valuta extends Activity {
	// Declare Variables
	JSONObject jsonobject;
	JSONArray jsonarray;
	ListView listview;
	ListViewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	private RelativeLayout arrow;
	static String ICONS = "icons";
	static String NAMES = "names";
	static String STATUS = "status";
	static String BOUGHT = "bought";
	static String SELLED = "selled";
	static String COSTS = "costs";

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_valuta);
		// Initialize reload button
		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {

				Valuta.this.finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		ImageView reload = (ImageView) findViewById(R.id.reload_list_valuta);
		reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Valuta.this,
						Valuta.class);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_in_left,
				// R.anim.slide_in_right);
				Toast.makeText(getApplicationContext(),
						"Internet bağlantılarınızı bir daha yoxlayın...",
						Toast.LENGTH_LONG).show();
			}
		});
		// Execute DownloadJSON AsyncTask
		new DownloadJSON()
				.execute("http://mezenne.az/?act=valyuta#.VYEEjflVhBc");
	}

	// DownloadJSON AsyncTask
	private class DownloadJSON extends
			AsyncTask<String, HashMap<String, String>, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(Valuta.this,
					R.style.MyTheme);
			mProgressDialog.setCancelable(true);
			mProgressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
			mProgressDialog.show();

			arraylist = new ArrayList<HashMap<String, String>>();

			listview = (ListView) findViewById(R.id.listView);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter(Valuta.this);
			// Set the adapter to the ListView
			listview.setAdapter(adapter);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(String... params) {
			Document doc;
			try {
				HashMap<String, String> map;
				doc = Jsoup.connect(params[0]).get();
				Element valutas = doc.getAllElements()
						.select("div.news_content").first();
				Elements trTag = valutas.getElementsByTag("tr");
				for (Element valuta : trTag) {
					map = new HashMap<String, String>();
					Log.i("+++ ####### +++",
							valuta.getAllElements().get(1).tagName());
					if (valuta.getAllElements().get(1).tagName().equals("td")) {
						map.put(ICONS, valuta.select("img").first().absUrl("src"));
						map.put(NAMES, valuta.getElementsByTag("td").get(0)
								.text());
						map.put(COSTS, valuta.getElementsByTag("td").get(1)
								.text());
						map.put(BOUGHT, valuta.getElementsByTag("td").get(2)
								.text());
						map.put(SELLED, valuta.getElementsByTag("td").get(3)
								.text());
						map.put(STATUS, valuta.select("img").last().absUrl("src"));
						arraylist.add(map);
						publishProgress(map);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onProgressUpdate(HashMap<String, String>... values) {
			adapter.addArticle(values[0]);
			Log.i("---- onprogres Update----", values[0].toString());
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			super.onPostExecute(result);
		}
	}
}