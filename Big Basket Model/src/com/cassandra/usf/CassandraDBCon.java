package com.cassandra.usf;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
* CassandraDBCon class is constructed to get session and cluster object.
* @author  Snehanshu Shankar
* @version 1.0
* @since   2016-04-25 
*/
public class CassandraDBCon {
	private Cluster cluster;
    /**
     * @return a Session for the connection of the Cassandra cluster.
	 */
	public Session getSession(String keyspace) {
		return this.cluster.connect(keyspace);
	}
    /**
     * @return a cluster instance for the Cassandra DB.
	 */
	public Cluster getCluster(String ip) {
		return cluster = Cluster.builder()
                .addContactPoint(ip).build();
		
	}

}
