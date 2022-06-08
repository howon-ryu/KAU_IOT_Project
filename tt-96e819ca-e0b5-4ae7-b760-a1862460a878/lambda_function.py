import json
import boto3
import os
import time
from boto3.dynamodb.conditions import Key

os.environ['TZ'] = 'Asia/Seoul'

# time.strftime('%Y-%m-%d', time.localtime(time.time()))
#full=time.strftime('%c', time.localtime(time.time()))
# hour=time.localtime(time.time()).tm_hour
# hour=str(hour+9)
# min = str(time.localtime(time.time()).tm_min)
# sec = str(time.localtime(time.time()).tm_sec)
# k = str(hour+":"+min+":"+sec)

def lambda_handler(event, context):
    full=time.strftime('%c', time.localtime(time.time()))
    hour=time.localtime(time.time()).tm_hour
    hour=str(hour+9)
    min = str(time.localtime(time.time()).tm_min)
    sec = str(time.localtime(time.time()).tm_sec)
    weekday = time.strftime('%a', time.localtime(time.time()))
    month = time.strftime('%b', time.localtime(time.time()))
    day = time.strftime('%d', time.localtime(time.time()))
    year = time.strftime('%Y', time.localtime(time.time()))
    k = str(weekday+" "+ month+" "+day+" "+hour+":"+min+":"+sec+" "+year)
    state = event['state']
    reported = state['reported']
    temp = reported['temp']
    humid = reported['humid']
    dust = reported['dust']
    count = reported['count']
    flag1 = reported['flag1']
    flag2 = reported['flag2']
    flag3 = reported['flag3']
    flag4 = reported['flag4']
    flag5 = reported['flag5']
    if dust>10000:
        dust = str("null")
        return
        

    dynamodb = boto3.client('dynamodb')

    response = dynamodb.put_item(
            
            TableName = 'Data_DB',
            Item = {
                'ID' : {
                    'S' : "0"
                },
                'DATE' : {
                    'S' : k
                },
                'HUMID' : {
                    'S' : str(humid)
                },
                'TEMP' : {
                    'S' : str(temp)
                },
                'DUST' : {
                    'S' : str(dust)
                },
                'COUNT' : {
                    'S' : str(count)
                },
                'FLAG1' : {
                    'S' : str(flag1)
                },
                'FLAG2' : {
                    'S' : str(flag2)
                },
                'FLAG3' : {
                    'S' : str(flag3)
                },
                'FLAG4' : {
                    'S' : str(flag4)
                },
                'FLAG5' : {
                    'S' : str(flag5)
                }
            })
    return response
    #{
        #response
        #'statusCode': 200,
        #'body': json.dumps('Hello from Lambda!')
        #'body':json.dumps(response['Items'])
    #}
