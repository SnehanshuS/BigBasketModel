package com.es.usf;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.utilities.usf.Constants;

/**
* The SearchElastic class is constructed in order to search for the
* indexed documents based on the product description.
** @author  Snehanshu Shankar
* @version 1.0
* @since   2016-04-25 
*/
public class SearchElastic {
	/**
	 * Returns a list of all the relevant product id's found with particular search.
	 * @param  client: client for the elasticsearch connection.
	 * @param  index is the name of index in elasticsearch.
	 * @param  type is the document type.
	 * @return an List<String> object containing all the relevant product id's found with particular search.
	 */
	 public static List<String> searchDocument(Client client, String index, String type,
	         String field, String value){
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
}