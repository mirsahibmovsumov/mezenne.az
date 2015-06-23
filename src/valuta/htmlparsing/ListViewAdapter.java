package valuta.htmlparsing;

import java.util.ArrayList;
import java.util.HashMap;

import com.squareup.picasso.Picasso;

import az.mezenne.*;
import android.annotation.SuppressLint;
import android.content.Context; 
import android.view.LayoutInflater;
import android.view.View; 
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ListViewAdapter(Context context) {
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

	private int lastPosition = -1;

	@SuppressLint("ViewHolder")
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		ImageView icon;
		TextView name;
		TextView cost;
		TextView bought;
		TextView selled;
		ImageView status;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.valuta_list_item, parent,
				false);
		// Get the position
		resultp = data.get(position);

		// Locate the TextViews in listview_item.xml
		name = (TextView) itemView.findViewById(R.id.name);
		cost = (TextView) itemView.findViewById(R.id.cost);
		bought = (TextView) itemView.findViewById(R.id.bought);
		selled = (TextView) itemView.findViewById(R.id.selled);

		// Locate the ImageView in listview_item.xml
		icon = (ImageView) itemView.findViewById(R.id.icon);
		status = (ImageView) itemView.findViewById(R.id.status);

		// Capture position and set results to the TextViews
		name.setText(resultp.get(Valuta.NAMES));
		cost.setText(format(resultp.get(Valuta.COSTS)));
		bought.setText(format(resultp.get(Valuta.BOUGHT)));
		selled.setText(format(resultp.get(Valuta.SELLED)));
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class

		Picasso.with(context).load(resultp.get(Valuta.ICONS))
				.resize(90, 70).into(icon);
		Picasso.with(context).load(resultp.get(Valuta.STATUS))
				.resize(33, 17).into(status);

		Animation animation = AnimationUtils.loadAnimation(context,
				(position > lastPosition) ? R.anim.push_down_in
						: R.anim.push_down_in);
		itemView.startAnimation(animation);
		lastPosition = position;

		return itemView;
	}

	private String format(String s) {
		String cost = s;
		int a = s.indexOf(".");
		s = s.substring(a + 1);
		cost = cost.substring(0, a + 1);
		while (s.length() < 4) {
			s += "0";
		}

		return cost + s;

	}
}