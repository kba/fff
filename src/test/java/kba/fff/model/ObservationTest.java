package kba.fff.model;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class ObservationTest extends TestCase {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	@Test
	public void testShit() {
		Observation ob = new Observation();
		ob.setCreated(DateTime.now());
		log.debug(ob.getTerseTurtle());
	}

}
