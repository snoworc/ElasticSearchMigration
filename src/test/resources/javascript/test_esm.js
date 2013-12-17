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
    host: "irvlinidx02.dev.local",
    port: 9200,
    secure: false
};

var client = new ElasticSearchClient(serverOptions);


// console.log(migration.mapping.user);


var river = new migration.river.instance({
    riverQueue : [
        {
            name: "user_river",
            index: "dg2",
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
            index: "dg3",
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

//console.log(migration.options);
//console.log(river.getConfigurationSettings().river_url); \
//client.deleteRiver('user_river');

var cfg = river.getRiverSettings('category_river');
/*
console.log('category_river');
console.log(cfg)

cfg = river.getRiverSettings('user_river');
console.log('user_river');
console.log(cfg)

cfg = river.getRiverSettings('hill_billy');
console.log('hill_billy');
console.log(cfg)
*/
//river.createRiver("user_river", cfg, client, logger)
//migration.core.createIndex('dg4', {}, client, logger);
//migration.core.putMapping('dg4', 'bogus' ,migration.mapping.user, client, logger);



