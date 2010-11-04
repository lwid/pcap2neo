package se.su.it.pcap2neo.neo;

import java.net.InetAddress;

import org.neo4j.graphdb.Relationship;

public interface NeoPcapFacade {
	public Relationship addTcpSyn(InetAddress src, InetAddress dst, Integer port);
}
