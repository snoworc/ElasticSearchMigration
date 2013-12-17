package configuration;

public class ElasticsearchConfigurationFactory  {
    public ElasticsearchConfigurationFactory() {
    }

    public static ElasticsearchConfiguration create() {
        ElasticsearchConfiguration cfg = new ElasticsearchConfiguration();
        cfg.loadConfiguration(ElasticsearchConfiguration.loadProperties());
        return cfg;
    }

}
