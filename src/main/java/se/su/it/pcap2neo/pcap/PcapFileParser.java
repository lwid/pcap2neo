package se.su.it.pcap2neo.pcap;

public interface PcapFileParser {

	int parseFile(String fname);
	
	public PcapCallback getPcapCallback();
	public void setPcapCallback(PcapCallback c);

}
