package migration;

import com.googlecode.flyway.core.api.FlywayException;
import org.apache.log4j.Logger;
import java.io.BufferedReader;

public class ResponseHandler {
    private static final Logger logger = Logger.getLogger(ResponseHandler.class);

    public static void process(BufferedReader reader) throws Exception {
        String line = null;
        boolean isValid = false;
        StringBuilder errors = new StringBuilder();

        while ( (line = reader.readLine()) != null) {
            if (line.contains("info: success: true")) {
               isValid = true;
            } else {
                errors.append(line);
            }
        }

        if (!isValid) {
            logger.error(errors.toString());
            throw new FlywayException(errors.toString());
        }
    }
}
