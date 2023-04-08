import requests
from datetime import datetime
from bs4 import BeautifulSoup

SESSION = requests.Session()

url = "https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&tserve_tip_write=||WID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero"
r = SESSION.get(url)
soup = BeautifulSoup(r.content, "html5lib")
if (soup.find("select", {"class": "optdefault"}) != None):
    payload = {
        "tserve_tip_read_destroy" : "",
        "tserve_host_code" : "HostZero",
        "tserve_tiphost_code" : "TipZero",
        "tserve_trans_config" : "RCRSSECTHP1.cfg",
        "tserve_tip_write" : "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName",
        "TransactionSource" : "H"
    }
    yinput = input("Enter the year: ")
    qinput = input("Enter the term: ")
    if (qinput == "winter"):
        yinput += "2"
    elif (qinput == "spring"):
        yinput += "3"
    elif (qinput == "summer"):
        yinput += "4"
    payload["Term"] = yinput
    r = SESSION.post("https://boss.latech.edu/ia-bin/tsrvweb.cgi", data = payload)
    soup = BeautifulSoup(r.content, "html5lib")
    if (soup.find("table", {"class": "dataentrytable"}) != None):
        sinput = input("Enter a subject: ")
        payload = {
            "tserve_tip_read_destroy" : "",
            "tserve_host_code" : "HostZero",
            "tserve_tiphost_code" : "TipZero",
            "tserve_trans_config" : "rcrssecthp2.cfg",
            "tserve_tip_write" : "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName",
            "TransactionSource" : "H",
            "ReqNum" : "2"
        }
        payload["Subject"] = sinput.capitalize()
        r = SESSION.post("https://boss.latech.edu/ia-bin/tsrvweb.cgi", data = payload)
        soup = BeautifulSoup(r.content, "html5lib")
        if (soup.find("table", {"class": "dataentrytable"}) != None):
            cNumInput = input("Enter the course number: ")
            if (cNumInput.len() < 4):
                cNumInput += " "
            payload = {
                "tserve_tip_read_destroy" : "",
                "tserve_host_code" : "HostZero",
                "tserve_tiphost_code" : "TipZero",
                "tserve_trans_config" : "rcrssecthp3.cfg",
                "tserve_tip_write" : "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName",
                "TransactionSource" : "H",
                "ReqNum" : "3"
            }
            payload["CourseID"] = cNumInput
            r = SESSION.post("https://boss.latech.edu/ia-bin/tsrvweb.cgi")
            soup = BeautifulSoup(r.content, "html5lib")
            
else:
    print(False)
#print(soup)