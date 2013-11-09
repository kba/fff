package kba.fff.model.dataobjectmixin;

import kba.fff.fff_common.NS;
import kba.fff.model.DataObject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.dm2e.grafeo.gom.SerializablePojo;
import eu.dm2e.grafeo.jena.GResourceImpl;
import eu.dm2e.grafeo.jena.GrafeoImpl;
import eu.dm2e.grafeo.jena.SparqlSelect;



public class AudioMixinTest {
	
	Logger log = LoggerFactory.getLogger("AudioMixinTest");
	
	@Test
	public void testMixin() {
		DataObject dObj = new DataObject();
		AudioMixin aMix = new AudioMixin();
		aMix.setChromaprint("123");
//		dObj.getDataObjectMixins().add(aMix);
		log.debug(dObj.getTurtle());
	}
	
	@Test 
	public void deserializeMixin() throws ClassNotFoundException {
		String nt ="@prefix ogp:   <http://ogp.me/ns#> . "
+"@prefix edm:   <http://www.europeana.eu/schemas/edm/> ."
+"@prefix geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#> ."
+"@prefix foaf:  <http://xmlns.com/foaf/0.1/> ."
+"@prefix oo:    <http://purl.org/openorg/> ."
+"@prefix dm2e:  <http://onto.dm2e.eu/omnom/> ."
+"@prefix omnom_types: <http://onto.dm2e.eu/omnom-types/> ."
+"@prefix void:  <http://rdfs.org/ns/void#> ."
+"@prefix ore:   <http://www.openarchives.org/ore/terms/> ."
+"@prefix omnom: <http://onto.dm2e.eu/omnom/> ."
+"@prefix dcterms: <http://purl.org/dc/terms/> ."
+"@prefix sioc:  <http://rdfs.org/sioc/ns#> ."
+"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> ."
+"@prefix co:    <http://purl.org/co/> ."
+"@prefix dct:   <http://purl.org/dc/terms/> ."
+"@prefix bibo:  <http://purl.org/ontology/bibo/> ."
+"@prefix owl:   <http://www.w3.org/2002/07/owl#> ."
+"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> ."
+"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."
+"@prefix gr:    <http://purl.org/goodrelations/v1#> ."
+"@prefix skos:  <http://www.w3.org/2004/02/skos/core#> ."
+"@prefix cc:    <http://creativecommons.org/ns#> ."
				+ "[ a       <http://onto.fff.kba/DataObject> ;"
  + "<http://onto.fff.kba/java/dataObjectMixin>"
          +"[ a       <http://onto.fff.kba/AudioMixin> ;"
            +"<http://onto.fff.kba/chromaprint>"
                    +"\"123\"^^xsd:string ;"
            +"           <http://onto.fff.kba/java/javaClassName>"
                    +"\"kba.fff.model.dataobjectmixin.AudioMixin\"^^xsd:string"
          +"]"
+"] .";
		GrafeoImpl g = new GrafeoImpl();
		g.readHeuristically(nt);
		log.debug(g.getTerseTurtle());
		GResourceImpl gres = g.findTopBlank(NS.FFF.CLASS_DATA_OBJECT);
		DataObject dObj = g.getObjectMapper().getObject(DataObject.class, gres);
		ResultSet resSet = new SparqlSelect.Builder()
			.prefix("fff", NS.FFF.BASE)
			.prefix("fff_java", NS.FFF_JAVA.BASE)
			.select("?mixin ?clazz")
			.where("?do <" + NS.FFF_JAVA.PROP_DATAOBJECT_MIXIN + "> ?mixin .\n"
                +  "?mixin <" + NS.FFF_JAVA.PROP_JAVA_CLASS_NAME +"> ?clazz.")
            .grafeo(g)
            .build()
            .execute();
		log.debug(""+resSet.hasNext());
		while (resSet.hasNext()) {
			QuerySolution sol = resSet.next();
			final Class<?> clazz = Class.forName(((Literal)sol.get("clazz")).getString());
			final GResourceImpl mixinRes = new GResourceImpl(g, (Resource) sol.get("mixin"));
			log.debug("Class is: " + clazz.getName());
			log.debug("Mixin Resource is: " + mixinRes);
			AudioMixin mixinObj = (AudioMixin) g.getObjectMapper().getObject(clazz, mixinRes);
			log.debug(mixinObj.getTerseTurtle());
		}
//		dObj.setDataObjectMixins(new HashSet<DataObjectMixin>());
//		for (GStatement mixinStmt : g.listStatements(gres, NS.FFF_JAVA.PROP_DATAOBJECT_MIXIN, null)) {
//			GResource mixinRes = (GResource) mixinStmt.getObject();
//			mixinJavaClassName = g.listStatements(mixinRes, NS.FFF_JAVA.PROP_JAVA_CLASS_NAME, null);
//			AudioMixin mixinObj = g.getObjectMapper().getObject(Class.forName(javaClassName), mixinRes);
//			log.debug(mixinObj.getChromaprint());
//		}

	}


}
