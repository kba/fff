package kba.fff.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import kba.fff.fff_common.NS;
import kba.fff.ws.service.AnnotationService;
import kba.fff.ws.service.BlobService;
import kba.fff.ws.service.DataInstanceService;
import kba.fff.ws.service.DataObjectService;
import kba.fff.ws.service.ObservationService;
import kba.fff.ws.service.OntologyService;
import eu.dm2e.grafeo.Grafeo;
import eu.dm2e.grafeo.jaxrs.GrafeoMessageBodyWriter;
import eu.dm2e.grafeo.jena.GrafeoImpl;

public class FffApplication extends Application {
	
//	public FffApplication() {
//		packages("kba.fff.ws.service");
//	}
	
	static {
		GrafeoImpl.addStaticNamespace("fff", NS.FFF.BASE);
		GrafeoImpl.addStaticNamespace("oa", NS.OA.BASE);
		GrafeoImpl.addStaticNamespace("cnt", NS.CNT.BASE);
	}
	
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(OntologyService.class);
        s.add(DataObjectService.class);
        s.add(DataInstanceService.class);
        s.add(ObservationService.class);
        s.add(AnnotationService.class);
        s.add(BlobService.class);

        s.add(MultiPartFeature.class);
        s.add(GrafeoMessageBodyWriter.class);
        return s;
    }
}
