package se.su.it.pcap2neo.neo;

public interface NeoStore {
	public int addRelation(String from, String to, Integer port);
}