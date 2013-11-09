===========================
HTTP Protocol
===========================

This section describes the HTTP API that forms the base of
FFF's system

Creating an Observation
-----------------------

Create the observation metadata::

    POST /observation/meta
    Headers:
        Accept: [RDF/JSON serialization]
        Content-Type: [Content of the body -- RDF/JSON]
        User-Agent: [URN of the User Agent]                     [[1]]
        From: [URN of the User]                                 [[2]]
    Body:
        [RDF description of the observation]

Returns on Success::

    201 CREATED
    Headers:
        Location: [URN of the observation]                      [[3]]
        X-FFF-Finalize-Token: [Random Unique String]            [[4]]
        Expires: [Date until the observation can be finalized]
    Body:
        [RDF description of the content]

Possibly encrypt the Finalize Token with the user's public key, then
react to the decoded version later on. Or require a signature.

Returns on Invalid RDF::
    
    400
    Body: INVALID RDF [+ maybe additional causes]

Returns on Bad Auth::
    
    403 FORBIDDEN

Create the observation::

    PUT /observation?urn=[[3]]
    Headers:
        Accept: [RDF/JSON serialization]
        Content-Type: [Content of the body -- RDF/JSON]
        X-FFF-Finalize-Token: [[4]]
        X-FFF-Finalize-Token-Signature: 
            [Signature of the finalize token using user's PGP]
    Body:
        [Body of the observation]

Returns on sucess::

    201 CREATED


Creating a DataInstance
-----------------------

TODO

Creating a DataObject
---------------------

TODO


