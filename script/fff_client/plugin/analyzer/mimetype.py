"""FUCK."""
from fff_client.plugin.analyzer.BaseAnalyzer import BaseAnalyzer
import magic
from rdflib import Literal
from namespaces import FFF


class FffPlugin(BaseAnalyzer):

    """ Create mimetype of objects. """

    def analyze_dataobject(self, graph, dataobject_id, filename):
        """ Analyze file, return mimetype. """
        mimetype = magic.from_file(filename, mime=True)
        graph.add((dataobject_id, FFF.mimeType, Literal(mimetype)))
