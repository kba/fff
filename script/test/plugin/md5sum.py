import sys

from test.FffUnitTest import FffUnitTest, DUMP
from fff_client.plugin.analyzer.md5sum import FffPlugin
from rdflib import *


class md5sumTest(FffUnitTest):

    instance = None

    def setUp(self):
        self.instance = FffPlugin(self.client, {
            "buffer_size": 1024 * 1024
        })

    def test(self):
        g = Graph()
        do = BNode()
        self.instance.analyze_dataobject(g, do, '../src/test/resources/image/screenshot.png')
        sum_should = "dc8f7cccdb4ba15ea08ed5f0070b3d3e"
        DUMP(g)
