package gxt.server.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements ServletContextListener, HttpSessionListener, ServletRequestListener {
	private static final String ATTRIBUTE_NAME = "gxt.server.servlet.SessionCounter";
//	private Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();
	private Map<String, String[]> sessions = new ConcurrentHashMap<String, String[]>();
    private static final boolean isTestServer = System.getProperty("rsys.isTest", "N").equals("Y");
    public static final int SES_UID = 0;
    public static final int SES_INFO = 1;
    
	public SessionCounter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInitialized(ServletRequestEvent arg0) {
	    HttpServletRequest request = (HttpServletRequest) arg0.getServletRequest();
	    HttpSession session = request.getSession();
//	    System.out.println("%%requestInitialized");
	    if (session.isNew()) {
		    System.out.println("%%isNew = " + session.getId());
	    	StringBuilder item = new StringBuilder();
	    	if (!isTestServer) item.append(request.getRemoteUser());
	    	item.append(" ").append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(new Date()))
	    	.append(" ").append(request.getRemoteAddr()).append(" ").append(request.getHeader("user-agent"));
	        String[] s = {"1", item.toString()};      	
	         sessions.put(session.getId(), s);
	    }
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
	    System.out.println("%%sessionDestroyed = "+arg0.getSession().getId());
		sessions.remove(arg0.getSession().getId());
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		arg0.getServletContext().setAttribute(ATTRIBUTE_NAME, this);
	}
	public static SessionCounter getInstance(ServletContext context) {
        return (SessionCounter) context.getAttribute(ATTRIBUTE_NAME);
    }

	public Map getSessions(){
		return sessions;
	}
//    public int getCount(String remoteAddr) {
//        return Collections.frequency(sessions.values(), remoteAddr);
//    }
}
