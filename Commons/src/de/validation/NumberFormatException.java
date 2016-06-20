package de.validation;

@SuppressWarnings("serial")
public class NumberFormatException extends Exception{
	public NumberFormatException(java.lang.NumberFormatException e) {
		super(e.getMessage());
	}
}
