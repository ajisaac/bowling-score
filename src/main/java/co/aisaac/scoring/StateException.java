package co.aisaac.scoring;

/**
 * Checked version of IllegalStateException. Using this as a generic catch all exception, no reason to add more specific
 * exceptions.
 */
public class StateException extends Exception {

	public StateException(String msg) {
		super(msg);
	}

	public StateException(String msg, Exception ex) {
		super(msg, ex);
	}

}
