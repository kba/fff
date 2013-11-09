
"""FUCK."""
from fff_client.plugin.analyzer.BaseAnalyzer import BaseAnalyzer
import os
import socket
from rdflib import Literal, BNode
from namespaces import FFF, RDF


class FffPlugin(BaseAnalyzer):

    """ Analyze file location. """

    def analyze_datainstance(self, graph, datainstance_res, filename):
        """ Analyze file location. """
        location_res = BNode()
        graph.add((location_res, RDF.type, FFF.DataLocation))
        graph.add((location_res, RDF.type, FFF.FileLocation))
        graph.add((location_res, FFF.filePath,
                   Literal(os.path.abspath(filename))))
        graph.add((location_res, FFF.hostname, Literal(socket.gethostname())))
        graph.add((location_res, FFF.uri, Literal("file://" +
                                                  socket.gethostname() +
                                                  os.path.abspath(filename))))
        graph.add((datainstance_res, FFF.dataLocation, location_res))
