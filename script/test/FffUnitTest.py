import fff_client
import rdflib

def DUMP(thing):
    as_str = ""
    if isinstance(thing, rdflib.Graph):
        as_str = thing.serialize(format='turtle')
    else:
        as_str = "{0}".format(thing)
    print as_str

class FffUnitTest(object):

    client = fff_client.FFFClient({})


