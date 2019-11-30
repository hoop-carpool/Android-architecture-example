import os, sys, re
import currentVersions

current_tag = currentVersions.currentTag()

def removeLastTag(current_tag):
	print "Deleting tag: " + current_tag
	os.popen("git tag -d " + current_tag)
	os.popen("git push --delete origin " + current_tag)

removeLastTag(current_tag)
