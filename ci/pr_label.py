import re
import requests

branch_name = "task/fdsjfbsf sdjkbfs sdfkj"

pr_label = re.findall("(\w+)/", branch_name)

headers = {
    'Authorization': 'token 0e4041403b78a119dad6a1a33c0c1140ad097942'
}

payload = {
    'labels': pr_label
}

r = requests.post('https://api.github.com/repos/hoop-carpool/Android-architecture-example/issues/9', headers=headers,
                  json=payload)