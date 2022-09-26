package tn.supcom;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.jnosql.diana.api.Settings;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.mongodb.document.MongoDBDocumentCollectionManager;
import org.jnosql.diana.mongodb.document.MongoDBDocumentConfiguration;

import java.util.Collections;
import java.util.Map;

@ApplicationPath("/rest-api")
public class JaxRSActivator extends Application{

    @ApplicationScoped
    public static class CDIConfigurator {

        private static final String DATABASE = "todos";

       /*

        @Inject
        @Database(provider = "todos", value = DatabaseType.DOCUMENT)
        @ConfigurationUnit(name = "document")
        private DocumentCollectionManagerFactory<MongoDBDocumentCollectionManager> managerFactory;

        @Produces
        public DocumentCollectionManager getEntityManager() {
            return managerFactory.get(DATABASE);
        }


        */



        @Produces
        public MongoDBDocumentCollectionManager createEntityManager(){
            MongoDBDocumentConfiguration configuration = new MongoDBDocumentConfiguration();
            Map<String, Object> settings = Collections.singletonMap("mongodb-server-host-1", "localhost:27017");
            return configuration.get(Settings.of(settings)).get("todos");
        }

        public void close(@Disposes DocumentCollectionManager entityManager) {
            entityManager.close();
        }
    }



}
