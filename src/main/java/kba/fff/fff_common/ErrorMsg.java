package kba.fff.fff_common;

public enum ErrorMsg {
	
	  MISSING_TOP_NODE("Top node is missing.")
	, QUERY_PARAMETER_INVALID("Missing/invalid query Parameter.")
	, INVALID_RDF_SERIALIZATION("RDF serialization cannot be parsed.")
	, INVALID_RDF("RDF is invalid (but well-formed).")
	;
	
	private String msg;
	public String getMsg() { return msg; }
	
	private ErrorMsg(String msg) {
		this.msg = msg;
	}

}
