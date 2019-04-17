# -*- coding: utf-8 -*-

# -*- coding: utf-8 -*-

#-*- conding:utf-8 -*-
import pandas as pd
from py2neo import Node,Relationship,Graph


###连接neo4j
g = Graph("http://localhost:7474")
#清空neo4j
g.run("MATCH (n) OPTIONAL MATCH (n)-[r]-()DELETE n,r")

rootpath="F:\\database\\红楼梦\\"
### 获取实体信息。
f = open(rootpath+"triples.csv",encoding='utf-8')
df = pd.read_csv(f)
# print(df['head'].unique)
#a = df['head'].value_counts()
a = df['head']
b=df['tail']
frames = [a,b]
result = pd.concat(frames)
result=result.drop_duplicates( keep='first', inplace=False)
result = result.rename(columns={'0': 'node'})
result.to_csv("F:\\database\\红楼梦\\node.csv",header=1)

###上传到neo4j

n = open(rootpath+"node.csv" , encoding='utf-8')
r = open(rootpath+"triples.csv", encoding='utf-8')
data01 = pd.read_csv(n)
data02 = pd.read_csv(r)


###上传节点
for i in range(len(data01)):
    temp= Node("Person",name=data01['0'][i])
    g.create(temp)

    

###上传关系
for i in range(len(data02)):
    object=g.find_one(label="Person",property_key='name',property_value=data02["head"][i])
    subject=g.find_one(label="Person",property_key='name',property_value=data02["tail"][i])        
    temp = Relationship(subject,data02['label'][i],object)
    g.create(temp)