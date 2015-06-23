package az.mezenne.database;

public class Article {

	private int id;
	private String title;
	private String name;
	private String image;

	public Article() {

	}

	public Article(int id, String title, String name, String image) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.image = image;

	}

	public Article(String title, String name, String image) {
		this.name = name;
		this.title = title;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
