package gxt.server.servlet;

import gxt.server.domain.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public class srvlUpFileBanks extends HttpServlet{
    private static final String FLG_IMPPART = "imppart";
    private static final String VAL_STOP = "S";
    private static final String VAL_ACTIVE = "A";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		InputStreamReader is = null;
		InputStream is = null;
		BufferedReader br = null;
		String bankId = null;
	    StringBuilder sb = new StringBuilder();
	    String flag = VAL_STOP;
	    boolean isRun = false;
	    String fileName = null;
//		System.out.println("SrvlUpFile = ");
   try{
//	 HttpSession session = request.getSession();
//	 System.out.println("session = " + session );
	 synchronized(flag){
	   flag = (String)getServletConfig().getServletContext().getAttribute(FLG_IMPPART);
	   if (flag == null || flag.equals(VAL_STOP)){
	        request.getSession().getServletContext().setAttribute(FLG_IMPPART, VAL_ACTIVE);
	        isRun = true;
	   }
	 }
     if (isRun){
//    	Enumeration atr = request.getAttributeNames();
//    	while(atr.hasMoreElements()){
//    		System.out.println(">>>>>atr.nextElement() = " + atr.nextElement());
//    	}
//    	Enumeration par = request.getParameterNames();
//    	while(par.hasMoreElements()){
//    		System.out.println(">>>>>atr.nextElement() = " + par.nextElement());
//    	}
    	 
//		ServletFileUpload upload = new ServletFileUpload();
//		FileItemIterator iter = upload.getItemIterator(request);
//		Iterator iter = uploadedItems.iterator();
//		FileItem fileItem = null;
//		while(iter.hasNext()){
//try{		
//		while(true){
//			FileItemStream item = iter.next();
//			if (item.isFormField() && item.getFieldName().equals("BankId")){
//				bankId = Streams.asString(item.openStream(),"UTF-8");
//			}
//			else  is = item.openStream();
//		}
//}catch(Exception e){}	

        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        for (FileItem item : items) {
        	if (item.isFormField()){
        	  if (item.getFieldName().equals("BankId"))
        		bankId = item.getString();
//				bankId = Streams.asString(item.openStream(),"UTF-8");
			}
			else  {
//				String fieldName = item.getFieldName();
                fileName = FilenameUtils.getName(item.getName());
				is = item.getInputStream();
			}
        }

		if (bankId == null ||is == null){
		 sb.append("Ошибка bank = " + bankId + "; is = " + (is != null));
		}else{
//		 if (bankId.equals("")) System.out.println("============= bank = ALFA");
	     Dao dao = new Dao(request.getRequestURL().toString());
//	     Dao dao = new Dao();
	     System.out.println("fileName = " + fileName);
     	 String[] s = (String[])SessionCounter.getInstance(request.getServletContext()).getSessions().get(request.getSession().getId());
//     	 System.out.println("bankId = " + bankId);
	     List<String> mess = dao.importBanks(new InputStreamReader(is,"UTF-8"), bankId, Long.parseLong(s[SessionCounter.SES_UID]));
//	     List<String> mess = dao.importBanks(new InputStreamReader(is,"WINDOWS-1251"), bankId, Long.parseLong(s[SessionCounter.SES_UID]));
//	     System.out.println("stop Dao()");
	     for(String it: mess) sb.append(it + "\n");
	     sb.append("Завершено");
		}
	  }else{
		 sb.append("Кто-то уже грузит");

		}

   }catch(Exception e){
		 sb.append("Ошибка сервера "+e.getMessage());
	   e.printStackTrace();
	   
   }finally{
	   if (br != null) br.close();
	   if (is != null) is.close();
	   if (isRun) request.getSession().getServletContext().setAttribute(FLG_IMPPART, VAL_STOP);
       response.setStatus(HttpServletResponse.SC_CREATED);
	   response.setContentType("text/html");
	   response.setCharacterEncoding("UTF-8");
       response.getWriter().print(sb.toString());
       response.flushBuffer();
   }
	}

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}

}
