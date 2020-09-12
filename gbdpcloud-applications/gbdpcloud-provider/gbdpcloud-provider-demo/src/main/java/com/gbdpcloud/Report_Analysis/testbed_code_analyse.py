import sys
import requests
from bs4 import BeautifulSoup as bs
import re
import csv
from collections import Counter


# 将每个规则在一个文件中的所在行号进行排序
# 排序关键字匹配
# 匹配开头数字序号
def sort_key(s):
    if s:
        try:
            c = re.findall('^\d+', s)[0]
        except:
            c = -1
        return int(c)


# 将读取的代码审查问题写入文件
# 参数 dic 为要输出的字典信息
# 参数 output_mid_code_review_filename  要输出的详细的审查报告的文件名称
def write_code_rewivew_file(dic, output_mid_code_review_filename):
    # 打开文件，追加a
    file = open(output_mid_code_review_filename, 'w', newline='')
    # 设定写入模式
    csv_write = csv.writer(file, dialect='excel')
    # 写入具体内容
    line = ['code', 'line', 'violation', 'standard', "filename", "funname", "fun_start_line", "fun_end_line"]
    csv_write.writerow(line)
    for item in dic:
        line = [item.get('code', ""), item.get('line', ""), \
                item.get('violation', ""), item.get('standard', ""), \
                item.get('filename', ""), item.get('funname', ""), \
                item.get('fun_start_line', ""), item.get('fun_end_line', "")]
        csv_write.writerow(line)
    print("write_code_rewivew_file write over")
    file.close()


# 参数 sum_dic 为要输出的字典信息
# 参数 output_sum_code_review_file  要输出的行汇总过的的审查报告的文件名称
def write_sum_code_rewivew_file(sum_dic, output_sum_code_review_file):
    file = open(output_sum_code_review_file, 'w', newline='')
    # 设定写入模式
    csv_write = csv.writer(file, dialect='excel')
    # 写入具体内容
    line = ['code', 'standard', 'violation', "filename", 'lines']
    csv_write.writerow(line)
    standards = list(sum_dic.keys())
    standards.sort()
    for item in standards:
        # 统计各行出现的次数
        line_count = Counter(sum_dic[item][4])
        line_count_with_times = []
        print("--------------------")
        print(item)
        line_nums = list(line_count.keys())
        line_nums.sort(key=sort_key)
        for line in line_nums:
            times = line_count[line]
            if times < 2:
                line_count_with_times.append(line)
            else:
                line_count_with_times.append(line + "(" + str(times) + "次)")
        print(line_count_with_times)
        # 排序
        # d = sorted(result.items(), key=lambda x: x[1], reverse=True)
        line = [sum_dic[item][0], sum_dic[item][1], sum_dic[item][2], sum_dic[item][3], ",".join(line_count_with_times)]
        # line=[sum_dic[item][0],sum_dic[item][1],sum_dic[item][2],sum_dic[item][3],",".join(list[sum_dic[item][4]])]
        csv_write.writerow(line)
    print("write_sum_code_rewivew_file write over")
    file.close()


def code_review_substract(testbed_code_review_report_filename):
    file = open(testbed_code_review_report_filename, 'r', encoding='gb2312')
    soup = bs(file, 'lxml')

    tables_erro_info = []  # 用于汇总所有表格中的错误信息
    table_erro_info = []  # 用于临时保存单个表中的错误信息
    all_table = soup.find_all("table")
    for table in all_table:
        filename = ""
        funname = ""
        fun_start_line = ""
        fun_end_line = ""
        table_erro_info = []
        head = table.find_all("th")
        # 情况1：全局错误信息，若表格列有4列，且标题为 code line violaion sandad，则认为找到表格
        if len(head) == 4 and head[0].getText().strip() == "Code" \
                and head[1].getText().strip() == "File: Src Line" and head[2].getText().strip() == "Violation" \
                and head[3].getText().strip() == "Standard":
            print(table)
            # 提取表格中的所有违规记录
            rows = table.find_all("tr")
            table_erro_info = []
            for row in rows:
                error = {}
                cols = row.find_all("td")
                # 表中存在列数不等于4的情况，该表数据丢弃
                if len(cols) == 4:
                    error['code'] = cols[0].getText().strip()
                    error['filename'] = cols[1].getText().strip().split(":", 1)[0].strip()
                    error['line'] = cols[1].getText().strip().split(":", 1)[1].strip()
                    error['violation'] = cols[2].getText().strip()
                    error['standard'] = cols[3].getText().strip()
                    table_erro_info.append(error)
                    print(error)

        # 若函数违规信息表格列有4列，且标题为 code line violaion sandad，则认为找到表格
        if len(head) == 4 and head[0].getText().strip() == "Code" \
                and head[1].getText().strip() == "Line" and head[2].getText().strip() == "Violation" \
                and head[3].getText().strip() == "Standard":
            print(table)
            # 提取表格上方的函数名以及起始行、终止行信息
            if table.find_previous("table", attrs={"width": "50%"}) is not None:
                if len(table.find_previous("table", attrs={"width": "50%"}).find_all("tr")) == 1:
                    whole_fun_info = table.find_previous("table", attrs={"width": "50%"}).getText().strip()
                    fun_list = whole_fun_info.replace("(", " ").replace(")", " ").split()
                    filename, funname, fun_start_line, fun_end_line = [fun_list[i] for i in [4, 0, 1, 3]]
                    print(filename, funname, fun_start_line, fun_end_line)

            # 提取表格中的所有违规记录
            rows = table.find_all("tr")
            table_erro_info = []
            for row in rows:
                error = {}
                cols = row.find_all("td")
                # 表中存在列数不等于4的情况，该表数据丢弃
                if len(cols) == 4 and cols[1].getText().strip().isdigit():
                    error['code'] = cols[0].getText().strip()
                    error['line'] = cols[1].getText().strip()
                    error['violation'] = cols[2].getText().strip()
                    error['standard'] = cols[3].getText().strip()
                    error['filename'] = filename
                    error['funname'] = funname
                    error['fun_start_line'] = fun_start_line
                    error['fun_end_line'] = fun_end_line
                    table_erro_info.append(error)
                    print(error)

        # 若函数违规信息表格列有3列，且标题为 code violaion sandad，则认为找到表格
        if len(head) == 3 and head[0].getText().strip() == "Code" \
                and head[1].getText().strip() == "Violation" and head[2].getText().strip() == "Standard":
            print(table)
            # 提取表格上方的函数名以及起始行、终止行信息
            if table.find_previous("table", attrs={"width": "50%"}) is not None:
                if len(table.find_previous("table", attrs={"width": "50%"}).find_all("tr")) == 1:
                    whole_fun_info = table.find_previous("table", attrs={"width": "50%"}).getText().strip()
                    fun_list = whole_fun_info.replace("(", " ").replace(")", " ").split()
                    filename, funname, fun_start_line, fun_end_line = [fun_list[i] for i in [4, 0, 1, 3]]
                    print(filename, funname, fun_start_line, fun_end_line)

            # 提取表格中的所有违规记录
            rows = table.find_all("tr")
            table_erro_info = []
            for row in rows:
                error = {}
                cols = row.find_all("td")
                # 表中存在列数不等于3的情况，该表数据丢弃
                if len(cols) == 3:
                    error['code'] = cols[0].getText().strip()
                    error['violation'] = cols[1].getText().strip().split(":")[0].strip()
                    error['line'] = funname  # 为了汇总时行号存在，用函数名代替行号
                    error['standard'] = cols[2].getText().strip()
                    error['filename'] = filename
                    error['funname'] = funname
                    error['fun_start_line'] = fun_start_line
                    error['fun_end_line'] = fun_end_line
                    table_erro_info.append(error)
                    print(error)
        tables_erro_info = tables_erro_info + table_erro_info
        print(len(tables_erro_info))

    sum_dic = {}
    for error in tables_erro_info:
        if error['standard'] + "#" + error['filename'] not in sum_dic:
            if "line" not in error:
                sum_dic[error['standard'] + "#" + error['filename']] = [error['code'], error['standard'],
                                                                        error['violation'], error['filename'], ['-1']]
            else:
                sum_dic[error['standard'] + "#" + error['filename']] = [error['code'], error['standard'],
                                                                        error['violation'], error['filename'],
                                                                        [error['line']]]
        else:
            if "line" in error:
                sum_dic[error['standard'] + "#" + error['filename']][4].append(error['line'])

    print("sum_dic:")
    for i in sum_dic:
        print(i, sum_dic[i])
    return tables_erro_info, sum_dic


print("Processing Testbed Analysis")
rps_name = sys.argv[1]
print(rps_name)
#rps_name = "CodeManager.rps.htm"
tables_erro_info, sum_dic = code_review_substract(rps_name)
write_code_rewivew_file(tables_erro_info, rps_name+'_detail.csv')
write_sum_code_rewivew_file(sum_dic, rps_name+'_sum.csv')