#!/usr/bin/env python
# coding: utf-8

# In[163]:


from sklearn.decomposition import PCA
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

from pyod.models.cof import COF
from pyod.models.knn import KNN 
from pyod.models.mcd import MCD
from pyod.models.lof import LOF
from pyod.models.iforest import IForest


# 处理的第一个数据集是wine_bennchmark
# 将benchmark中的数据分为原数据和noise，去重后根据需要使用数据集对模型进行训练。

# In[164]:


def show_scatter(clf_name, df, y_train_pred, pos):
    plt.figure(clf_name+' (Blue:Inliers Red: Outliers)')
    plt.figure(figsize=(10,8))
    plt.scatter(np.array(df)[:pos,0],np.array(df)[:pos,1],s = 10,c='b',alpha = 0.1)
    outliers = []
    for i in range(pos):
        if(y_train_pred[i]==1):
            outliers.append([df.iloc[i,0],df.iloc[i,1]])
    outliers = pd.DataFrame(outliers)  
    plt.scatter(outliers[0], outliers[1],s = 30,c='r',alpha = 0.2)
    plt.title(clf_name+' (Blue:Inliers Red: Outliers)')


# In[165]:


from os import walk
df = []
df_noise = []

for i,_,j in walk("C:/Users/Administrator/Documents/DM_dataset/wine_benchmarks/wine/benchmarks"):
    for f in j:
        tp = pd.read_csv(i + '/' + f )
        if(tp.shape[1] == 17):
            df.append(tp)
        elif(tp.shape[1] == 50):
            df_noise.append(tp)

dfALL_wine = pd.concat(df)
dfALL_noise_wine = pd.concat(df_noise)

print(dfALL_wine.columns)
print(dfALL_noise_wine.columns)


# In[166]:


fin_data = pd.read_csv('C:/Users/Administrator/Documents/DM_dataset/wine_benchmarks/wine/meta_data/wine.original.csv')
fin_origin = fin_data[['quality','fixed.acidity','volatile.acidity','citric.acid','residual.sugar','chlorides','free.sulfur.dioxide','total.sulfur.dioxide','density','pH','sulphates','alcohol']]
print(fin_origin.shape)

pca=PCA(n_components=2)     #加载PCA算法，设置降维后主成分数目为2
tp = pca.fit_transform(fin_origin)#对样本进行降维
df_fin = pd.DataFrame(tp)
df_fin.columns = ['0','1']
print(df_fin.shape)


# 这一步是对数据集上所有数据进行检测时使用数据包中提供的original数据集并进行处理。
# 对数据维度降至2维，为了方便后续的可视化展示。
# 下一步，提取数据集中的数值属性，作为模型的训练数据。
# 由于是降维后的可视化输出，因此检测结果可能与直观有所出入。

# In[167]:


origin_all = dfALL_wine[['fixed.acidity','volatile.acidity','citric.acid','residual.sugar','chlorides','free.sulfur.dioxide','total.sulfur.dioxide','density','pH','sulphates','alcohol']]
print(origin_all.shape)
new_origin_all = origin_all.drop_duplicates(subset=['fixed.acidity','volatile.acidity','citric.acid','residual.sugar','chlorides','free.sulfur.dioxide','total.sulfur.dioxide','density','pH','sulphates','alcohol'
], keep='first')
print(new_origin_all.shape)

origin_noise_all = dfALL_noise_wine[['fixed.acidity','volatile.acidity','citric.acid','residual.sugar','chlorides','free.sulfur.dioxide','total.sulfur.dioxide','density','pH','sulphates','alcohol']]
print(origin_noise_all.shape)
new_origin_noise_all = origin_noise_all.drop_duplicates(subset=['fixed.acidity','volatile.acidity','citric.acid','residual.sugar','chlorides','free.sulfur.dioxide','total.sulfur.dioxide','density','pH','sulphates','alcohol'
], keep='first')
print(new_origin_noise_all.shape)


# In[168]:


pca=PCA(n_components=2)     #加载PCA算法，设置降维后主成分数目为2
X_train = pca.fit_transform(new_origin_all)#对样本进行降维
df = pd.DataFrame(X_train)
df.columns = ['0','1']

pos = (int(len(df)/10))*9 #对数据集进行9/1划分


# 接下来，选定训练集，选取部分pyOD算法库中的算法，对数据集上的异常点进行检测并输出可视化图像。
# 选定的算法是KNN,COF,LOF,IForest和MCD。
# 根据得到的分类标签，对降维后的数据进行标记并在图像中进行展示。

# In[169]:


# 训练一个kNN检测器
clf_name = 'kNN'
clf = KNN() # 初始化检测器
clf.fit(new_origin_all[:pos]) # 使用训练集训练检测器clf

# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(new_origin_all[pos:]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(new_origin_all[pos:]) # 返回未知数据上的异常值

show_scatter(clf_name, df, y_train_pred, pos)


# In[170]:


clf_name = 'COF'
clf = COF(n_neighbors=30)
clf.fit(new_origin_all[:pos])

# get the prediction labels and outlier scores of the training data
y_train_pred = clf.labels_  # binary labels (0: inliers, 1: outliers)
y_train_scores = clf.decision_scores_  # raw outlier scores

# get the prediction on the test data
y_test_pred = clf.predict(new_origin_all[pos:])  # outlier labels (0 or 1)
y_test_scores = clf.decision_function(new_origin_all[pos:])  # outlier scores

show_scatter(clf_name, df, y_train_pred, pos)


# In[171]:


# train LOF detector
clf_name = 'LOF'
clf = LOF()
clf.fit(new_origin_all[:pos])

# get the prediction labels and outlier scores of the training data
y_train_pred = clf.labels_  # binary labels (0: inliers, 1: outliers)
y_train_scores = clf.decision_scores_  # raw outlier scores

# get the prediction on the test data
y_test_pred = clf.predict(new_origin_all[pos:])  # outlier labels (0 or 1)
y_test_scores = clf.decision_function(new_origin_all[pos:])  # outlier scores

show_scatter(clf_name, df, y_train_pred, pos)


# In[172]:


clf_name = 'IForest'
clf = IForest()
clf.fit(new_origin_all[:pos])

# get the prediction labels and outlier scores of the training data
y_train_pred = clf.labels_  # binary labels (0: inliers, 1: outliers)
y_train_scores = clf.decision_scores_  # raw outlier scores
# get the prediction on the test data
y_test_pred = clf.predict(new_origin_all[pos:])  # outlier labels (0 or 1)
y_test_scores = clf.decision_function(new_origin_all[pos:])  # outlier scores

show_scatter(clf_name, df, y_train_pred, pos)


# In[173]:


# train MCD detector
clf_name = 'MCD'
clf = MCD()
clf.fit(new_origin_all[:pos])

# get the prediction labels and outlier scores of the training data
y_train_pred = clf.labels_  # binary labels (0: inliers, 1: outliers)
y_train_scores = clf.decision_scores_  # raw outlier scores

# get the prediction on the test data
y_test_pred = clf.predict(new_origin_all[pos:])  # outlier labels (0 or 1)
y_test_scores = clf.decision_function(new_origin_all[pos:])  # outlier scores

show_scatter(clf_name, df, y_train_pred, pos)


# In[174]:


# train MCD detector
clf_name = 'MCD'
clf = MCD()
clf.fit(df_fin[:pos])

# get the prediction labels and outlier scores of the training data
y_train_pred = clf.labels_  # binary labels (0: inliers, 1: outliers)
y_train_scores = clf.decision_scores_  # raw outlier scores

# get the prediction on the test data
y_test_pred = clf.predict(df_fin[pos:])  # outlier labels (0 or 1)
y_test_scores = clf.decision_function(df_fin[pos:])  # outlier scores

train = df_fin[:pos]
train['label'] = y_train_pred
print(train[:50])

show_scatter(clf_name, df_fin, y_train_pred, pos)


# 输出了对前50个原始数据的检测结果，并输出MCD检测在原始数据训练集上的结果。
# 对降维后的数据集进行异常点检测，其可视化可能更加直观。如图所示。

# In[175]:


# train LOF detector
clf_name = 'LOF'
clf = LOF()
clf.fit(df[:pos])

# get the prediction labels and outlier scores of the training data
y_train_pred = clf.labels_  # binary labels (0: inliers, 1: outliers)
y_train_scores = clf.decision_scores_  # raw outlier scores

# get the prediction on the test data
y_test_pred = clf.predict(df[pos:])  # outlier labels (0 or 1)
y_test_scores = clf.decision_function(df[pos:])  # outlier scores

train = df[:pos]
train['label'] = y_train_pred
print(train[:50])

show_scatter(clf_name, df, y_train_pred, pos)


# 以相似的方法，处理处理第二个数据集。第二个数据集选择skin_benchmarks。

# In[138]:


df = []
df_noise = []

for i,_,j in walk("C:/Users/Administrator/Documents/DM_dataset/skin_benchmarks/skin/benchmarks"):
    for f in j:
        tp = pd.read_csv(i + '/' + f )
        if(tp.shape[1] == 9):
            df.append(tp)
        elif(tp.shape[1] == 10):
            df_noise.append(tp)

dfALL_skin = pd.concat(df)
dfALL_noise_skin = pd.concat(df_noise)

print(dfALL_skin.columns)
print(dfALL_noise_skin.columns)

fin_data = pd.read_csv('C:/Users/Administrator/Documents/DM_dataset/skin_benchmarks/skin/meta_data/skin.original.csv')
fin_origin = fin_data[['R','G','B']]
print(fin_origin.shape)

pca=PCA(n_components=2)     #加载PCA算法，设置降维后主成分数目为2
tp = pca.fit_transform(fin_origin)#对样本进行降维
df_fin_skin = pd.DataFrame(tp)

df_fin_skin.columns = ['0','1']#original数据训练集
df_fin_skin = df_fin_skin.drop_duplicates(subset=['0','1'],keep='first')

print(df_fin_skin.shape)
print("___________________________________________________")

origin_all = dfALL_skin[['R','G','B']]
print(origin_all.shape)

new_origin_all = origin_all.drop_duplicates(subset=['R','G','B'],keep='first')

print(new_origin_all.shape)

origin_noise_all = dfALL_noise_skin[['R','G','B']]
print(origin_noise_all.shape)
new_origin_noise_all = origin_noise_all.drop_duplicates(subset=['R','G','B'], keep='first')
print(new_origin_noise_all.shape)


# In[150]:


pca=PCA(n_components=2)     #加载PCA算法，设置降维后主成分数目为2
X_train = pca.fit_transform(new_origin_all)#对样本进行降维
df = pd.DataFrame(X_train)
df.columns = ['0','1']
print(df.shape)
pos = (int(len(df)/10))*3 #对数据集进行划分


# 数据集过于庞大，故削减了可视化图表中的散点数量。

# In[155]:


# 训练一个kNN检测器
clf_name = 'kNN'
clf = KNN() # 初始化检测器
clf.fit(new_origin_all[:pos]) # 使用训练集训练检测器clf

# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(new_origin_all[pos:2*pos]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(new_origin_all[pos:2*pos]) # 返回未知数据上的异常值

show_scatter(clf_name, df, y_train_pred, pos)


# In[156]:


# 训练一个LOF检测器
clf_name = 'LOF'
clf = LOF() # 初始化检测器
clf.fit(new_origin_all[:pos]) # 使用训练集训练检测器clf

# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(new_origin_all[pos:2*pos]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(new_origin_all[pos:2*pos]) # 返回未知数据上的异常值

show_scatter(clf_name, df, y_train_pred, pos)


# In[157]:


# 训练一个MCD检测器
clf_name = 'MCD'
clf = MCD() # 初始化检测器
clf.fit(new_origin_all[:pos]) # 使用训练集训练检测器clf

# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(new_origin_all[pos:2*pos]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(new_origin_all[pos:2*pos]) # 返回未知数据上的异常值

show_scatter(clf_name, df, y_train_pred, pos)


# In[159]:


# 训练一个IForest检测器
clf_name = 'IForest'
clf = IForest() # 初始化检测器
clf.fit(new_origin_all[:pos]) # 使用训练集训练检测器clf

# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(new_origin_all[pos:2*pos]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(new_origin_all[pos:2*pos]) # 返回未知数据上的异常值

show_scatter(clf_name, df, y_train_pred, pos)


# In[ ]:


最后用original数据集，观察先降维后检测的效果。


# In[161]:


# 训练一个KNN检测器
clf_name = 'KNN'
clf = KNN() # 初始化检测器
clf.fit(df_fin_skin[:pos]) # 使用训练集训练检测器clf，这次使用的数据集是original
print(df_fin_skin.shape)
# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(df_fin_skin[pos:2*pos]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(df_fin_skin[pos:2*pos]) # 返回未知数据上的异常值

show_scatter(clf_name, df_fin_skin, y_train_pred, pos)

train = df_fin_skin[:pos]
train['label'] = y_train_pred
print(train[:35])


# In[162]:


# 训练一个LOF检测器
clf_name = 'LOF'
clf = LOF() # 初始化检测器
clf.fit(df_fin_skin[:pos]) # 使用训练集训练检测器clf，这次使用的数据集是original
print(df_fin_skin.shape)
# 返回训练数据X_train上的异常标签和异常分值
y_train_pred = clf.labels_ # 返回训练数据上的分类标签 (0: 正常值, 1: 异常值)
y_train_scores = clf.decision_scores_ # 返回训练数据上的异常值 (分值越大越异常)

# 用训练好的clf来预测未知数据中的异常值
y_test_pred = clf.predict(df_fin_skin[pos:2*pos]) # 返回未知数据上的分类标签 (0: 正常值, 1: 异常值) 
y_test_scores = clf.decision_function(df_fin_skin[pos:2*pos]) # 返回未知数据上的异常值

show_scatter(clf_name, df_fin_skin, y_train_pred, pos)

train = df_fin_skin[:pos]
train['label'] = y_train_pred
print(train[:35])


# 从图像中可以看出，可以看到KNN算法和LOF算法在二维的数据上还是很好地识别出了异常点。
