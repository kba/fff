package kba.fff.client.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import eu.dm2e.grafeo.GResource;
import eu.dm2e.grafeo.Grafeo;

public interface IAnalyzerPlugin {

	/**
	 * @param g
	 * @param dataObjectRes
	 * @param file
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	void analyzeFile(Grafeo grafeo, GResource dataObjectRes, File file) throws IOException;

	/**
	 * @param g
	 * @param dataObjectRes
	 * @param file
	 * @throws IOException 
	 */
	void analyzeInputStream(Grafeo g, GResource dataObjectRes, InputStream file) throws IOException;
}
