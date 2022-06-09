from xml.etree.ElementTree import tostring
from flask import Flask
from flask import request
from mlcode3.Run import test
from datetime import datetime
import sys
import json

app = Flask(__name__)

result_cache = {}


@app.route("/tospring", methods=['POST'])
def spring():
    jsonData = request.get_json()
    email = jsonData["email"]
    problist = jsonData["data"]

    probIdList = []
    for x in problist:
        probIdList.append(x["id"])

    ret = {}
    if email in result_cache:
        now = datetime.now()
        date_diff = now - result_cache[email]["date"]
        if date_diff.days < 1:
            ret["data"] = result_cache[email]["data"]
            return json.dumps(ret)
        if sorted(probIdList) == result_cache[email]["solved"]:
            result_cache[email]["date"] = now
            ret["data"] = result_cache[email]["data"]
            return json.dumps(ret)

    recommProb = test(probIdList)

    for idx in range(len(recommProb)):
        recommProb[idx] = int(recommProb[idx])

    temp = {}
    temp["date"] = datetime.now()
    temp["data"] = recommProb
    temp["solved"] = sorted(probIdList)

    result_cache[email] = temp

    ret = {
        "data": recommProb
    }

    # ret = test(probIdList)
    print(ret)
    return json.dumps(ret)


if __name__ == '__main__':
    app.run(debug=False, host="0.0.0.0", port=5050)
