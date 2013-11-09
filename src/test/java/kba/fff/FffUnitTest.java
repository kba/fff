package kba.fff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FffUnitTest {
	
	protected Logger log = LoggerFactory.getLogger(getClass().getName());
	
	protected void DEBUG (Object obj) { log.debug(obj == null ? "" : obj.toString()); }
	protected void TRACE (Object obj) { log.trace(obj == null ? "" : obj.toString()); }
	protected void ERROR (Object obj) { log.error(obj == null ? "" : obj.toString()); }

}
