package configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class Path {

    private final static Logger logger = Logger.getLogger(Path.class);

    public static String getBasePathFromClass(Class clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        String[] absoluteDirParts = StringUtils.split(path, "/");
        String[] baseDirParts = Arrays.copyOfRange(absoluteDirParts, 0, absoluteDirParts.length - 2); // go back 2 spaces, "[1]/esm[2]/jars"

        String returnPath =  StringUtils.join(baseDirParts, "/");
        String osName = System.getProperty("os.name").toLowerCase();

        logger.info("Running on OS: " + osName);

        if(osName.indexOf("win") < 0)
        {
            // Append a leading '/' to get to the root
            return "/" + returnPath;
        }
        else
        {
            // IF windows - dont mess with the path
            return returnPath;
        }

    }
}
