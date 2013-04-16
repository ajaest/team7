package so.chalmers.eda397.team7.so.data;

public class SQLDataRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3605491669151858265L;

	public SQLDataRuntimeException() {
		super();
	}

	public SQLDataRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SQLDataRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public SQLDataRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
