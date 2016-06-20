package de.remotes;

import java.text.ParseException;
import java.util.Date;

import javax.ejb.Remote;

import de.validation.NumberFormatException;

@Remote
public interface PoliciesRemote {
	
	Integer parseInt(String i) throws NumberFormatException;
	
	Date parseDate(String date) throws ParseException;
	
	String format(Date date) throws ParseException;
	
	void parseError(StringBuffer report, String name, String value);
	 
}