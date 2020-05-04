# Data_Mining-for-2020-Semester

DM_W4_CBG.py 和 DM_W4_Wine.py 分别处理的是 cbg_patterns.csv 和 winemag-data_first150k.csv

数据集链接
https://www.kaggle.com/safegraph/visit-patterns-by-census-block-group
https://www.kaggle.com/zynicide/wine-reviews

对CBG数据集进行分析，可得到：
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

对WINE数据进行分析，可得到其数据类型：
country         object
description     object
designation     object
points           int64
price          float64
province        object
region_1        object
region_2        object
variety         object
winery          object

后续处理方式在py文件中有说明。
