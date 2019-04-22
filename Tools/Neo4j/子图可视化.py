# -*- coding: utf-8 -*-

#-*- conding:utf-8 -*-
import pandas as pd
from py2neo import Node,Relationship,Graph


###连接neo4j
g = Graph("http://localhost:7474")
#清空neo4j
g.run("MATCH (n) OPTIONAL MATCH (n)-[r]-()DELETE n,r")


###选择数据集
database=2
### 选择集群号
clusterNo=2

### 获取实体信息。
rootpath="F:\\GitHub\\Knowledge_Graph_Partitioning\\Database\\Database_"+str(database)+"\\Cluster\\"

f = open(rootpath+"Cluster"+str(clusterNo)+"_Node.csv",encoding='utf-8')
df = pd.read_csv(f)
# print(df['head'].unique)
#a = df['head'].value_counts()
data01 = df['Name'].drop_duplicates( keep='first', inplace=False)
data01=data01.to_frame()

r = open(rootpath+"Cluster"+str(clusterNo)+"_spo.csv", encoding='utf-8')
data02 = pd.read_csv(r)


###上传到neo4j
###上传节点
for i in range(len(data01)):
    n=data01['Name'][i].replace('\"','')
    temp= Node("Person",name=n)  
    #print(n)
    g.create(temp)
###上传关系
for i in range(len(data02)):
   # temp = Relationship(g.find_one(label="Person",property_key='name',property_value=data02["Head"][i]),data02['Relation'][i],
   #                     g.find_one(label="Person", property_key='name', property_value=data02["Tail"][i]))

   object=g.find_one(label="Person",property_key='name',property_value=data02["Head"][i])
   subject=g.find_one(label="Person",property_key='name',property_value=data02["Tail"][i])
   if str(subject)!="None" and str(object)!="None":
       temp = Relationship(subject,data02['Relation'][i],object)
       #print(temp)
       g.create(temp)           
       
       
       
 #  if temp ==str("None")
       
   #g.create(temp)