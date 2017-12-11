package gxt.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class srvlUpFile extends HttpServlet{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStreamReader is = null;
		BufferedReader br = null;
//		System.out.println("SrvlUpFile = ");
   try{
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		if (!iter.hasNext()) return;
        FileItemStream item = iter.next();
//        String name = item.getFieldName();
//        InputStream stream = item.openStream();
        is = new InputStreamReader(item.openStream());
        br = new BufferedReader(is);
        StringBuilder sb = new StringBuilder();
        String read = null;
        while((read = br.readLine()) != null) sb.append(read);
//        System.out.println("sb = " + sb.toString());
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().print(sb.toString());
        response.flushBuffer();
   }catch(Exception e){
	   e.printStackTrace();
   }finally{
	   if (br != null) br.close();
	   if (is != null) is.close();
   }
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}

}
