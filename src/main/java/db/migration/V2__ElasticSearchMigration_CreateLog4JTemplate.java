package db.migration;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;
import configuration.ElasticsearchConfiguration;
import configuration.ElasticsearchConfigurationFactory;
import configuration.Path;
import migration.NodeJS;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class V2__ElasticSearchMigration_CreateLog4JTemplate implements JdbcMigration {
    private final static Logger logger = Logger.getLogger(V2__ElasticSearchMigration_CreateLog4JTemplate.class);
    private final String node_exe_path = "node";
    private final String node_script_path = Path.getBasePathFromClass(V2__ElasticSearchMigration_CreateLog4JTemplate.class) + "/misc/javascript/" + this.getClass().getSimpleName() + ".js";

    public V2__ElasticSearchMigration_CreateLog4JTemplate() {
    }

    @Override
    public void migrate(Connection connection) throws Exception {
        logger.info("Begin migration, invoking: " + this.node_exe_path + " " + this.node_script_path);

        ElasticsearchConfiguration es = ElasticsearchConfigurationFactory.create();

        NodeJS.migrateScript(this.node_exe_path, this.node_script_path, es);

        logger.info("Flyway migration successful");
    }
}
