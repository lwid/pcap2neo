package se.su.it.pcap2neo;

import se.su.it.pcap2neo.pcap.PcapFileParser;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// Run tcpdump as:
// tcpdump -i eth1 'tcp[13] == 2'

public class App 
{
	static final String PCAPFILE = "/home/lwid/dump.pcap";

	public static void main(String[] args) {
	    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
	    ctx.scan("se.su.it.pcap2neo");
	    ctx.refresh();

	    PcapFileParser parser = (PcapFileParser) ctx.getBean(PcapFileParser.class);
	    parser.parseFile(PCAPFILE);
	    
	    // ...add more parseFile here...

	    System.exit(0);
	    
	}
}
