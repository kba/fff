"""FUCK."""
from fff_client.plugin.analyzer.BaseAnalyzer import BaseAnalyzer
import hashlib
import io
from rdflib import BNode, Literal
from namespaces import FFF, RDF


class FffPlugin(BaseAnalyzer):

    """ Create checksums of objects. """

    def analyze_dataobject(self, graph, dataobject_id, filename):
        """ Analyze file, return checksum. """

        m = hashlib.md5()
        instream = io.open(filename, mode="rb")
        buf = instream.read(self.config['buffer_size'])
        m.update(buf)
        digest = m.hexdigest()

        checksumRes = BNode()
        byteRegionRes = BNode()

        graph.add((checksumRes, FFF.checksumValue, Literal(digest)))
        graph.add((checksumRes, FFF.checksumAlgorithm, Literal("md5")))
        graph.add((byteRegionRes, RDF.type, FFF.ByteRegion))
        graph.add((byteRegionRes, FFF.offset, Literal(0)))
        graph.add((byteRegionRes, FFF.length,
                   Literal(self.config['buffer_size'])))
        graph.add((checksumRes, FFF.byteRegion, byteRegionRes))
        graph.add((dataobject_id, FFF.checksum, checksumRes))
        return digest
