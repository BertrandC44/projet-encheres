package fr.eni.encheres.exception;
import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception{
	

	private static final long serialVersionUID = 1L;
	
	
	private List<String> errors = new ArrayList<String>();

	public Iterable<String> getErrors() {
		return errors;
	}
	
	public void add(String message) {
		errors.add(message);
	}
	
	public boolean hasError() {
		return errors.isEmpty();
	}


	public void addError(String message) {
		errors.add(message);
	}


//	private List<String> messages;
//
//	public BusinessException() {
//		this.messages = new ArrayList<String>();
//	}
//
//	public BusinessException(String string) {
//		this.messages = new ArrayList<String>();
//	}
//
//	public Iterable<String> getMessagesBE() {
//		return messages;
//	}


}


