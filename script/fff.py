#!/usr/bin/env python

"""
CLI Interface::

    FFF CLI tool.

    Usage:
        fff man
        fff ingest-file <filenames>...
        fff state <filename> <qname> <node>
        fff all-about <urn>
        fff describe-file <filename>
        fff do <filename>
        fff tag [--no-auto-inst] add <filename> <tag>...
        fff tag [--no-auto-inst] add-timed <filename> <timeregion> <tag>...
        fff tag remove <filename> <tag>...
        fff tag find-inst <tag>...
        fff tag find <tag>...
        fff blob store <filename>

    Options:
        -h --help     Show this screen.
        --version     Show version.
        --no-auto-inst   Automatically create DataInstances.
        --speed=<kn>  Speed in knots [default: 10].

"""
from docopt import docopt
from fff_client import FFFClient
from namespaces import expandNS, nsm, ns
import re
from rdflib import Graph, URIRef, Literal
import os.path
# import time


class FFFApp(object):

    fff_client = FFFClient({
        'tag': {},
        'size': {},
        'mimetype': {},
        'file_location': {},
        'blob': {},
        'md5sum': {'buffer_size': 1024 * 1024},
    })

    def run(self, arguments):

        fff_client = self.fff_client

        if (arguments['man']):
            print "RTFM"

        if (arguments['state']):
            qname = arguments['<qname>']
            node_str = arguments['<node>']
            filename = arguments['<filename>']
            pred = expandNS(qname)

            if re.match("^<?(http|urn|file):", node_str):
                node_str = re.sub("^<", "")
                node_str = re.sub("<$", "")
                node = URIRef(node_str)
            else:
                node = Literal(node_str)
            dobj = fff_client.find_dataobject_for_filename(filename)
            g = Graph(namespace_manager=nsm)
            g.add((URIRef(dobj), URIRef(pred), node))
            obs_id = fff_client.create_observation()
            dobj_id = fff_client.post_graph('observation?urn=' + obs_id, g)
            print dobj_id

        elif (arguments['ingest-file']):
            filenames = arguments['<filenames>']
            for filename in filenames:
                if not os.path.isfile(filename):
                    continue
                print(filename + " : " + self.fff_client.publish_file(filename))
        elif (arguments['do']):
            filenames = arguments['<filenames>']
            print fff_client.find_dataobject_for_filename(arguments['<filename>'])
        elif (arguments['all-about']):
            g = Graph(namespace_manager=nsm)
            # for p, k in nsm.namespaces():
                # print k
                # g.bind(p, k)
            # print g.qname(u'http://onto.fff.kba/x')
            fff_client.get_graph_about_urn(arguments['<urn>'], g)
            print g
            print g.serialize(format='turtle')

        elif (arguments['tag']):
            if (arguments['add']):
                dobj = fff_client.find_dataobject_for_filename(arguments['<filename>'])
                if (not dobj):
                    raise BaseException("Could not determine DataObject")
                if (not arguments['--no-auto-inst']):
                    dinst = fff_client.publish_datainstance(dobj, arguments['<filename>'])
                    print "Tagging {0} / {1}".format(dobj, dinst)
                else:
                    print "Tagging {0} ".format(dobj)
                urn = fff_client.plugins['tag'].add_tag_to_dataobject(dobj, arguments['<tag>'])
                print urn
            elif (arguments['remove']):
                print "NOT IMPLEMENTED"
            elif (arguments['find-inst']):
                for inst in fff_client.plugins['tag'].find_datainstances_for_tags(arguments['<tag>']):
                    print inst
            elif (arguments['find']):
                for fp, tags in fff_client.plugins['tag'].find_filenames_for_tags(arguments['<tag>']).iteritems():
                    print fp + " [" + " ".join(tags) + "]"

        elif (arguments['blob']):
            print "fnork"
            if (arguments['store']):
                filename = arguments['<filename>']
                r = fff_client.plugins['blob'].store_file(filename)
                print r.content
                print r.headers
                print r.request.headers


if __name__ == '__main__':
    arguments = docopt(__doc__, version='FFF 0.1.1')

    app = FFFApp()
    app.run(arguments)
