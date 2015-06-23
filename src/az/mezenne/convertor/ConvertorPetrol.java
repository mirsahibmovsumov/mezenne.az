package az.mezenne.convertor;

import java.text.DecimalFormat;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import az.mezenne.R;
import az.mezenne.database.Divs;
import az.mezenne.database.MySQLiteHelper;

public class ConvertorPetrol extends Activity {

	Spinner spinner;
	private RelativeLayout arrow;
	private EditText petrol;
	private EditText interval;
	private EditText km_litr;
	private EditText km_azn;
	private MySQLiteHelper db;
	private String petrol_text;
	private Element doc;
	private String[] petrol_array = new String[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.convertor_oil);

		petrol = (EditText) findViewById(R.id.yanacaq);
		interval = (EditText) findViewById(R.id.mesafe);
		km_litr = (EditText) findViewById(R.id.edit1);
		km_azn = (EditText) findViewById(R.id.edit2);

		spinner = (Spinner) findViewById(R.id.spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.petrol_arrays,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		super.onCreate(savedInstanceState);

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ConvertorPetrol.this,
						Convertor.class);
				startActivity(intent);
				// overridePendingTransition(R.anim.push_up_in,
				// R.anim.push_up_out);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_in_right);

			}
		});

		db = new MySQLiteHelper(getApplicationContext());
		List<Divs> divs = db.getAllDivs();
		for (Divs div : divs) {
			petrol_text = div.getPetrol();
		}
		{
			doc = Jsoup.parse(petrol_text);
			Elements rowCells = doc.getElementsByTag("td");
			petrol_array[0] = rowCells.get(1).text();
			petrol_array[1] = rowCells.get(3).text();
			petrol_array[2] = rowCells.get(5).text();
			petrol_array[3] = rowCells.get(7).text();
			Log.i("--TD Petrol-- ", petrol_array[0] + " --- " + petrol_array[1]
					+ " --- " + petrol_array[2] + " --- " + petrol_array[3]);
		}

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					final int position, long id) {

				if (!(petrol.getText().toString().equals("") || interval
						.getText().toString().equals(""))) {

					km_litr.setText(format(String.valueOf(Double.valueOf(petrol
							.getText().toString())
							/ Double.valueOf(interval.getText().toString())), 2));
					km_azn.setText(format(String.valueOf(Double.valueOf(petrol
							.getText().toString())
							* Double.valueOf(petrol_array[position])
							/ Double.valueOf(interval.getText().toString())), 3));

				}

				if (petrol.getText().toString().equals("")) {
					km_azn.setText("");
					km_litr.setText("");
				}

				if (interval.getText().toString().equals("")) {
					km_azn.setText("");
					km_litr.setText("");
				}

				petrol.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!(petrol.getText().toString().equals("") || interval
								.getText().toString().equals(""))) {

							km_litr.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									/ Double.valueOf(interval.getText()
											.toString())), 2));
							km_azn.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									* Double.valueOf(petrol_array[position])
									/ Double.valueOf(interval.getText()
											.toString())), 3));

						}
						if (petrol.getText().toString().equals("")) {
							km_azn.setText("");
							km_litr.setText("");
						}

					}
				});

				interval.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!(petrol.getText().toString().equals("") || interval
								.getText().toString().equals(""))) {

							km_litr.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									/ Double.valueOf(interval.getText()
											.toString())), 2));
							km_azn.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									* Double.valueOf(petrol_array[position])
									/ Double.valueOf(interval.getText()
											.toString())), 3));

						}
						if (interval.getText().toString().equals("")) {
							km_azn.setText("");
							km_litr.setText("");
						}

					}
				});

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

				if (!(petrol.getText().toString().equals("") || interval
						.getText().toString().equals(""))) {

					km_litr.setText(format(String.valueOf(Double.valueOf(petrol
							.getText().toString())
							/ Double.valueOf(interval.getText().toString())), 2));
					km_azn.setText(format(String.valueOf(Double.valueOf(petrol
							.getText().toString())
							* Double.valueOf(petrol_array[0])
							/ Double.valueOf(interval.getText().toString())), 3));

				}

				if (petrol.getText().toString().equals("")) {
					km_azn.setText("");
					km_litr.setText("");
				}

				if (interval.getText().toString().equals("")) {
					km_azn.setText("");
					km_litr.setText("");
				}

				petrol.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!(petrol.getText().toString().equals("") || interval
								.getText().toString().equals(""))) {

							km_litr.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									/ Double.valueOf(interval.getText()
											.toString())), 2));
							km_azn.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									* Double.valueOf(petrol_array[0])
									/ Double.valueOf(interval.getText()
											.toString())), 3));

						}
						if (petrol.getText().toString().equals("")) {
							km_azn.setText("");
							km_litr.setText("");
						}

					}
				});
				interval.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!(petrol.getText().toString().equals("") || interval
								.getText().toString().equals(""))) {

							km_litr.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									/ Double.valueOf(interval.getText()
											.toString())), 2));
							km_azn.setText(format(String.valueOf(Double
									.valueOf(petrol.getText().toString())
									* Double.valueOf(petrol_array[0])
									/ Double.valueOf(interval.getText()
											.toString())), 3));

						}
						if (interval.getText().toString().equals("")) {
							km_azn.setText("");
							km_litr.setText("");
						}

					}
				});
			}
		});

	}

	private String format(String s, int n) {
		String cost = s;
		int a = s.indexOf(".");
		s = s.substring(a + 1);
		cost = cost.substring(0, a + 1);
		while (s.length() < n) {
			s += "0";
		}
		String r = cost + s;
		r = String.valueOf(new DecimalFormat("#0.0000").format(Double
				.valueOf(r)));
		if (n == 2)
			return r + " " + "Litr";
		return r + " " + "AZN";

	}

}
