package kba.fff.model;

import java.net.URI;

import kba.fff.fff_common.NS;
import eu.dm2e.grafeo.annotations.RDFClass;
import eu.dm2e.grafeo.annotations.RDFProperty;
import eu.dm2e.grafeo.gom.SerializablePojo;


@RDFClass(NS.FFF.CLASS_DATA_LOCATION)
public class DataLocation extends SerializablePojo<DataLocation>{
	
	@RDFProperty(NS.FFF.PROP_FILE_PATH)
	private String filePath;
	public String getFilePath() { return filePath; }
	public void setFilePath(String path) { this.filePath = path; }

	@RDFProperty(NS.FFF.PROP_HOSTNAME)
	private String hostname;
	public String getHostname() { return hostname; }
	public void setHostname(String hostname) { this.hostname = hostname; }

	@RDFProperty(NS.FFF.PROP_URI)
	private URI uri;
	public URI getUri() { return uri; }
	public void setUri(URI uri) { this.uri = uri; }
}
