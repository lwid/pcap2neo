package se.su.it.pcap2neo.neo.impl;

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.su.it.pcap2neo.neo.NeoPcapFacade;
import se.su.it.pcap2neo.neo.NeoStore;
import se.su.it.pcap2neo.pcap.impl.PcapCallbackImpl;

@Component
public class NeoPcapFacadeImpl implements NeoPcapFacade {

	private Logger logger = Logger.getLogger(PcapCallbackImpl.class);
	
	private NeoStore neoStore = null;
	
	@Override
	public Relationship addTcpSyn(InetAddress src, InetAddress dst, Integer port) {
		logger.info("Creating relation " + src + "=>" + dst + ", port=" + port);
		neoStore.addRelation(src.getHostAddress(), dst.getHostAddress(), port);
		return null;
	}

	public NeoStore getNeoStore() {
		return neoStore;
	}

	@Autowired
	public void setNeoStore(NeoStore neoStore) {
		this.neoStore = neoStore;
	}
	
}
