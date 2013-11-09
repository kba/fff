package kba.fff.client.plugin;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import kba.fff.FffUnitTest;
import kba.fff.fff_common.NS;

import org.junit.Before;
import org.junit.Test;

import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.GValue;
import eu.dm2e.grafeo.Grafeo;
import eu.dm2e.grafeo.jena.GrafeoImpl;

public class ChecksumPluginTest extends FffUnitTest {
	
	private ChecksumPlugin	instance;

	@Before
	public void setUp() {
		this.instance = new ChecksumPlugin();
	}
	
	@Test
	public void testAnalyze() throws IOException, NoSuchAlgorithmException {
		
		URL fileURL = getClass().getResource("/image/screenshot.png");
		File file = new File(fileURL.getFile());
		InputStream is = null;
		is = fileURL.openStream();
		ERROR(is.available());
		is.close();
		
		Grafeo g = new GrafeoImpl();
		GResource res = g.createBlank();		
		this.instance.analyzeFile(g, res, file);
		assertThat(g.size(), not(0L));
		GValue x = g.firstMatchingObject(null, NS.CNT.CHARS);
		ERROR(x);
//		DEBUG(g.getTerseTurtle());
	}

}
;