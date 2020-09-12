# 使用流程

## 一.项目结构
#### **gbdpcloud-provider    ----------   放置项目接口，service，mapper**
#### **gbdpcloud-provider-api    ----------   放置项目entity，feign接口，熔断默认返回**
#### **逻辑关系上，provider-api是微服务内部调用所使用的对象，同时同一个微服务provider项目需要依赖自己的provider-api**

## 二.导入项目
#### **项目中所使用到的jar包放在目录项目根目录的jars文件夹下，需要在ide中手动导入**

## 三.新建项目
#### **参考demo项目结构，将项目按其结构分为provider和provider-api两部分。分别放在两个module下,**
#### **config包下的配置可以直接copy，其他类按照demo项目示例实现**