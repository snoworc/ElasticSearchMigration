package configuration;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.util.Properties;

public class ElasticsearchConfiguration {
    private final static Logger logger = Logger.getLogger(ElasticsearchConfiguration.class);
    private final static String CONFIG_FILE_PATH = Path.getBasePathFromClass(ElasticsearchConfiguration.class) + "/misc/config.properties";

    private String protocol;
    private String host;
    private int port;
    private String index;
    private String riverUrl;
    private String riverUser;
    private String riverPassword;
    private int riverBulkSize;
    private int riverMaxBulkRequests;

    public ElasticsearchConfiguration() {
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();

        logger.info("Config file path: " + CONFIG_FILE_PATH);
        try {
            properties.load(new FileInputStream(CONFIG_FILE_PATH));
        } catch (Exception ex) {
            logger.error("Unable to load configuration file. File not found: " + CONFIG_FILE_PATH, ex);
            return null;
        }
        return properties;
    }

    public void loadConfiguration(Properties properties) {
        this.setProtocol(properties.getProperty("es.protocol", "http://"));
        this.setHost(properties.getProperty("es.host"));
        this.setPort(Integer.valueOf(properties.getProperty("es.port", "9200")));
        this.setIndex(properties.getProperty("es.index", "dg"));
        this.setRiverUrl(properties.getProperty("es.river.url"));
        this.setRiverUser(properties.getProperty("es.river.user"));
        this.setRiverPassword(properties.getProperty("es.river.password"));
        this.setRiverBulkSize(Integer.valueOf(properties.getProperty("es.river.bulksize")));
        this.setRiverMaxBulkRequests(Integer.valueOf(properties.getProperty("es.river.maxbulkrequests")));
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getRiverUrl() {
        return riverUrl;
    }

    public void setRiverUrl(String riverUrl) {
        this.riverUrl = riverUrl;
    }

    public String getRiverUser() {
        return riverUser;
    }

    public void setRiverUser(String riverUser) {
        this.riverUser = riverUser;
    }

    public String getRiverPassword() {
        return riverPassword;
    }

    public void setRiverPassword(String riverPassword) {
        this.riverPassword = riverPassword;
    }

    public int getRiverBulkSize() {
        return riverBulkSize;
    }

    public void setRiverBulkSize(int bulkSize) {
        this.riverBulkSize = bulkSize;
    }

    public int getRiverMaxBulkRequests() {
        return riverMaxBulkRequests;
    }

    public void setRiverMaxBulkRequests(int maxBulkRequests) {
        this.riverMaxBulkRequests = maxBulkRequests;
    }
}
