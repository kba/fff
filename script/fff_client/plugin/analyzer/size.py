"""FUCK."""
from fff_client.plugin.analyzer.BaseAnalyzer import BaseAnalyzer
import os
from rdflib import Literal
from namespaces import FFF


class FffPlugin(BaseAnalyzer):

    """ Analyze file size. """

    def analyze_dataobject(self, graph, dataobject_id, filename):
        """ Analyze file size. """
        statinfo = os.stat(filename)
        graph.add((dataobject_id, FFF.length, Literal(statinfo.st_size)))
