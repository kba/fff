package kba.fff.client.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kba.fff.fff_common.NS;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.Grafeo;

public class ChecksumPlugin implements IAnalyzerPlugin{
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	@Override
	public void analyzeInputStream(Grafeo g, GResource dataObjectRes, InputStream is) throws IOException {
		
		final String algo = "MD5";
		final int bufferSize = 1024 * 1024;
		int actualBufferSize = bufferSize;

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algo);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		byte[] buffer = new byte[bufferSize];
		byte[] digest = null;
		String hexdigest;
		DigestInputStream dis = new DigestInputStream(is, md);
		int actuallyRead = dis.read(buffer);
		if (actuallyRead < bufferSize) {
			actualBufferSize = actuallyRead;
		}
		dis.close();
		//			is.reset();
		is.close();
		digest = md.digest();
		hexdigest = new String(Hex.encodeHex(digest));
		log.debug(hexdigest);

		GResource annoRes = g.createBlank();
		GResource bodyRes = g.createBlank();
		GResource targetRes = g.createBlank();
		GResource specificRes = g.createBlank();
		GResource selectorRes = g.createBlank();
		//			
		g.addTriple(bodyRes, NS.RDF.TYPE, g.resource(NS.FFF.CLASS_CHECKSUM));
		g.addTriple(bodyRes, NS.RDF.TYPE, g.resource(NS.OA.TAG));
		g.addTriple(bodyRes, NS.RDF.TYPE, g.resource(NS.CNT.CONTENT_AS_TEXT));
		g.addTriple(bodyRes, NS.OA.MOTIVATED_BY, g.resource(NS.OA.IDENTIFYING));
		g.addTriple(bodyRes, NS.CNT.CHARS, g.literal(hexdigest));
		g.addTriple(bodyRes, NS.FFF.PROP_CHECKSUM_ALGORITHM, g.literal(algo));

		g.addTriple(selectorRes, NS.RDF.TYPE, g.resource(NS.OA.DATA_POSITION_SELECTOR));
		g.addTriple(selectorRes, NS.OA.START, g.literal(0));
		g.addTriple(selectorRes, NS.DC.EXTENT, g.literal(actuallyRead));
		g.addTriple(selectorRes, NS.OA.END, g.literal(actuallyRead));
		g.addTriple(bodyRes, NS.FFF.PROP_BYTE_REGION, selectorRes);
		g.addTriple(dataObjectRes, NS.FFF.PROP_CHECKSUM, bodyRes);

		//			
		//			g.addTriple(specificRes, NS.RDF.TYPE, g.resource(NS.OA.SPECIFIC_RESOURCE));
		//			g.addTriple(specificRes, NS.OA.HAS_SOURCE, dataObjectRes);
		//			g.addTriple(specificRes, NS.OA.HAS_SELECTOR, selectorRes);
		//			
		//			g.addTriple(annoRes, NS.OA.HAS_TARGET, targetRes);
		//			g.addTriple(annoRes, NS.OA.HAS_BODY, bodyRes);

	}

	@Override
	public void analyzeFile(Grafeo g, GResource dataObjectRes, File file) throws IOException {
		
		try {
			g.addTriple(dataObjectRes, NS.DC.EXTENT, g.literal(file.length()));
			analyzeInputStream(g, dataObjectRes, new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw e;
		}
		
	}
}
