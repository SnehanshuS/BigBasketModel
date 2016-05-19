package com.cassandra.usf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.client.Client;

import redis.clients.jedis.Jedis;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.es.usf.ESConnection;
import com.es.usf.SearchElastic;
import com.utilities.usf.Constants;

/**
* Servlet implementation class Search for the products search based on the product description.
* @author  Snehanshu Shankar
* @version 1.0
* @since   2016-04-25 
*/
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
    CassandraDBCon conCas ;
    Cluster clusterCassan ;
    Session sessionCassan ;
    Client client = ESConnection.getConnection();
    Jedis jedis;

    /**
     * init method to initialize objects.
     */
    @Override
    public void init() throws ServletException {
    	this.conCas = new CassandraDBCon();
    	this.clusterCassan = conCas.getCluster(Constants.IP_ADDRESS);
    	this.sessionCassan = conCas.getSession(Constants.KEYSPACE);
    	this.jedis = new Jedis(Constants.HOST);
    }
    
    /**
     * @param  request: HttpServletRequest.
	 * @param  response: HttpServletResponse.     
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          response.setContentType("text/html");
		  String searchString = request.getParameter(Constants.PRODUCT_ID).trim();
		  if(searchString != null && !searchString.isEmpty()){
			  List<String> listPids = SearchElastic.searchDocument(client, Constants.INDEX, Constants.DOCUMENT_TYPE, Constants.PRODUCT_DESC, searchString);
  			  ArrayList<ArrayList<String>> pid_list =new ArrayList<>();
			  Map<String, String> pdtHashMap = new HashMap<>();
	          for(String pid : listPids){
				 if (jedis.exists(pid)){
					 ArrayList<String> redisCacheDta = getDataFromCache(pid,jedis);
					 pid_list.add(redisCacheDta);
				 }
				 else{
					 //Insert Hash into Redis
					 ArrayList<String> cassandraDBdata = getDataFromCassandra(pid, clusterCassan, sessionCassan);
					 pid_list.add(cassandraDBdata);
					 pdtHashMap = new HashMap<String, String>();
					 pdtHashMap.put(Constants.PRODUCT_ID, cassandraDBdata.get(0));
					 pdtHashMap.put(Constants.PRODUCT_NAME, cassandraDBdata.get(1));
					 pdtHashMap.put(Constants.PRODUCT_DESC, cassandraDBdata.get(2));
					 pdtHashMap.put(Constants.PRODUCT_COST, cassandraDBdata.get(3));
					 pdtHashMap.put(Constants.IMAGE_URL, cassandraDBdata.get(4));
					 jedis.hmset(pid, pdtHashMap);
					//Set the TTL in seconds
					//jedis.expire(pid, 1245);
				 }
	 		  }
	        try {
	            request.setAttribute("piList", pid_list);
	            RequestDispatcher view = request.getRequestDispatcher("/searchresult.jsp");
	            view.forward(request, response);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		  }else{
			  RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
			  view.forward(request, response);
		  }
    }
      
    /**
     * Returns a list of information related to the product ID from Cassandra database in case not found in Cache.
     * @param  pid (product ID)of the product.
     * @param  session is a Session instance.
	 * @param  cluster is an instance of Cassandra Cluster.
	 * @return an ArrayList<String> containing information related to the product ID from Cassandra database.
	 */
    
    public ArrayList<String> getDataFromCassandra(String pid, Cluster cluster, Session session){
		String pdtid = null, pdtname = null, pdtdesc = null;
		String price = null, imageUrl =null;
		ResultSet results = session.execute("SELECT * FROM products where pid = "+pid);
		ArrayList<String> productInfo = null;
 		for (Row row : results) {
 			productInfo = new ArrayList<String>();
			pdtid = Integer.toString(row.getInt(Constants.PRODUCT_ID));
			pdtname = row.getString(Constants.PRODUCT_NAME);
			pdtdesc = row.getString(Constants.PRODUCT_DESC);
			price = Float.toString(row.getFloat(Constants.PRODUCT_COST));
			imageUrl = row.getString(Constants.IMAGE_URL);
			productInfo.add(pdtid);
			productInfo.add(pdtname);
			productInfo.add(pdtdesc);
			productInfo.add(price);
			productInfo.add(imageUrl);

		}
		return productInfo;

        
    }
    /**
     * Returns a list of information related to the product ID in cache.
     * @param  pid (product ID)of the product.
	 * @param  jedis is an instance of Jedis.
	 * @return an ArrayList<String> containing information related to the product ID in cache.
	 */
    public ArrayList<String> getDataFromCache(String pid, Jedis jedis){
    	Map<String, String> pdtRetrieveHM = jedis.hgetAll(pid); 
     	ArrayList<String> rmapvalues = new ArrayList<String>();
     	rmapvalues.add(0, pdtRetrieveHM.get(Constants.PRODUCT_ID));
     	rmapvalues.add(1, pdtRetrieveHM.get(Constants.PRODUCT_NAME));
     	rmapvalues.add(2, pdtRetrieveHM.get(Constants.PRODUCT_DESC));
     	rmapvalues.add(3, pdtRetrieveHM.get(Constants.PRODUCT_COST));
     	rmapvalues.add(4, pdtRetrieveHM.get(Constants.IMAGE_URL));
    	return rmapvalues;
    }
 
    /**
     * Closing all the open connections.
     */
    @Override
    public void destroy() {
    	jedis.close();
        clusterCassan.close();
        sessionCassan.close();
      }
}