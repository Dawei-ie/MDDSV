from lxml import etree
import shelve

# add detailed specifications/operations to a state
def addSpecToState (state, spec):
    for vertice in tree.findall(".//vertices[@name='" + state + "']"):
        vertice.attrib['specification'] = spec
        print  vertice.attrib['name']
        print  vertice.attrib['specification']

# get all the states of a statechart
def getStates():
    vertices = tree.findall(".//vertices[@name]")
    print len(vertices)
    return vertices

def printStates():
    vertices = getStates()
    for vertice in vertices:
        name = vertice.attrib['name']
        print name

def addAllstatesWithSpecs():
    vertices = getStates()
    for vertice in vertices:
        name = vertice.attrib['name']
        spec = readSpecification(name)
        addSpecToState(name, spec)

# save the existing specifications of a state to file
# delete the existing specifications
def saveSpecification():
    d = shelve.open("name_spec")
    vertices = tree.findall(".//vertices[@specification]")
    print len(vertices)
    for vertice in vertices:
        name = vertice.attrib['name']
        specification = vertice.attrib['specification']
        d[name] = specification
        vertice.attrib.pop('specification')
        print name
        print specification
    d.close

# read the specifications from a file
def readSpecification(stateName):
    d = shelve.open("name_spec")
    if d.has_key(stateName):
        return d[stateName]
    else:
        return ""
    d.close

# main starts here!!!
# generate a new SC without specifications, save specs in a file
tree = etree.parse("TrafficLightWaiting.sct")
printStates()
saveSpecification()
tree.write('GenTrafficLightWaiting.sct', encoding="UTF-8")

# read specs from a file, add to corresponding states
tree = etree.parse("GenTrafficLightWaiting.sct")
addAllstatesWithSpecs();
tree.write('GenTrafficLightWaiting.sct', encoding="UTF-8")
