#!/usr/bin/env python
# coding: utf-8

# In[4]:


from pyspark.sql import SparkSession
from pyspark.sql.types import *
from pyspark.sql.functions import *
import pandas as pd


# ## Chnages the bellow path to run locally and in EMR

# In[2]:


#Uncomment this to run locally
# listings_data = "../data/listings.csv"
# out_bucket = "../output_csv/listing_data/"

#Uncomment this to run on EMR
listings_data = "s3://com.bigdatasdsu.airbnb/listings.csv"
out_bucket = "s3://com.bigdatasdsu.airbnb/listing_data/"


# In[6]:


spark = SparkSession.builder            .appName("AirBnB Listings Data Analysis")             .getOrCreate() 

pandas_df=pd.read_csv(listings_data)    

columns_required = ['host_response_time',
                    'host_response_rate',
                    'host_is_superhost',
                    'instant_bookable', 
                    'host_identity_verified',
                    'extra_people',
                    'host_has_profile_pic',
                    'guests_included',
                    'security_deposit',
                    'accommodates',
                    'bathrooms',
                    'bedrooms',
                    'beds',
                    'cleaning_fee',
                    'price',
                    'room_type',
                    'property_type',
                    'neighbourhood_cleansed',
                    'review_scores_rating',
                    'reviews_per_month']


pandas_df=pandas_df[columns_required]


# In[7]:


define_schema=StructType([
                        StructField("host_response_time", StringType(), True)\
                       ,StructField("host_response_rate", FloatType(), True)\
                       ,StructField("host_is_superhost", StringType(), True)\
                       ,StructField("instant_bookable", StringType(), True)\
                       ,StructField("host_identity_verified", StringType(), True)\
                        ,StructField("extra_people", IntegerType(), True)\
                        ,StructField("host_has_profile_pic", StringType(), True)\
                       ,StructField("guests_included", IntegerType(), True)\
                        ,StructField("security_deposit", FloatType(), True)\
                       ,StructField("accommodates", IntegerType(), True)\
                        ,StructField("bathrooms", FloatType(), True)\
                       ,StructField("bedrooms", FloatType(), True)\
                       ,StructField("beds", FloatType(), True)\
                        ,StructField("cleaning_fee", FloatType(), True)\
                         ,StructField("price", IntegerType(), True)\
                       ,StructField("room_type", StringType(), True)\
                        ,StructField("property_type", StringType(), True)\
                       ,StructField("neighbourhood_cleansed", StringType(), True)\
                       ,StructField("review_scores_rating", FloatType(), True)\
                        ,StructField("reviews_per_month", FloatType(), True)])

new_df=spark.createDataFrame(pandas_df,schema=define_schema)
input_DF = new_df.fillna(0)
input_DF = input_DF.filter("beds > 0")
input_DF = input_DF.filter("bedrooms > 0")
input_DF = input_DF.filter("bathrooms > 0")
input_DF = input_DF.filter("price > 0")
input_DF = input_DF.filter("review_scores_rating > 0")
input_DF = input_DF.filter("reviews_per_month > 0")
input_DF = input_DF.filter("accommodates > 0")


# In[8]:


def write_to_file(data_frame,out_file):
    out_file_path = out_bucket + out_file
    data_frame.coalesce(1).write.option("header", "true").mode('overwrite').format('csv').save(out_file_path)


# # Calculation for part 1

# In[9]:



def calcualte_room_type_count(input_DF):
    # calcualte number of listings based on room type
    property_type_DF=input_DF.groupby('room_type').agg({'room_type':'count'}).withColumnRenamed('count(room_type)',"number_of_listings")
    return property_type_DF

room_type_count_df = calcualte_room_type_count(input_DF)
write_to_file(room_type_count_df, "room_type_count_1")


# In[11]:



def calcualte_property_type_count(input_DF):
    # calcualte number of listings based on property type
    res_df =input_DF.groupby('property_type').agg({'property_type':'count'}).withColumnRenamed('count(property_type)',"number_of_listings")
    return res_df

property_type_count_df = calcualte_property_type_count(input_DF)
write_to_file(property_type_count_df, "property_type_count_2")
    


# In[12]:



def calcualte_property_room_avg_price(input_DF):
    res_df=input_DF.groupby('property_type','room_type').agg({'price':'mean'}).withColumnRenamed('avg(price)',"average_price")
    return res_df
    
property_room_avg_price = calcualte_property_room_avg_price(input_DF)
write_to_file(property_room_avg_price, "property_room_avg_price_3")


# In[14]:


def calculate_avg_room_price(input_DF):
    #what type of room has maximun price
    room_type_price_mean_DF=input_DF.groupby('room_type').agg({'price':'mean'}).withColumnRenamed('avg(price)',"average_price")
    return room_type_price_mean_DF

avg_room_price = calculate_avg_room_price(input_DF)
write_to_file(avg_room_price, "avg_room_price_4")


# In[15]:


def get_neighbourhood_lists(input_DF):
    apartment_df = input_DF.filter(input_DF['room_type'] == "Entire home/apt")
    neighbourhood_lists = apartment_df.groupby('neighbourhood_cleansed').count().withColumnRenamed('count','number_of_lists').orderBy('number_of_lists', ascending=False)
    neighbourhood_lists = neighbourhood_lists.filter(neighbourhood_lists['number_of_lists'] > 75)
    return neighbourhood_lists


neighbourhood_lists = get_neighbourhood_lists(input_DF)
write_to_file(neighbourhood_lists, "neighbourhood_lists_5")


# In[16]:


def get_neighbourhood_price(input_DF):
    apartment_df = input_DF.filter(input_DF['room_type'] == "Entire home/apt")
    neighbourhood_price = apartment_df.groupby('neighbourhood_cleansed').agg({'price':'mean'})                                                                        .withColumnRenamed('avg(price)','price')                                                                        .orderBy('price', ascending=False)


    avg_price = neighbourhood_price.select("price").agg({'price':'mean'}).collect()
    neighbourhood_price = neighbourhood_price.filter(neighbourhood_price['price'] > avg_price[0]['avg(price)'])
    return neighbourhood_price

neighbourhood_price = get_neighbourhood_price(input_DF)
write_to_file(neighbourhood_price, "neighbourhood_price_6")


# # Calculation for part 2

# In[20]:


def calcualte_new_score(new_df):
    new_review_score_setter = udf(lambda review_per_month,review_scores_rating : (review_per_month*review_scores_rating) , FloatType())
    new_df = new_df.withColumn("new_review_score", new_review_score_setter(new_df['reviews_per_month'],new_df['review_scores_rating']))
    return new_df

input_new_DF = calcualte_new_score(input_DF)


# In[21]:


def convert_tf(val):
    if(val == "t"):
        return 1
    else:
        return 0


# In[22]:


def convert_time(time_str):
    if time_str == "within an hour":
        time_int =1
    elif time_str == "within a few hours":
        time_int =4
    elif time_str == "within a day":
        time_int =24
    elif time_str == "a few days or more":
        time_int = 48
    else:
        time_int = 96
    return time_int


# In[1]:


def clean_up_data(new_df):
    tf_converter = udf(lambda row: convert_tf(row) , IntegerType())
    new_df = new_df.withColumn("host_is_superhost", tf_converter(new_df['host_is_superhost']))
    new_df = new_df.withColumn("instant_bookable", tf_converter(new_df['instant_bookable']))

    new_df = new_df.withColumn("host_identity_verified", tf_converter(new_df['host_identity_verified']))
    new_df = new_df.withColumn("host_has_profile_pic", tf_converter(new_df['host_has_profile_pic']))
    
    time_converter = udf(lambda row: convert_time(row) , IntegerType())
    new_df = new_df.withColumn("host_response_time", time_converter(new_df['host_response_time']))
    
    new_df = new_df.fillna(0)
    
    columns_to_drop = ["room_type","property_type","neighbourhood_cleansed","review_scores_rating","reviews_per_month"]
    new_df = new_df.drop(*columns_to_drop)
    
    return new_df

input_new_DF = clean_up_data(input_new_DF)
write_to_file(input_new_DF, "cleaned_data_with_new_score_7")

