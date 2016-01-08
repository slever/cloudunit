package fr.treeptik.tutum.common;

import org.apache.http.client.HttpClient;

/**
 * Created by nicolas on 08/01/2016.
 */
public class TutumClient {

    /**
     * Authorization Token for Accessing Tutum API
     */
    protected String authToken;

    /**
     * API Version
     */
    public static String API_VERSION = "api/v1";

    /**
     * EndPoint REST
     */
    public static String endPoint_REST = "https://dashboard.tutum.co/";

    /**
     * EndPoint WebSocket
     */
    public static String endPoint_WS = "wss://stream.tutum.co/";

    protected HttpClient httpClient;

    public TutumClient( String authToken ) {
        this.authToken = authToken;
    }

    public void setBaseUrl() {
        String restHost = System.getenv("TUTUM_REST_HOST");
        if (restHost == null) {

        }
    }

}
