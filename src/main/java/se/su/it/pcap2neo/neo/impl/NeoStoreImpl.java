package se.su.it.pcap2neo.neo.impl;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.stereotype.Component;

import se.su.it.pcap2neo.neo.NeoStore;


@Component
public class NeoStoreImpl implements NeoStore {

	public enum connTypes implements RelationshipType
	{
	    TCP, UDP
	}

	private GraphDatabaseService graphDb; 
	private IndexService index;
	
	public NeoStoreImpl() {
		graphDb = new EmbeddedGraphDatabase( "/opt/neodb/pcap4neo" );
		index = new LuceneIndexService( graphDb );
	}
	
	
	/* (non-Javadoc)
	 * @see se.su.it.pcap2neo.neo.impl.NeoStore#addRelation(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public int addRelation(String from, String to, Integer port) {
		
		Transaction tx = graphDb.beginTx();
		
		try {

			// Fetch, or create, the source node
			Node src = index.getSingleNode( "addr", from);
			if( src == null) {
				src = graphDb.createNode();
				src.setProperty( "addr", from );
			}
		
			// Fetch, or create, the destination node
			Node dst = index.getSingleNode( "addr", to);
			if( dst == null) {
				dst = graphDb.createNode();
				dst.setProperty( "addr", to );
			}
		
			// Create the relation
			Relationship syn = src.createRelationshipTo( dst, connTypes.TCP );
			syn.setProperty( "port", port );
		}
		finally {
			tx.finish();
		}

		return 0;
		
	}
	
	
	
	
}
