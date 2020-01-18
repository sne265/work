#!/usr/bin/env python
# coding: utf-8

# In[ ]:



from pyspark.sql.functions import col,sum,count
from pyspark.sql import SparkSession

head_data="s3://bigdataspring19/indiv_header_file.csv"


# In[ ]:


def load_and_prepare_data(input_data):
        
    spark = SparkSession.builder            .appName("Election Campaign")             .getOrCreate() 
    
    # read input data
    election_df= spark.read.option("header","false")                .option("delimiter","|")                .csv(input_data)
    
    # read header file containing column names
    header_df = spark.read.option("header","true").option("delimiter",",")                .csv(head_data)
    
    election_df=header_df.union(election_df)
    
    # remove null values if any
    election_df = election_df.filter(election_df.TRANSACTION_AMT.isNotNull())
    
    # # create dictionary to give names to committee ids that we are interested in
    committee_ids={"C00575795":"HILLARY FOR AMERICA","C00577130":"BERNIE 2016",
                   "C00580100":"DONALD J. TRUMP FOR PRESIDENT, INC."}
    
    # filter the dataset for required committee ids
    committee_3_df = election_df.select("CMTE_ID","TRANSACTION_AMT","NAME","ZIP_CODE")                    .where(col("TRANSACTION_AMT") > 0).where(col("CMTE_ID").isin(list(committee_ids.keys())))
    
    # assign committee names
    committee_3_df = committee_3_df.withColumn("COMMITEE_NAME",col("CMTE_ID")).                    replace(to_replace=committee_ids, subset=['COMMITEE_NAME'])
    # display dataframe
    # committee_3_df.show()
    
    return committee_3_df


# In[ ]:



def count_donations(committee_3_df):
 
    # count number of donations per committee
    count_df=committee_3_df.groupBy("CMTE_ID").agg(count("TRANSACTION_AMT").alias("NUMBER_OF_DONATIONS"))
    
    count_df.coalesce(1).write.format('csv').save("s3://bigdataspring19/count_donation", header='true')
    
       


# In[ ]:



def calculate_total_donation(committee_3_df):
    
    # compute the sum of total donations receeived by each committee
    
    total_donation_df = committee_3_df.groupBy("CMTE_ID")                    .agg(sum("TRANSACTION_AMT").alias("TOTAL_DONATION"))
    
    total_donation_df.coalesce(1).write.format('csv').save("s3://bigdataspring19/total_donations", header='true')
    
    return total_donation_df
    


# In[ ]:



def percentage_of_small_contribution(committee_3_df,total_df):
    
    # find the total sum each person has donated to each campaign
    sum_df=committee_3_df.groupby(["CMTE_ID","NAME"]).agg(sum("TRANSACTION_AMT").alias("SUM"))           .select("CMTE_ID","SUM")
    
    #find small contributions
    small_sum_df=sum_df.where((col("SUM")<200)).groupby("CMTE_ID")                .agg(sum("SUM").alias("SMALL_CONTRIBUTIONS"))
    
    
    percent_df = small_sum_df.join(total_df, "CMTE_ID").withColumn("PERCENTAGE_OF_SMALL_CONTRIBUTIONS",                 ((col("SMALL_CONTRIBUTIONS") / col("TOTAL_DONATION"))*100))
    
    percent_df.coalesce(1).write.format('csv').save("s3://bigdataspring19/small_contributors", header='true')
    


# In[ ]:



def histogram_of_donations(committee_3_df,output_data):
   
    
    hist_df =committee_3_df.select("COMMITEE_NAME","TRANSACTION_AMT")
        
    hist_df.coalesce(1).write.format('csv').save(output_data, header='true')
   


# In[ ]:



def main(input_data,output_data):
    
    committee_3_df=load_and_prepare_data(input_data)    
    
    count_donations(committee_3_df)
    
    total_df=calculate_total_donation(committee_3_df) 
    
    percentage_of_small_contribution(committee_3_df,total_df)
    
    histogram_of_donations(committee_3_df,output_data)
    


# In[ ]:



def read_arguments():
    
    import argparse
    
    parser = argparse.ArgumentParser()
    parser.add_argument('-i','--input',default='input')
    parser.add_argument('-o','--output',default='output')
    args=parser.parse_args()
    
    return(args.input,args.output)


# In[ ]:



if __name__ == "__main__":
    
    input_data,output_data = read_arguments()
    
    main(input_data,output_data)
    

