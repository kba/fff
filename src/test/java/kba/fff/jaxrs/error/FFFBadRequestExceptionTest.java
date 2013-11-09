package kba.fff.jaxrs.error;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import kba.fff.fff_common.ErrorMsg;

import org.junit.Test;



public class FFFBadRequestExceptionTest {
	
	@Test
	public void testIt() {
		
		{
			FFFBadRequestException exc1 = new FFFBadRequestException(ErrorMsg.INVALID_RDF);
			System.out.println(exc1);
			assertThat(exc1, not(nullValue()));
		}

		
	}

}
