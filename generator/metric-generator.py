#!/usr/bin/python

# Name 	 : 	Arnold van Wijnbergen or short AvW
# Company: 	Devoteam
# Description:	Metric generator for Graphite simulation 

import signal
import sys
import random
import time
import socket
import logging
logger = logging.getLogger('metric-generator')

def signal_handler(signal, frame):
	print("You pressed CTRL+C, we will close the thread")
	sys.exit(0)

def setlog (loglevel):
	logger.setLevel(logging.DEBUG)
	ch = logging.StreamHandler()
	ch.setLevel(logging.DEBUG)
	formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
	ch.setFormatter(formatter)
	logger.addHandler(ch)
	return logger

def execmetric (minValue, maxValue, metricName):
	log("Minimum value set to:" + str(minValue))
	log("Maximum value set to:" + str(maxValue))
	log("Metric set to: " + str(metricName))
	message = '%s %s %d\n' % (metricName, random.uniform(minValue,maxValue), int(time.time()))
	log("The message to graphite is : " + str(message))
	log("Message send to : " + str(CARBON_SERVER) + " on port " + str(CARBON_PORT))
	sock = socket.socket()
	sock.connect((CARBON_SERVER, CARBON_PORT))
	sock.sendall(message)
        return

def log (message):
	if debug:
		logger.debug(message)

# Main script

shoot = True
debug = False # True
Timestamp = int()
CARBON_SERVER = "juggernaut.monitor.now"
CARBON_PORT = 2003

signal.signal(signal.SIGINT, signal_handler)

if debug:
	setlog("DEBUG")

if __name__ == '__main__':

	while shoot:
		execmetric(1,100,"app.bankit.prd.businessvolumes.fullfilment.count")
		execmetric(1,2000,"app.bankit.prd.processing.transactions.count")
		execmetric(1,200,"app.bankit.prd.concurrent.users.avg")
		time.sleep(60)
