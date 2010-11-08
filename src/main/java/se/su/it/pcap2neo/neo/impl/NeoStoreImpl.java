package se.su.it.pcap2neo.neo.impl;


import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.RelationshipIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.stereotype.Component;
import se.su.it.pcap2neo.neo.NeoStore;

@Component
public class NeoStoreImpl implements NeoStore {

	
	private static final String REF_PROP = "refcnt";
	
	private Logger logger = Logger.getLogger(NeoStoreImpl.class);
	
	public enum connTypes implements RelationshipType
	{
	    TCP, UDP
	}

	private GraphDatabaseService graphDb; 
	private Index<Node> index;
	private RelationshipIndex relIndex;
	
	public NeoStoreImpl() {
		logger.info("Setting up NeoStoreImpl");
		graphDb = new EmbeddedGraphDatabase( "/opt/neodb/pcap4neo" );
		index = graphDb.index().forNodes( "host" );
		relIndex = graphDb.index().forRelationships( "conn" );
	}
	
	
	/* (non-Javadoc)
	 * @see se.su.it.pcap2neo.neo.impl.NeoStore#addRelation(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public int addRelation(String from, String to, Integer port) {
		
		Transaction tx = graphDb.beginTx();
		
		try {
			Node src = getOrCreateNode("addr", from);
			Node dst = getOrCreateNode("addr", to);
			getOrCreateRelationship("port", port.toString(), src, dst);
			tx.success();
		}
		finally {
			tx.finish();
		}

		return 0;
		
	}
	
	/**
	 * Create or fetch a node by attribute
	 * @param key Primary key name
	 * @param value Primary key value
	 * @return The Node created/found
	 */
	private Node getOrCreateNode( String key, String value )
	{
		IndexHits<Node> res = index.get( key, value);
		Node node = null;
		if ( !res.hasNext() ) 
		{
			logger.info("No node found for " + key + "=" + value + " - creating it.");
			node = graphDb.createNode();
			node.setProperty( key, value);
			index.add( node, key, value);
		} else {
			node = res.getSingle();
			logger.info("Returning existing node for " + key + "=" + value + " - reusing it.");
			res.close();
		}
		return node;
	}

	
	private Relationship getOrCreateRelationship( String key, String value, Node from, Node to )
	{
		Relationship rel = null;
		IndexHits<Relationship> res = null;
		try {
			res = relIndex.query( key, value, from, to);
		} catch(NullPointerException npe) {
			logger.warn("Got a NPE when searching index, this is not too good.");
		}

		if ( res==null || !res.hasNext() ) 
		{
			logger.info("No relationship found for " + key + "=" + value + " for " + from + "=>" + to + " - creating it.");
			rel = from.createRelationshipTo( to, connTypes.TCP );
			rel.setProperty( key, value );
			rel.setProperty( REF_PROP, 1 );
			relIndex.add( rel, key, value);
		} else {
			logger.info("Returning existing relationship for " + key + "=" + value + " - reusing it.");
			rel = res.getSingle();
			res.close();
			rel.setProperty( key, (Integer)rel.getProperty(REF_PROP) + 1);
		}
		return rel
		;
	}



	
}