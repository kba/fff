""" FFF Client. """


from rdflib import URIRef, BNode, Graph
from namespaces import RDF, FFF
import requests
import importlib

################
# import plugins
#
# import plugin.analyzer.md5sum import ChecksumAnalyzer
# import plugin.analyzer.size import SizeAnalyzer
# import plugin.analyzer.mimetype import MimetypeAnalyzer
# import plugin.analyzer.file_location import FileLocationAnalyzer
# import plugin.tag

default_config = dict(
    fff_api="http://localhost:8080/rest/"
)


class FFFClient(object):

    """ Client class for FFF. """

    """ Plugins to use """
    plugins = {}
    cache = {}

    def __init__(self, plugin_dict, config=default_config):
        self.config = config
        for plugin_name, plugin_config in plugin_dict.iteritems():
            plugin_module = None
            try:
                plugin_module = importlib.import_module("fff_client.plugin." + plugin_name)
            except:
                plugin_module = importlib.import_module("fff_client.plugin.analyzer." + plugin_name)
            self.plugins[plugin_name] = plugin_module.FffPlugin(self, plugin_config)

    def analyze_object_minimal(self, filename):
        """ Do Analyzes a file for checksum, size. Return foo."""

        # create graph and resources
        g = Graph()
        g.bind('fff', FFF)
        object_res = BNode()
        g.add((object_res, RDF.type, FFF.DataObject))

        ###################################
        # calculate md5 sum of first 1MB
        #
        self.plugins['md5sum'].analyze_dataobject(g, object_res, filename)

        #####################
        # calculate size
        #
        self.plugins['size'].analyze_dataobject(g, object_res, filename)

        ########################
        # calculate media type
        #
        self.plugins['mimetype'].analyze_dataobject(g, object_res, filename)

        return g

    def analyze_instance(self, filename, object_id):
        """Analyze a file for location. Return bar."""

        g = Graph()
        instance_res = BNode()
        g.add((instance_res, RDF.type, FFF.DataInstance))
        g.add((instance_res, FFF.dataObject, URIRef(object_id)))

        self.plugins['file_location'].analyze_datainstance(g,
                                                           instance_res,
                                                           filename)
        return g

    def find_dataobject_for_filename(self, filename):
        """ Find or create data object for filename, return URN. """
        try:
            object_id = self.cache[filename]['dataobject']
            return object_id
        except(KeyError):
            g = self.analyze_object_minimal(filename)
            r = self.post_graph("dataobject", g)
            object_id = r.headers.get('location')
            try:
                self.cache[filename]
            except(KeyError):
                self.cache[filename] = {}
            self.cache[filename]['dataobject'] = object_id
            # cache[filename]['observation_id'] = observation_id
            return object_id
        # print(observation_id)
        # return g

    def create_observation(self):
        """ Create and return an observation. """
        target = "{0}{1}".format(self.config['fff_api'], 'observation/meta')
        r = requests.post(target, headers={
            'accept': 'text/turtle'
        })
        print r.headers
        return r.headers.get('location')

    def post_graph(self, resource, graph):
        """ POST graph to resource. """
        target = "{0}{1}".format(self.config['fff_api'], resource)
        # print target
        # print resource
        # print graph.serialize(format='turtle')
        r = requests.post(target,
                          data=graph.serialize(format='turtle'),
                          headers={
                              'content-type': 'text/turtle',
                              'accept': 'text/turtle'
                          })
        print r.content
        # print r
        return r

    def put_graph(self, target, graph):
        """ PUT graph to resource. """
        r = request.post(self.config['fff_api'] + target,
                         data=g.serialize(format='turtle'),
                         headers={
                             'content-type': 'text/turtle',
                             'accept': 'text/turtle'
                         })
        return r

    def publish_datainstance(self, dataobject, filename):
        """Publish a file as a DataInstance, return it."""
        dins_graph = self.analyze_instance(filename, dataobject)
        r = self.post_graph('datainstance', dins_graph)
        return r.headers['location']

    def publish_file(self, filename):
        """Publish a file as a DataInstance, return it."""
        dobj = self.find_dataobject_for_filename(filename)
        dins_graph = self.analyze_instance(filename, dobj)
        r = self.post_graph('datainstance', dins_graph)
        print r
        return r.headers['location']

    def target(self, resource):
        return "{0}{1}".format(self.config['fff_api'], resource)

    def get_graph_about_urn(self, urn, g):
        """Retrieve all data about a URN and return a graph."""
        url = self.target('observation/data-about?urn=' + urn)
        print url
        g.parse(url)
        return g
        # r = requests.get(
                         # headers={
                             # 'accept': 'text/turtle'
                         # })


# if __name__ == '__main__':
#     filename = sys.argv[1] or 'bla.py'

#     # Publish object
#     object_graph = analyze_object_shallow(filename)
#     r1 = requests.post(self.config["fff-api"] + "dataobject",
#             data=object_graph.serialize(format='turtle'),
#             headers={
#                 'content-type': 'text/turtle',
#                 'accept': 'text/turtle'
#                 })
#     observation_id = r1.headers.get('x-fff-observation')
#     object_id = r1.headers.get('location')
#     print(observation_id)
#     print(object_id)
#     if (not object_id):
#         print(object_graph.serialize(format="turtle"))
#         print(r1)
#         print(r1.content)
#         raise BaseException("Shit")

#     instance_graph = analyze_instance(filename, object_id)
#     r2 = requests.post(self.config["fff-api"] + "datainstance",
#             data=instance_graph.serialize(format='turtle'),
#             params={
#                 'observation': observation_id
#             },
#             headers={
#                 'content-type': 'text/turtle',
#                 'accept': 'text/turtle'
#             })
#     instance_id = r2.headers.get('location')
#     print(instance_id)
#     print(object_graph.serialize(format='turtle'))
#     print(instance_graph.serialize(format='turtle'))

#     r3 = requests.get(self.config["fff-api"] + "observation/data-about",
#             headers={
#                 'accept': 'text/turtle'
#             },
#             params={
#                 'urn' : object_id
#             },)
#     print r3
#     print r3.content
