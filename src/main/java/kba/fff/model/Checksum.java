package kba.fff.model;

import kba.fff.fff_common.NS;
import eu.dm2e.grafeo.annotations.RDFProperty;
import eu.dm2e.grafeo.gom.SerializablePojo;

//@RDFClass(NS.FFF.CLASS_CHECKSUM)
public class Checksum extends SerializablePojo<Checksum>{
	
	@RDFProperty(NS.FFF.PROP_CHECKSUM_ALGORITHM)
	private String checkusmAlgorithm;
	public String getCheckusmAlgorithm() { return checkusmAlgorithm; }
	public void setCheckusmAlgorithm(String checkusmAlgorithm) { this.checkusmAlgorithm = checkusmAlgorithm; }
	
//	@RDFProperty(NS.FFF.PROP_BYTE_REGION)
	private ByteRegion byteRegion;
	public ByteRegion getByteRegion() { return byteRegion; }
	public void setByteRegion(ByteRegion byteRegion) { this.byteRegion = byteRegion; }
	
	@RDFProperty(NS.CNT.CHARS)
	private String checksumValue;
	public String getChecksumValue() { return checksumValue; }
	public void setChecksumValue(String checksumValue) { this.checksumValue = checksumValue; }
	

}
