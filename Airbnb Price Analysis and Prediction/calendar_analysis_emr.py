#!/usr/bin/env python
# coding: utf-8

# In[3]:


from pyspark.sql import SparkSession
from pyspark.sql.functions import *
from pyspark.sql.types import *
from datetime import date
import datetime
import calendar
import holidays
usa_holidays = holidays.US()


# ## Chnages the bellow path to run locally and in EMR
# ### Warning : Calendar data is having more than 1 Million rows

# In[5]:


#Uncomment this to run locally
# calendar_file = "../data/calendar.csv"
# out_bucket = "../output_csv/calendar_data/"

#Uncomment this to run on EMR
calendar_file = "s3://com.bigdatasdsu.airbnb/calendar.csv"
out_bucket = "s3://com.bigdatasdsu.airbnb/calendar_data/"


# In[2]:


def write_to_file(data_frame,out_file):
    out_file_path = out_bucket + out_file
    data_frame.coalesce(1).write.option("header", "true").mode('overwrite').format('csv').save(out_file_path)


# In[6]:


spark = SparkSession.builder.appName("AirBnb Seasonal Data Analysis").getOrCreate()

calendar_df = spark.read.csv(calendar_file,header=True,sep=",")
calendar_df = calendar_df.fillna(0)
calendar_df = calendar_df.select('listing_id','date','available','price')
calendar_df = calendar_df.withColumn('price', regexp_replace('price', '[\$,]', ''))
calendar_df = calendar_df.withColumn('price', regexp_replace('price', '[\$,]', ''))
calendar_df = calendar_df.filter('price > 0')


# In[7]:


calendar_df = calendar_df.withColumn("dayofmonth", dayofmonth(col("date")))            .withColumn("month", month(col("date")))            .withColumn("year", year(col("date")))
calendar_df = calendar_df.filter(calendar_df['year'] < 2020)


# In[8]:



year_price_df = calendar_df.groupBy(['year','month'])                            .agg({"price": "avg"})                            .withColumnRenamed('avg(price)','average_price')                            .orderBy('average_price', ascending=False)


# In[ ]:


## 


# In[9]:



year_price_df = year_price_df.withColumn('year-month', concat(col('year'),lit('-'), col('month')))
write_to_file(year_price_df,"year_month_avg_price_1")
year_price_df.show(5)


# In[8]:



def get_day_name(row):
    date_obj = datetime.date(int(row['year']),int(row['month']),int(row['dayofmonth']))
    return str(calendar.day_name[date_obj.weekday()])

def get_holiday_name(row):
    date_obj = datetime.date(int(row['year']),int(row['month']),int(row['dayofmonth']))
    holiday_name = usa_holidays.get(date_obj)
    return holiday_name

def is_holiday(row):
    date_obj = datetime.date(int(row['year']),int(row['month']),int(row['dayofmonth']))
    value = date_obj in usa_holidays
    return value
    

day_name_setter = udf(lambda row: get_day_name(row) , StringType())
holiday_name_setter = udf(lambda row: get_holiday_name(row) , StringType())
holiday_setter = udf(lambda row: is_holiday(row) , StringType())

calendar_extra_df = calendar_df.withColumn("day_name", day_name_setter(struct([calendar_df[x] for x in calendar_df.columns])))
calendar_extra_df = calendar_extra_df.withColumn("holiday_name",holiday_name_setter(struct([calendar_df[x] for x in calendar_df.columns])))
calendar_extra_df = calendar_extra_df.withColumn("holiday",holiday_setter(struct([calendar_df[x] for x in calendar_df.columns])))


# In[8]:



day_df = calendar_extra_df.groupby('day_name').agg({"price": "avg"})                            .withColumnRenamed('avg(price)','average_price')                            .orderBy('average_price', ascending=False)
write_to_file(day_df,"day_avg_price_2")
day_df.show()


# In[9]:



holiday_df = calendar_extra_df.groupby('holiday_name').agg({"price": "avg"})                            .withColumnRenamed('avg(price)','average_price')                            .orderBy('average_price', ascending=False)
write_to_file(holiday_df,"holiday_avg_price_3")
holiday_df.show()


# In[ ]:


holiday_listing_df = calendar_extra_df.groupby('holiday_name').agg({"listing_id": "count"})                                        .withColumnRenamed('count(listing_id)','total_listing')                                        .orderBy('total_listing', ascending=False)
write_to_file(holiday_listing_df,"holiday_total_listings_4")
holiday_listing_df.show(5)


# In[39]:


data_2019 = calendar_df.filter(calendar_extra_df['year']==2019)
data_2019_7 = data_2019.filter(calendar_extra_df['month']==7)
data_2019_7_avg = data_2019_7.groupby(['year','month','dayofmonth']).agg({"price": "avg"})                            .withColumnRenamed('avg(price)','average_price')                            .orderBy('dayofmonth', ascending=True)


data_2019_12 = data_2019.filter(calendar_extra_df['month']==12)
data_2019_12_avg = data_2019_12.groupby(['year','month','dayofmonth']).agg({"price": "avg"})                            .withColumnRenamed('avg(price)','average_price')                            .orderBy('dayofmonth', ascending=True)


# In[40]:


data_2019_7_avg.show(30)
write_to_file(data_2019_7_avg,"avg_2019_july_5")


# In[41]:


data_2019_12_avg.show(30)
write_to_file(data_2019_12_avg,"avg_2019_dec_6")

