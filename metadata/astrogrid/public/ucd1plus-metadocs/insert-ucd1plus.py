#!/usr/bin/env python

import sys

### Function for finding the UCD+ match for the UCD
def findUCD(ucd1, mapList):
	### Go through the maplist and find the matching pair
	for item in mapList:
		if item.find(ucd1) != -1:
			start = item.index("#") + 1
			stop = item.index('\n')
			return item[start:stop]
	### no match found, return error
	return "??"

### Open the input XML file and the specified output file
###   The input file is the first arg, output the second
inFile = open(sys.argv[1], 'r')
outFile = open(sys.argv[2], 'w')

### Open the UCD mapping file and store the matching pairs
mapFile = open('UCD.map', 'r')
mapList = []
for line in mapFile:
	mapList.append(line)

### Lazy placeholder for pretty XML
indentMe = "         "

### Read the input file line by line, replace UCD lines and print all others as they are
for line in inFile:
	if line.find("UCD version") != -1:
		### get the ucd
		startSpot = line.index(">") + 1
		stopSpot = line.index("<",12)
		ucd = line[startSpot:stopSpot]
		
		### if the UCD is blank/unknown, leave it that way
		if ucd == '':
			outFile.write(indentMe + "<UCD version=\"1+\"></UCD>\n")
		elif ucd == '??':
			outFile.write(indentMe + "<UCD version=\"1+\"></UCD>\n")
		elif ucd == '????':
			outFile.write(indentMe + "<UCD version=\"1+\"></UCD>\n")
		
		### UCD is known, so find its match
		else:	
			outFile.write(indentMe + "<UCD version=\"1+\">" + findUCD(ucd, mapList) + "</UCD>\n" )
	else:
		outFile.write(line)