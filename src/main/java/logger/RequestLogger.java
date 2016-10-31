package logger;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import services.SentimentService;

/**
 * @author nilstes
 */
public class RequestLogger {

    private static final Logger log = Logger.getLogger(SentimentService.class.getName());

    private File databaseFile; 
    private LookupService lookupService;
    private ESWriter esWriter = null;
            
    public RequestLogger() {
        try {
            log.info("Initialising RequestLogger...");
            databaseFile = new File("/var/geolite/GeoLiteCity.dat");
            lookupService = new LookupService(databaseFile, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
            esWriter = new ESWriter("sentiment-web-app", "messages");
            log.info("RequestLogger OK");
        } catch(IOException e) {
            log.log(Level.WARNING, "GeoLocation database file {0} not found. Not logging geo locations.", databaseFile.getPath());
        } catch(Exception e) {
            log.log(Level.WARNING, "Error connecting to ElasticSearch. Not logging geo locations.");            
        }
    }
  
    public void logRequest(String message, int sentiment, HttpServletRequest request) {
        if(esWriter != null) {
            String ip = getClientIpAddr(request);
            Location location = lookupService.getLocation(ip);
            if(location != null) {
                log.log(Level.INFO, "Location for ip {0}: lat={1}, lon={2}", new Object[]{ip, location.latitude, location.longitude});
                Message msg = new Message(message, new Date(), sentiment);
                msg.setLocation(location.latitude, location.longitude);
                esWriter.addStatus(msg);
            } else {
                log.log(Level.INFO, "Location for ip {0} not found", ip);                
            }
        }
    }
    
    public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
}
