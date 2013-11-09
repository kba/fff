package kba.fff.model;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChecksumTest {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	@Test
	public void testPojo() {
		
		Checksum ck = new Checksum();
		ck.setChecksumValue("123");
		ck.setCheckusmAlgorithm("MD5");
		ByteRegion br = new ByteRegion();
		br.setLength(-1);
		br.setOffset(0);
		ck.setByteRegion(br);
		log.debug(ck.getTurtle());
	}

}
