/*
 Calling convention:
 => node <this_file.js> <elastic-search-protocol> <elastic-search-host> <elastic-search-port> <river-url> <river-user> <river-password>

 example:
 => node test.js http:// irvlinidx02.dev.local 9200 jdbc:mysql://irvlindbs05.dev.local:3306/cvp_orgman root Password1*

 // flyway -url=jdbc:mysql://irvlindbs05.dev.local:3306/cvp_migration_es -user=root -password=Password1* -locations=db/migration  migrate
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

var index = "dg";

// do not allow line breaks in the sql property
var river = new migration.river.instance({
    riverQueue : [
        {
            name: "user_river",
            index: index,
            type: "User",
            url: migration.options.river_url,
            user: migration.options.river_user,
            password: migration.options.river_password,
            sql: "select u.id, u.userName, TRIM(LEADING FROM CONCAT(u.firstName, ' ', u.lastName)) as 'fullName', u.firstName, u.lastName, u.customerId, u.email, u.phone, u.mobilePhone, u.otherPhone, u.fax, u.active, u.locked, u.title, u.address1, u.address2, u.city, u.state, u.postalCode, u.country, u.timeZone, u.language, u.region, u.lastLoginDate, u.lastModifiedDate, u.createdDate, u.createdBy, u.salesforceId, u.status, c.customerName from user u left outer join customer c on c.id = u.customerid",
            bulkSize: 30,
            maxBulkRequests: 30,
            verification_wait_ms: 15000,
            verification_records_threshold: 100
        },
        {
            name: "category_river",
            index: index,
            type: "categoryNode",
            url: migration.options.river_url,
            user: migration.options.river_user,
            password: migration.options.river_password,
            sql: "select c.id, c.name, replace(c.tagIds,' | ', ' ') as tagIds, c.tags as tags, c.categorySchemaCategoryTypeId as categoryTypeId, c.parentId, c.active, s.level, s.name as levelName from om_category c inner join om_categorySchemaCategoryType s on s.id = c.categorySchemaCategoryTypeId",
            bulkSize: 30,
            maxBulkRequests: 30,
            verification_wait_ms: 15000,
            verification_records_threshold: 1000
        }
    ]
});

migration.core.createIndex(index, {"settings":{"index":{"number_of_shards":"8","number_of_replicas":"1"}}}, elasticSearchClient, logger);

var waitForIndexCreation = setTimeout(function() {
    migration.core.putMapping(index, 'User' ,migration.mapping.user, elasticSearchClient, logger);

    migration.core.putMapping(index, 'categoryNode' ,migration.mapping.categoryNode, elasticSearchClient, logger);

    migration.core.putMapping(index, 'categoryTree' ,migration.mapping.categoryTree, elasticSearchClient, logger);

    river.createRiver("user_river", river.getRiverSettings('user_river'), elasticSearchClient, logger);

    river.createRiver("category_river", river.getRiverSettings('category_river'), elasticSearchClient, logger);

    if (!fs.existsSync(migration.options.log)) {
        logger.log('info', 'success: true');
    }

    var waitForAsyncLogFileCreation= setTimeout(function() {}, 3000);

}, 3000);


