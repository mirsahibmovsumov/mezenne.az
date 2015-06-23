package az.mezenne;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import valuta.htmlparsing.Valuta;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import article.htmlparsing.Article;
import az.mezenne.convertor.Convertor;
import az.mezenne.database.Divs;
import az.mezenne.database.MySQLiteHelper;

public class MainActivity extends Activity {

	ImageView rate;
	ImageView convertor;
	ImageView gold;
	ImageView oil;
	ImageView valuta;
	MySQLiteHelper db;
	private ImageView article;
	static String NAME = "name";
	static String IMAGE = "image";
	static String TITLE = "title";
	static String ID = "id";

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		rate = (ImageView) findViewById(R.id.rate);
		convertor = (ImageView) findViewById(R.id.convertor);
		gold = (ImageView) findViewById(R.id.gold);
		oil = (ImageView) findViewById(R.id.oil);
		valuta = (ImageView) findViewById(R.id.valute);
		article = (ImageView) findViewById(R.id.article);

		try {

			db = new MySQLiteHelper(this);

			if (isNetworkConnected()) {

				new URLConnect().execute("http://mezenne.az/");
				new DownloadArticle()
						.execute("http://mezenne.az/?act=meqale&article_id=");

				rate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this, Rate.class);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_in_left);

					}
				});

				convertor.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent i = new Intent(MainActivity.this,
								Convertor.class);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_in_left);
					}
				});

				gold.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isNetworkConnected()) {
							Intent i = new Intent(MainActivity.this, Gold.class);
							startActivity(i);
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_in_left);
						} else {
							Toast.makeText(MainActivity.this,
									"Internet bağlantılarınızı yoxlayın...",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

				oil.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isNetworkConnected()) {
							Intent i = new Intent(MainActivity.this, Oil.class);
							startActivity(i);
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_in_left);
						} else {
							Toast.makeText(MainActivity.this,
									"Internet bağlantılarınızı yoxlayın...",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

				valuta.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isNetworkConnected()) {
							Intent i = new Intent(MainActivity.this,
									Valuta.class);
							startActivity(i);
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_in_left);
						} else {
							Toast.makeText(MainActivity.this,
									"Internet bağlantılarınızı yoxlayın...",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

				article.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isNetworkConnected()) {
							Intent i = new Intent(MainActivity.this,
									Article.class);
							startActivity(i);
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_in_left);
						} else {
							Toast.makeText(MainActivity.this,
									"Internet bağlantılarınızı yoxlayın...",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			} else {
				// Toast.makeText(MainActivity.this,
				// "İnternet bağlantılarınızı yoxlayın...",
				// Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(MainActivity.this, "Yenidən yoxlayın",
			// Toast.LENGTH_SHORT).show();
		} finally {
			db.close();
		}

		try {

			// Create a new PendingIntent and add it to the AlarmManager
			Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					MainActivity.this, 0, intent, 0);
			int interval = 60 * 60 * 1000;
			AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), interval, pendingIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class URLConnect extends AsyncTask<String, Integer, String> {

		// pBar = (ProgressBar) findViewById(R.id.progressBar1);

		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... params) {

			Document doc;

			try {

				doc = Jsoup.connect(params[0]).get();
				db = new MySQLiteHelper(MainActivity.this);
				String today = "";
				for (Divs divs : db.getAllDivs()) {
					today = divs.getDate();
				}
				if (today.equals(getDate())) {
						Log.i("IIINNNNFFFOOO", today);
				} else {
					Element valuta = doc.select("div[class=box visible]")
							.first();
					String valuta_text = valuta.toString();
					valuta_text = valuta_text.replace("<tr style",
							"<tr style=\"background:#4F81BD\"");
					valuta_text = valuta_text
							.replace("img/", "http://www.mezenne.com/img/")
							.replace(
									"<div",
									"<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body><div");
					valuta_text = valuta_text.replace("Bütün siyahı", "");

					Log.i(" information  valuta   ", valuta_text);

					Element trade = doc
							.select("div:nth-child(1) > div.post-block-style2 > div > div > div > div:nth-child(3)")
							.first();
					// ele = ele.getElementsContainingOwnText("Xam Neft");
					String trade_text = trade.toString();
					trade_text = trade_text
							.replace("img/", "http://www.mezenne.com/img/")
							.replace(
									"<div",
									"<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body><div");
					Log.i("information  trade    ", trade_text);

					Element petrol = doc
							.select("div:nth-child(1) > div.post-block-style2 > div > div > div > div:nth-child(4)")
							.first();
					String petrol_text = petrol.toString();
					petrol_text = petrol_text
							.replace(
									"<div",
									"<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body><div");
					petrol_text = petrol_text.replace("</div>",
							"</div></body></HTML>");
					petrol_text = petrol_text.replace("img/",
							"http://www.mezenne.com/img/");
					Log.i("information  petrol    ", petrol_text);

					String date = getDate();

					db.addDivs(new Divs(valuta_text, trade_text, petrol_text,
							date));
				}

			} catch (Exception e) {
				e.printStackTrace();
				// Toast.makeText(MainActivity.this,
				// "İnternet bağlantılarınızı yoxlayın...",
				// Toast.LENGTH_LONG).show();
			} finally {
				db.close();
			}
			return null;
		}

	}

	class DownloadArticle extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			Document doc;
			try {
				db = new MySQLiteHelper(MainActivity.this);

				Log.i("+++ MAX ID +++", String.valueOf(db.getMaxId()));

				if (db.getMaxId() > 0) {
					int a = (int) db.getMaxId();
					for (int j = a + 1; j <= a + 6; j++) {

						doc = Jsoup.connect(params[0] + String.valueOf(j))
								.get();

						String title = "";
						String image = "";
						String name = "";
						String id1 = "";
						if (doc.getAllElements().is(
								"h3[style=margin:5px;color:#a72121;]")) {
							title = doc
									.select("h3[style=margin:5px;color:#a72121;]")
									.first().text();
							id1 = String.valueOf(j);
							Log.i("+++ Title +++ ID",
									title + "  " + String.valueOf(j));
						}
						if (doc.getAllElements().is(
								"div[style=width:80px;margin-left:20px;]")) {
							Element url = doc
									.select("div[style=width:80px;margin-left:20px;]")
									.first().select("img").first();
							image = url.absUrl("src");
							Log.i("+++ Image +++", image);
						}
						if (doc.getAllElements()
								.is("div[style=position:absolute;left:100px;top:5px;color:#485058;font-size:14px;]")) {
							name = doc
									.select("div[style=position:absolute;left:100px;top:5px;color:#485058;font-size:14px;]")
									.first().text();
							Log.i("+++ Name +++", name);
						}

						if (!(title.equals("") && name.equals("") && image
								.equals(""))) {

							Log.i("ALL ARTICLES", db.getAllArticle().toString());

							db.addArticle(new az.mezenne.database.Article(
									Integer.parseInt(id1), title, name, image));
							Log.i("DATABASE ADDED", "ok");
						}
					}

				} else {
					int count = 0;
					for (int j = 6; j <= 1000; j++) {

						doc = Jsoup.connect(params[0] + String.valueOf(j))
								.get();
						if (count == 5) {
							j = 1001;
						}

						String title = "";
						String image = "";
						String name = "";
						String id1 = "";
						if (doc.getAllElements().is(
								"h3[style=margin:5px;color:#a72121;]")) {
							count = 0;
							title = doc
									.select("h3[style=margin:5px;color:#a72121;]")
									.first().text();
							id1 = String.valueOf(j);
							Log.i("+++ Title yeni+++ ID",
									title + "  " + String.valueOf(j));
						} else {
							count++;
						}
						if (doc.getAllElements().is(
								"div[style=width:80px;margin-left:20px;]")) {
							Element url = doc
									.select("div[style=width:80px;margin-left:20px;]")
									.first().select("img").first();
							image = url.absUrl("src");
							Log.i("+++ Image yeni+++", image);
						}
						if (doc.getAllElements()
								.is("div[style=position:absolute;left:100px;top:5px;color:#485058;font-size:14px;]")) {
							name = doc
									.select("div[style=position:absolute;left:100px;top:5px;color:#485058;font-size:14px;]")
									.first().text();
							Log.i("+++ Name yeni+++", name);
						}

						if (!(title.equals("") && name.equals("") && image
								.equals(""))) {

							db.addArticle(new az.mezenne.database.Article(
									Integer.parseInt(id1), title, name, image));
							Log.i("DATABASE ADDED", "ok");

						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				// Toast.makeText(Article.this,
				// "İnternet bağlantılarınızı yoxlayın...",
				// Toast.LENGTH_LONG).show();
			} finally {
				db.close();
			}
			return null;
		}

	}

	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		// Add current date to the TextView
		return formattedDate;
	}

}
