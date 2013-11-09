"""FUCK."""
from fff_client.plugin.BasePlugin import BasePlugin
from rdflib import Graph, Literal, BNode, URIRef
from namespaces import FFF, MOAT, XSD, OA, CNT, RDF, ns
from rdflib.plugins.sparql import prepareQuery
import requests
import os.path


class FffPlugin(BasePlugin):

    """ Tag Dataobjects. """

    def add_tag_to_dataobject(self, dataobject_res, tags):
        """ Add tag to dataobject, return observation. """
        if not dataobject_res:
            raise BaseException("No DataObject passed")
        anno_list = []
        for tag in tags:
            graph = Graph()
            anno_res = BNode()
            target_res = URIRef(dataobject_res)
            graph.add((anno_res, RDF.type, OA.Annotation))
            graph.add((anno_res, OA.hasTarget, target_res))
            graph.add((anno_res, OA.hasTarget, target_res))
            graph.add((anno_res, OA.motivatedBy, OA.tagging))
            body_res = BNode()
            graph.add((anno_res, OA.hasBody, body_res))
            graph.add((body_res, RDF.type, OA.Tag))
            graph.add((body_res, RDF.type, CNT.ContentAsText))
            graph.add((body_res, CNT.chars, Literal(tag, datatype=XSD.string)))

            r = self.client.post_graph('annotation', graph)
            anno_list.append(r.headers['location'])
        return anno_list

    def add_tag_to_dataobject(self, dataobject_res, tags):
        """ Add tag to dataobject, return observation. """
        if not dataobject_res:
            raise BaseException("No DataObject passed")
        anno_list = []
        for tag in tags:
            graph = Graph()
            anno_res = BNode()
            target_res = URIRef(dataobject_res)
            graph.add((anno_res, RDF.type, OA.Annotation))
            graph.add((anno_res, OA.hasTarget, target_res))
            graph.add((anno_res, OA.hasTarget, target_res))
            graph.add((anno_res, OA.motivatedBy, OA.tagging))
            body_res = BNode()
            graph.add((anno_res, OA.hasBody, body_res))
            graph.add((body_res, RDF.type, OA.Tag))
            graph.add((body_res, RDF.type, CNT.ContentAsText))
            graph.add((body_res, CNT.chars, Literal(tag, datatype=XSD.string)))

            r = self.client.post_graph('annotation', graph)
            anno_list.append(r.headers['location'])
        return anno_list

    def find_datainstances_for_tags(self, tags):
        """Find DataInstances for tag"""
        first = True
        dobj_set = set()
        for tag in tags:
            r = requests.get(self.client.target('annotation/findDataObjectsByTag?tag=' + tag))
            if first:
                dobj_set = set(r.json())
                first = False
            else:
                dobj_set = dobj_set & set(r.json())
        print dobj_set

        dinst_set = set()
        for dobj in dobj_set:
            r = requests.get(self.client.target('datainstance/findByDataObject?urn=' + dobj))
            dinst_set = dinst_set | set(r.json())

        return dinst_set

    def find_filenames_for_tags(self, tags, flag_only_existing=False):
        dinst_set = self.find_datainstances_for_tags(tags)
        print(dinst_set)
        g = Graph()
        for dinst in dinst_set:
            g.parse(self.client.target('observation/data-about?urn=' + dinst))
        filepaths = {}
        for dinst in dinst_set:
            queryDataInstance = prepareQuery("""
                SELECT DISTINCT ?filepath ?dataobject
                WHERE {
                    ?dataInstance fff:dataObject ?dataobject .
                    ?dataInstance fff:dataLocation ?dataLocation .
                    ?dataLocation fff:filePath ?filepath .
                }""", initNs=ns)
            queryDataObject = prepareQuery(
                """
                SELECT ?tag
                WHERE {
                    ?anno oa:hasTarget ?dataobject .
                    ?anno oa:hasBody ?body .
                    ?body cnt:chars ?tag .
                }""", initNs=ns)

            queryDataInstance
            dinst_uri = URIRef(dinst)
            filepath = ""
            tags = set()
            for row in g.query(queryDataInstance, initBindings=dict(dataInstance=dinst_uri)):
                try:
                    row['filepath']
                    fp = row['filepath'].value
                    filepath = fp
                    print fp
                except KeyError:
                    pass
                try:
                    dataobject = "" + row['dataobject']
                    g.parse(self.client.target("observation/data-about?urn=" + dataobject))
                    # print len(g)
                    for row2 in g.query(queryDataObject, initBindings=dict(dataobject=dataobject)):
                        try:
                            row2['tag']
                            tag = row2['tag'].value
                            tags.add(tag)
                        except KeyError:
                            pass
                except KeyError:
                    pass
        # if (flag_only_existing and os.path.exists(filepath)):
        #     filepaths[filepath] = tags
        # else:
            filepaths[filepath] = tags

        return filepaths

