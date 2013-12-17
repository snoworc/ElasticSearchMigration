package migration;

import configuration.ElasticsearchConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class NodeJS {

    private static final Logger logger = Logger.getLogger(NodeJS.class);

    public static void migrateScript(String nodeExePath, String nodeJsPath, ElasticsearchConfiguration es) throws Exception {

        List<String> params =
                java.util.Arrays.asList(nodeExePath,
                        nodeJsPath,
                        es.getProtocol(),
                        es.getHost(),
                        String.valueOf(es.getPort()),
                        es.getRiverUrl(),
                        es.getRiverUser(),
                        es.getRiverPassword());

        ProcessBuilder builder = new ProcessBuilder(params);
        logger.info("running: " + StringUtils.join(params, " "));
        Process process = builder.start();
        process.waitFor();

        ResponseHandler.process(Files.newBufferedReader(Paths.get(nodeJsPath + ".log"), Charset.forName("US-ASCII")));
    }
}
