package gxt.server.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Servlet implementation class jasperin
 */
//@WebServlet("/jasperin")
public class jasperin extends HttpServlet {
	private static final long serialVersionUID = 1L;
        private static final String JASPER_PATH = System.getProperty("rsys.jasper", "");
	private static final String MIME_XLS = "application/vnd.ms-excel";
	private static final String repAtm = "atmPlace";
	private static final String extXLS = "XLSX";
	private static final String jdbcProd = "jdbc/prod_rep";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public jasperin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		java.io.ByteArrayOutputStream baosXLS = null;
		Connection db_conn = null;
	     try{	
		 resp.setContentType(MIME_XLS);    
		 resp.setHeader( "Content-disposition", "attachment;filename="+repAtm+"."+extXLS);  
		 resp.setCharacterEncoding("Cp1251");

		 ServletOutputStream out = resp.getOutputStream();
		 JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(JASPER_PATH + repAtm + ".jasper"));
//		 db_conn = dsProd.getConnection();//emAt().unwrap(java.sql.Connection.class);
		 db_conn = ((DataSource)InitialContext.doLookup(jdbcProd)).getConnection();
		 JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, db_conn);
	         JRXlsxExporter exporterXLS = new JRXlsxExporter();
	         exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
	         exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
	         exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
	         exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
	         exporterXLS.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	         baosXLS = new java.io.ByteArrayOutputStream();
	         exporterXLS.setParameter(JRExporterParameter.OUTPUT_STREAM, baosXLS);
//	         exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, repAtm+".XLS");
	         exporterXLS.exportReport();
	         resp.setContentLength(baosXLS.size());
	         baosXLS.writeTo(out);
	         out.flush();
	     }catch(Exception e){
		 e.printStackTrace();
	     }finally{
		 try{if (db_conn != null) db_conn.close();}catch(Exception ee){};
		 try{if (baosXLS != null) baosXLS.close();}catch(Exception ee){};
	     }

	}

}
