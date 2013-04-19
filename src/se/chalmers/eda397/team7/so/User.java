package se.chalmers.eda397.team7.so;

public class User {

	
	private int votesUp;
	private int votesDown;
	private int reputation;
	private String name;
	
	
	
	public User(int votesUp, int votesDown, int reputation, String name) {
		super();
		this.votesUp = votesUp;
		this.votesDown = votesDown;
		this.reputation = reputation;
		this.name = name;
	}
	
	public int getVotesUp() {
		return votesUp;
	}
	public void setVotesUp(int votesUp) {
		this.votesUp = votesUp;
	}
	public int getVotesDown() {
		return votesDown;
	}
	public void setVotesDown(int votesDown) {
		this.votesDown = votesDown;
	}
	public int getReputation() {
		return reputation;
	}
	public void setReputation(int reputation) {
		this.reputation = reputation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
