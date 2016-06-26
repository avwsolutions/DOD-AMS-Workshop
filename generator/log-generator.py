#!/usr/bin/python

# Name   :      Arnold van Wijnbergen or short AvW
# Company:      Devoteam
# Description:  Log generator for Logstash simulation

import signal
import sys
import time
import random
import logging
from datetime import datetime
logger = logging.getLogger('log-generator')

def signal_handler(signal, frame):
	print("You pressed CTRL-C, we will close the thread")
	sys.exit(0)

def setlog (loglevel):
	logger.setLevel(logging.DEBUG)
	cr = logging.StreamHandler()
	cr.setLevel(logging.DEBUG)
	formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
	cr.setFormatter(formatter)
	logger.addHandler(cr)
	return logger

def log (msg):
	if debug:
		logger.debug(msg)
	else:
		logger.debug(msg)

def dropEntry (bag,type):
        with open(bag, 'a') as venco:
                log("Open the bag of venco and drop the " + type + " IT")
		if type == 'catalina':
                	msgs = ["SEVERE [localhost-startStop-2] org.apache.catalina.loader.WebappClassLoader.checkThreadLocalMapForLeaks The web application has started with leaks","SEVERE [localhost-startStop-2] org.apache.catalina.loader.WebappClassLoader.checkThreadLocalMapForLeaks The web application has shutdown with leaks","SEVERE [localhost-CoreDump] org.apache.catalina.SeriousBreakDown Java Exception occurred\n java.lang.RuntimeException (message=null, stacktrace A) caused by\n java.io.IOException (message=\"Problems with io\", stacktrace B) caused by\n java.lang.IllegalStateException (message=\"Some error text\", stacktrace C), cause=null"]
                	message = random.choice(msgs)
                	timestamp = datetime.now().strftime('%d-%b-%Y %H:%M:%S.%f')[:-3]
			entry = timestamp + ' ' + message
		elif type == 'application':
			msgs = ["SEVERE [NAWModule] com.openbank.bankit.NAWModule.Update John Doe record updated with Flevostraat 100, Purmerend","INFO [HouseKeepingModule] com.openbank.bankit.HouseKeeping.Export Export started for main customer info", "WARNING [RegistrationModule] com.openbank.bankit.Registration.Account Customer registrated, but no valid account record found"]
                        message = random.choice(msgs)
                        timestamp = datetime.now().strftime('%d-%b-%Y %H:%M:%S.%f')[:-3]
			entry = timestamp + ' ' + message
		elif type == 'keyvalue':
			module = str('claims')
                	timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]
                	entry = 'key=' + str(timestamp) + '|value=' + str(int(random.uniform(1,100))) + '|module=' + module
		else:
			log("No valid drop type specified")
                log("Entry dropped to " + str(bag) + " as entry " + entry)
                venco.write(entry+'\n')
        venco.closed
        return entry

def dorySleep ():
	period = float(random.uniform(1,60))
	log("Dory will sleep again ..... ;-) for " + str(period) + " seconds")
	time.sleep(period)


# Main script
	
shoot = True
debug = False 
 
signal.signal(signal.SIGINT, signal_handler)

if debug:
	setlog("DEBUG")

if __name__ == '__main__':
	while shoot:
		dropEntry('/var/log/tomcat/catalina.log','catalina')
		dropEntry('/var/log/tomcat/BankIT-application.log','application')
		dropEntry('/var/log/tomcat/BankIT-performance.log','keyvalue')
		dorySleep()
