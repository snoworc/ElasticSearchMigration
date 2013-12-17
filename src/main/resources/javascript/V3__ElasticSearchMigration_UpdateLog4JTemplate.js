/*
 Calling convention:
 => node <this_file.js> <elastic-search-protocol> <elastic-search-host> <elastic-search-port> <river-url> <river-user> <river-password>

 example:
 => node test.js http:// irvlinidx02.dev.local 9200 jdbc:mysql://irvlindbs05.dev.local:3306/cvp_orgman root Password1*

 */

ElasticSearchClient = require('elasticsearchclient');
var winston = require('winston');
var fs = require('fs');
var migration = require('elasticsearchmigration/lib/elasticsearchmigration');
migration.loadOptions(process.argv);

fs.unlink(migration.options.log, function (err) {});

var logger = new (winston.Logger)({
    transports: [
        new (winston.transports.File)({
            filename: migration.options.log,
            json: false,
            timestamp: false })
    ]
});

var serverOptions = {
    host: migration.options.host,
    port: migration.options.port,
    secure: migration.options.secure
};

var elasticSearchClient = new ElasticSearchClient(serverOptions);

migration.core.createTemplate("logstash", migration.template.logstash, elasticSearchClient, logger);

var waitForTemplateCreation = setTimeout(function() {
    if (!fs.existsSync(migration.options.log)) {
        logger.log('info', 'success: true');
    }

    var waitForAsyncLogFileCreation= setTimeout(function() {}, 3000);

}, 3000);


