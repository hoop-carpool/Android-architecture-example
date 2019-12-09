import re
import requests

branch_name = "task/fdsjfbsf sdjkbfs sdfkj"

pr_label = re.findall("(\w+)/", branch_name)

headers = {
    'Authorization': 'token cca00efc6d4fc709d95573b0e233dff3053783b4'
}

payload = {
    'labels': pr_label
}

r = requests.post('https://api.github.com/repos/hoop-carpool/Android-architecture-example/issues/9', headers=headers,
                  json=payload)

print r.text