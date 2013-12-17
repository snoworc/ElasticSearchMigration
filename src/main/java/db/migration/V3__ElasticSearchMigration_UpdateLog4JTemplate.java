package db.migration;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;
import configuration.ElasticsearchConfiguration;
import configuration.ElasticsearchConfigurationFactory;
import configuration.Path;
import migration.NodeJS;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: sshah
 * Date: 10/21/13
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class V3__ElasticSearchMigration_UpdateLog4JTemplate implements JdbcMigration {

    private final static Logger logger = Logger.getLogger(V3__ElasticSearchMigration_UpdateLog4JTemplate.class);
    private final String node_exe_path = "node";
    private final String node_script_path = Path.getBasePathFromClass(V3__ElasticSearchMigration_UpdateLog4JTemplate.class) + "/misc/javascript/" + this.getClass().getSimpleName() + ".js";

    @Override
    public void migrate(Connection connection) throws Exception {
        logger.info("Begin migration, invoking: " + this.node_exe_path + " " + this.node_script_path);

        ElasticsearchConfiguration es = ElasticsearchConfigurationFactory.create();

        NodeJS.migrateScript(this.node_exe_path, this.node_script_path, es);

        logger.info("Flyway migration successful");
    }
}
