# zuul-route-jdbc-spring-cloud-starter
> A Spring Cloud Oracle-jdbc store for Zuul routes.


## Features

Extends the Spring Cloud's `DiscoveryClientRouteLocator` with capabilities of loading routes out of the configured jdbc-database(https://github.com/yangtao309/zuul-route-jdbc-spring-cloud-starter) to Oracle-database (zuul 1.4.4).

Instead of configuring your routes through `zuul.routes` like follows:

```yaml
zuul:
  ignoredServices: '*'
  routes:
    resource:
      path: /api/**
      serviceId: rest-service
```

You can store the routes in Oracle-DB.

Keep in mind that the other properties except for routes are still relevant.

```application.properties
zuul.store.jdbc.enabled = true
```

## Setup

Make sure the version of String Cloud Starter Zuul your project is using is at compatible with 1.4.4.RELEASE which this 
project is extending.

Add the Spring Cloud starter to your project:

```xml
<dependency>
  <groupId>io.github.jukyellow</groupId>
  <artifactId>zuul-route-oracle-spring-cloud-starter</artifactId>
  <version>2.0.0-SNAPSHOT</version>
</dependency>
```

Connect to oracle-db and create a keyspace(table):

```sql(oracle)

CREATE TABLE zuul_routes(
    id VARCHAR2(50),
    path VARCHAR2(500),
    service_id VARCHAR2(50),
    url VARCHAR2(500),
    strip_prefix char(1),
    retryable char(1),
    sensitive_headers VARCHAR2(500)    
);
ALTER TABLE zuul_routes add constraint pk_zuul_routes PRIMARY KEY(id);
```

Register `JdbcOperations` bean within your application:

```java
@SpringBootApplication
public static class Application {

  @Bean
  public DataSource dataSource(
  	  @Value("${spring.datasource.driverClassName}") String driverClassName, //(2019.05.29,juk) add
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String username,
      @Value("${spring.datasource.password}") String password,
      @Value("${spring.datasource.maxActive}") int maxActive) {

    BasicDataSource dataSource = new BasicDataSource(); //Apache Commons DBCP
    dataSource.setDriverClassName(driverClassName); 
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMaxActive(maxActive);
    return dataSource;
  }

  @Bean
  public JdbcOperations mysqlJdbcTemplate(@Autowired DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
```

Configure the jdbc to be used for loading the Zuul routes:

```application.properties
zuul.store.jdbc.enabled = true
```

Configure add application.properties for java source using: 

``` (2019.05.29,juk) add
spring.datasource.driverClassName= your-driver
spring.datasource.url= your-url
spring.datasource.username= your-user-name
spring.datasource.password= your-user-password
spring.datasource.maxActive= your-max-active
```

Finally enable the Zuul proxy with `@EnableZuulProxyStore` - use this annotation as a replacement for standard `@EnableZuulProxy`:

```java
@EnableZuulProxyStore
@EnableEurekaClient
@SpringBootApplication
public static class Application {
    ...
}
```

## Properties

```application.properties
zuul.store.jdbc.enabled=true# false by default
```

## License

Apache 2.0
