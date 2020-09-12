from xml.dom.minidom import parse
import xml.dom.minidom
import csv
import sys
# 使用minidom解析器打开 XML 文档
print("Processing Testbed Analysis")
rps_name = sys.argv[1]

csv_file = "D:\\JavaToPython\\klocwork_ana_detail.csv"
DOMTree = xml.dom.minidom.parse(rps_name)
collection = DOMTree.documentElement
if collection.hasAttribute("shelf"):
   print ("Root element : %s" % collection.getAttribute("shelf"))

# 在集合中获取所有problem
problems = collection.getElementsByTagName("problem")

#准备写入文件

file = open(csv_file, 'w', newline='')
csv_write = csv.writer(file, dialect='excel')

ele_list = ['problemID','file','method','code','message','citingStatus','severity','severitylevel','displayAs','taxonomy']
csv_write.writerow(ele_list)

for problem in problems:
    tmp_line = []
    for ele in ele_list:
        tex = problem.getElementsByTagName(ele)[0]
        if ele == 'taxonomy':
            item = tex.getAttribute('name')
            tmp_line.append(item)
        else:
            item = tex.childNodes[0].data
            tmp_line.append(item)

    csv_write.writerow(tmp_line)

print("Csv finished.")
file.close()

'''
    problemID = problem.getElementsByTagName('problemID')[0]
    print("problemID: %s" % problemID.childNodes[0].data)
    file = problem.getElementsByTagName('file')[0]
    print("file: %s" % file.childNodes[0].data)
    method = problem.getElementsByTagName('method')[0]
    print("method: %s" % method.childNodes[0].data)
    code = problem.getElementsByTagName('code')[0]
    print("code: %s" % code.childNodes[0].data)
    message = problem.getElementsByTagName('message')[0]
    print("message: %s" % message.childNodes[0].data)
    citingStatus = problem.getElementsByTagName('citingStatus')[0]
    print("citingStatus: %s" % citingStatus.childNodes[0].data)
    severity = problem.getElementsByTagName('severity')[0]
    print("severity: %s" % severity.childNodes[0].data)
    severitylevel = problem.getElementsByTagName('severitylevel')[0]
    print("severitylevel: %s" % severitylevel.childNodes[0].data)
    displayAs = problem.getElementsByTagName('displayAs')[0]
    print("displayAs: %s" % displayAs.childNodes[0].data)

    #taxonomies = problem.getElementsByTagName('taxonomy')
    taxonomy = problem.getElementsByTagName('taxonomy')[0]
    name = taxonomy.getAttribute('name')
    print("taxonomy: %s" % name)
'''