package gxt.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CopyModel implements Serializable{
	private static final long serialVersionUID = 8484724883330574967L;
	Long id;
	List<String> mess;// = new ArrayList<String>(); 
	List<String> model;// = new ArrayList<Model>();

	public CopyModel(){
		id = (long) 1;
		model = new ArrayList<String>();
		mess = new ArrayList<String>();
	   }
	
	public static CopyModel findCopyModel(Long Id){
	 return new CopyModel();	
	}
	
	public void AddModel(String Id1, String Id2){
		model.add(Id1 +" "+ Id2);
//		model.add(new LabVal(Id1, Id2));
	   }
	public boolean isExists(String Key1, String Key2){
		if (Key1.equals(Key2)) return true;
		for(String it: model){
		  if (it.substring(0, it.indexOf(" ")).equals(Key1)||it.substring(it.indexOf(" ")+1).equals(Key2)||
		      it.substring(0, it.indexOf(" ")).equals(Key2)||it.substring(it.indexOf(" ")+1).equals(Key1)) return true;
		}
		return false;
	}
		
//	public boolean isExists(String Key1, String Key2){
//		if (Key1.equals(Key2)) return true; 
//		for(LabVal it: model){
//		   if (it.getValue().equals(Key1)||it.getLabel().equals(Key2)||
//		 	   it.getValue().equals(Key2)||it.getLabel().equals(Key1)) return true;
//		}
//		return false;
//	}

	public void AddMess(String Mess){
		mess.add(Mess);
	}
	public List<String> getMess() {
		return mess;
	}

	public void setMess(ArrayList<String> mess) {
		this.mess = mess;
	}

	public List<String> getModel() {
		return model;
	}

	public void setModel(ArrayList<String> model) {
		this.model = model;
	}
	public Integer getVersion(){ return 1;}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
