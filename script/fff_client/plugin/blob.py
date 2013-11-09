"""FUCK."""
from fff_client.plugin.BasePlugin import BasePlugin
from rdflib import Graph, Literal, BNode, URIRef
from namespaces import FFF, MOAT, XSD, OA, CNT, RDF, ns
from rdflib.plugins.sparql import prepareQuery
import requests
import os.path


class FffPlugin(BasePlugin):

    """ Tag Dataobjects. """

    def store_file(self, filename):
        f = open(filename, 'rb')
        r = requests.post(self.client.target('blob'), files=dict(blob=f))
        return r

