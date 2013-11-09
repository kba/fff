""" Namespaces. """
from rdflib import URIRef, Graph
import rdflib.namespace
from rdflib.namespace import Namespace, NamespaceManager


RDF = rdflib.Namespace('http://www.w3.org/1999/02/22-rdf-syntax-ns#')
FFF = rdflib.Namespace("http://onto.fff/fff/")
MOAT = rdflib.Namespace('http://TODO/moat/')
XSD = rdflib.Namespace('http://www.w3.org/2001/XMLSchema#')
CNT = rdflib.Namespace('http://www.w3.org/2011/content#')
OA = rdflib.Namespace('http://www.w3.org/ns/oa#')
DC = rdflib.Namespace('http://purl.org/dc/elements/1.1/')
DCT = rdflib.Namespace('http://purl.org/dc/terms/')
DCTERMS = DCT

ns = dict(
    rdf=RDF,
    fff=FFF,
    moat=MOAT,
    xsd=XSD,
    cnt=CNT,
    oa=OA,
    dc=DC,
    dct=DCT,
    dcterms=DCTERMS,
)
nsm = NamespaceManager(rdflib.Graph())
for prefix, base in ns.iteritems():
    # print prefix
    baseUri = URIRef(base)
    # print baseUri
    # print prefix + " : " + baseUri
    nsm.bind(prefix, baseUri)


def expandNS(qname):
    try:
        prefix, resource = qname.split(':')
    except ValueError:
        print "Invalid qname: " + qname
        return
    if prefix in ns.keys():
        return ns[prefix] + resource
    return
