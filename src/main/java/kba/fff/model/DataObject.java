package kba.fff.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kba.fff.fff_common.NS;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import eu.dm2e.grafeo.annotations.RDFClass;
import eu.dm2e.grafeo.annotations.RDFProperty;
import eu.dm2e.grafeo.gom.SerializablePojo;
import eu.dm2e.grafeo.jena.GrafeoImpl;
import eu.dm2e.grafeo.jena.SparqlSelect;

@RDFClass(NS.FFF.CLASS_DATA_OBJECT)
public class DataObject extends SerializablePojo<DataObject>{
	
	static Logger log = LoggerFactory.getLogger(DataObject.class.getName());
	
//	@RDFProperty(NS.FFF_JAVA.PROP_DATAOBJECT_MIXIN)
//	private Set<DataObjectMixin> dataObjectMixins = new HashSet<>();
//	public Set<DataObjectMixin> getDataObjectMixins() { return dataObjectMixins; }
//	public void setDataObjectMixins(Set<DataObjectMixin> dataObjectMixins) { this.dataObjectMixins = dataObjectMixins; }
	
	@RDFProperty(NS.FFF.PROP_CHECKSUM)
	private Set<Checksum> checksum;
	public Set<Checksum> getChecksum() { return checksum; }
	public void setChecksum(Set<Checksum> checksum) { this.checksum = checksum; }
	
	@RDFProperty(NS.FFF.PROP_LENGTH)
	private Long size;
	public Long getSize() { return size; }
	public void setSize(Long size) { this.size = size; }
	
	public static List<String> findByChecksum(final String endpoint, String checksum, String algo) {
		GrafeoImpl g = new GrafeoImpl();
		SparqlSelect query = new SparqlSelect.Builder()
			.prefixes(g.getNamespaces())
			.select("?dataobject")
			.where("GRAPH ?g {\n"
				 + "  ?dataobject <" + NS.FFF.PROP_CHECKSUM +"> ?chk.\n"
				 + "  ?chk <" + NS.CNT.CHARS + "> ?value.\n"
				 + "  FILTER (REGEX(STR(?value), \"" + checksum + "\", \"i\"))"
				 + "}\n"
				 )
			.endpoint(endpoint)
			.build();
		log.debug(query.toString());
		ResultSet resultSet = query.execute();
		Set<String> doList = new HashSet<>();
		while (resultSet.hasNext()) {
			QuerySolution sol = resultSet.next();
			doList.add(sol.get("dataobject").toString());
		}
		return new ArrayList<String>(doList);
	}
	public static List<String> findIdenticalDataObjects(String endpoint, DataObject queryObj) {
		Set<String> doList = new HashSet<>();
		log.debug("Number of shit: " + queryObj.getChecksum().size());
		for (Checksum chk : queryObj.getChecksum()) {
			doList.addAll(findByChecksum(endpoint, chk.getChecksumValue(), chk.getCheckusmAlgorithm()));
		}
		return new ArrayList<String>(doList);
	}
}
