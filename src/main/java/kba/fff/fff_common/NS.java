package kba.fff.fff_common;


/**
 * Namespaces and URIs
 * @author Konstantin Baierer
 *
 */
public class NS {
	
	public static final String A = RDF.TYPE;
	
	/**
	 * Open Annotation
	 * @author Konstantin Baierer
	 *
	 */
	public static class OA {
		public static final String BASE = "http://www.w3.org/ns/oa#";
		public static final String ANNOTATION   = BASE + "Annotation";
		public static final String SPECIFIC_RESOURCE   = BASE + "SpecificResource";
		public static final String TAG   		= BASE + "Tag";
		public static final String SEMANTIC_TAG = BASE + "SemanticTag";
		public static final String HAS_BODY     = BASE + "hasBody";
		public static final String MOTIVATED_BY = BASE + "motivatedBy";
		public static final String HAS_TARGET   = BASE + "hasTarget";
		public static final String HAS_SOURCE   = BASE + "hasSource";
		public static final String HAS_SELECTOR = BASE + "hasSelector";
		public static final String	START	= BASE + "start";
		public static final String	END	= BASE + "end";
		public static final String	DATA_POSITION_SELECTOR	= BASE + "DataPositionSelector";
		public static final String	IDENTIFYING	= BASE + "identifying";
	}
	/**
	 * Content in RDF ontology
	 * @author Konstantin Baierer
	 *
	 */
	public static class CNT {
		public static final String BASE = "http://www.w3.org/2011/content#";
		public static final String CONTENT_AS_TEXT = BASE + "ContentAsText";
		public static final String CHARS = BASE + "chars";
	}
	public static class DC {
		public static final String BASE = "http://purl.org/dc/elements/1.1/";
		public static final String CREATED = BASE + "created";
		public static final String MODIFIED = BASE + "modified";
		public static final String	EXTENT	= BASE + "extent";
	}
	public static class FFF_JAVA {
		public static final String BASE = "http://onto.fff.kba/java/";
		public static final String	PROP_DATATYPE	= BASE + "dataType";
		public static final String CLASS_DATAOBJECT_MIXIN = BASE + "DataObjectMixin";
		public static final String	PROP_DATAOBJECT_MIXIN	= BASE + "dataObjectMixin";
		public static final String CLASS_AUDIO_MIXIN = BASE + "AudioMixin";
		public static final String PROP_JAVA_CLASS_NAME = BASE + "javaClassName";
	}
	
	public static class FFF {
		public static final String BASE = "http://onto.fff/fff/";
		public static final String CLASS_DATA_INSTANCE = BASE + "DataInstance";
		public static final String CLASS_DATA_OBJECT = BASE + "DataObject";
		public static final String CLASS_OBSERVATION = BASE + "Observation";
		public static final String PROP_OFFSET	= BASE + "offset";
		public static final String PROP_LENGTH = BASE + "length";
		public static final String PROP_CHECKSUM_ALGORITHM = BASE + "checksumAlgorithm";
		public static final String PROP_BYTE_REGION	= BASE + "byteRegion";
		public static final String PROP_CHECKSUM	= BASE + "checksum";
		public static final String	PROP_DATA_OBJECT	= BASE + "dataObject";
		public static final String	PROP_CHECKSUM_VALUE	= BASE + "checksumValue";
		public static final String CLASS_CHECKSUM = BASE + "Checksum";
		public static final String CLASS_BYTE_REGION = BASE + "ByteRegion";
		public static final String	PROP_CHROMAPRINT	= BASE + "chromaprint";
		public static final String	PROP_FILE_PATH	= BASE + "filePath";
		public static final String	CLASS_DATA_LOCATION	= BASE + "DataLocation";
		public static final String	PROP_DATA_LOCATION	= BASE + "dataLocation";
		public static final String	PROP_HOSTNAME	= BASE + "hostname";
		public static final String	PROP_URI	= BASE + "uri";
		public static final String	CLASS_HTTP_LOCATION	= BASE + "HttpLocation";
	}

	public static class RDF {
		public static final String BASE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		public static final String TYPE = BASE + "type";
	}
}
