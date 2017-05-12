package client;

public class Book {
	private String[] authors;
	private String title;
	private int numPages = 0;
	Book(String a, String t) {
		String[] oneElementArray = { a };
		this.authors = oneElementArray;
		this.title = t;
	}
	Book(String a, String t, int numPages) {
		String[] oneElementArray = { a };
		this.authors = oneElementArray;
		this.title = t;
		this.numPages = numPages;
	}
	Book(String[] as, String t) {
		this.authors = as;
		this.title = t;
	}
	Book() {
		// required by Gson
	}
	public String toString() {
		return "Book from " + authors + " called " + title;
	}
	public String getTitle() {
		return title;
	}
	public String[] getAuthors() {
		return authors;
	}
}
