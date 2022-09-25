package tn.supcom;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerFactory;
import org.jnosql.diana.mongodb.document.MongoDBDocumentCollectionManager;

@ApplicationPath("/rest-api")
public class JaxRSActivator extends Application{

    @ApplicationScoped
    public static class CDIConfigurator {

        private static final String DATABASE = "todos";

        @Inject
        @ConfigurationUnit(name = "document")
        private DocumentCollectionManagerFactory<MongoDBDocumentCollectionManager> managerFactory;

        @Produces
        public DocumentCollectionManager getEntityManager() {
            return managerFactory.get(DATABASE);
        }

        public void close(@Disposes DocumentCollectionManager entityManager) {
            entityManager.close();
        }

    }

}
