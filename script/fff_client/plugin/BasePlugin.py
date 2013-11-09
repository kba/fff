"""Base Class of all plugins"""

from abc import ABCMeta, abstractmethod


class BasePlugin(object):

    """ Is Base of all plugins. """

    __metaclass__ = ABCMeta

    config = {}
    client = None

    def __init__(self, client, config={}):
        self.config = config
        self.client = client

