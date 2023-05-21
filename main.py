import requests
import pprint
from datetime import datetime
from bs4 import BeautifulSoup
import courses

SESSION = requests.Session()
while (True):
    url = "https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&tserve_tip_write=||WID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero"
    r = SESSION.get(url)
    soup = BeautifulSoup(r.content, "html.parser")
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
        if (qinput == "fall"):
            year = int(yinput) + 1
            yinput =  str(year) + "1"
        elif (qinput == "winter"):
            yinput += "2"
        elif (qinput == "spring"):
            yinput += "3"
        elif (qinput == "summer"):
            yinput += "4"
        payload["Term"] = yinput
        r = SESSION.post("https://boss.latech.edu/ia-bin/tsrvweb.cgi", data = payload)
        soup = BeautifulSoup(r.content, "html.parser")
        if (soup.find("table", {"class": "dataentrytable"}) != None):
            sinput = input("Enter a subject: ")
            sinput = sinput.upper()
            payload = {
                "tserve_tip_read_destroy" : "",
                "tserve_host_code" : "HostZero",
                "tserve_tiphost_code" : "TipZero",
                "tserve_trans_config" : "rcrssecthp2.cfg",
                "tserve_tip_write" : "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName",
                "TransactionSource" : "H",
                "ReqNum" : "2"
            }
            payload["Subject"] = sinput
            r = SESSION.post("https://boss.latech.edu/ia-bin/tsrvweb.cgi", data = payload)
            soup = BeautifulSoup(r.content, "html.parser")
            if (soup.find("table", {"class": "dataentrytable"}) != None):
                cNumInput = input("Enter the course number: ")
                if (len(sinput) < 4):
                    sinput += " "
                payload = {
                    "tserve_tip_read_destroy" : "",
                    "tserve_host_code" : "HostZero",
                    "tserve_tiphost_code" : "TipZero",
                    "tserve_trans_config" : "rcrssecthp3.cfg",
                    "tserve_tip_write" : "||WID|SID|PIN|Term|Subject|CourseID|AppTerm|ConfigName",
                    "TransactionSource" : "H",
                    "ReqNum" : "3"
                }
                payload["CourseID"] = sinput + "-" + cNumInput
                r = SESSION.post("https://boss.latech.edu/ia-bin/tsrvweb.cgi", data = payload)
                soup = BeautifulSoup(r.content, "html.parser")
                if (soup.find("table", {"class": "datadisplaytable"}) != None):
                    rows = soup.find("table", {"class": "datadisplaytable"}).find_all("tr")
                    courseList = []
                    for row in rows:
                        tableData = row.find_all("td")
                        course = None
                        nullRow = False
                        for data in tableData:
                            if (nullRow):
                                continue
                            elif (data.has_attr("headers")):
                                header = str(data["headers"])
                                if (header == "['CourseID']"):
                                    if (len(data.text) > 1):
                                        course = courses.Course(data.text, sinput)
                                        courseList.append(course)
                                    else:
                                        nullRow = True
                                elif (header == "['CallNumber']"):
                                    course.setCallNum(data.text)
                                elif (header == "['StatusAndSeats']"):
                                    course.setStatAndSeat(data.text)
                                elif (header == "['Modality']"):
                                    course.setModality(data.text)
                                elif (header == "['DaysTimeLocation']"):
                                    course.setSetting(data.text)
                                elif (header == "['Instructor']"):
                                    course.setInstructor(data.text)
                    for course in courseList:
                        print(course)
                        print("-" * 35)


    else:
        print(False)