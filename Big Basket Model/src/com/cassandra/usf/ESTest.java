package com.cassandra.usf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.utilities.usf.Constants;
/**
* The ESTest class is constructed in order to provide test functionality for 
* searchDocument, getDocument, updateDocument & deleteDocumentcreate.
** @author  Snehanshu Shankar
* @version 1.0
* @since   2016-04-25 
*/
public class ESTest {

	 public static void main(String args[]) throws IOException{

	    	Settings settings = ImmutableSettings.settingsBuilder()
	    	        .put(Constants.CLUSTER_NAME, Constants.ELASTIC_SEARCH).build();
	    	Client client = new TransportClient(settings)
	    	        .addTransportAddress(new InetSocketTransportAddress(Constants.HOST, 9300));
	    	// Execute below code in order to create indexes for the product document with an index name ecom.
			// client.prepareIndex("ecom", "product", "1").setSource(putJsonDocument("1001", "Laptop Dell Intel core i5 processor")).execute().actionGet();
			// client.prepareIndex("ecom", "product", "2").setSource(putJsonDocument("1002","Laptop Lenevo Intel core i5 processor")).execute().actionGet();
			// client.prepareIndex("ecom", "product", "3").setSource(putJsonDocument("1003","Camera Nikon, 21 MP, SLR")).execute().actionGet();
			// client.prepareIndex("ecom", "product", "4").setSource(putJsonDocument("1004","Mobile Apple Phone 6S, 16GB")).execute().actionGet();
			// client.prepareIndex("ecom", "product", "5").setSource(putJsonDocument("1005","Laptop HP , 15 inches touch screen, intel core i7 processor")).execute().actionGet();
			// client.prepareIndex("ecom", "product", "6").setSource(putJsonDocument("1006","Laptop Sony , 14 inches touch screen, intel core i7 processor")).execute().actionGet();
	    	// client.prepareIndex("ecom", "product", "7").setSource(putJsonDocument("1007","Samsung Galaxy Tablet, 10.1 inch displaym MicroSD, 64GB")).execute().actionGet();
	    	// client.prepareIndex("ecom", "product", "8").setSource(putJsonDocument("1008","Microsoft Surface3 Tablet, 10.8 inch, windows 10, 128GB")).execute().actionGet();
	    	// client.prepareIndex("ecom", "product", "9").setSource(putJsonDocument("1010","Camera Canon, 18 MP, SLR, CMOS sensor")).execute().actionGet();
      
	        // updateDocument(client, "sneh", "article", "1", "title", "ElasticSearch: Java API");
	        // updateDocument(client, "sneh", "article", "1", "tags", new String[]{"bigdata"});
	        // getDocument(client, "sneh", "article", "1");
	        // System.out.println("Final PIDS"+searchDocument(client, "ecom", "product", "pdesc", "apple"));
	        // deleteDocument(client, "ecom", "product", "6");
	        
	 }
	    
	    public static Map<String, Object> putJsonDocument(String pid, String pdesc){
	        
	        Map<String, Object> jsonDocument = new HashMap<String, Object>();
	        
	        jsonDocument.put(Constants.PRODUCT_ID, pid);
	        jsonDocument.put(Constants.PRODUCT_DESC, pdesc);
	        return jsonDocument;	
	    }
	    
		/**
		 * Returns a list of all the relevant product id's found with particular search.
		 * @param  client: client for the elasticsearch connection.
		 * @param  index is the name of index in elasticsearch.
		 * @param  type is the document type.
		 * @return an List<String> object containing all the relevant product id's found with particular search.
		 */
	    public static List<String> searchDocument(Client client, String index, String type,
            String field, String value){
    	    String[] arr =value.split(" ");
			ArrayList<String> productIds =new ArrayList<>();
			QueryBuilder query = null;
            query =	QueryBuilders.boolQuery().minimumShouldMatch("75%").should(QueryBuilders.fuzzyQuery(field,value).maxExpansions(1).prefixLength(value.length() - value.length()/2))
   			.should(QueryBuilders.wildcardQuery(field, "*"+value+"*"));

			QueryBuilder query1 =  QueryBuilders.matchQuery(field, value);
			SearchRequestBuilder response = client.prepareSearch(index)
			                  .setTypes(type)
			                  .setQuery(query).addHighlightedField(field, 0, 0);
			SearchRequestBuilder response1 = client.prepareSearch(index)
	                  .setTypes(type)
	                  .setQuery(query1).addHighlightedField(field, 0, 0);
			MultiSearchResponse res = client.prepareMultiSearch().add(response).add(response1).execute().actionGet();
			for(Item sr  : res.getResponses()){
				SearchHit[] results = sr.getResponse().getHits().getHits();
				for (SearchHit hit : results) {
					System.out.println("------------------------------");
					Map<String,Object> result = hit.getSource();  
					productIds.add((String)result.get(Constants.PRODUCT_ID));
				}
			}
			ArrayList<String> pids = new ArrayList<>();
			Set<String> hs = new HashSet<>();
			hs.addAll(productIds);
			pids.addAll(hs);

			return pids;
		}
	    
		/**
		 * This method is to retrieve documents in elasticsearch.
		 * @param  client: client for the elasticsearch connection.
		 * @param  index is the name of index in elasticsearch.
		 * @param  type is the document type.
		 * @param  id: id of the document.
		 */
	    public static Map<String, Object> getDocument(Client client, String index, String type, String id){
	        
	        GetResponse getResponse = client.prepareGet(index, type, id)
	                                        .execute()
	                                        .actionGet();
	        Map<String, Object> source = getResponse.getSource();
	        return source;
	        
 	    }
	    
		/**
		 * This method is to update a document in elasticsearch.
		 * @param  client: client for the elasticsearch connection.
		 * @param  index is the name of index in elasticsearch.
		 * @param  type is the document type.
		 * @param  id of the document.
		 * @param  field in the document.
		 * @param  newValue for the field.
		 */
	    public static void updateDocument(Client client, String index, String type, 
	                                      String id, String field, String newValue){
	        
	        Map<String, Object> updateObject = new HashMap<String, Object>();
	        updateObject.put(field, newValue);
	        client.prepareUpdate(index, type, id)
	              .setScript("ctx._source." + field + "=" + field)
	              .setScriptParams(updateObject).execute().actionGet();
	    }

		/**
		 * This method is to delete a document in elasticsearch.
		 * @param  client: client for the elasticsearch connection.
		 * @param  index is the name of index in elasticsearch.
		 * @param  type is the document type.
		 * @param  id of the document.
		 */
  	    public static void deleteDocument(Client client, String index, String type, String id){
	        
	        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();

	    }
	}
