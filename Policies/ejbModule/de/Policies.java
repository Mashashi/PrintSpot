package de;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;

import de.remotes.PoliciesRemote;
import de.validation.NumberFormatException;

@Stateless(name="Defined")
public class Policies implements PoliciesRemote {
	
	private Policies(){}
	
	// https://en.wikipedia.org/wiki/ISO_8601
	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
	
	public Date parseDate(String date) throws ParseException{
		return date!=null ? formatter.parse(date) : null;
	}
	
	public String format(Date date) throws ParseException{
		return date==null ? null : formatter.format(date);
	}
	
	public void parseError(StringBuffer report, String name, String value){
		report.append("The \""+name+"\" field with value \""+value+"\" is not in ISO 8601 valid date format.");
		report.append("\n");
	}

	@Override
	public Integer parseInt(String i) throws NumberFormatException {
		if(i == null || i.length() == 0){
			return null;
		}else{
			try{
				return Integer.parseInt(i);
			}catch(java.lang.NumberFormatException e){
				throw new NumberFormatException(e);
			}
		}
	}
	
	/*public String validateNulls(){
	StringBuffer report = new StringBuffer("");
	for(Field f: this.getClass().getDeclaredFields()){
		try {
			if(f.get(this)==null)
				report.append("Field "+f.getAnnotation(XmlElement.class).name()+" can not be null");
		} catch (IllegalArgumentException|IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	return report.toString();
	}*/
}
