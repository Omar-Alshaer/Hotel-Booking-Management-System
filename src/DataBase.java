import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class DataBase {

    private MongoClient mongoClient;

    private MongoDatabase database;

    private static final String CONNECTION_STRING =
            "mongodb+srv://HotelDB:omarreda2006@cluster0.o8cvhey.mongodb.net/?appName=Cluster0";

    public DataBase() {

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build();

        mongoClient = MongoClients.create(settings);

        database = mongoClient.getDatabase("HotelDB");
    }

    public MongoCollection<Document> getGuestsCollection() {
        return database.getCollection("guests");
    }

    public MongoCollection<Document> getRoomsCollection() {
        return database.getCollection("rooms");
    }

    public MongoCollection<Document> getReservationsCollection() {
        return database.getCollection("reservations");
    }

    public MongoCollection<Document> getServiceRequestsCollection() {
        return database.getCollection("serviceRequests");
    }

    public MongoCollection<Document> getBillingCollection() {
        return database.getCollection("billing");
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
