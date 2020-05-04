#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from matplotlib import pyplot as plt

origin_cbg = pd.read_csv('cbg_patterns.csv')
print(origin_cbg.shape)


# In[2]:


print(origin_cbg.dtypes)


# In[3]:


def draw_Histograms(data, name, num):
    new_data = data.dropna(axis=0, how='any', inplace=False)
    plt.figure(figsize=(12,6))
    plt.hist(x = new_data, # 指定绘图数据
             bins = num, # 指定直方图中条块的个数
             color = 'steelblue', # 指定直方图的填充色
             edgecolor = 'black' # 指定直方图的边框色
            )
    plt.xlabel(name)
    plt.ylabel('Frequency')
    # 添加标题
    plt.title('Distribution of '+str(name))
    # 显示图形
    plt.show()


# In[4]:


def comparison_figure(data_before, data_after, name):
    plt.figure(figsize=(18,6))
    x = np.arange(len(data_before.index))
    y = data_before
    y1 = data_after

    bar_width = 0.35
    tick_label = data_before.index

    plt.bar(x, y, bar_width, align="center", color="c", label="BEFORE", alpha=0.5)
    plt.bar(x+bar_width, y1, bar_width, color="b", align="center", label="AFTER", alpha=0.5)

    plt.xlabel("Frequency of "+ name)
    plt.ylabel("Value")

    plt.xticks(x+bar_width/2, tick_label)

    plt.legend()

    plt.show()


# In[5]:


def comparison_boxplot(data_before, data_after, name):
    fig,axes = plt.subplots(1,2)
    color = dict(boxes='DarkGreen', whiskers='DarkOrange',
                  medians='DarkBlue', caps='Red')
    # boxes表示箱体，whisker表示触须线
    # medians表示中位数，caps表示最大与最小值界限
    f = pd.concat([data_before, data_after],axis=1)
    f.plot(kind='box',ax=axes,subplots=True,title='Boxplots of '+name+'(BEFORE AND AFTER)',color=color,sym='r+')
    # sym参数表示异常值标记的方式

    axes[0].set_ylabel('(BEFORE)values of '+ name)
    axes[1].set_ylabel('(AFTER)values of '+ name)

    fig.subplots_adjust(wspace=1,hspace=1)  # 调整子图之间的间距
    fig.savefig('p2.png') 


# In[6]:


'''
大致解释某些列的数据含义。
census_block_group：CBG的唯一12位FIPS代码，相当于ID，作为字符串考虑。
date_range_start,date_range_end: 时间测量开始和结束的时间戳。
raw_visit_count，raw_visitor_count：时间周期内的访问数和访客数（两者不一定相等。）
visitor_home_cbgs，visitor_work_cbgs：访问某个CBG的ID和次数记录
distance_from_home：访问距离（米）
related_same_day_brand，related_same_month_brand：当日和当月的访问品牌（给定的是前十）
popularity_by_hour，popularity_by_day:按小时和按日访问数

确认了数据含义，可作为后续盒图或频数求解依据。
cencus_block_group不存在重复数据；
date_range_start,date_range_end可得出访问时间信息，即统计的访问时间段是固定的，没有变化

raw_visit_count，raw_visitor_count，distance_from_home作为数值属性处理
popularity_by_hour，popularity_by_day的单项数据分别为列表和词典
related_same_day_brand，related_same_month_brand，top_brands为标称列表
visitor_home_cbgs，visitor_work_cbgs作为词典处理
'''
color = dict(boxes='DarkGreen', whiskers='DarkOrange',medians='DarkBlue', caps='Red')

data_num = origin_cbg[['raw_visit_count','raw_visitor_count','distance_from_home']]
data_num_com = origin_cbg[['popularity_by_hour','popularity_by_day']]
data_num_brand = origin_cbg[['related_same_day_brand','related_same_month_brand','top_brands']]
data_visit=origin_cbg[['visitor_home_cbgs','visitor_work_cbgs']]

for col in data_num.columns:#处理数值属性
    describe = data_num[col].describe()
    miss_count = data_num[col].isnull().sum()
    print(describe)
    print("Missing: %d "%(miss_count))
    
    fig,axes = plt.subplots()
    data_num[col].plot(kind='box',ax=axes, color=color, sym='r+') 
    axes.set_ylabel(str(col))
    fig.savefig(str(col) + '.png') 
        
    #fig,axes = plt.subplots()
    draw_Histograms(data_num[col], str(col), 100)
    print("-------------------------------------"+
          "-------------------------------------------------")    


# In[7]:


#处理data_num_com
df = data_num_com['popularity_by_hour']
data_list = []

for i in df:
    tmp = eval(i)
    data_list.extend(tmp)

tp = {'popularity_by_hour':data_list}
df_num =  pd.DataFrame(tp)

describe = df_num.describe()
print(describe)
fig,axes = plt.subplots()
data_num[col].plot(kind='box',ax=axes, color=color, sym='r+') 
axes.set_ylabel(str(col))
fig.savefig(str(col) + '.png') 
#fig,axes = plt.subplots()
draw_Histograms(df_num['popularity_by_hour'],'popularity_by_hour', 100)
print("-------------------------------------"+
    "-------------------------------------------------")   


# In[7]:


#处理data_num_com
dff = data_num_com['popularity_by_day']
data_dict = []

for i in dff:
    tmp = eval(i)
    if(tmp):
        data_dict.append(np.float64(tmp['Monday']))
        data_dict.append(np.float64(tmp['Tuesday']))
        data_dict.append(np.float64(tmp['Wednesday']))
        data_dict.append(np.float64(tmp['Thursday']))
        data_dict.append(np.float64(tmp['Friday']))
        data_dict.append(np.float64(tmp['Saturday']))
        data_dict.append(np.float64(tmp['Sunday']))
        
tps = {'popularity_by_day':data_dict}
df_num =  pd.DataFrame(tps)
describe = df_num.describe()
print(describe)
fig,axes = plt.subplots()
data_num[col].plot(kind='box',ax=axes, color=color, sym='r+') 
axes.set_ylabel(str(col))
fig.savefig(str(col) + '.png') 
#fig,axes = plt.subplots()
draw_Histograms(df_num['popularity_by_day'], 'popularity_by_day', 100)
print("-------------------------------------"+
    "-------------------------------------------------")


# In[9]:


for col in data_num_brand.columns:
    tmp_list = []
    for it in data_num_brand[col]:
        itt = eval(it)
        tmp_list.extend(itt)
    tp = { str(col):tmp_list }
    tmp_df = pd.DataFrame(tp)
    counts = tmp_df[col].value_counts()
    cnts = counts[:10]
    print(cnts)
    
    fig,axes = plt.subplots()
    plt.ylabel(str(col)+' frequency')
    cnts.plot.bar()
    plt.show()
    print("-------------------------------------"+
    "-------------------------------------------------")


# In[8]:


#data_visit=origin_cbg[['visitor_home_cbgs','visitor_work_cbgs']]

eval_dict = {}
for col in data_visit.columns:
    tmp_dict = {}
    for it in data_visit[col]:
        it_dict = eval(it)
        for k in it_dict.keys():
            if k in tmp_dict:
                add = int(it_dict[k])
                tmp_dict[k] = tmp_dict[k] + add
            else:
                tmp_dict[k] = it_dict[k]
    #print(tmp_dict)
    eval_dict = tmp_dict
'''
数据处理后发现两个数据项的汇总数据是一样的，则频数图只画一个
'''


# In[16]:


x,y = [],[]

for key, value in eval_dict.items():
    x.append(key)
    y.append(value)

plt.figure(figsize=(15,15))
plt.barh(x[:69],y[:69])  # 横放条形图函数 barh
plt.title('Frequency of visitors')
plt.show()


# In[17]:


'''
下一步执行数据异常值处理。
数据缺失的原因可能有很多，比如访问数据在收集时的缺失，亦或是当天站点进行维护从而访问情况为空。
考虑到原数据的构成，在异常值处理中主要对raw_visit_count，raw_visitor_count，distance_from_home
进行处理。处理策略相同。
'''

#data_num = origin_cbg[['raw_visit_count','raw_visitor_count','distance_from_home']]
#去掉空值处理

for col in data_num.columns:
    tmp_data = data_num[col].copy(deep = True)
    origin_des = data_num[col].describe()
    origin_mis = data_num[col].isnull().sum()
    print(origin_des)
    print("Missing Before: "+str(origin_mis))
    print("---------------------------------------------"+
              "-----------------------------------------")
    tmp_data_c = tmp_data.dropna(axis=0,how='any',inplace = False)
    tmp_des = tmp_data_c.describe()
    tmp_mis = tmp_data_c.isnull().sum()
    print(tmp_des)
    print("Missing After: "+str(tmp_mis))
    print("---------------------------------------------"+
              "-----------------------------------------")
    comparison_boxplot(tmp_data, tmp_data_c, col)


# In[18]:


'''如前所述，去除操作对分布没有产生变化。'''
#众数填补处理
for col in data_num.columns:
    tmp_data = data_num[col].copy(deep = True)
    origin_des = data_num[col].describe()
    origin_mis = data_num[col].isnull().sum()
    print(origin_des)
    print("Missing Before: "+str(origin_mis))
    print("---------------------------------------------"+
              "-----------------------------------------")
    #用最大频数数据填补异常值
    value = tmp_data.value_counts()
    tmp_data_c = tmp_data.fillna(value.index[0])
    
    tmp_des = tmp_data_c.describe()
    tmp_mis = tmp_data_c.isnull().sum()
    print(tmp_des)
    print("Missing After: "+str(tmp_mis))
    print("---------------------------------------------"+
              "-----------------------------------------")
    comparison_boxplot(tmp_data, tmp_data_c, col)


# In[19]:


'''众数填补操作对数据分布产生的变化不明显。这可能和数据本身的分布情况有关。
接下来是根据属性相似性进行数据异常值处理。使用非线性模型对数据进行回归预测。'''
print(data_num.corr())


# In[20]:


'''根据相关系数，可判断visit和visitor数据之间存在强相关关系。
但visit和visitor的缺失数据处是重合的。考虑将popularity的数据进行处理后作为新的数据列
从而进行后续处理。'''
#data_num_com = origin_cbg[['popularity_by_hour','popularity_by_day']]
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import PolynomialFeatures

list_hour_sum = []
list_day_sum = []

for it in data_num_com['popularity_by_hour']:
    it_list = eval(it)
    res = 0
    for n in it_list:
        res = res + int(n)
    list_hour_sum.append(res)
    
for it in data_num_com['popularity_by_day']:
    it_dict = eval(it)
    res = 0
    for key,value in it_dict.items():
        res = res + int(value)
    list_day_sum.append(res)

source = {'popularity_by_hour':list_hour_sum,
          'popularity_by_day':list_day_sum}
source_df = pd.DataFrame(source)

train = pd.concat([data_num, source_df],axis=1).dropna(axis=0,how='any',inplace = False)
print(train.columns)
print(train.corr())


# In[21]:


'''分析发现新增数据依旧无法对缺失值进行预测，原因同上。
故这部分处理主要对distance_from_home进行预测。
训练数据为raw_visitor_count（相关性更强一点）'''

x_data, y_data = [], []

for i in zip(train['raw_visitor_count'], train['distance_from_home']):
    x_data.append([i[0]])
    y_data.append([i[1]])

poly_reg = PolynomialFeatures(degree=2)
x_poly = poly_reg.fit_transform(x_data)

linear_reg = LinearRegression()
linear_reg.fit(x_poly, y_data)


# In[22]:


ori_data = data_num[['raw_visitor_count','distance_from_home']].copy(deep = True)

for i in range(len(ori_data)):
    if( pd.isnull(ori_data.iloc[i, 1]) == True and pd.isnull(ori_data.iloc[i, 0]) == False ):
        x = ori_data.iloc[i, 0]
        x_poly = poly_reg.fit_transform([[x]])
        y_pred = linear_reg.predict(x_poly)
        ori_data.loc[i,'distance_from_home'] = y_pred[0,0]
        #print(y_pred[0,0])
        #print(t.loc[i,'price'])

des_before = data_num['distance_from_home'].describe()
mis_before = data_num['distance_from_home'].isnull().sum()

des_after = ori_data['distance_from_home'].describe()
mis_after = ori_data['distance_from_home'].isnull().sum()

print(des_before)
print("Missing Before: "+ str(mis_before))
print("---------------------------------------------"+
              "-----------------------------------------")
print(des_after)
print("Missing After: "+ str(mis_after))
comparison_boxplot(data_num['distance_from_home'], ori_data['distance_from_home'],'distance_from_home')


# In[23]:


'''
从结果上来看，用相似性对数据进行修复并没有将数据分布进行改变，可能是
数据缺失的量“微不足道”的缘故。
最后根据数据对象之间的相似性来填补缺失值。经过分析，决定对
related_same_day_brand，related_same_month_brand，top_brands三行作出处理。
分析中可知，若某个brand存在于related_same_day_brand和related_same_month_brand，则
这个brand有极大概率存在于top_brands中。根据这个规律，对top_brands进行异常值填充。
'''
# data_num_brand = origin_cbg[['related_same_day_brand','related_same_month_brand','top_brands']]

fill_brand = data_num_brand.copy(deep = True)

for i in range(len(fill_brand)):
    if(fill_brand.iloc[i,0] == '[]'or fill_brand.iloc[i,1]=='[]' or fill_brand.iloc[i,2]):
        continue
    x = eval(fill_brand.iloc[i,0])
    y = eval(fill_brand.iloc[i,1])
    x_y = [t for t in x if t in y]
    fill_brand.loc[i,'top_brands'] = str(x_y)

ori_li = []
now_li = []
for it in data_num_brand['top_brands']:
    tmp = eval(it)
    for itm in tmp:
        ori_li.append(itm)
for it in fill_brand['top_brands']:
    tmp = eval(it)
    for itm in tmp:
        now_li.append(itm)

df = {'origin_top_brands':ori_li,
      'now_top_brands':now_li}

df = pd.DataFrame(df)
old_count = df['origin_top_brands'].value_counts()[:10]
new_count = df['now_top_brands'].value_counts()[:10]
comparison_figure(old_count, new_count, 'top_brands')


# In[ ]:


'''可能是异常值太少，或是填充依据太少的缘故，数据前后分布没有较大变化。'''

