package gxt.server.domain;

import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

public class KfBin implements ContentHandler{
	private boolean endJson = false;
	private boolean isResult = false, isArray = false;
	private ArrayList<String[]> atm = new ArrayList<String[]>();
	private String curKey;

	public KfBin() {
	}
	
	public ArrayList<String[]> getAtm(){
		return atm;
	}
	public boolean isEnd(){
	    return endJson;
	  }

	@Override
	public void startJSON() throws ParseException, IOException {
		endJson = false;		
	}
	@Override
	public void endJSON() throws ParseException, IOException {
		endJson = true;		
	}

	@Override
	public boolean startObjectEntry(String key) throws ParseException,	IOException {
//		System.out.println("---- startObjectEntry = " + key);
		this.curKey = key;
		if (key.equalsIgnoreCase("result")) isResult = true;
		return true;
	}
	
	@Override
	public boolean endObjectEntry() throws ParseException, IOException {
//		System.out.println("---- endObjectEntry()");
//		if (isResult) isResult = false;
		if (!isArray) return false;
		return true;
	}
	
	@Override
	public boolean startArray() throws ParseException, IOException {
//		System.out.println("---- startArray()");
		if (isResult) isArray = true;
		return true;
	}
	
	@Override
	public boolean endArray() throws ParseException, IOException {
//		System.out.println("---- endArray()");
		if (isResult) {
			atm.clear();
			isArray = false;
			endJson = true;
			return false;
		}
		return true;
	}


	@Override
	public boolean startObject() throws ParseException, IOException {
//		System.out.println("---- startObject()");
		if (isArray){
			atm.clear();
		}
		return true;
	}
	@Override
	public boolean endObject() throws ParseException, IOException {
//		System.out.println("---- endObject()");
		if (!isArray) endJson = false;
	    return false;
	}


	@Override
	public boolean primitive(Object value) throws ParseException, IOException {
		atm.add(new String[]{curKey, value == null?"":(String)value});
//		System.out.println("---- primitive = " + (value == null? "null":value.toString()));
		return true;
	}
}
