package az.mezenne;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import article.htmlparsing.NotificationSingleItemView;
import az.mezenne.database.MySQLiteHelper;

public class AlarmReceiver extends BroadcastReceiver {

	MediaPlayer mp = null;
	MySQLiteHelper db;
	private Element doc;
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("alllallalalla", "jjgjgjgjg");
		this.context = context;

		new DownloadForNotification()
				.execute("http://mezenne.az/?act=meqale&article_id=");

		// // For our recurring task, we'll just display a message
		// Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
	}

	class DownloadForNotification extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				db = new MySQLiteHelper(context);

				int a = (int) db.getMaxId();
				for (int j = a + 1; j <= a + 6; j++) {

					doc = Jsoup.connect(params[0]

					+ String.valueOf(j)).timeout(7000).get();

					Log.i("Girdi jsjjsjsj", "  =====Yoxladi ===");
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

						db.addArticle(new az.mezenne.database.Article(Integer
								.parseInt(id1), title, name, image));
						Log.i("DATABASE ADDED+++NOTİFİCATİON",
								"ok+++NOTİFİCATİON");

						mp = MediaPlayer.create(context, R.raw.audio);

						playSound(context);

						Uri soundUri = RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

						Intent intent1 = new Intent(context,
								NotificationSingleItemView.class);
						PendingIntent pIntent = PendingIntent.getActivity(
								context, 0, intent1,
								PendingIntent.FLAG_UPDATE_CURRENT);

						// this is it, we'll build the notification!
						// in the addAction method, if you don't want any icon,
						// just
						// set the first param to 0
						Notification mNotification = new Notification.Builder(
								context)

						.setContentTitle("Yeni məqalə").setContentText(title)
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentIntent(pIntent).setSound(soundUri)
								.setAutoCancel(true).build();

						NotificationManager notificationManager = (NotificationManager) context
								.getSystemService(Context.NOTIFICATION_SERVICE);

						// If you want to hide the notification after it was
						// selected, do the code below
						// myNotification.flags |=
						// Notification.FLAG_AUTO_CANCEL;

						notificationManager.notify(0, mNotification);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void playSound(final Context context) {

		Thread background = new Thread(new Runnable() {
			public void run() {
				try {

					mp.start();

				} catch (Throwable t) {
					Log.i("Animation", "Thread  exception " + t);
				}
			}
		});
		background.start();
	}
}