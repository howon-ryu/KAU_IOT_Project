import boto3
import json


dynamodb = boto3.resource('dynamodb')


# Change topic, qos and payload
client = boto3.client('iot-data', region_name='ap-northeast-2')
def lambda_handler(event, context):
    print(event['ID'])
    if event['ID'] != "###":
            client.publish(topic = '$aws/things/BME280/shadow/update',qos=1 ,payload = json.dumps({
        "state": {
            "desired": {
            "ask": "livedata",
            "ID" : event['ID']
            }
        }
        }))
        # event['ID'] != "###":
        #     client.publish(topic = '$aws/things/BME280/shadow/update',qos=1 ,payload = json.dumps({
        # "state": {
        #     "desired": {
        #     "ask": "livedata",
        #     "ID" : event['ID']
        #     }
        # }
        # }))
    else:
        for i in range(0,5):
                client.publish(topic = '$aws/things/BME280/shadow/update',qos=1 ,payload = json.dumps({
                "state": {
                    "desired": {
                    "ask": "currentdata",
                    "ID" : event['ID']
                    
                    },
                    "reported": {
                    "flag5": 0
                    
                    }
                }
                
                }))
        # client.publish(topic = 'bme280/realtime',qos=1 ,payload = json.dumps({
        # "state": {
        #     "desired": {
        #     "ask": "currentdata",
        #     "ID" : event['ID']
            
        #     },
        #     "reported": {
        #     "flag5": 0
            
        #     }
        # }
        
        # }))
       
        
  


    
    
    # response = client.publish(
    #     topic='esp32/123',
    #     qos=1,
    #     payload=json.dumps({"foo":"bar"})
    # )
    str_Name = event['ID']
    table = dynamodb.Table('ID_DB')
    response = table.put_item(
      Item={
            'ID': str_Name
        }
    )
    return {
        'statusCode': event['ID'],
        'body': "success"
    }


    
   
   
    