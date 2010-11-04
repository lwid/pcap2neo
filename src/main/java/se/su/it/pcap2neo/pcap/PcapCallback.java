package se.su.it.pcap2neo.pcap;

import edu.gatech.sjpcap.IPPacket;

public interface PcapCallback {
	public int ipPacketCallback(IPPacket packet);
}
