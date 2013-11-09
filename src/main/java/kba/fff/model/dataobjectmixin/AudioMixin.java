package kba.fff.model.dataobjectmixin;

import kba.fff.fff_common.NS;
import kba.fff.model.DataObjectMixin;
import eu.dm2e.grafeo.annotations.RDFClass;
import eu.dm2e.grafeo.annotations.RDFProperty;
import eu.dm2e.grafeo.gom.SerializablePojo;

@RDFClass(NS.FFF_JAVA.CLASS_AUDIO_MIXIN)
public class AudioMixin extends SerializablePojo<AudioMixin>{
	
	@RDFProperty(NS.FFF_JAVA.PROP_JAVA_CLASS_NAME)
	public String _className = AudioMixin.class.getName();
	public String get_className() { return _className; }
	
	@RDFProperty(NS.FFF.PROP_CHROMAPRINT)
	private String chromaprint;
	public String getChromaprint() { return chromaprint; }
	public void setChromaprint(String chromaprint) { this.chromaprint = chromaprint; }

}
