## FIRST Stage
#JavaEE application building

FROM maven:3.6.0-jdk-8-alpine AS build
WORKDIR /myapp
RUN wget https://jdbc.postgresql.org/download/postgresql-42.2.5.jar \
         -O database-connector.jar
COPY pom.xml .
RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.1.1:go-offline
COPY src .
RUN mvn -B -e -o -T 1C verify

## SECOND Stage
# Application image building
FROM payara/server-full:5.183
#installs postgresql jdbc driver, jdbc connection pool and ressource are added in the src/main/webapp/WEB-INF/glassfish-resources.xml
ENV PATH="/opt/payara5/bin/:${PATH}"
COPY --from=build /myapp/database-connector.jar /opt/payara5/glassfish/domains/domain1/lib/
RUN asadmin start-domain && \
    asadmin --user admin \
    		--passwordfile=/opt/pwdfile \
    			      set-hazelcast-configuration --enabled=true --dynamic=true && \
    asadmin --user admin \
            --passwordfile=/opt/pwdfile \
			     create-jdbc-connection-pool \
			--datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource  \
			--restype javax.sql.ConnectionPoolDataSource \
			--property portNumber=5432:password=glassfishdbpassword:user=glassfish:serverName=db:databaseName=glassfish \
				postgres-pool && \
    asadmin --user admin \
                   --passwordfile=/opt/pwdfile \
			       create-jdbc-resource --connectionpoolid postgres-pool jdbc/postgres-pool && \
    asadmin stop-domain
COPY --from=build /myapp/target/*.war $DEPLOY_DIR
