package services;

import analysis.Analyser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import logger.RequestLogger;

/**
 * Service that analyzes text sentiment
 * @author nilstes
 */
@Path("sentiment")
public class SentimentService {
    
    private static final Logger log = Logger.getLogger(SentimentService.class.getName());

    private Analyser analyser = new Analyser();
    private RequestLogger requestLogger = new RequestLogger();
    
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public int getSentiment(@Context HttpServletRequest req, String text) { 
        log.log(Level.INFO, "GameService.getSentiment(): {0}", text);
        int sentiment = analyser.findSentiment(text);
        requestLogger.logRequest(text, sentiment, req);
        log.log(Level.INFO, "Returning {0}", sentiment);      
        return sentiment;
    }    
}
