package gxt.server.domain;

import gxt.server.servlet.SessionCounter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.JSONParser;

import ui.UserInfo;

import jpa.atm.AtCatalog;
import jpa.atm.AtCatalogExt;
import jpa.atm.AtCatalogStat;
import jpa.atm.AtMetro;
import jpa.atm.AtMetroExt;
import jpa.atm.AtMetroLine;
import jpa.atm.AtMetroStat;
import jpa.atm.AtMstation;
import jpa.atm.AtMstationExt;
import jpa.atm.AtMstationStat;
import jpa.atm.AtOkato;
import jpa.atm.AtPart;
import jpa.atm.AtServ;
import jpa.atm.AtServExt;
import jpa.atm.AtServStat;
import jpa.atm.AtTel;
import jpa.atm.AtTelExt;
import jpa.atm.AtTelStat;
import jpa.atm.AtTerm;
import jpa.atm.ExtTerm;
import jpa.atm.ExtTermStat;
import jpa.atm.F250Town;

import anytools.JsonLog;
import anytools.JsonSend;

//import com.google.gwt.json.client.JSONParser;
//import com.google.gwt.json.client.JSONParser;
//import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public class Dao {
    private static final EntityManagerFactory emfUser = Persistence.createEntityManagerFactory("jpaUser");

//    private static final String USER_BY_SERT = "select d from Arm_user d where UPPER(d.certId)=?1";
//    private static final String FI_BY_USER_ID = "select fi.branch_code from reporter.ARM#USERS#FILIALS fu, ows.f_i fi where fu.id_arm_users=? and fi.id = fu.f_i";
//    private static String SEL_COUNT_T = "select count(t)";
    private static final String SERVLET_KEYS = "tKeys";
    private static final String FLG_UPDATE = "update";
    private static final String PROJECT = "gxt.atm";
    private static final String LOGURL = "/rsys-adm/log";
    private String urlServlet;
    public String getUrlServlet() {
		return urlServlet;
	}
//    private static final String LOGURL = "/rsys-adm/log";
//    private String url = RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString();
//    private String HOSTURL = url.substring(0, url.indexOf("/", url.indexOf("/", url.indexOf("/") + 1) + 1));

    UserInfo uf;
//    = new UserInfo(RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString(),PROJECT){
//	    @Override
//	    protected String TransRole(String TabRole){
//		if (TabRole.equals("REPS_ATM_ADM")) return "A";
//		if (TabRole.equals("REPS_ATM")) return "U";
//		else return "X";
//	    }
//	    protected void WriteLog(String Mess, long UserId){
//	    	String url = RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString();
//	    	String hostUrl = url.substring(0, url.indexOf("/", url.indexOf("/", url.indexOf("/") + 1) + 1));
//	    	System.out.println("getRequestURL() = " + HOSTURL + LOGURL);
//	    	System.out.println("url = " + url);
//	    	int i1 = url.indexOf("/");
//	    	int i2 = url.indexOf("/", i1 + 1);
//	    	int i3 = url.indexOf("/", i2 + 1);
//	    	System.out.println("getRequestURL() = " +i1+" "+i2+" "+i3+" "+ url.substring(0, i3));
//	    	System.out.println("getRequestURL() = " + url.substring(0, url.indexOf("/", url.indexOf("/", url.indexOf("/") + 1) + 1)));

//	    	JsonLog json = new JsonLog();
//			json.setClassMet(this.getClass().getName()+".setAttrs");
//			json.setLevelLog(JsonLog.LEVEL_M);
//			json.setMessage(Mess);
//			json.setMessType("login");
//			json.setProject(PROJECT);
//			json.setTrace("");
//			json.setUser(UserId);
//			JsonSend js = new JsonSend(json, HOSTURL + LOGURL);
//			js.Send(false);
			
//try{
//	HttpsURLConnection con = (HttpsURLConnection) new URL(hostUrl + LOGURL).openConnection();
////	HttpsURLConnection con = (HttpsURLConnection) new URL("https://msk-hibtest.corp.icba.biz:8181/rsys/startPoint/log").openConnection();
////	HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8181/rsys/startPoint/log").openConnection();
////	System.out.println("con = " + con);
//	con.setRequestMethod("POST");
//	con.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//	con.setRequestProperty("Content-Length", ""+json.obj2json().toJSONString().length());
////  	 con.setReadTimeout(curTimeOut);
//	 con.setDoOutput(true);
//	 con.setDoInput(true);
//	 con.connect();
////	 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//	 OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
////	 wr.writeBytes(json.obj2json().toJSONString());
//	 wr.write(json.obj2json().toJSONString());
//	 wr.flush();
//	 wr.close();
//	 con.getOutputStream().close();
//
//}catch(Exception e){
//	e.printStackTrace();
//}
//			JsonSend js = new JsonSend(json, "localhost/rsys/startPoint/log");
//			js.Send(false);
//			System.out.println("responseCode");
//			System.out.println("responseCode = " + js.Send(false));
//	    }
//      }; 
//      public static class ByteArrayPrintWriter extends PrintWriter {
//    	    public ByteArrayPrintWriter(OutputStream out) {
//    	        super(out);
//    	    }
//    	}
    public static EntityManager emUser() {
	   EntityManager em = emfUser.createEntityManager();
	   em.setFlushMode(FlushModeType.COMMIT);
	   return em;
	   }
    
    public Dao(){
    	setUserInfo(RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString());
    }
    public Dao(String url){
    	setUserInfo(url);
    }
    private void setUserInfo(String url){
    	urlServlet = url.substring(0, url.indexOf("/", url.indexOf("/", url.indexOf("/") + 1) + 1)) + LOGURL;
    	uf = new UserInfo(urlServlet, PROJECT){
    	    @Override
    	    protected String TransRole(String TabRole){
    			if (TabRole.equals("REPS_ATM_ADM")) return "A";
    			if (TabRole.equals("REPS_ATM")) return "U";
    			else return "X";
    	    }
    	    @Override
    	    protected void SetSesUid(String Uid){
    	    	SessionCounter counter = SessionCounter.getInstance(RequestFactoryServlet.getThreadLocalRequest().getServletContext());
    	    	String sesId = RequestFactoryServlet.getThreadLocalRequest().getSession().getId();
    	    	String[] s = (String[])counter.getSessions().get(sesId);
                s[SessionCounter.SES_UID] = Uid;
                counter.getSessions().put( sesId, s);
    	    }
        };
    }
//=========================== Import ATM ===============================
    public List<String> importBanks(InputStreamReader Import, String BankId, long uId) throws Exception{
    	List<String> ret = new ArrayList<String>();
    	if (BankId.equals(String.valueOf(AtPart.PID_ALFA))) ret = importAlfa(Import, uId);
//    	else if (BankId.equals(String.valueOf(AtPart.PID_BIN))) ret = importBin(Import, uId);
    	else if (BankId.equals(String.valueOf(AtPart.PID_BIN))) {
        	StringWriter writer = new StringWriter();
        	IOUtils.copy(Import, writer);//, encoding);
        	String impString = writer.toString();
//        	System.out.println("impString.substring(0,10).indexOf = " + impString.substring(0,10));
        	if (impString.substring(0,10).indexOf("{") >= 0) ret = importJsonBin(impString, uId);
        	else ret = importCsvBin(impString, uId);
    	}
    	else {
    		ret.add("Для этого банка загрузки нет");
    	}
    	return ret;
    }
//=====================CSV BIN BANK
    public List<String> importCsvBin(String Import, long uId){
    	ArrayList<String> mess = new ArrayList<String>();
		final int F_ACTIVE = 3;
		final int F_TERM = 12;
		final int F_CITY = 16;
		final int F_TIME = 17;
		final int F_ADR = 15;
		final int F_LOC = 21;
		final int F_SHI = 26;
		final int F_DOL = 18;
		final int F_METRO = 19;
//		ArrayList<String[]> obj = null;
		String address = "", loc = "", term = "", city = "", metro = "", shi = "", dol = "";
		String[] time = new String[7];
		F250Town town = null;
		String line = null;
		BufferedReader br = null;
		String[] flds = null;
		boolean isReaded = false;
		ExtTerm atTerm = null;
		int i = 1;
		try{
//		    br = new BufferedReader(new FileReader(new File("C:/ep36/jpaAt2b/src/jpa/atm.csv"),""));
//			br = new BufferedReader(new InputStreamReader(new FileInputStream("C:/ep36/jpaAt2b/src/jpa/atm.csv"), "CP1251"));
//			InputStreamReader is = new InputStreamReader(new FileInputStream("C:/ep36/jpaAt2b/src/jpa/atm.csv"));
//			byte b[] = Import.getBytes("CP1251"); 
//			br = new BufferedReader(new StringReader(new String(b)));
			br = new BufferedReader(new StringReader(Import));
			line = br.readLine();
			while(true){
			   address = ""; loc = ""; term = ""; city = ""; metro = ""; shi = ""; dol = "";
			   setVal(time, "");
			   i = 0;
			   if (!isReaded) line = br.readLine();
			   if (line == null) break;
			   if (!isReaded) {flds = line.split(";"); }
//			   else i = 2;
			   if (!flds[F_ACTIVE].trim().equals("Y")) continue;
			   term = flds[F_TERM].trim();
			   if (term.length() < 8 ) break;
			   city = flds[F_CITY].trim();
			   address = flds[F_ADR].trim();
			   loc =  flds[F_LOC].trim().replaceAll("(^\")|(\"$)", "").replaceAll("(\"\")", "\"").replaceAll("\u00AB|\u00BB", "'");
			   shi = flds[F_SHI].trim();
			   dol = flds[F_DOL].trim();
			   metro = flds[F_METRO].trim();
			   time[i] = flds[F_TIME].trim();
			   i++;
			   isReaded = false;
			   for(; i < 9; i++){
				if ((line = br.readLine()) != null){
				  flds = line.split(";");
				  if (!term.equals(flds[F_TERM].trim())) {isReaded = true; break;}
				}else break;
				if ( i < 7 ) time[i] = flds[F_TIME].trim();
			   }
			   if (isReaded) {
//				   System.out.println("Атм = " + term + " мало записей");
				   mess.add("Атм = " + term + " мало записей");
				   continue;
			   }
//			   if (i < 7) {
//				 System.out.println("Атм = " + term + " мало записей");
//				 continue;
//			   }
			   
			   try{
	 		     town = getBinCity(city);
			   }catch(Exception e){
//				 System.out.println("Терминал не добавлен " + term + ", " + e.getMessage());
				 mess.add("Терминал не добавлен " + term + ", " + e.getMessage());
				 continue;
			   }
			   try{
			     atTerm = getBinCsvTerm(term, town, address, loc, dol, shi, time, mess);
			     updBinMetro(metro, atTerm, town);
				 updBinServ(atTerm);
			   }catch(Exception e){mess.add(e.getMessage());  }
			}
		   }catch(Exception e){
			  e.printStackTrace();
	       }finally{
	    	   if (br != null) try{br.close();}catch(Exception e){};
	       }

    	return mess;
    }
	static private ExtTerm getBinCsvTerm(String Term, F250Town City, String Address, String Loc, String Dol, String Shi, String[] Time, ArrayList<String> Mess) throws Exception{
		EntityManager em = emfUser.createEntityManager();
		AtPart part = em.find(AtPart.class, AtPart.PID_BIN);
		List<ExtTerm> term = null;
		ExtTerm oTerm = null;
//		System.out.println("shi = " + Shi);
//		int sub = Address.indexOf("<br>");
//		String adr = Address.substring(0, sub);
//		String loc = Address.substring(sub + 4);
//		String[] s = adr.split(","); 
//		if (s[0].indexOf(City.getRusName()) >= 0) adr = s[1];
	try{
	   term = em.createQuery("select t from "+ExtTerm.class.getSimpleName()+ " t where t.atPart=?1 and t.term='"+Term+"'", ExtTerm.class).setParameter(1, part).getResultList();
       em.getTransaction().begin();
       if (!term.isEmpty()){
         oTerm = term.get(0);
         if (oTerm.getF250Town().getId() != oTerm.getId()) oTerm.setF250Town(City);
          oTerm.setAddr(Address);
          oTerm.setLocation(Loc);
          oTerm.setDol(Dol);
          oTerm.setShi(Shi);
          oTerm.setIsForall("Y");
          updBinCsvProcessing( oTerm, Time, Mess);
          em.merge(oTerm);
       }else{
         oTerm = new ExtTerm();
         oTerm.setTerm(Term);
         oTerm.setAtPart(part);
         oTerm.setF250Town(City);
         oTerm.setAddr(Address);
         oTerm.setLocation(Loc);
         oTerm.setDol(Dol);
         oTerm.setShi(Shi);
         updBinCsvProcessing( oTerm, Time, Mess);
         em.persist(oTerm);
//         System.out.println("Добавлен терминал " + oTerm.getTerm());
         Mess.add("Добавлен терминал " + oTerm.getTerm());
       }
       em.getTransaction().commit();

       return oTerm;
	}catch(Exception e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
	      throw new Exception("Term = " + Term + "; " + e.getMessage());
	}
	}

	static private void updBinCsvProcessing(ExtTerm oTerm, String[] Time, ArrayList<String> Mess) throws Exception{
//		String timeMatch = "^(\\d{2}):(\\d{2}) *- *(\\d{2}):(\\d{2})$";
		String timeMatch = "^([01]\\d|2[0-3]):([0-5]\\d) *- *([01]\\d|2[0-3]):([0-5]\\d)$";
		  String[] Days = new String[7]; setVal(Days, "N");
		  String[] DaysA = new String[7]; setVal(DaysA, "N");
		  String[] wfr = new String[7], wto = new String[7], bfr = new String[7], bto = new String[7];

//		  int w = Work.indexOf("ежедневно");
//		  if (w >= 0) Work = Work.substring(w+9).trim();
		  
		  for(int i = 0; i < Time.length; i++){
			  if (Time[i].matches("^(\\d{2}):(\\d{2}) *- *00:00$")||
				  Time[i].matches("^(\\d{2}):(\\d{2}) *- *24:00")) {
				Time[i] = Time[i].substring(0, 7) + "23:59"; 
//				  System.out.println("Time[i] = "+Time[i]);
			  }
			  
			  if (Time[i].equals("00:00 - 00:00")){
				DaysA[i] = "Y";
			  }else if (Time[i].matches(timeMatch)){
				String[] t = Time[i].split("-");   
			    String begTime = t[0].trim(); 
			    String endTime = t[1].trim();  
		    	try{
				  if (!testTime( begTime, endTime)){ 
				   	 bfr[i] = endTime; bto[i] = begTime;
				     wfr[i]=null; wto[i]=null;
				     setVal(DaysA, "Y");
				  }else{
				     wfr[i] = begTime; wto[i] = endTime;
				     bfr[i] = null; bto[i] = null;
				   }
//				   setVal(Days, "Y");
			    }catch(Exception e){
			      oTerm.setDaysNotrans(Time[i]);
			      Mess.add("Атм = "+oTerm.getTerm() + "; ошибка во времени работы = " + Time[i]);
			    }
			  }else{
				oTerm.setDaysNotrans(Time[i]);
		        Mess.add("Атм = "+oTerm.getTerm() + "; ошибка во времени работы = " + Time[i]);
			  }
			  Days[i] = "Y"; 
		  }
		  
		  oTerm.setDMo(Days[0]); oTerm.setDMoAll(DaysA[0]); oTerm.setDMoFr(wfr[0]); oTerm.setDMoTo(wto[0]);oTerm.setpMoFr(bfr[0]);oTerm.setpMoTo(bto[0]);
		  oTerm.setDTu(Days[1]); oTerm.setDTuAll(DaysA[1]); oTerm.setDTuFr(wfr[1]); oTerm.setDTuTo(wto[1]);oTerm.setpTuFr(bfr[1]);oTerm.setpTuTo(bto[1]);
		  oTerm.setDWe(Days[2]); oTerm.setDWeAll(DaysA[2]); oTerm.setDWeFr(wfr[2]); oTerm.setDWeTo(wto[2]);oTerm.setpWeFr(bfr[2]);oTerm.setpWeTo(bto[2]);
		  oTerm.setDTh(Days[3]); oTerm.setDThAll(DaysA[3]); oTerm.setDThFr(wfr[3]); oTerm.setDThTo(wto[3]);oTerm.setpThFr(bfr[3]);oTerm.setpThTo(bto[3]);
		  oTerm.setDFr(Days[4]); oTerm.setDFrAll(DaysA[4]); oTerm.setDFrFr(wfr[4]); oTerm.setDFrTo(wto[4]);oTerm.setpFrFr(bfr[4]);oTerm.setpFrTo(bto[4]);
		  oTerm.setDSa(Days[5]); oTerm.setDSaAll(DaysA[5]); oTerm.setDSaFr(wfr[5]); oTerm.setDSaTo(wto[5]);oTerm.setpSaFr(bfr[5]);oTerm.setpSaTo(bto[5]);
		  oTerm.setDSu(Days[6]); oTerm.setDSuAll(DaysA[6]); oTerm.setDSuFr(wfr[6]); oTerm.setDSuTo(wto[6]);oTerm.setpSuFr(bfr[6]);oTerm.setpSuTo(bto[6]);

	      Set<ConstraintViolation<ExtTerm>> cvs = null;
	 	  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Validator validator = factory.getValidator();
		  cvs = validator.validate(oTerm);
		  if (cvs.size() != 0) {
		    for (ConstraintViolation<?> cv : cvs)
//		   	 System.out.println(String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath()));
		     throw new Exception(oTerm.getTerm()+ String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath())); 
	   	   }
	}

//=====================JSON BIN BANK
//    public List<String> importBin(InputStreamReader Import, long uId){
    public List<String> importJsonBin(String Import, long uId){
    	ArrayList<String> mess = new ArrayList<String>();
    	
		ArrayList<String[]> obj = null;
		String address = "", term = "", city = "", work_time = "", metro = "", shi = "", dol = "";
		JSONParser parser = new JSONParser();
		F250Town town = null;
		
		try{
			KfBin finder = new KfBin();
			while(!finder.isEnd()){
			   parser.parse(Import, finder, true);
			   obj = finder.getAtm();
			   if (obj.size() == 0) continue;
//			   StringBuilder b = new StringBuilder(); 
			   address = ""; term = ""; city = ""; work_time = ""; metro = "";
			   shi = ""; dol = "";
			   for(String[] it: obj){
				   if (it[0].equals("id")) term = it[1];
				   else if (it[0].equals("work_time")) work_time = it[1].trim();
				   else if (it[0].equals("metro")) metro = it[1].trim();
				   else if (it[0].equals("city")) city = it[1].trim();
				   else if (it[0].equals("address")) address = it[1].trim();
				   else if (it[0].equals("latitude")) shi = it[1].trim();
				   else if (it[0].equals("longitude")) dol = it[1].trim();
			   }
			   try{
 			     town = getBinCity(city);
			   }catch(Exception e){
//				  System.out.println("Терминал не добавлен " + term + ", " + e.getMessage());
				  mess.add("Терминал не добавлен " + term + ", " + e.getMessage());
				  continue;
			   }
			    try{
			     ExtTerm atTerm = getBinTerm(term, town, address, dol, shi, work_time, mess);
			     updBinMetro(metro, atTerm, town);
			     updBinServ(atTerm);
			   }catch(Exception e){ mess.add(e.getMessage()); }
			}
		   }catch(Exception e){
			  e.printStackTrace();
	       }

    	return mess;
    }
    
    ExtTerm getBinTerm(String Term, F250Town City, String Address, String Dol, 
    		           String Shi, String Work_time, ArrayList<String> Mess) throws Exception{
		EntityManager em = emfUser.createEntityManager();
		AtPart part = em.find(AtPart.class, AtPart.PID_BIN);
		List<ExtTerm> term = null;
		ExtTerm oTerm = null;
		
		int sub = Address.indexOf("<br>");
		String adr = Address.substring(0, sub);
		String loc = Address.substring(sub + 4);
		String[] s = adr.split(","); 
		if (s[0].indexOf(City.getRusName()) >= 0) adr = adr.substring(adr.indexOf(",") + 1);
	try{
	   term = em.createQuery("select t from "+ExtTerm.class.getSimpleName()+ " t where t.atPart=?1 and t.term='"+Term+"'", ExtTerm.class).setParameter(1, part).getResultList();
       em.getTransaction().begin();
       if (!term.isEmpty()){
         oTerm = term.get(0);
         if (oTerm.getF250Town().getId() != oTerm.getId()) oTerm.setF250Town(City);
          oTerm.setAddr(adr);
          oTerm.setLocation(loc.replaceAll("\u00AB|\u00BB", "'"));
          oTerm.setDol(Dol);
          oTerm.setShi(Shi);
          oTerm.setIsForall("Y");
          updBinProcessing( oTerm, Work_time, Mess);
          em.merge(oTerm);
       }else{
         oTerm = new ExtTerm();
         oTerm.setTerm(Term);
         oTerm.setAtPart(part);
         oTerm.setF250Town(City);
         oTerm.setAddr(adr);
         oTerm.setLocation(loc.replaceAll("\u00AB|\u00BB", "'"));
         oTerm.setDol(Dol);
         oTerm.setShi(Shi);
         updBinProcessing( oTerm, Work_time, Mess);
         em.persist(oTerm);
//         System.out.println("Добавлен терминал " + oTerm.getTerm());
         Mess.add("Добавлен терминал " + oTerm.getTerm());
       }
       em.getTransaction().commit();

       return oTerm;
	}catch(Exception e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
	      throw new Exception("Атм = " + Term + "; " + e.getMessage());
	}
	}
    
    void updBinProcessing(ExtTerm oTerm, String Work, ArrayList<String> Mess) throws Exception{
//		String timeMatch = "^(\\d{2}):(\\d{2}) *- *(\\d{2}):(\\d{2})$";
		String timeMatch = "^([01]\\d|2[0-3]):([0-5]\\d) *- *([01]\\d|2[0-3]):([0-5]\\d)$";
		  String[] Days = new String[7]; setVal(Days, "N");
		  String[] DaysA = new String[7]; setVal(DaysA, "N");
		  String[] wfr = new String[7], wto = new String[7], bfr = new String[7], bto = new String[7];

		  int w = Work.indexOf("ежедневно");
		  if (w >= 0) Work = Work.substring(w+9).trim();
		  
		  if (Work.equals("круглосуточно")) {
	    	setVal(Days, "Y");
	    	setVal(DaysA, "Y");
	      }else if (Work.matches(timeMatch)){
	        String[] t = Work.split("-");   
	    	String begTime = t[0].trim(); 
	    	String endTime = t[1].trim();
	    	try{
		       if (!testTime( begTime, endTime)){ 
		           for(int i = 0; i < 7; i++){
		    	   	bfr[i] = endTime; bto[i] = begTime;
		    	   	wfr[i]=null; wto[i]=null;
		    	   }
		      	  setVal(DaysA, "Y");
		       }else{
		      	   for(int i = 0; i < 7; i++){
		    	    wfr[i] = begTime; wto[i] = endTime;
		    	    bfr[i] = null; bto[i] = null;
		    	   }
		       }
		    	 setVal(Days, "Y");
		     }catch(Exception e){
		      oTerm.setDaysNotrans(Work);
//		   	  System.out.println("Term = "+oTerm.getTerm() + "; time ignored = " + Work);
		   	  Mess.add("Атм = "+oTerm.getTerm() + "; time ignored = " + Work);
	         }
	      }else{
	        String bwTime = null; 
		    String ewTime = null;
		    String bbTime = null; 
		    String ebTime = null;
		    String[] week = Work.split(",");
		    if (week.length < 2 ) {
		      oTerm.setDaysNotrans(Work);
		    }else for(String item: week){
		      String[] days = item.trim().split("\\s");
		      String time = days[1];
		      if (time.equals("круглосуточно")) time = "always";
              else {
		       	 if (!time.matches(timeMatch)) {
//		       	   oTerm.setDaysNotrans(Work);
		       	   Mess.add("Атм = "+oTerm.getTerm() + "; ошибка во времени работы = " + time);
//		       	   System.out.println("Атм = "+oTerm.getTerm() + "; ошибка во времени работы = " + time);
		       	   continue;
		          }
		         bwTime = time.substring( 0, 5); 
		      	 ewTime = time.substring( 6, 11);
		         bbTime = null; 
		      	 ebTime = null;
		      	 try{
		      	    if (!testTime( bwTime, ewTime)){ 
		      	    	bbTime = ewTime; ewTime = null;
		      	        ebTime = bwTime; bwTime = null;
		      	      }
		      	  }catch(Exception e){
//		           	 System.out.println("time ignored = " + time);
		           continue;
		      	  }
		       }
		      String d = getNumDays(days[0].trim());
		      char[] b = d.toCharArray();
		            for(int bi = 0; bi < b.length; bi++){
		         	 int it = Character.digit(b[bi], 10) - 1;
		              Days[it] = "Y";
		         	 if (time.equals("always")) DaysA[it] = "Y";
		         	 else {
		    	    	  wfr[it] = bwTime; wto[it] = ewTime;
		 	    	  bfr[it] = bbTime; bto[it] = ebTime;
		 	    	  if (bfr[it] != null) DaysA[it] = "Y";
		         	 }
		            }
//		            res = res + "time = " + time + "; days = " + d +"; ";
		          } 
	      }
		  
		  oTerm.setDMo(Days[0]); oTerm.setDMoAll(DaysA[0]); oTerm.setDMoFr(wfr[0]); oTerm.setDMoTo(wto[0]);oTerm.setpMoFr(bfr[0]);oTerm.setpMoTo(bto[0]);
		  oTerm.setDTu(Days[1]); oTerm.setDTuAll(DaysA[1]); oTerm.setDTuFr(wfr[1]); oTerm.setDTuTo(wto[1]);oTerm.setpTuFr(bfr[1]);oTerm.setpTuTo(bto[1]);
		  oTerm.setDWe(Days[2]); oTerm.setDWeAll(DaysA[2]); oTerm.setDWeFr(wfr[2]); oTerm.setDWeTo(wto[2]);oTerm.setpWeFr(bfr[2]);oTerm.setpWeTo(bto[2]);
		  oTerm.setDTh(Days[3]); oTerm.setDThAll(DaysA[3]); oTerm.setDThFr(wfr[3]); oTerm.setDThTo(wto[3]);oTerm.setpThFr(bfr[3]);oTerm.setpThTo(bto[3]);
		  oTerm.setDFr(Days[4]); oTerm.setDFrAll(DaysA[4]); oTerm.setDFrFr(wfr[4]); oTerm.setDFrTo(wto[4]);oTerm.setpFrFr(bfr[4]);oTerm.setpFrTo(bto[4]);
		  oTerm.setDSa(Days[5]); oTerm.setDSaAll(DaysA[5]); oTerm.setDSaFr(wfr[5]); oTerm.setDSaTo(wto[5]);oTerm.setpSaFr(bfr[5]);oTerm.setpSaTo(bto[5]);
		  oTerm.setDSu(Days[6]); oTerm.setDSuAll(DaysA[6]); oTerm.setDSuFr(wfr[6]); oTerm.setDSuTo(wto[6]);oTerm.setpSuFr(bfr[6]);oTerm.setpSuTo(bto[6]);

	      Set<ConstraintViolation<ExtTerm>> cvs = null;
	 	  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Validator validator = factory.getValidator();
		  cvs = validator.validate(oTerm);
		  if (cvs.size() != 0) {
		    for (ConstraintViolation<?> cv : cvs)
//		    Mess.add(oTerm.getTerm()+ String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath()));
//		   	 System.out.println(String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath()));
		     throw new Exception(oTerm.getTerm()+ String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath())); 
	   	   }
	}
    
    void updBinMetro(String Metro, ExtTerm Term, F250Town Town) throws Exception{
		   EntityManager em = emfUser.createEntityManager();
		   List<AtMetroExt> oldMetro = null;
		   String[] Metros = new String[]{};
		   oldMetro = em.createQuery("select t from "+AtMetroExt.class.getSimpleName()+ " t where t.atTerm=?1", AtMetroExt.class).setParameter(1, Term).getResultList();
	try{
		 if (Metro != null && !Metro.isEmpty())  
			 Metros = Metro.split(",");
		 em.getTransaction().begin();

		 for(int m = 0; m < Metros.length; m++){
	        String it1 = Metros[m].replace("ё", "е").trim();
			int o = 0; 
		    for(; o < oldMetro.size(); o++){
		      if (it1.equalsIgnoreCase(oldMetro.get(o).getAtMstation().getName())) break;
		    }
		    if (o < oldMetro.size()) oldMetro.remove(o);
		    else {
		     int i = 0;
		     for (; i < m; i++) if (Metros[i].equals(it1)) break; 
		     if ( i < m ) continue;
		     List<AtMstationExt> mets = em.createQuery("select t from "+AtMstationExt.class.getSimpleName()+ " t, "+
		    		                                AtMetroLine.class.getSimpleName()+ " l where upper(t.name)='"+it1.toUpperCase()+"' and t.atMetroLine=l and l.f250Town=?1", AtMstationExt.class).setParameter(1, Town).getResultList();
		     if (mets.isEmpty()) throw new Exception("метро не найдено "+it1);
		     AtMetroExt newMetro = new AtMetroExt();
		     newMetro.setAtMstation(mets.get(0));
		     newMetro.setAtTerm(Term);
	   	     em.persist(newMetro);
		    }
		 }
		 for(AtMetroExt it: oldMetro){
		   	em.remove(it);
		 }
	     em.getTransaction().commit();
	}catch(Exception e){
		if (em.getTransaction().isActive()) em.getTransaction().rollback();
		throw new Exception("Атм = "+Term.getTerm() + "; "+e.getMessage());
	}

	}
    
    F250Town getBinCity(String City) throws Exception{
		EntityManager em = emfUser.createEntityManager();
		List<F250Town> town = null;
	    AtOkato reg = null;

	    town = em.createQuery("select t from "+F250Town.class.getSimpleName()+ " t where upper(t.rusName)='"+City.toUpperCase()+"'", F250Town.class).getResultList();
     	 if (town.isEmpty()) throw new Exception("Город "+ City +" не найден");

     	return town.get(0); 
	}

    void updBinServ(ExtTerm atTerm) throws Exception{
		 long[] def = new long[]{20250,20257}; 
		 EntityManager em = emfUser.createEntityManager();
		 List<AtServExt> oldServ = null;
		 oldServ = em.createQuery("select t from "+AtServExt.class.getSimpleName()+ " t where t.atTerm=?1", AtServExt.class).setParameter(1, atTerm).getResultList();
		 if (!oldServ.isEmpty()) return;
	try{
		 em.getTransaction().begin();
		 for(long it: def){
	       AtServExt newServ = new AtServExt();
	       newServ.setAtCatalog(em.find(AtCatalogExt.class, it));
	       newServ.setAtTerm(atTerm);
  	       em.persist(newServ);
		 }
		 em.getTransaction().commit();
	}catch(Exception e){
		if (em.getTransaction().isActive()) em.getTransaction().rollback();
		throw new Exception("updBinServ() Term = "+atTerm.getTerm());
	}

	}
//=====================ALFA BANK
    public List<String> importAlfa(InputStreamReader Import, long uId){
    	JSONParser parser = new JSONParser();
    	ArrayList<String> mess = new ArrayList<String>();
		ArrayList<String[]> obj = null;
		String dol = "", shi = "";
		String dop = "", address = "", term = "", city = "", city_code = "", region = "", 
				servs_in = "", servs_out = "", processing = "";
		String[] metros = null;
try{
	  KfAlfa finder = new KfAlfa();
	  List<ExtTerm> allTerms = getAlfaTerm();
		while(!finder.isEnd()){
		   parser.parse(Import, finder, true);
		   obj = finder.getAtm();
		   if (obj.size() == 0) continue;
		   term = ((String[])obj.get(0))[0]; 
		   metros = null; servs_out = ""; servs_in = ""; dop = ""; city = ""; city_code = ""; region = "";dol = ""; shi = "";
		   for(String[] it: obj){
			 if (it[0].equals("id")) term = it[1];
			 else if (it[0].equals("title"))	 dop = it[1].replace("&nbsp;"," ").replaceAll("&raquo;|&laquo;|&mdash;", "\"");
			 else if (it[0].equals("address")) address = it[1].replace("&nbsp;"," ");
			 else if (it[0].equals("lon")) dol = it[1];
			 else if (it[0].equals("lat")) shi = it[1];
			 else if (it[0].equals("city")) city = it[1];
			 else if (it[0].equals("city_code")) city_code = it[1];
			 else if (it[0].equals("processing")) processing = it[1];
			 else if (it[0].equals("region")) {region = it[1]; if (region.length() == 1) region = "0"+region;}
			 else if (it[0].equals("out")) servs_out = it[1];
			 else if (it[0].equals("in")) servs_in = it[1];
			 else if (it[0].equals("metros")) {metros = it;}
		   }
		   F250Town town = getAlfaCity(term.trim(), city.trim(), city_code.trim(), region.trim());
		   ExtTerm atTerm = getAlfaTerm(term.trim(), town, dop, address, dol, shi, processing, mess);
 		   int i = 0;
		   String[] in = servs_in.trim().split(" ");
 		   for(int j = 0; j < in.length; j++){
 			 if (!in[j].isEmpty()){  
			  in[j] = "in_" + in[j];
			  i++;
 			 }
			}
		   String[] out = servs_out.trim().split(" ");
		   for(int j = 0; j < out.length; j++){
	 	     if (!out[j].isEmpty()){  
	 		  out[j] = "out_" + out[j];
	 		  i++;
	 	 	 }
		   }
		   String[] servs = new String[i + 2]; //visa, mc
		   servs[i] = "visa"; servs[i+1] = "mastercard";
		   if (servs.length - 2 > 0 ){
			 for(String it: in) if (!it.isEmpty())servs[--i] = it;  
			 for(String it: out) if (!it.isEmpty())servs[--i] = it;
		   }
		   updAlfaServ(servs, atTerm);
		   updAlfaMetro(metros, atTerm, town);
		   for(int x = 0; x < allTerms.size(); x++){
			 if (allTerms.get(x).getTerm().equals(atTerm.getTerm())){
				 allTerms.remove(x);
				 break;
			 }
		   }
		}
		removeRest(allTerms, mess);
//		uf.WriteLog( "атм из альфа", this.getClass().getName()+".importBanks", JsonLog.LEVEL_M, "atmproc", PROJECT, "");
//		HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
		JsonLog jl = new JsonLog( "атм из альфа", this.getClass().getName()+".importBanks", JsonLog.LEVEL_M, "atmproc", PROJECT, "", uId);
		jl.WriteLog(getUrlServlet());
}catch(Exception e){
	  mess.add("Ошибка " + e.getMessage());	
//	  uf.WriteLog( "атм из альфа", this.getClass().getName()+".importBanks", JsonLog.LEVEL_E, "atmproc", PROJECT, JsonLog.stackTraceToString(e));
//	  HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
	  JsonLog jl = new JsonLog( "атм из альфа", this.getClass().getName()+".importBanks", JsonLog.LEVEL_E, "atmproc", PROJECT, JsonLog.stackTraceToString(e), uId /*Long.parseLong(m.get(UserInfo.KEY_UI))*/);
	  jl.WriteLog(getUrlServlet());
	  e.printStackTrace();
}
    	
    	return mess;
    }
//-----
	static private void removeRest(List<ExtTerm> AllTerms, List<String> Mess) throws Exception{
		  EntityManager em = emfUser.createEntityManager();
		  em.getTransaction().begin();
	 	  for(ExtTerm it: AllTerms){
	 		ExtTerm t = em.find(ExtTerm.class, it.getId());
	 		t.setIsForall("N");
	 		em.merge(t);
	 		Mess.add("Деактивирован терминал " + it.getTerm());
//			em.remove(em.find(ExtTerm.class, it.getId()));
		 }
	     em.getTransaction().commit();
		  
		}
//-----	
	private static ExtTerm getAlfaTerm(String Term, F250Town town, String dop, String address, 
            String dol, String shi, String processing, List<String> Mess) throws Exception{
            EntityManager em = emfUser.createEntityManager();
        AtPart part = null;
        List<ExtTerm> term = null;
        ExtTerm oTerm = null;
        if (dol.indexOf(".") < 0) dol = dol + ".0"; 
        if (shi.indexOf(".") < 0) shi = shi + ".0";
try{
        part = em.createQuery("select t from "+AtPart.class.getSimpleName()+ " t where upper(t.name)='АЛЬФА-БАНК'", AtPart.class).getSingleResult();
        term = em.createQuery("select t from "+ExtTerm.class.getSimpleName()+ " t where t.atPart=?1 and t.term='"+Term+"'", ExtTerm.class).setParameter(1, part).getResultList();

        em.getTransaction().begin();
        if (!term.isEmpty()){
          oTerm = term.get(0);
          if (oTerm.getF250Town().getId() != oTerm.getId()) oTerm.setF250Town(town);
           oTerm.setAddr(address);
           oTerm.setLocation(dop);
           oTerm.setDol(dol.toString());
           oTerm.setShi(shi.toString());
           oTerm.setIsForall("Y");
           updProcessing( oTerm, processing);
           em.merge(oTerm);
        }else{
          oTerm = new ExtTerm();
          oTerm.setTerm(Term);
          oTerm.setAtPart(part);
          oTerm.setF250Town(town);
          oTerm.setAddr(address);
          oTerm.setLocation(dop);
          oTerm.setDol(dol.toString());
          oTerm.setShi(shi.toString());
          updProcessing( oTerm, processing);
          em.persist(oTerm);
          Mess.add("Добавлен терминал " + oTerm.getTerm());
        }
        em.getTransaction().commit();
       return oTerm;
}catch(Exception e){
      if (em.getTransaction().isActive()) em.getTransaction().rollback();
      throw new Exception("Term = " + Term + "; " + e.getMessage());
}
}
//-----
	private static ExtTerm updProcessing( ExtTerm oTerm, String processing) throws Exception{
	      String timeMatch = "^(\\d{2}):(\\d{2})-(\\d{2}):(\\d{2})$";
		  String[] Days = new String[7]; setVal(Days, "N");
		  String[] DaysA = new String[7]; setVal(DaysA, "N");
		  String[] wfr = new String[7], wto = new String[7], bfr = new String[7], bto = new String[7];
	      if (processing.equals("круглосуточно")) {
	    	  setVal(Days, "Y");
	    	  setVal(DaysA, "Y");
	      }else if (processing.matches(timeMatch)){
	    	  String begTime = processing.substring( 0, 5); 
	    	  String endTime = processing.substring( 6, 11);
	    	  try{
	    	    if (!testTime( begTime, endTime)){ 
	    	      for(int i = 0; i < 7; i++){
	    	    	bfr[i] = endTime; bto[i] = begTime;
	    	    	wfr[i]=null; wto[i]=null;
	    	      }
	      	      setVal(DaysA, "Y");
	            }else{
	      	      for(int i = 0; i < 7; i++){
	    	    	  wfr[i] = begTime; wto[i] = endTime;
	    	    	  bfr[i] = null; bto[i] = null;
	    	      }
	            }
	    	    setVal(Days, "Y");
	    	  }catch(Exception e){
	    		  oTerm.setDaysNotrans(processing);
//	    		  System.out.println("alfa Term = "+oTerm.getTerm() + "; time ignored = "+processing);
	    	  }
	      }else{
	         String bwTime = null; 
	   	     String ewTime = null;
	         String bbTime = null; 
	   	     String ebTime = null;
	         String[] week = processing.split("<br/>");
	          if (week.length < 2 ) {
	        	oTerm.setDaysNotrans(processing);
	          }else  for(String item: week){
	            String[] days = item.split("\\s");
	            String time = days[1];
	            if (time.equals("круглосуточно")) time = "always";
	            else if (time.equals("выходной")) time = "F";
	            else {
	         	 if (!time.matches(timeMatch)) {
//	          	   System.out.println("alfa Term = "+oTerm.getTerm() + "; time ignored = " + time);
	         	   continue;
	              }
	              bwTime = time.substring( 0, 5); 
	      	     ewTime = time.substring( 6, 11);
	              bbTime = null; 
	      	     ebTime = null;
	      	     try{
	      	      if (!testTime( bwTime, ewTime)){ 
	      	    	bbTime = ewTime; ewTime = null;
	      	        ebTime = bwTime; bwTime = null;
	      	      }
	      	     }catch(Exception e){
//	           	 System.out.println("time ignored = " + time);
	         	 continue;
	      	     }
	            }
	            String d = getNumDays(days[0].trim());
	            char[] b = d.toCharArray();
	            for(int bi = 0; bi < b.length; bi++){
	         	 int it = Character.digit(b[bi], 10) - 1;
	              Days[it] = "Y";
	         	 if (time.equals("always")) DaysA[it] = "Y";
	         	 else if (time.equals("F")) Days[it] = "N";
	         	 else {
	    	    	  wfr[it] = bwTime; wto[it] = ewTime;
	 	    	  bfr[it] = bbTime; bto[it] = ebTime;
	 	    	  if (bfr[it] != null) DaysA[it] = "Y";
	         	 }
	            }
//	            res = res + "time = " + time + "; days = " + d +"; ";
	          }
	       }
		  oTerm.setDMo(Days[0]); oTerm.setDMoAll(DaysA[0]); oTerm.setDMoFr(wfr[0]); oTerm.setDMoTo(wto[0]);oTerm.setpMoFr(bfr[0]);oTerm.setpMoTo(bto[0]);
		  oTerm.setDTu(Days[1]); oTerm.setDTuAll(DaysA[1]); oTerm.setDTuFr(wfr[1]); oTerm.setDTuTo(wto[1]);oTerm.setpTuFr(bfr[1]);oTerm.setpTuTo(bto[1]);
		  oTerm.setDWe(Days[2]); oTerm.setDWeAll(DaysA[2]); oTerm.setDWeFr(wfr[2]); oTerm.setDWeTo(wto[2]);oTerm.setpWeFr(bfr[2]);oTerm.setpWeTo(bto[2]);
		  oTerm.setDTh(Days[3]); oTerm.setDThAll(DaysA[3]); oTerm.setDThFr(wfr[3]); oTerm.setDThTo(wto[3]);oTerm.setpThFr(bfr[3]);oTerm.setpThTo(bto[3]);
		  oTerm.setDFr(Days[4]); oTerm.setDFrAll(DaysA[4]); oTerm.setDFrFr(wfr[4]); oTerm.setDFrTo(wto[4]);oTerm.setpFrFr(bfr[4]);oTerm.setpFrTo(bto[4]);
		  oTerm.setDSa(Days[5]); oTerm.setDSaAll(DaysA[5]); oTerm.setDSaFr(wfr[5]); oTerm.setDSaTo(wto[5]);oTerm.setpSaFr(bfr[5]);oTerm.setpSaTo(bto[5]);
		  oTerm.setDSu(Days[6]); oTerm.setDSuAll(DaysA[6]); oTerm.setDSuFr(wfr[6]); oTerm.setDSuTo(wto[6]);oTerm.setpSuFr(bfr[6]);oTerm.setpSuTo(bto[6]);

	      Set<ConstraintViolation<ExtTerm>> cvs = null;
	 	  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Validator validator = factory.getValidator();
		  cvs = validator.validate(oTerm);
		  if (cvs.size() != 0) {
		    for (ConstraintViolation<?> cv : cvs)
//		   	 System.out.println(String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath()));
		     throw new Exception(oTerm.getTerm()+ String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath())); 
	   	   }

		  return oTerm;
		}
//-----
	private static String getNumDays(String s) throws Exception{
	    String ret = "";
		if (s.length() == 2){
		  ret =  String.valueOf(getNumDay(s));
		  }else if (s.matches("(\\p{Ll}{2})-(\\p{Ll}{2})")){
		    String[] per = s.split("-");
		    int end = getNumDay(per[1]);
		    for(int i = getNumDay(per[0]); i <= end; i++){
		      ret = ret + String.valueOf(i);
		    }
		  }else if(s.matches("(\\p{Ll}{2}),(\\p{Ll}{2})")){
		    String[] per = s.split(",");
		    ret = String.valueOf(getNumDay(per[0])) + String.valueOf(getNumDay(per[1]));
		  }else ret = "X";
		
	    return ret;
	  }
	  private static int getNumDay(String s) throws Exception{
		    int ret = 0;
		    String names = "пнвтсрчтптсбвс";
		    int n = names.indexOf(s);
		    if ((n >= 0) && (n%2 == 0)){
			ret = n / 2 + 1;   
		    }else throw new Exception("day not found "+s);
		    return ret;
		  }

	static private void setVal(String[] a, String s){
		for(int i = 0; i < 7; i++){
		    a[i] = s;
		}
	}
	static private boolean testTime(String from, String to) throws Exception{
        Calendar f = Calendar.getInstance();
       f.set(Calendar.HOUR_OF_DAY, Integer.parseInt(from.substring(0,2)));
       f.set(Calendar.MINUTE, Integer.parseInt(from.substring(3,5)));
       f.set(Calendar.SECOND, 0);
       Calendar t = Calendar.getInstance();
       t.set(Calendar.HOUR_OF_DAY, Integer.parseInt(to.substring(0,2)));
       t.set(Calendar.MINUTE, Integer.parseInt(to.substring(3,5)));
       t.set(Calendar.SECOND, 0);
     return (f.getTimeInMillis() <= t.getTimeInMillis());
}
//-----	
	static private List<ExtTerm> getAlfaTerm() throws Exception{
		  EntityManager em = emfUser.createEntityManager();
		  AtPart part = em.createQuery("select t from "+AtPart.class.getSimpleName()+ " t where upper(t.name)='АЛЬФА-БАНК'", AtPart.class).getSingleResult();
		  return em.createQuery("select t from "+ExtTerm.class.getSimpleName()+ " t where t.atPart=?1 and t.isForall='Y'", ExtTerm.class).setParameter(1, part).getResultList();
		}
//-----	
	private static F250Town getAlfaCity(String Term, String City, String CityCode, String Region) throws Exception{
		EntityManager em = emfUser.createEntityManager();
		List<F250Town> town = null;
	 AtOkato reg = null;
	 try{
		reg = em.createQuery("select t from "+AtOkato.class.getSimpleName()+ " t where t.region='"+Region+"'", AtOkato.class).getSingleResult();
		town = em.createQuery("select t from "+F250Town.class.getSimpleName()+ " t where t.alfaBank='"+CityCode+"' and t.atOkato=?1", F250Town.class).setParameter(1, reg).getResultList();
     	if (town.isEmpty()) {
     	 town = em.createQuery("select t from "+F250Town.class.getSimpleName()+ " t where upper(t.rusName)='"+City.toUpperCase()+"' and t.atOkato=?1", F250Town.class).setParameter(1, reg).getResultList();
     	 if (!town.isEmpty()) {
     	   em.getTransaction().begin();
     	   F250Town t = town.get(0);
     	   t.setAlfaBank(CityCode);
     	   em.merge(t);
     	   em.getTransaction().commit();
     	   return t;
     	 }else{
     	   F250Town t = new F250Town();
     	   t.setAlfaBank(CityCode);
     	   t.setAtOkato(reg);
     	   t.setName(CityCode);
     	   t.setRusName(City);
     	   t.setTelCode("XXX");
     	   em.getTransaction().begin();
     	   em.persist(t);
     	   em.getTransaction().commit();
     	   return t;
     	 }
     	}
   	  }catch(Exception e){
   		  throw new Exception("Term = " +Term + "; city = " + CityCode + "; Region = '" +Region + "'; " + e.getMessage());
   	  }
	 return town.get(0); 
	}
//-----	
	private static void updAlfaMetro(String[] Metros, ExtTerm Term, F250Town Town) throws Exception{
		   EntityManager em = emfUser.createEntityManager();
		   List<AtMetroExt> oldMetro = null;
		   oldMetro = em.createQuery("select t from "+AtMetroExt.class.getSimpleName()+ " t where t.atTerm=?1", AtMetroExt.class).setParameter(1, Term).getResultList();
	try{
		 em.getTransaction().begin();
		 int m = 1;
		 if (Metros == null){
			 Metros = new String[]{""};
		 }
		 for(; m < Metros.length; m++){
	        String it1 = Metros[m].replace("ё", "е");
			int o = 0; 
		    for(; o < oldMetro.size(); o++){
		      if (it1.equalsIgnoreCase(oldMetro.get(o).getAtMstation().getName())) break;
		    }
		    if (o < oldMetro.size()) oldMetro.remove(o);
		    else {
		     int i = 1;
		     for (; i < m; i++) if (Metros[i].equals(it1)) break; 
		     if ( i < m ) continue;
		     List<AtMstationExt> mets = em.createQuery("select t from "+AtMstationExt.class.getSimpleName()+ " t, "+
		    		                                AtMetroLine.class.getSimpleName()+ " l where upper(t.name)='"+it1.toUpperCase()+"' and t.atMetroLine=l and l.f250Town=?1", AtMstationExt.class).setParameter(1, Town).getResultList();
		     AtMetroExt newMetro = new AtMetroExt();
		     newMetro.setAtMstation(mets.get(0));
		     newMetro.setAtTerm(Term);
	   	     em.persist(newMetro);
		    }
		 }
		 for(AtMetro it: oldMetro){
		   	em.remove(it);
		 }
	     em.getTransaction().commit();
	}catch(Exception e){
		if (em.getTransaction().isActive()) em.getTransaction().rollback();
		throw new Exception("Term = "+Term.getTerm()+"; Metros = " + Metros.length+"; "+e.getMessage());
	}
 	}
//-----		
		private static void updAlfaServ(String[] Servs, ExtTerm Term) throws Exception{
		 EntityManager em = emfUser.createEntityManager();
//		 String[] s = Servs.trim().split(" ");
		 List<AtServExt> oldServ = null;
		 oldServ = em.createQuery("select t from "+AtServExt.class.getSimpleName()+ " t where t.atTerm=?1", AtServExt.class).setParameter(1, Term).getResultList();	    
	try{
		 em.getTransaction().begin();
		 for(String it: Servs){
			int o = 0; 
		    for(; o < oldServ.size(); o++){
		      if (it.equalsIgnoreCase(oldServ.get(o).getAtCatalog().getAlfaBank())) break;
		    }
		    if (o < oldServ.size()) oldServ.remove(o);
		    else if (!it.isEmpty()){
//		     System.out.println("it=|"+it+"|"); 
		     AtCatalogExt cat = em.createQuery("select t from "+AtCatalogExt.class.getSimpleName()+ " t where t.alfaBank='"+it+"'", AtCatalogExt.class).getSingleResult();
		     AtServExt newServ = new AtServExt();
		     newServ.setAtCatalog(cat);
		     newServ.setAtTerm(Term);
	   	     em.persist(newServ);
//	   	     System.out.println("em.persist " + Term.getTerm()+"; serv = " + it);
		    }
		 }
		 for(AtServExt it: oldServ){
	   	   em.remove(it);
//	   	   System.out.println("em.remove " + Term.getTerm()+"; serv = " + it.getAtCatalog().getName());
		 }
	     em.getTransaction().commit();
	}catch(Exception e){
		if (em.getTransaction().isActive()) em.getTransaction().rollback();
		throw new Exception("Term = "+Term.getTerm()+"; Servs = " + Servs +"; "+e.getMessage());
	}
		}

//=========================== Copy atm =================================
    static final String sqlTermExists = "select t from "+ExtTerm.class.getSimpleName()+" t, "+
                        AtPart.class.getSimpleName()+" p where t.term=?1 and t.atPart=p and p.name='МДМ'";
    public CopyModel doTestCopy(String s) {
    	EntityManager em = emUser();
    	CopyModel cm = new CopyModel();
    	Query sql = em.createQuery(sqlTermExists);
		String[] its = s.split(",");
		for(String it: its){
		  String[] arr = it.trim().split(" +");
		  String from = arr[0].trim();
		  String to = arr[1].trim();
		  if (cm.isExists(from, to)) cm.AddMess(it + "  дублируется");
		  else {
	 try {
     	    List<ExtTerm> resFrom = sql.setParameter(1, from).getResultList();
     	    List<ExtTerm> resTo = sql.setParameter(1, to).getResultList();
     	    if (resFrom.size() == 0 ) cm.AddMess(from + "  не найден");
     	    if (resTo.size() == 0 ) cm.AddMess(to + "  не найден");
     	    if ((resFrom.size() > 0) && (resTo.size() > 0 )) cm.AddModel(from, to);
     	    
     } catch (Exception ex) {
   	       ex.printStackTrace();
		   throw new RuntimeException(ex);
	 }
		  }
		}
//		System.out.println("ret = " + ret);
	    return cm;
    }
    
	public CopyModel doTrueCopy(List<String> atms) {
		CopyModel cm = new CopyModel();
		EntityManager em = emUser();
		ExtTerm From, To;
		Query sql = em.createQuery(sqlTermExists);

		for(String it: atms){
		   String[] arr = it.trim().split(" +");
		   String from = arr[0].trim();
		   String to = arr[1].trim();
 try {
		   From = (ExtTerm) sql.setParameter(1, from).getSingleResult();
    	   To = (ExtTerm) sql.setParameter(1, to).getSingleResult();
    	   em.getTransaction().begin();
    	   em.detach(From);
    	   From.setId(0);
    	   From.setTerm(To.getTerm());
    	   From.setOId(To.getOId());
    	   From.setChanged(new java.util.Date());
//    	   for(AtServ srv :To.getAtServs()){
//    		   em.remove(srv);
//    	   }
    	   em.remove(To);
    	   em.flush();
//    	   System.out.println( "From.getAtServs().size() = " + From.getClass().getSimpleName());
    	   em.persist(From);
    	   for(AtServExt srv :From.getAtServs()){
    		   em.detach(srv);
    		   srv.setId(0);
    		   srv.setAtTerm(From);
    		   srv.setChanged(new java.util.Date());
    		   em.persist(srv);
    	   }
    	   for(AtTelExt tel :From.getAtTels()){
    		   em.detach(tel);
    		   tel.setId(0);
    		   tel.setAtTerm(From);
    		   em.persist(tel);
    	   }
    	   for(AtMetroExt me :From.getAtMetros()){
    		   em.detach(me);
    		   me.setId(0);
    		   me.setAtTerm(From);
    		   me.setChanged(new java.util.Date());
    		   em.persist(me);
    	   }
    	   
		   em.getTransaction().commit();
		   cm.AddMess(from + "->" + to + " скопировано");
//		   System.out.println(from + "->" + to + " copyed");
//    	   uf.WriteLog( "копирование атм", this.getClass().getName()+".doTrueCopy", JsonLog.LEVEL_M, "atmproc", PROJECT, "");
   		HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
   		JsonLog jl = new JsonLog( "копирование атм", this.getClass().getName()+".doTrueCopy", JsonLog.LEVEL_M, "atmproc", PROJECT, "", Long.parseLong(m.get(UserInfo.KEY_UI)));
   		jl.WriteLog(getUrlServlet());

     }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
   	      e.printStackTrace();
   	      cm.AddMess(from + "->" + to + " ошибка");
//   	   uf.WriteLog( "копирование атм", this.getClass().getName()+".doTrueCopy", JsonLog.LEVEL_E, "atmproc", PROJECT, JsonLog.stackTraceToString(e));
   		HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
   		JsonLog jl = new JsonLog( "копирование атм", this.getClass().getName()+".doTrueCopy", JsonLog.LEVEL_E, "atmproc", PROJECT, JsonLog.stackTraceToString(e), Long.parseLong(m.get(UserInfo.KEY_UI)));
   		jl.WriteLog(getUrlServlet());
   	   System.out.println(from + "->" + to + " erro");
//	      throw e;
    }

        }
//		for(String it: atms){
//			cm.AddMess(it + " скопирован");
//		}
	    return cm;
	}

//=========================== Proc =================================
    public static final String MDM_ATM = "select ac.id id,ac.contract_number term,f.branch_code brcode,decode(nvl(ads.status,9),4,'N','Y') status,tw.id town_id "+
    "from ows.device_rec dr,ows.f_i f,ows.acnt_contract ac,ows.ACQ_DEVICE_STATE ADS "+
    ",ows.device_parms dp,reporter.f250_town tw "+
    "where dr.amnd_state = 'A' "+
    "and ac.amnd_state = 'A' "+
    "and f.amnd_state = 'A' "+
    "and ac.f_i = f.id "+
//    "and ac.channel = 'A' "+
    "and REGEXP_LIKE(f.branch_code, '^0[012]\\d\\d$') "+
    "and dr.acnt_contract__oid = ac.id "+
    "and dr.term_cat = 'A' "+ 
    "and ADS.ID(+) = DR.ID "+
    "and tw.name = dp.device_city "+
    "and dp.amnd_state = 'A' "+
    "and dp.acnt_contract__oid = ac.id "+
    "and dp.device_parms__id is null";

    public List<String> loadMdm() throws Exception{
	setFlagUpdate(true);
	EntityManager em = emUser();
    	List results = null;
    	ExtTerm t = null;
        int edit = 0, ins = 0;
        long oid = 0;
        String term = "";
        List<String> ret = new ArrayList<String>();
    try {
       results = em.createNativeQuery(MDM_ATM).getResultList();
       Iterator it = results.iterator( );
       while (it.hasNext( )) {
       	 Object[] result = (Object[])it.next(); 
         oid = ((BigDecimal)result[0]).longValue();
         term = (String)result[1];
         String fi = (String)result[2];
         String st = (String)result[3];
         long town = ((BigDecimal)result[4]).longValue();
         try{
//           t = (ExtTerm) em.createQuery("select t from "+ExtTerm.class.getSimpleName()+" t where t.oId="+oid).getSingleResult();
//           t = (ExtTermStat) em.createNativeQuery("select t.*, 'x' status from AT_TERM t where t.O_ID="+oid, ExtTermStat.class).getSingleResult();
           t = (ExtTerm) em.createNativeQuery("select t.* from AT_TERM t where t.O_ID="+oid, ExtTerm.class).getSingleResult();
    	   boolean isChanged = false;
    	   if (!t.getTerm().equals(term.trim())) {t.setTerm(term.trim()); isChanged = true;} 
    	   if (!t.getIsActive().equals(st)) {t.setIsActive(st); isChanged = true;}
    	   if (!t.getFin().equals(fi)) {t.setFin(fi); isChanged = true;}
    	   if (t.getF250Town().getId() != town) {
    	      t.setF250Town( em.find(F250Town.class, town));
    	      isChanged = true;
    	      }
    	   if (isChanged) {
    	       EntityTransaction tr = em.getTransaction();
    	       tr.begin();
    	       em.merge(t);
    	       tr.commit();
    	       edit++;} 
    	  }catch(NoResultException ne){
             if (!st.equals("N")){
//        	System.out.println("oid = " + oid);
        	ExtTerm at = new ExtTerm(oid, term.trim(), em.find(F250Town.class, town)); 
//    		at.setPart("M");
    		at.setFin(fi);
//    		t.setOId(new BigDecimal(oid));
    		at.setAtPart(em.find(AtPart.class, AtPart.PID_MDM));
    		at.setIsForall("N");
    		 Set<ConstraintViolation<ExtTerm>> cvs = null;
   	 	     ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
   		     Validator validator = factory.getValidator();
   		     cvs = validator.validate(at);
//   		     System.out.println("cvs.size() = "+cvs.size());
//   		     if (cvs.size() != 0) {
//   		       for (ConstraintViolation<?> cv : cvs)
//   		   	 System.out.println(at.getTerm()+ String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath()));
////   		     throw new Exception(at.getTerm()+ String.format("%1s(%2s.%3s)", cv.getMessage(), cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath())); 
//   	   	   }
    		
    		EntityTransaction tr = em.getTransaction();
     	        tr.begin();
    		em.persist(at);
    		tr.commit();
   		ins++;
      	     }
    	  }
       }
//        }catch(RuntimeException e){
//          System.out.println(e.getMessage());  
////    	  em.getTransaction().rollback();
//    	  e.printStackTrace();
//    	  throw e;
        }catch(Exception e){
            System.out.println(e.getMessage());
//            e.getConstraintViolations();
            System.out.println("t.getTerm() = " + term + "; oid = " + oid);
          	e.printStackTrace();
//       	    uf.WriteLog( "атм из опенвей", this.getClass().getName()+".loadMdm", JsonLog.LEVEL_E, "atmproc", PROJECT, JsonLog.stackTraceToString(e));
       		HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
       		JsonLog jl = new JsonLog( "атм из опенвей", this.getClass().getName()+".loadMdm", JsonLog.LEVEL_E, "atmproc", PROJECT, "", Long.parseLong(m.get(UserInfo.KEY_UI)));
       		jl.WriteLog(getUrlServlet());
          	throw e;
        }finally {/*em.close();*/ if (results!=null) results.clear();
	  setFlagUpdate(false);
        }
      ret.add(String.valueOf(edit));
      ret.add(String.valueOf(ins));
//      uf.WriteLog( "атм из опенвей edit = "+edit+" ins = "+ins, this.getClass().getName()+".loadMdm", JsonLog.LEVEL_M, "atmproc", PROJECT, "");
 		HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
 		JsonLog jl = new JsonLog( "атм из опенвей edit = "+edit+" ins = "+ins, this.getClass().getName()+".loadMdm", JsonLog.LEVEL_M, "atmproc", PROJECT, "", Long.parseLong(m.get(UserInfo.KEY_UI)));
 		jl.WriteLog(getUrlServlet());
      return ret;
    }

  //=========================== Tables =================================
    public static final String SQL_TERM_STAT = "select t.*,(select decode(nvl(ads.status,9),4,'C','A') from ows.device_rec dr,ows.ACQ_DEVICE_STATE ADS "+
              "where dr.amnd_state = 'A' "+
                "and dr.acnt_contract__oid = t.o_id "+
                "and dr.term_cat = 'A' "+ 
                "and ADS.ID(+) = DR.ID ) status, (select fi.name from ows.f_i fi where fi.amnd_state = 'A' and t.fin = fi.branch_code) finName ";// 
    public static final String WHERE_TERM_STAT = " from AT_TERM t, AT_OKATO o, F250_TOWN w, at_part p where p.id = t.part_id and w.OKATO=o.id and w.id=t.TOWN_ID ";//  
    public TermPagingLoadResultBean getTerms(int start, int limit, List<SortInfoBean> sortInfo, List<FilterConfigBean> filterConfig) {
	EntityManager em = emUser();
	int total = 0;
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by");
	String filtr = "", orderIt = "", filterIt;
      try {
           for(SortInfo it:sortInfo){
              if (it.getSortField().equals("atPart")) orderIt = " p.name";
              else if (it.getSortField().equals("finName")) orderIt = " finName";
              else if (it.getSortField().equals("townString")) orderIt = " w.RUS_NAME";
              else if (it.getSortField().equals("atOkato")) orderIt = " o.name";
              else orderIt = " t." + it.getSortField();
//              order = order.append(" t.").append(orderIt).append(" ").append(it.getSortDir()).append(",");
              order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
           }
           order.setCharAt( order.length()-1, ' ');

           for(FilterConfigBean it: filterConfig){
              if (it.getField().equals("atPart")) //filterIt = "p.name";
                 filterIt = " p.id in ("+it.getValue().replace("::", ",")+") ";
              else if (it.getField().equals("atOkato"))
            	 filterIt = " o.id in ('"+it.getValue().replace("::", "','")+"') ";
              else if (it.getField().equals("townString"))
            	 filterIt = " upper(w.RUS_NAME) like '"+ it.getValue().toUpperCase() +"%' ";
              else filterIt = "t." + it.getField() + " like '" + it.getValue() +"%' ";
              filtr = filtr + "and "+filterIt;
           }
           
	 String sqlPage = SQL_TERM_STAT + WHERE_TERM_STAT + filtr + order.toString();
	 List<ExtTermStat> page = em.createNativeQuery(sqlPage, ExtTermStat.class).setFirstResult(start).setMaxResults(limit).getResultList();
//	 total = ((Long) em.createQuery(SEL_COUNT_T +  WHERE_TERM_STAT + filtr).getSingleResult()).intValue();
	 total = ((BigDecimal) em.createNativeQuery("select count(*) " +  WHERE_TERM_STAT + filtr).getSingleResult()).intValue();
//	 System.out.println("page = " + page.get(0).getAtOkato().getId());
//	 System.out.println(sqlPage);
	 return new TermPagingLoadResultBean(page, total, start);
       } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new RuntimeException(ex);
       }
	
    }
    
    static final String SQL_MS = " from "+AtMstationStat.class.getSimpleName() + " m, "+
                                        AtMetroLine.class.getSimpleName()+" l where m.atMetroLine=l ";
    public MSPagingLoadResultBean getPageMS(int start, int limit, List<SortInfoBean> sortInfo, List<FilterConfigBean> filterConfig) {
	EntityManager em = emUser();
	int total = 0;
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by");
	String filtr = "", orderIt = "", filterIt;
      try {
           for(SortInfo it:sortInfo){
              if (it.getSortField().equals("atMetroLine")) orderIt = " l.name";
              else orderIt = " m." + it.getSortField();
//              order = order.append(" t.").append(orderIt).append(" ").append(it.getSortDir()).append(",");
              order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
           }
           order.setCharAt( order.length()-1, ' ');

           for(FilterConfigBean it: filterConfig){
//               System.out.println("it.getField() = "+it.getField() +";it.getValue()="+it.getValue());
              if (it.getField().equals("atMetroLine")) {
//        	  String fl = it.getValue().replace("::", ",");
        	  filterIt = " l.id in ("+it.getValue().replace("::", ",")+") ";
              }
              else filterIt = "m.name like '" + it.getValue().trim() +"%'";
              filtr = filtr + "and "+filterIt;
           }
//         System.out.println("sql = "+"select m" + SQL_MS  + filtr + order.toString());
//	 String sqlPage = SQL_MS  + filtr + order.toString();
	 List<AtMstationStat> page = em.createQuery("select m" + SQL_MS  + filtr + order.toString(), AtMstationStat.class).setFirstResult(start).setMaxResults(limit).getResultList();
//	 total = ((Long) em.createQuery(SEL_COUNT_T +  WHERE_TERM_STAT + filtr).getSingleResult()).intValue();
	 total = ((Long) em.createQuery("select count(m)" + SQL_MS + filtr).getSingleResult()).intValue();
//	 System.out.println("page = " + page.size());
//	 System.out.println(sqlPage);
	 return new MSPagingLoadResultBean(page, total, start);
       } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new RuntimeException(ex);
       }
    }
    
    static final String SQL_TOWN = " from "+F250Town.class.getSimpleName() + " t, "+
            AtOkato.class.getSimpleName()+" o where t.atOkato=o ";
    public TownPagingLoadResultBean getPageTown(int start, int limit, List<SortInfoBean> sortInfo, List<FilterConfigBean> filterConfig) {
	EntityManager em = emUser();
	int total = 0;
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by");
	String filtr = "", orderIt = "", filterIt;
      try {
          for(SortInfo it:sortInfo){
              if (it.getSortField().equals("atOkato")) orderIt = " o.name";
              else orderIt = " t." + it.getSortField();
              order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
           }
           order.setCharAt( order.length()-1, ' ');

          for(FilterConfigBean it: filterConfig){
//             System.out.println("it.getField() = "+it.getField() +";it.getValue()="+it.getValue());
            if (it.getField().equals("atOkato")) {
        	StringBuilder f = new StringBuilder("");
        	for(String fit: it.getValue().split("::")){
        	   f = f.append("'").append(fit).append("',");
        	}
        	f.setCharAt( f.length()-1, ' ');
        	filterIt = " o.id in ("+f.toString()+") ";
            }
            else filterIt = "t."+it.getField()+" like '" + it.getValue().trim() +"%'";
            filtr = filtr + "and "+filterIt;
         }

	  List<F250Town> page = em.createQuery("select t" + SQL_TOWN  + filtr + order.toString(), F250Town.class).setFirstResult(start).setMaxResults(limit).getResultList();
	  total = ((Long) em.createQuery("select count(t)" + SQL_TOWN + filtr).getSingleResult()).intValue();
		 return new TownPagingLoadResultBean(page, total, start);
       } catch (Exception ex) {
         ex.printStackTrace();
         throw new RuntimeException(ex);
       }
    }
    
    private static String PAGE_SERVS = "select t from " + AtServStat.class.getSimpleName() + " t, "+
	    AtCatalogStat.class.getSimpleName()+" c where t.atTerm=?1 and t.atCatalog=c ";
    public ServLoadResultBean getServs(List<SortInfoBean> sortInfo, ExtTermStat term){
	EntityManager em = emUser();
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by c.name "+ sortInfo.get(0).getSortDir());
     try {
//	 System.out.println(PAGE_SERVS + order);
	 List<AtServStat> page = em.createQuery(PAGE_SERVS + order).setParameter(1, term).getResultList();
	 return new ServLoadResultBean(page);
     } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new RuntimeException(ex);
     }
    }

    private static String PAGE_METROS = "select t from " + AtMetroStat.class.getSimpleName() + " t, "+
	    AtMstationStat.class.getSimpleName()+" m where t.atTerm=?1 and t.atMstation=m ";
    public MetroLoadResultBean getMetros(List<SortInfoBean> sortInfo, ExtTermStat term){
	EntityManager em = emUser();
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by m.name "+ sortInfo.get(0).getSortDir());
     try {
//	 System.out.println(PAGE_SERVS + order);
	 List<AtMetroStat> page = em.createQuery(PAGE_METROS + order).setParameter(1, term).getResultList();
	 return new MetroLoadResultBean(page);
     } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new RuntimeException(ex);
     }

    }

    private static String PAGE_TELS = "select t from " + AtTelStat.class.getSimpleName() + " t where t.atTerm=?1 ";
    public TelLoadResultBean getTels(List<SortInfoBean> sortInfo, ExtTermStat term){
	EntityManager em = emUser();
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by t.num "+ sortInfo.get(0).getSortDir());
     try {
	 List<AtTelStat> page = em.createQuery(PAGE_TELS + order).setParameter(1, term).getResultList();
	 return new TelLoadResultBean(page);
     } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new RuntimeException(ex);
     }
    }
    private static String PAGE_MLS = "select l from " + AtMetroLine.class.getSimpleName()+" l,"+F250Town.class.getSimpleName()+
	                             " t where l.f250Town=t";
    public MLLoadResultBean getMLs(List<SortInfoBean> sortInfo){
	EntityManager em = emUser();
	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":" order by");
	String orderIt = "";
//	StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":"order by t.num "+ sortInfo.get(0).getSortDir());
     try {
	 for(SortInfo it:sortInfo){
             if (it.getSortField().equals("f250Town")) orderIt = " t.rusName";
             else orderIt = " l." + it.getSortField();
             order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
          }
	 order.setCharAt( order.length()-1, ' ');
//	 System.out.println(PAGE_SERVS + order);
	 List<AtMetroLine> page = em.createQuery(PAGE_MLS + order).getResultList();
	 return new MLLoadResultBean(page);
     } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new RuntimeException(ex);
     }
    }

    private static String PAGE_CAT = "select c from " + AtCatalogStat.class.getSimpleName()+" c ";
    public CatLoadResultBean getAllCats(List<SortInfoBean> sortInfo){
        EntityManager em = emUser();
        StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":" order by");
        String orderIt = "";
        try {
           for(SortInfo it:sortInfo){
              orderIt = " c." + it.getSortField();
              order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
            }
           order.setCharAt( order.length()-1, ' ');
//System.out.println(PAGE_SERVS + order);
           List<AtCatalogStat> page = em.createQuery(PAGE_CAT + order).getResultList();
           return new CatLoadResultBean(page);
        } catch (Exception ex) {
          ex.printStackTrace();
          throw new RuntimeException(ex);
        }
    }

//    private static String PAGE_OK = "select c from " + AtOkato.class.getSimpleName()+" c order by c.name";
//    public List<AtOkato> getAllOkato(){
//        EntityManager em = emUser();
//        try {
//           return (List<AtOkato>) em.createQuery(PAGE_OK).getResultList();
//        } catch (Exception ex) {
//          ex.printStackTrace();
//          throw new RuntimeException(ex);
//        }
//    }
    
    public List<AtCatalogStat> pageCatByType(String type, ExtTermStat Term){
	EntityManager em = emUser();
        try {
            return em.createQuery("select c from "+AtCatalogStat.class.getSimpleName()+
         	                  " c where c.catType='"+type+"' and c.id not in (select s.atCatalog.id from "+
                                  AtServStat.class.getSimpleName() +" s where  s.atTerm=?1) order by c.name").setParameter(1, Term).getResultList();
        }catch (RuntimeException re) {
       	  re.printStackTrace();
          throw re;
        }  
    }

    public List<AtMstationStat> pageMSByTerm(ExtTermStat Term){
	EntityManager em = emUser();
        try {
            return em.createQuery("select s from "+AtMstationStat.class.getSimpleName()+" s," +AtMetroLine.class.getSimpleName()+
            		" l where s.atMetroLine=l and l.f250Town=?1 and s.id not in (select t.atMstation.id from "+
                                  AtMetroStat.class.getSimpleName() +" t where  t.atTerm=?2) order by s.name").setParameter(1, Term.getF250Town()).setParameter(2, Term).getResultList();
        }catch (RuntimeException re) {
       	  re.printStackTrace();
          throw re;
        }  
    }
    public Boolean mertoInTown(AtTerm Term){
	EntityManager em = emUser();
	String sql2 = "select count(l.id) from AT_TERM t, at_metro_line l where l.Town_id=t.Town_id and t.id=? and rownum<2";
	 try {
	     BigDecimal size = (BigDecimal) em.createNativeQuery(sql2).setParameter(1, Term.getId()).getSingleResult();
//	     System.out.println("size.longValue() = "+size.longValue());
	     return size.longValue() > 0 /*? Term: null*/;
	 }catch (RuntimeException re) {
	    	  re.printStackTrace();
	          throw re;
	      }  
    }

    final static String SQL_ALLTOWN = "select t from "+F250Town.class.getSimpleName()+" t order by t.rusName";
    final static String SQL_ALLLINE = "select l from "+AtMetroLine.class.getSimpleName()+" l order by l.name";
    final static String SQL_ALLPART = "select l from "+AtPart.class.getSimpleName()+" l order by l.name";
    final static String SQL_ALLOK = "select c from " + AtOkato.class.getSimpleName()+" c order by c.name";
    public List getAllRec(String Sql){
	EntityManager em = emUser();
	 try {
//	   if (Sql == 1) sql = SQL_ALLTOWN;
//	   else if (Sql == 2 ) sql = SQL_ALLLINE; 
//	   else if (Sql == 3 ) sql = SQL_ALLPART; 
//	   else if (Sql == 4 ) sql = SQL_ALLOK;
           return (List)em.createQuery(Sql).getResultList();
	 }catch (RuntimeException re) {
	    	  re.printStackTrace();
	          throw re;
	 }  
    }
    
    public List<AtOkato> getAllOkato(){
    	return getAllRec(SQL_ALLOK);
    }
    
    public List<F250Town> getAllTown(){
		 return getAllRec(SQL_ALLTOWN);
    }
    public List<AtMetroLine> getAllLine(){
		 return getAllRec(SQL_ALLLINE);
    }
    public List<AtPart> getAllPart(){
		 return getAllRec(SQL_ALLPART);
    }
  //=========================== Context =================================
    public List<String> getSessionUsers(){
    	SessionCounter counter = SessionCounter.getInstance(RequestFactoryServlet.getThreadLocalRequest().getServletContext());
    	ArrayList<String> ret = new ArrayList<String>();
    	Iterator it = counter.getSessions().entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          ret.add((String) ((String[])pair.getValue())[SessionCounter.SES_INFO]);
        }
      return ret;	
    }
    
    public String getUserInfo(){
	return uf.getUserInfo(RequestFactoryServlet.getThreadLocalRequest(), emUser());
    }
    
    public String getFIs(){
	return uf.getFIs(RequestFactoryServlet.getThreadLocalRequest(), emUser());
    }

    public String getRole(){
//	System.out.println("role = "+uf.getRole(RequestFactoryServlet.getThreadLocalRequest(), emUser()));
	return uf.getRole(RequestFactoryServlet.getThreadLocalRequest(), emUser());
    }
    public String getLogin(){
//    	uf.WriteLog( "создание сессии", this.getClass().getName()+".getLogin", JsonLog.LEVEL_M, "login", PROJECT, "");
	return uf.getLogin(RequestFactoryServlet.getThreadLocalRequest(), emUser()).toUpperCase();
    }

    public boolean isUpdateRun(){
	HashMap<String,String> m = (HashMap<String,String>)RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getAttribute(SERVLET_KEYS);
	if (m == null){
//	  System.out.println("atm.isUpdateRun() SERVLET_KEYS = null!!!");
	  m = new HashMap<String,String>();
	  setServletAttr(m);
	}
      return m.get(FLG_UPDATE).equals("A");
    }

    public boolean setFlagUpdate(boolean f){
	HashMap<String,String> m = (HashMap<String,String>)RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getAttribute(SERVLET_KEYS);
	if (m == null){
//	  System.out.println("setFlagUpdate SERVLET_KEYS = null!!!");
	  m = new HashMap<String,String>();
	  setServletAttr(m);
	}
        if (f){
          if (m.get(FLG_UPDATE).equals("A")) return false;
          else {
            m.put( FLG_UPDATE, "A");
            RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().setAttribute(SERVLET_KEYS, m);
          }
        }else{
     	   m.put( FLG_UPDATE, "S");
    	   RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().setAttribute(SERVLET_KEYS, m);
        }
       return true; 
    }

    private void setServletAttr(HashMap<String,String> m){
	//A - active, S - stop
	m.put( FLG_UPDATE, "S");
	RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().setAttribute(SERVLET_KEYS, m);
    }
//=========================================================================
    public void merg(Object rec){
	EntityManager em = emUser();
        try {
	      em.getTransaction().begin();
	      if (rec.toString().equals("0")) em.persist(rec);
	      else em.merge(rec);
	      em.flush();
	      em.getTransaction().commit();
	    }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
   	      e.printStackTrace();
	      throw e;
	    }
    }
//--- del    
    public void remov(Object rec, Class<?> cl, long id){
        EntityManager em = emUser();
        try {
	      em.getTransaction().begin();
	      em.remove(em.find(cl, id));
	      em.flush();
	      em.getTransaction().commit();
	    }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
    	      e.printStackTrace();
	      throw e;
	    } 
    }

    public void removTel(AtTelStat rec) throws RuntimeException{
	   remov(rec, AtTelStat.class, rec.getId());
    }
    public void removServ(AtServStat rec) throws RuntimeException{
	   remov(rec, AtServStat.class, rec.getId());
    }
    public void removMetro(AtMetroStat rec) throws RuntimeException{
	   remov(rec, AtMetroStat.class, rec.getId());
    }
    public void removML(AtMetroLine rec) throws RuntimeException{
	   remov(rec, AtMetroLine.class, rec.getId());
    }
    public void removMS(AtMstationStat rec) throws RuntimeException{
	   remov(rec, AtMstationStat.class, rec.getId());
    }
    public void removCat(AtCatalogStat rec) throws RuntimeException{
	   remov(rec, AtCatalogStat.class, rec.getId());
    }
    public void removTown(F250Town rec) throws RuntimeException{
	   remov(rec, F250Town.class, rec.getId());
    }
    
//--- find    
    public static AtOkato findAtOkato(String id) throws RuntimeException{
	       return (AtOkato) findObject(AtOkato.class, id);
    }
    
    public static F250Town findF250Town(Long id) throws RuntimeException{
	       return (F250Town) findObject(F250Town.class, id);
    }
//    public static ExtTermStat findAtTerm(Long id) throws RuntimeException{
//	       return (ExtTermStat) findObject(ExtTermStat.class, id);
//    }

    static final String SQL_TERM_STAT_ID = "select t.*,(select decode(nvl(ads.status,9),4,'C','A') from ows.device_rec dr,ows.ACQ_DEVICE_STATE ADS "+
            "where dr.amnd_state = 'A' "+
              "and dr.acnt_contract__oid = t.o_id "+
              "and dr.term_cat = 'A' "+ 
              "and ADS.ID(+) = DR.ID ) status, (select fi.name from ows.f_i fi where fi.amnd_state = 'A' and t.fin = fi.branch_code) finName "+
              " from AT_TERM t, at_part p where p.id = t.part_id and t.id=?1";
    public static ExtTermStat findAtTerm(Long id) throws RuntimeException{
//    	System.out.println("ExtTermStat findAtTerm");
	       if (id == null) return null; 
	       EntityManager em = emUser();
	       try {
		   Cache cache = em.getEntityManagerFactory().getCache();
		   if (cache.contains(ExtTermStat.class, id)) return em.find(ExtTermStat.class, id);
		   else {
		       return (ExtTermStat) em.createNativeQuery(SQL_TERM_STAT_ID, ExtTermStat.class).setParameter(1, id).getSingleResult();
		   }
	           }catch(RuntimeException e){
		     e.printStackTrace();
		     throw e;
	           } 
    }
    
    public static AtCatalogStat findAtCatalog(Long id) throws RuntimeException{
	       return (AtCatalogStat) findObject(AtCatalogStat.class, id);
    }
    public static AtPart findAtPart(Long id) throws RuntimeException{
	       return (AtPart) findObject(AtPart.class, id);
}
 
    public static AtServStat findAtServ(Long id) throws RuntimeException{
	       return (AtServStat) findObject(AtServStat.class, id);
}
    public static AtTelStat findAtTel(Long id) throws RuntimeException{
	       return (AtTelStat) findObject(AtTelStat.class, id);
}
    public static AtMetroStat findAtMetro(Long id) throws RuntimeException{
	       return (AtMetroStat) findObject(AtMetroStat.class, id);
}
    public static AtMstationStat findAtMstation(Long id) throws RuntimeException{
	       return (AtMstationStat) findObject(AtMstationStat.class, id);
}
    public static AtMetroLine findAtMetroLine(Long id) throws RuntimeException{
	       return (AtMetroLine) findObject(AtMetroLine.class, id);
}
    
    
    public static Object findObject(Class<?> cl, String id) {
	       if (id == null) return null; 
	       EntityManager em = emUser();
	       try {
		     return em.find(cl, id);
	           }catch(RuntimeException e){
		     e.printStackTrace();
		     throw e;
	           } 
}
    public static Object findObject(Class<?> cl, Long id) {
	       if (id == null) return null; 
	       EntityManager em = emUser();
	       try {
		     return em.find(cl, id);
	           }catch(RuntimeException e){
		     e.printStackTrace();
		     throw e;
	           } 
     }
}
