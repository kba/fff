import test.plugin.md5sum

if __name__ == '__main__':

    testClasses = []
    testClasses.append(test.plugin.md5sum.md5sumTest())

    for testClass in testClasses:
        testClass.setUp()
        testClass.test()
