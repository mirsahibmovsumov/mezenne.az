package az.mezenne.convertor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import az.mezenne.R;

public class Convertor extends Activity {

	ImageView oil;
	ImageView valuta;
	private RelativeLayout arrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.convertor);
		// initialize valuta button
		valuta = (ImageView) findViewById(R.id.valuta);
		// initialize oil button
		oil = (ImageView) findViewById(R.id.oil);
		// initialize back return imageView
		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				Convertor.this.finish();
				// overridePendingTransition(R.anim.push_up_in,
				// R.anim.push_up_out);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
		});

		valuta.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Convertor.this, ConvertorValuta.class);
				startActivity(i);
			}
		});

		oil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Convertor.this, ConvertorPetrol.class);
				startActivity(i);

			}
		});
		super.onCreate(savedInstanceState);
	}

}
