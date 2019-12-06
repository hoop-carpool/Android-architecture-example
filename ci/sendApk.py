import requests
import os
import sys
import currentVersions

current_tag = currentVersions.currentTag()
previous_tag = currentVersions.previousPatchTag()	

change_log= os.popen("git log --pretty=%s '{}'..'{}'".format(previous_tag, current_tag)).read()

apk_path = sys.argv[1]
apk_filename = sys.argv[2]

headers = {
	'Content-type': 'multipart/form-data'
}

file={
	'file': open(apk_path, 'rb')
}

payload = {
	'token': os.environ['SLACK_GITHUB_BOT_TOKEN'],
	'channels': 'CQJ7JQAMP',
	'filename': apk_filename+'-'+current_tag+'.apk',
	'filetype': 'apk',
	'initial_comment': "Version '{}' \n '{}'".format(current_tag, change_log)
}

r = requests.post('https://slack.com/api/files.upload', params=payload, files=file)

print(r.text)