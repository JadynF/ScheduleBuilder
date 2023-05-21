import re
class Course:
    def __init__(self, section, sinput):
        self.sinput = sinput
        self.course = ""
        self.section = self.setSection(section)
        self.callNum = 0
        self.open = False
        self.openSeats = None
        self.maxSeats = None
        self.modality = ""
        self.days = None
        self.time = None
        self.location = ""
        self.instructor = ""
        self.restrictions = None

    def setSection(self, section):
        parsed = section.split()
        if (" " not in self.sinput):
            self.course = parsed[0]
            return parsed[1]
        else:
            self.course = parsed[0] + parsed[1]
            return parsed[2]

    def setCallNum(self, newCallNum):
        if (len(newCallNum) == 0):
            return
        self.callNum = int(newCallNum)
    
    def setStatAndSeat(self, sAndS):
        parsed = sAndS.split()
        if (len(parsed) == 0):
            return
        if (parsed[0] == "Open"):
            self.open = True
            if (len(parsed) == 5):
                self.openSeats = parsed[2]
                self.maxSeats = parsed[4]

    def setModality(self, mode):
        if (len(mode) == 0):
            self.modality = "Not Specified"
            return
        self.modality = mode

    def setSetting(self, setting):
        setting = setting.replace(u'\xa0', u' ')
        setting = setting.replace(u'\r\n', u' ')
        if (setting.startswith(" ")):
            self.location = "Main Campus"
            return
        elif (setting.startswith("To Be Arranged")):
            self.location = "TBA"
            return
            
        parsed = re.split(r'\s+', setting)
        
        self.days = parsed[0]
        self.time = parsed[1]
        
        i = 2
        while (i < len(parsed)):
            self.location += parsed[i] + " "
            i += 1
    
    def setInstructor(self, ins):
        parsed = ins.split()
        if (len(parsed) == 0):
            return
        self.instructor = parsed[0] + " " + parsed[1][0]
        if (len(parsed[1]) > 1):
            self.restrictions = parsed[1][1 : len(parsed[1])]
            for i in range(2, len(parsed)):
                self.restrictions += " " + parsed[i]
        

    def __str__(self):
        string = ""
        string += "Section: " + str(self.section) + "\n" + "Call number: " + str(self.callNum) + "\n" + "Is open: " + str(self.open) + "\n"
        if (self.openSeats != None):
            string += "With " + str(self.openSeats) + " of " + str(self.maxSeats) + " seats open" + "\n"
        string += "Modality: " + self.modality + "\n"
        if (self.days != None and self.time != None):
            string += "Every " + self.days + " at " + self.time + "\n"
        string += "At " + self.location + ", being instructed by " + self.instructor + "\n"
        if (self.restrictions != None):
            string += "Restrictions: " + self.restrictions
        return string

        
