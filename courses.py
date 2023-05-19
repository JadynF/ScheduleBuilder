class Course:
    def __init__(self, section):
        self.course = ""
        self.section = self.setSection(section)
        self.callNum = 0
        self.open = False
        self.openSeats = None
        self.maxSeats = None
        self.modality = ""
        self.days = ""
        self.time = ""
        self.location = ""
        self.instructor = ""

    def setSection(self, section):
        print(repr(section))
        parsed = section.split()
        self.course = parsed[0] + parsed[1]
        return parsed[2]

    def setCallNum(self, newCallNum):
        self.callNum = int(newCallNum)
    
    def setStatAndSeat(self, sAndS):
        parsed = sAndS.split()
        if (parsed[0] == "Open"):
            self.open = True
            if (len(parsed) == 5):
                self.openSeats = parsed[2]
                self.maxSeats = parsed[4]

    def setModality(self, mode):
        self.modality = mode

    def setSetting(self, setting):
        print(repr(setting))
        return
        parsed = setting.split()
        
        self.days = parsed[0]
        self.time = parsed[1]
        self.location = parsed[2]
        i = 3
        while (i < len(parsed)):
            self.location += " " + parsed[i]
            i += 1
    
    def setInstructor(self, ins):
        self.instructor = ins

    def __str__(self):
        string = ""
        string += "Section: " + str(self.section) + "\n" + "Call number: " + str(self.callNum) + "\n" + "Is open: " + str(self.open) + "\n"
        if (self.openSeats != None):
            string += "With " + str(self.openSeats) + " of " + str(self.maxSeats) + " seats open" + "\n"
        string += "It is " + self.modality + "\n" + "At " + self.time + " every " + self.days + ", located at " + self.location + "\n" + "Being instructed by " + self.instructor
        return string

        
