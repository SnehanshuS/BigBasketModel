package com.es.usf;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.utilities.usf.Constants;

/**
* ESConnection class is constructed to get client object for the connection 
* using TransportClient with the elasticsearch.  
* @author  Snehanshu Shankar
* @version 1.0
* @since   2016-04-25 
*/
public class ESConnection {
	
    /**
     * @return a client for the elasticsearch connection.
	 */
	public static Client getConnection(){
    	Settings settings = ImmutableSettings.settingsBuilder()
    	        .put(Constants.CLUSTER_NAME, Constants.ELASTIC_SEARCH).build();
    	Client client = new TransportClient(settings)
     	        .addTransportAddress(new InetSocketTransportAddress(Constants.HOST, 9300));
       return client;

	}
    /**
     * @return a Map after constructing the JSON Document object with product ID and product description.
	 */
	public static Map<String, Object> putJsonDocument(String productId, String productDescription){
	     Map<String, Object> jsonDocument = new HashMap<String, Object>();
	     jsonDocument.put(Constants.PRODUCT_ID, productId);
	     jsonDocument.put(Constants.PRODUCT_DESC, productDescription);
	     return jsonDocument;
	}

}
