package az.mezenne.database;

public class Divs {

	private int id;

	private String valuta;

	private String trade;

	private String petrol;

	private String date;

	public Divs() {

	}

	public Divs(String _valuta, String _trade, String _petrol, String _date) {
		valuta = _valuta;
		trade = _trade;
		petrol = _petrol;
		date = _date;
	}

	public Divs(int _id, String _valuta, String _trade, String _petrol,
			String _date) {
		id = _id;
		valuta = _valuta;
		trade = _trade;
		petrol = _petrol;
		date = _date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPetrol() {
		return petrol;
	}

	public void setPetrol(String petrol) {
		this.petrol = petrol;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

}
