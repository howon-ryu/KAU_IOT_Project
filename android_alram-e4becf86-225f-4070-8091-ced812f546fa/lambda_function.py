import json
import firebase_admin
import boto3
from firebase_admin import messaging
from firebase_admin import credentials
 
credential_file_path = './myfirebasenotification-53d66-firebase-adminsdk-ckyc6-dc3747fafa.json'
 
def send_fcm_update_notification():
    cred = credentials.Certificate(credential_file_path)
    try:
        push_service = firebase_admin.get_app()
    except:
        push_service = firebase_admin.initialize_app(cred)
    topic = "custom"
    message = messaging.Message(
        data = {
            'title': "앱 테스트 업데이트!",
            'body': "뭔지 모르지만 업데이트 하세!"
        },
        topic = topic)
    response = messaging.send(message)
    print(response)
 
def send_fcm_notify_notification1():
    cred = credentials.Certificate(credential_file_path)
    try:
        push_service = firebase_admin.get_app()
    except:
        push_service = firebase_admin.initialize_app(cred)
    topic = "notify1"
    message = messaging.Message(
        data = {
            'title': "건조",
            'body': "식물이 너무 건조해요...ㅠㅠ ʘ̥_ʘ "
        },
        topic = topic)
    response = messaging.send(message)
    print(response)

def send_fcm_notify_notification2():
    cred = credentials.Certificate(credential_file_path)
    try:
        push_service = firebase_admin.get_app()
    except:
        push_service = firebase_admin.initialize_app(cred)
    topic = "notify2"
    message = messaging.Message(
        data = {
            'title': "과습",
            'body': "식물이 너무 습해요...ㅠㅠ (゜▽゜;)!"
        },
        topic = topic)
    response = messaging.send(message)
    print(response)
    
def send_fcm_notify_notification3():
    cred = credentials.Certificate(credential_file_path)
    try:
        push_service = firebase_admin.get_app()
    except:
        push_service = firebase_admin.initialize_app(cred)
    topic = "notify3"
    message = messaging.Message(
        data = {
            'title': "저온",
            'body': "식물이 너무 추워요...ㅠㅠ  :(⁄ ⁄ᵒ̶̶̷́⁄⚰⁄ᵒ̶̶̷̀⁄ ⁄):!"
        },
        topic = topic)
    response = messaging.send(message)
    print(response)
    
def send_fcm_notify_notification4():
    cred = credentials.Certificate(credential_file_path)
    try:
        push_service = firebase_admin.get_app()
    except:
        push_service = firebase_admin.initialize_app(cred)
    topic = "notify4"
    message = messaging.Message(
        data = {
            'title': "고온",
            'body': "식물이 너무 더워요...ㅠㅠ ヽ(;ﾟ;∀;ﾟ; )ﾉ!"
        },
        topic = topic)
    response = messaging.send(message)
    print(response)
    
def send_fcm_send_data(data):
    cred = credentials.Certificate(credential_file_path)
    try:
        push_service = firebase_admin.get_app()
    except:
        push_service = firebase_admin.initialize_app(cred)
    topic = "realTime"
    message = messaging.Message(
        data = {
            'title': "앱 테스트 노티!",
            'body': data
        },
        topic = topic)
    response = messaging.send(message)
    print(response)    

 
 
def lambda_handler(event, context):
    state = event['state']
    reported = state['reported']
    temp = reported['temp']
    humid = reported['humid']
    dust = reported['dust']
    
    flag1 = reported['flag1']
    flag2 = reported['flag2']
    flag3 = reported['flag3']
    flag4 = reported['flag4']
    flag5 = reported['flag5']
    if flag1==1:
        send_fcm_notify_notification1()
        
    if flag2==1:
        send_fcm_notify_notification2()
        
    if flag3==1:
        send_fcm_notify_notification3()
        
    if flag4==1:
        send_fcm_notify_notification4()
    
    if flag5==1 :
        send_fcm_send_data("temp : {0} humid : {1} dust : {2}".format(temp,humid,dust))
        
    return