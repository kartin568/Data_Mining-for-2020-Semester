#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from matplotlib import pyplot as plt

req_data = pd.read_csv('winemag-data_first150k.csv')
print(req_data.shape)


# In[2]:


fin_data = req_data.iloc[:,1:]
print(fin_data.shape)


# In[3]:


column = list(fin_data)
column_num = len(column)
print(column_num)


# In[4]:


print(fin_data.dtypes)#输出各列的名称及数据类型


# In[5]:


def comparison_figure(data_before, data_after, name):
    plt.figure(figsize=(15,6))
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


# In[6]:


def draw_Histograms(data, name, num):
    new_data = data.dropna(axis=0, how='any', inplace=False)
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


# In[7]:


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


# In[9]:


import matplotlib.pyplot as plt

cols = fin_data.columns
color = dict(boxes='DarkGreen', whiskers='DarkOrange',medians='DarkBlue', caps='Red')

for col in cols:#object对象输出计数最多的四位，数值属性对象输出5数概括以及确实值
    if str(fin_data[col].dtype) == 'float64':
        describe_data = fin_data[col].describe()
        print(describe_data)
        miss_count = fin_data[col].isnull().sum()
        print("Missing: %d "%(miss_count))
        
        fig,axes = plt.subplots()
        fin_data[col].plot(kind='box',ax=axes, color=color, sym='r+') 
        axes.set_ylabel(str(col))
        fig.savefig(str(col) + '.png') 
        
        fig,axes = plt.subplots()
        draw_Histograms(fin_data[col], str(col), 50)
        print("------------------------------------"+
              "--------------------------------------------------")
        
    if str(fin_data[col].dtype) == 'int64':
        describe_data = fin_data[col].describe()
        print(describe_data)
        miss_count = fin_data[col].isnull().sum()
        print("Missing: %d "%(miss_count))
        
        fig,axes = plt.subplots()
        fin_data[col].plot(kind='box',ax=axes, color=color, sym='r+') 
        axes.set_ylabel(str(col))
        fig.savefig(str(col) + '.png')
        fig,axes = plt.subplots()
        draw_Histograms(fin_data[col], str(col), 50)
        print("------------------------------------"+
              "--------------------------------------------------")
        
    if str(fin_data[col].dtype) == 'object':
        tmp_counts = fin_data[col].value_counts()
        show_counts = tmp_counts[:10]
        print(tmp_counts[:10])
        miss_count = fin_data[col].isnull().sum()
        print("Missing: %d "%(miss_count))
        
        fig,axes = plt.subplots()
        plt.ylabel(str(col)+' frequency')
        show_counts.plot.bar()
        plt.show()
        print("------------------------------------"+
              "--------------------------------------------------")


# In[10]:


draw_Histograms(fin_data[u'points'], 'points', 20)


# In[11]:


draw_Histograms(fin_data[u'price'], 'price', 200)


# In[12]:


'''
数据集一共有11列，其中description，points，variety，winery没有数据缺失，其余列数据项均有数据缺失。
分析数据原因：可能是收集数据时存在遗漏或酒类本身并没有过多的产区，致使部分酒类的产国、单价、其他产区等数据缺失，
而品酒师可能是因为部分酒类并没有品酒师
'''
copy_fin_data = fin_data
for col in cols:#对比，剔除处理，重新观察五数分布,标称型观察条形图变化
    if str(fin_data[col].dtype) == 'float64' or str(fin_data[col].dtype) == 'int64':
        describe_data = fin_data[col].describe()
        print(describe_data)
        miss_count = fin_data[col].isnull().sum()
        print("/*/*/*/* Length before: "+ str(len(fin_data[col])))
        if(miss_count == 0):
            print("--------------------------------------------------------------------------------------")
            continue
        print("**********************************   Processed   ************************************")
        processed_data = fin_data[col].dropna(axis=0, how='any', inplace=False)
        print("/*/*/*/* Length after: "+ str(len(processed_data)))
        processed_describe_data = processed_data.describe()
        print(processed_describe_data)
        miss_count = processed_describe_data.isnull().sum()
        print("Missing: %d "%(miss_count))
        
#**********************************************************************************************        
        
        comparison_boxplot(fin_data[col], processed_data, str(col))

        print("--------------------------------------------------------------------------------------")
    
    if str(fin_data[col].dtype) == 'object':#非数值属性输出饼状图观察变化
        tmp_counts = fin_data[col].value_counts()
        print(tmp_counts[:10])
        miss_count = fin_data[col].isnull().sum()
        print("Missing: %d "%(miss_count))
        print("/*/*/*/* Length before: "+ str(len(fin_data[col])))
        if(miss_count == 0):
            print("--------------------------------------------------------------------------------------")
            continue
        print("**********************************   Processed   ************************************")
        processed = fin_data[col].dropna(axis=0, how='any', inplace=False)
       
        processed_data = processed.value_counts()
        print(processed_data[:10])
        p = processed_data[:10]
        miss_count = processed.isnull().sum()
        print("/*/*/*/* Length after: "+ str(len(processed)))
        print("Missing: %d "%(miss_count))
        
        before = tmp_counts[:10]
        after = processed_data[:10]
        comparison_figure(before, after, str(col))

#**********************************************************************************************        

        
        print("--------------------------------------------------------------------------------------")
        
            


# In[13]:


'''
直接剔除数据，对整体分布来说并没有什么明显的变化——或者说python库中本身的机制就在计算时将数据剔除，亦或是异常数据点远远小于数据量本身
下面观察用最高频率值补全的策略给数据分布带来的变化
'''
copy =  req_data.iloc[:,1:]
for col in cols:#众数补全
    
    tmp1 = copy[col]
    value = tmp1.value_counts()
    tmp2 = tmp1.fillna(value.index[0])
    
    if str(fin_data[col].dtype) == 'float64' or str(fin_data[col].dtype) == 'int64':
        origin = fin_data[col].describe()
        print(origin)
        miss_count = fin_data[col].isnull().sum()
        print("Missing: %d "%(miss_count))
        if(miss_count == 0):
            print("------------------------------------------------"+
                  "--------------------------------------")
            continue
        print("**********************************   Processed   ************************************")
        
        processed = tmp2.describe()
        print(processed)
        miss_count = tmp2.isnull().sum()
        print("Missing: %d "%(miss_count))
        
#**********************************************************************************************        
        
        comparison_boxplot(fin_data[col], processed, str(col))

        print("---------------------------------------------"+
              "-----------------------------------------")
    
    if str(fin_data[col].dtype) == 'object':#非数值属性输出直方图观察变化
        o = fin_data[col].value_counts()
        origin = o[:10]
        print(origin)
        miss_count = fin_data[col].isnull().sum()
        print("Missing: %d "%(miss_count))
        if(miss_count == 0):
            print("-----------------------------------"+
                  "---------------------------------------------------")
            continue
        print("**********************************   Processed   ***"+
              "*********************************")
        p = tmp2.value_counts()
        processed = p[:10]
        print(processed)
        miss_count = processed_describe_data.isnull().sum()
        print("Missing: %d "%(miss_count))
        
        comparison_figure(origin, processed, str(col))

#**********************************************************************************************        
        print("-----------------------------------"+
                  "---------------------------------------------------")


# In[14]:


'''
对数值属性而言，添加频数最高的策略可以消灭偏离点，改善数据的分布情况；对标称属性而言，
当异常值较多时，策略会增大众数的频数来改变数据的分布。
下面用属性的相关关系来填补缺失值，重新观察数据的分布情况。
首先使用非线性模型对数据进行回归预测

points没有缺失值，所以我们处理price
'''

from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import PolynomialFeatures

data = fin_data[['points','price']]

train = data.dropna(axis=0,how='any',inplace = False)

x_data, y_data = [], []

for i in zip(train['points'], train['price']):
    x_data.append([i[0]])
    y_data.append([i[1]])

poly_reg = PolynomialFeatures(degree=2)
x_poly = poly_reg.fit_transform(x_data)

linear_reg = LinearRegression()
linear_reg.fit(x_poly, y_data)


# In[15]:


tmpp = data.copy(deep=True)

for i in range(len(tmpp)):
    if( pd.isnull(tmpp.iloc[i, 1])==True ):
        x = tmpp.iloc[i, 0]
        x_poly = poly_reg.fit_transform([[x]])
        y_pred = linear_reg.predict(x_poly)
        tmpp.loc[i,'price'] = y_pred[0,0]
        #print(y_pred[0,0])
        #print(t.loc[i,'price'])
miss_count = tmpp['price'].isnull().sum()
print(miss_count)
print(data['price'].isnull().sum())


# In[16]:


print("Origin Missing: "+ str(data['price'].isnull().sum()))
print("Now Missing: "+str(tmpp['price'].isnull().sum()))
print("\nProcessed Result: ")
comparison_boxplot(data['price'], tmpp['price'], 'price')


# In[17]:


'''
从结果上看根据相似性处理缺失值并没有改变数值属性的数据分布。
最后根据数据对象的相似性，选取variety，winery相似的数据的平均值对price数据进行填充，并观察
分布的变化。
'''
data_new = fin_data[['price','variety','winery']]

tmp = data_new.copy(deep=True)
grouped_tmp = tmp.groupby(['variety'])
mean_data = grouped_tmp.agg(np.mean)
#p = mean_data[mean_data.index == 'Pinot Noir']

tg = data_new.copy(deep=True)

for i in range(len(tg)):
    if( pd.isnull(tg.iloc[i, 0])==True ):
        x = tg.iloc[i, 1]
        p = mean_data[mean_data.index == str(x)]
        pri = p['price'].iloc[0]
        tg.iloc[i,0]=pri

miss_count = tg['price'].isnull().sum()
print(miss_count)


# In[18]:


print("Origin Missing: "+ str(data_new['price'].isnull().sum()))
print("Now Missing: "+str(tg['price'].isnull().sum()))
print("\nProcessed Result: ")
comparison_boxplot(data['price'], tmp['price'], 'price grouped by variety')


# In[19]:


'''
观察分布，根据数据对象的相似性的策略进行填充消异的数据集的分布也没有发生明显变化。
'''


# In[ ]:




