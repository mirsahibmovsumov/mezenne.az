package article.htmlparsing;

import java.util.ArrayList;
import java.util.HashMap;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import az.mezenne.R;

public class ArticleListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ArticleListAdapter(Context context) {
		this.context = context;
		data = new ArrayList<HashMap<String, String>>();

	}

	public void addArticle(HashMap<String, String> article) {
		data.add(article);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	// private int lastPosition = -1;

	@SuppressLint("ViewHolder")
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		ImageView image;
		TextView title;
		TextView auther;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.article_list_item, parent,
				false);
		// Get the position
		resultp = data.get(position);

		// Locate the TextViews in listview_item.xml
		title = (TextView) itemView.findViewById(R.id.article_title);
		auther = (TextView) itemView.findViewById(R.id.article_auther);
		// Locate the ImageView in listview_item.xml
		image = (ImageView) itemView.findViewById(R.id.articleLogo);

		// Capture position and set results to the TextViews
		title.setText(resultp.get(Article.TITLE));
		auther.setText(resultp.get(Article.NAME));

		Picasso.with(context).load(resultp.get(Article.IMAGE)).resize(90, 90)
				.into(image);

		// Animation animation = AnimationUtils
		// .loadAnimation(
		// context,
		// // (position > lastPosition && lastPosition == -1) ?
		// R.anim.push_down_in
		// // : R.anim.push_down_in);
		// // itemView.startAnimation(animation);
		// lastPosition = position;

		return itemView;
	}

}
