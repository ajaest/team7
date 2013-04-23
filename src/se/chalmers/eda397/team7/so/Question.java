package se.chalmers.eda397.team7.so;


public class Question {

	private String title;
	private int nAnswers;

	public Question(String title, int nAnswers) {
		super();
		this.title = title;
		this.nAnswers = nAnswers;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getnAnswers() {
		return nAnswers;
	}

	public void setnAnswers(int nAnswers) {
		this.nAnswers = nAnswers;
	}



}