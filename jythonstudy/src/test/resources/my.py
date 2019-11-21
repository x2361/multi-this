from java.lang import System # Java import
from my import JythonHelloWorld #
from org.slf4j import Logger
from org.slf4j import LoggerFactory

application = request.getApp()
LoggerFactory.getLogger("abc").error("from PY.")
start = request.getStart()
print('StartInPY:'+str(System.currentTimeMillis()-start))

print('Running on Java version: ' + System.getProperty('java.version'))
print('Unix time from Java: ' + str(System.currentTimeMillis()))

c = request.read()
print('c:'+c)
request.write(c)

print('EndInPY:'+str(System.currentTimeMillis()-start))
