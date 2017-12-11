package gxt.server.domain;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

public class KfAlfa implements ContentHandler{
	private boolean bResp = false, bData = false, bMetros = false;
	private boolean endJson = false;
	private String curKey;
	private ArrayList<String[]> atm = null;
	private ArrayList<String> stack = null;

	public KfAlfa() {
		stack = new ArrayList<String>();
		atm = new ArrayList<String[]>();
	}
	public boolean isEnd(){
	    return endJson;
	  }
	
	public ArrayList<String[]> getAtm(){
		return atm;
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
	public boolean primitive(Object value) throws ParseException, IOException {
//		System.out.println("---- primitive = " + value.toString());
		if (!bResp) return true; 
		if (curKey.equalsIgnoreCase("status")){
			if (!value.toString().equalsIgnoreCase("ok")) return false;
			else return true;
		}
		if (bData){
		  String[] s = atm.get(atm.size() - 1);
		  if (curKey.equalsIgnoreCase("in") || curKey.equalsIgnoreCase("out") || curKey.equalsIgnoreCase("func")){
			s[1] = s[1] + " " + value.toString();
		  }else if (!bMetros) s[1] = value.toString();
		  else if (bMetros && curKey.equalsIgnoreCase("title")){
			String val = value.toString(); 
			String[] m = new String[s.length + 1];
			int i = 0;
			for(; i < s.length; i++) m[i] = s[i];
			if (val.indexOf("(") > -1) val = val.substring(0, val.indexOf("(")).trim();  
			m[i] = val;
			atm.set( atm.size() - 1, m );
		  }
		}
	   return true;
	}

	@Override
	public boolean startObject() throws ParseException, IOException {
		if (stack.size() == 1) atm.clear();
		return true;
	}

	@Override
	public boolean endObject() throws ParseException, IOException {
		if (bMetros) return true;
		else if (bData) return false;
		return true;
	}

	@Override
	public boolean startObjectEntry(String key) throws ParseException, IOException {
//		System.out.println("---- startObjectEntry = " + key);
		this.curKey = key;
		if (bData) {
//		  if (stack.size() == 1) atm.clear();
		  stack.add(key);
		  if (key.equalsIgnoreCase("metros")) {
			  bMetros = true;
			  atm.add(new String[]{key});
		  }
		  if (!bMetros){ 
			     atm.add(new String[]{key, ""});
			  }
		  return true;
		}
		if (key.equalsIgnoreCase("response")){
		  bResp = true;
		}else if (key.equalsIgnoreCase("data")){
		  bData = true;
		  stack.add(key);
		}
	    return true;
	}

	@Override
	public boolean endObjectEntry() throws ParseException, IOException {
//		System.out.println("---- endObjectEntry");
		if (bData) {
		  if (stack.get(stack.size() - 1).equalsIgnoreCase("metros")) bMetros = false;
		  stack.remove(stack.size() - 1);
//		  if (stack.size() == 1){
//			return false;
//		  }
		  if (stack.size() == 0) {bData = false; atm.clear();}
		}
		return true;
	}

	@Override
	public boolean startArray() throws ParseException, IOException {
//		System.out.println("---- startArray");
		return true;
	}

	@Override
	public boolean endArray() throws ParseException, IOException {
//		System.out.println("---- endArray");
		return true;
	}

}
