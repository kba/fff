package kba.fff.model;

import eu.dm2e.grafeo.gom.SerializablePojo;

//@RDFClass(NS.FFF.CLASS_BYTE_REGION)
public class ByteRegion extends SerializablePojo<ByteRegion> {
	
//	@RDFProperty(NS.FFF.PROP_DATA_OBJECT)
	private DataObject dataObject;
	public DataObject getDataObject() { return dataObject; }
	public void setDataObject(DataObject dataObject) { this.dataObject = dataObject; }
	
//	@RDFProperty(NS.FFF.PROP_OFFSET)
	private long offset;
	public long getOffset() { return offset; }
	public void setOffset(long offset) { this.offset = offset; }

//	@RDFProperty(NS.FFF.PROP_LENGTH)
	private long length;
	public long getLength() { return length; }
	public void setLength(long length) { this.length = length; }
	
}
