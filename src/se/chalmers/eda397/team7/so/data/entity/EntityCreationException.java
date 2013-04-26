package se.chalmers.eda397.team7.so.data.entity;

public class EntityCreationException extends RuntimeException {

	private static final long serialVersionUID = -1495312442106541428L;

	public EntityCreationException() {
		super();
	}

	public EntityCreationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public EntityCreationException(String detailMessage) {
		super(detailMessage);
	}

	public EntityCreationException(Throwable throwable) {
		super(throwable);
	}

}
