package article.htmlparsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import az.mezenne.R;
import az.mezenne.database.MySQLiteHelper;

public class Article extends Activity {

	static String NAME = "name";
	static String IMAGE = "image";
	static String TITLE = "title";
	static String ID = "id";

	ArticleListAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	private RelativeLayout arrow;
	MySQLiteHelper db;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.list_article);
		super.onCreate(savedInstanceState);

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {

				Article.this.finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		ImageView reload = (ImageView) findViewById(R.id.reload_list_valuta);
		reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Article.this, Article.class);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_in_left,
				// R.anim.slide_in_right);
				Toast.makeText(getApplicationContext(),
						"Internet bağlantılarınızı bir daha yoxlayın...",
						Toast.LENGTH_LONG).show();
			}
		});

		new DownloadArticle()
				.execute("http://mezenne.az/?act=meqale&article_id=");

	}

	@SuppressWarnings("unchecked")
	class DownloadArticle extends
			AsyncTask<String, HashMap<String, String>, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Create a progress dialog
			// mProgressDialog = new ProgressDialog(Article.this,
			// R.style.MyTheme);
			// mProgressDialog.setCancelable(true);
			// mProgressDialog
			// .setProgressStyle(android.R.style.Widget_ProgressBar_Large);
			// // mProgressDialog.show();

			arraylist = new ArrayList<HashMap<String, String>>();

			listView = (ListView) findViewById(R.id.listViewArticle);
			adapter = new ArticleListAdapter(Article.this);
			// Set the adapter to the ListView
			listView.setAdapter(adapter);
			Log.i("+++ ADAPTER +++", arraylist.toString());
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					int viewId = Integer.parseInt(arraylist.get(position).get(
							"id"));
					String name = arraylist.get(position).get("title");
					Intent i = new Intent(Article.this, SingleItemView.class);
					i.putExtra("id", viewId);
					i.putExtra("name", name);
					Log.i("------   ---------", String.valueOf(viewId));
					startActivity(i);
				}
			});

		}

		@Override
		protected String doInBackground(String... params) {

			Document doc;
			try {
				db = new MySQLiteHelper(Article.this);

				Log.i("+++ MAX ID +++", String.valueOf(db.getMaxId()));

				if (db.getMaxId() > 0) {

					HashMap<String, String> map = new HashMap<String, String>();
					List<az.mezenne.database.Article> articles = db
							.getAllArticle();
					for (az.mezenne.database.Article article : articles) {
						map = new HashMap<String, String>();
						map.put(TITLE, article.getTitle());
						map.put(IMAGE, article.getImage());
						map.put(NAME, article.getName());
						map.put(ID, String.valueOf(article.getId()));
						publishProgress(map);
						arraylist.add(map);
						Log.i("+++ LOG +++", map.toString());

					}

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
							map = new HashMap<String, String>();
							map.put(TITLE, title);
							map.put(IMAGE, image);
							map.put(NAME, name);
							map.put(ID, String.valueOf(j));
							arraylist.add(map);
							publishProgress(map);

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

						HashMap<String, String> map = new HashMap<String, String>();
						if (!(title.equals("") && name.equals("") && image
								.equals(""))) {

							db.addArticle(new az.mezenne.database.Article(
									Integer.parseInt(id1), title, name, image));
							Log.i("DATABASE ADDED", "ok");

							map.put(TITLE, title);
							map.put(IMAGE, image);
							map.put(NAME, name);
							map.put(ID, String.valueOf(j));
							arraylist.add(map);
							publishProgress(map);

						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				// Toast.makeText(Article.this,
				// "İnternet bağlantılarınızı yoxlayın...",
				// Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(HashMap<String, String>... values) {

			adapter.addArticle(values[0]);
			Log.i("---- onprogres Update----", values[0].toString());
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String args) {

			// Locate the listview in listview_main.xml

			// Pass the results into ListViewAdapter.java
			if (arraylist.toString() == "[]")
				Toast.makeText(Article.this,
						"İnternet bağlantılarınızı yoxlayın...",
						Toast.LENGTH_LONG).show();

			// Close the progress dialog
			// mProgressDialog.dismiss();

		}
	}
}
