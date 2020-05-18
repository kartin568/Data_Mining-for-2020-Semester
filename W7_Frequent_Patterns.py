#!/usr/bin/env python
# coding: utf-8

# # 选择数据集为winemag-data_first150k，这是一个酒类数据。

# 1.对数据集进行处理，转换成适合进行关联规则挖掘的形式。
# 由原生数据，去掉第一行的序列号和description，选取剩余9列作为数据。
# 将一行数据转换成适合进行关联规则挖掘的形式：将数据集按行处理为事务，即一行构成一项事务。
# 对数据的预处理，出于对数据的准确性考虑，对有缺省的事务作丢弃处理，剩余事务仍然具有一定量。

# In[105]:


import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from matplotlib import pyplot as plt

req_data = pd.read_csv('winemag-data_first150k.csv')
print(req_data.shape)
origin_data = req_data.iloc[:,1:]
print(origin_data.shape)
origin = origin_data.dropna(axis=0, how='any', inplace=False)

print(origin.isnull().sum())
print(origin.shape)

fin_origin = origin.loc[:,['country','designation','points','price','province','region_1','region_2','variety','winery']]
print(fin_origin.shape)

#对数据集进行处理，转换成适合进行关联规则挖掘的形式：将数据集按行处理为事务，即一行构成一项事务

dataset = []
for index, row in fin_origin.iterrows():
    tmp_ds = []
    tmp_ds.append(row[0])
    tmp_ds.append(row[1])
    tmp_ds.append(row[2])
    tmp_ds.append(row[3])
    tmp_ds.append(row[4])
    tmp_ds.append(row[5])
    tmp_ds.append(row[6])
    tmp_ds.append(row[7])
    dataset.append(tmp_ds)

print(len(dataset))


# 这一步来找出频繁模式，导出关联规则，计算其支持度和置信度。这里使用Apriori算法。
# Create_C1(原事务集)：对事务集进行处理，得到所有的1项集作为1候选集。
# Judge_apriori(K候选集，k-1频繁集)：判断K候选集的各子集是否为频繁集。非频繁集的超集不是频繁集。
# Create_Ck(k-1频繁集, k)：由k-1频繁集获得k候选集。
# Generate_Fk_by_Ck(原事务集, K候选集, 最小支持度, 支持度数据):由候选集得到频繁集
# Generate_F(原事务集, max_k, 最小支持度)：获得最终的频繁集
# Generate_Rule(频繁集, 支持度数据, 最小置信度):关联规则挖掘

# In[106]:


#找出频繁模式；导出关联规则，计算其支持度和置信度。这里使用Apriori算法。

def Create_C1(dataset):
    
    C1 = set()
    for piece in dataset:
        for item in piece:
            itemset = frozenset([str(item)])
            C1.add(itemset)
    return C1

def Judge_apriori(Ck_item,Fk_sub_1):
    
    for item in Ck_item:
        sub_item = Ck_item - frozenset([item])
        if sub_item not in Fk_sub_1:
            return False
    return True

def Create_Ck(Fk_sub_1, k):
    
    Ck = set()
    len_Fk_sub_1 = len(Fk_sub_1)
    list_Fk_sub_1 = list(Fk_sub_1)
    
    for i in range(len_Fk_sub_1):
        for j in range(i+1,len_Fk_sub_1):
            list1 = list(list_Fk_sub_1[i])
            list2 = list(list_Fk_sub_1[j])
            list1.sort()
            list2.sort()
            
            if list1[0:k-2] == list2[0:k-2]:
                Ck_item = list_Fk_sub_1[i] | list_Fk_sub_1[j]
                if Judge_apriori(Ck_item, Fk_sub_1):
                    Ck.add(Ck_item)
    return Ck

def Generate_Fk_by_Ck(dataset, Ck, minsup, support_data):
    
    Fk = set()
    item_count = {}
    
    for piece in dataset:
        for Ck_item in Ck:
            if Ck_item.issubset(piece):
                if Ck_item not in item_count:
                    item_count[Ck_item] = 1
                else:
                    item_count[Ck_item] += 1
                    
    data_num = float(len(dataset))
    for item in item_count:
        if( item_count[item] / data_num ) >= minsup:
            Fk.add(item)
            support_data[item] = item_count[item] /data_num
            
    return Fk

def Generate_F(dataset, max_k, minsup):
    
    support_data = {}
    C1 = Create_C1(dataset)
    F1 = Generate_Fk_by_Ck(dataset, C1, minsup, support_data)
    Fk_sub_1 = F1.copy()
    F = []
    
    F.append(Fk_sub_1)
    
    for k in range(2, max_k+1):
        Ck = Create_Ck(Fk_sub_1, k)
        Fk = Generate_Fk_by_Ck(dataset, Ck, minsup, support_data)
        Fk_sub_1 = Fk.copy()
        F.append(Fk_sub_1)
    
    return F, support_data

def Generate_Rule(F, support_data, minconf):
    
    rule_list = []
    subset_list = []
    
    for i in range(len(F)):
        for frequent_set in F[i]:
            for subset in subset_list:
                if subset.issubset(frequent_set):
                    conf = support_data[frequent_set] / support_data[subset]
                    rule = (subset, frequent_set-subset, conf)
                    if conf >= minconf and rule not in rule_list:
                        rule_list.append(rule)
            subset_list.append(frequent_set)
    return rule_list


# In[111]:


#设定最小支持度和最小置信度,导出关联规则，计算其支持度和置信度
minsup = 0.05
minconf = 0.75
F, support_data = Generate_F(dataset, 8, minsup)
rule_list = Generate_Rule(F, support_data, minconf)

for Fk in F:
    if len(list(Fk)) == 0:
        continue
    print("-"*80)
    print("Frequent\t"+ str(len(list(Fk)[0])) +"-itemsets\t\tSupport")
    print("-"*80)
    for frequent_set in Fk:
        print(frequent_set, '\t', support_data[frequent_set])
print()
print("Extracted Rules")
for item in rule_list:
    print(item[0], "=>", item[1], "\t\t  conf: ", item[2])


# 对规则进行评价，可使用Lift、卡方和其它教材中提及的指标, 至少2种。
# 对提取出的规则，计算Kulc、IR和Lift。

# In[108]:



confs = []
kulcs = []
lifts = []
irs = []

for item in rule_list:
    x = support_data[item[0]]
    y = support_data[item[1]]
    x_to_y = item[2]
    kulc = 0.5*(x_to_y/x+x_to_y/y)
    ir = abs(x - y)/(x+y-x_to_y)
    
    confs.append(x_to_y)
    kulcs.append(kulc)
    lifts.append(float( x_to_y/y ))
    irs.append(ir)
    print(item[0], "=>", item[1], " lift: ", float( x_to_y/y ), "  kulc: ", kulc," IR: ",ir)


# 对结果画图。由于规则的置信度几乎都是1.0，所以主要观察kulc，lift和IR间的分布影响。

# In[109]:


import numpy as np
import matplotlib.pyplot as plt

x = kulcs
y = lifts

fig = plt.figure(figsize=(9,6))
ax1 = fig.add_subplot(111)
ax1.set_title('Scatter Plot for kulcs to lifts')

plt.xlabel('kulcs')
plt.ylabel('lifts')
ax1.scatter(x,y,c = 'r',marker = 'x')

plt.show()


# In[ ]:


可以看到，lift几乎都是分布在1左右。


# In[110]:


x = kulcs
y = irs

fig = plt.figure(figsize=(9,6))
ax1 = fig.add_subplot(111)

ax1.set_title('Scatter Plot for kulcs to IRs')
plt.xlabel('kulcs')
plt.ylabel('IRs')

ax1.scatter(x,y,c = 'r',marker = 'x')
plt.show()


# 可以看到在这幅图中IR与kulcs部分存在线性元素。
# 
# 对挖掘结果进行分析:挖掘得到的关联规则大部分是关于产地和国家和省份的关系的，因而存在置信度为1的情况，如({'Columbia Valley', 'Columbia Valley (WA)', 'US'}) => frozenset({'Washington'})；存在部分由产地、国家及省份推断酒类和酒厂的关联规则，其置信度也比较高。
