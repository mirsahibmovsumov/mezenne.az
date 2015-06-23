package az.mezenne.convertor;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText; 
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import az.mezenne.R;
import az.mezenne.database.Divs;
import az.mezenne.database.MySQLiteHelper;

public class ConvertorValuta extends Activity {

	private RelativeLayout arrow;
	private MySQLiteHelper db;
	private String valuta_text;
	private Document doc;
	private RadioButton sale;
	private RadioButton purchase;
	private EditText azn;
	private EditText usd;
	private EditText eur;
	private String purchaseUSD;
	private String saleUSD;
	private String purchaseEUR;
	private String saleEUR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.convertor_valuta);
		super.onCreate(savedInstanceState);

		sale = (RadioButton) findViewById(R.id.radioButton_sale);
		purchase = (RadioButton) findViewById(R.id.radioButton_purchase);

		azn = (EditText) findViewById(R.id.edit_azn);
		usd = (EditText) findViewById(R.id.edit_usd);
		eur = (EditText) findViewById(R.id.edit_eur);

		arrow = (RelativeLayout) findViewById(R.id.back_layout);
		arrow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ConvertorValuta.this,
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
			valuta_text = div.getValuta();
		}
		{
			doc = Jsoup.parse(valuta_text);
			Elements rowCells = doc.getElementsByTag("td");
			purchaseUSD = rowCells.get(6).text();
			saleUSD = rowCells.get(7).text();

			purchaseEUR = rowCells.get(10).text();
			saleEUR = rowCells.get(11).text();

			Log.i("--ALIGN_RIGHT USD-- ", purchaseUSD + "  " + saleUSD);
			Log.i("--ALIGN_RIGHT EUR--", purchaseEUR + "  " + saleEUR);
		}
		purchase.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				azn.setText("");
				eur.setText("");
				usd.setText("");
				if (isChecked) {

					azn.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						@Override
						public void afterTextChanged(Editable s) {
							if (!azn.getText().toString().equals("")) {
								usd.setText(String.valueOf((Double
										.parseDouble(azn.getText().toString()) * 1.0 / Double
										.parseDouble(purchaseUSD))));
								eur.setText(String.valueOf((Double
										.parseDouble(azn.getText().toString()) * 1.0 / Double
										.parseDouble(purchaseEUR))));
							} else {
								usd.setText("");
								eur.setText("");
							}
						}
					});
				}

			}
		});

		sale.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				azn.setText("");
				eur.setText("");
				usd.setText("");
				if (isChecked) {

					azn.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						@Override
						public void afterTextChanged(Editable s) {
							if (!azn.getText().toString().equals("")) {
								usd.setText(String.valueOf((Double
										.parseDouble(azn.getText().toString()) * 1.0 / Double
										.parseDouble(saleUSD))));
								eur.setText(String.valueOf((Double
										.parseDouble(azn.getText().toString()) * 1.0 / Double
										.parseDouble(saleEUR))));
							} else {
								usd.setText("");
								eur.setText("");
							}
						}
					});
				}

			}
		});

	}

}
