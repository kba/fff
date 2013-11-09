package kba.fff.fff_common;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class UriMinterTest {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	@Test
	public void testRandomNumber() {
		String randStr1 = UriMinter.randomString(8);
		assertEquals(8, randStr1.length());
		String randStr2 = UriMinter.randomString();
		assertEquals(8, randStr2.length());
		assertNotSame(randStr1, randStr2);
	}

	@Test
	public void testMinter() {
		{
			String uri = UriMinter.mintURI("urn:x-fff:${uuid}");
			assertThat(uri, not(containsString("${uuid}")));
		}
		{
			String uri = UriMinter.mintURI("urn:x-fff:${timestamp}");
			assertThat(uri, not(containsString("${timestamp}")));
		}
		{
			String uri = UriMinter.mintURI("urn:x-fff:${iso8601}");
			assertThat(uri, not(containsString("${iso8601}")));
		}
		{
			String uri = UriMinter.mintURI("urn:x-fff:${randomString}");
			assertThat(uri, not(containsString("${randomString}")));
		}
		{
			String uri = UriMinter.mintURI("urn:x-fff:${randomString[7]}");
			assertThat(uri, not(containsString("${randomString[7]}")));
		}
		{
			String uri = UriMinter.mintURI("urn:x-fff:${randomString[2]}/${randomString[20]}");
			assertThat(uri, not(containsString("${randomString[}")));
		}
		
	}

}
