from __future__ import print_function

import datetime
import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
from firebase_admin import db as fb_db
import MySQLdb


def send_to_topic(title, body,progress):
    # [START android_message]
    message = messaging.Message(
        android=messaging.AndroidConfig(
            ttl=datetime.timedelta(seconds=3600),
            priority='normal',
            notification=messaging.AndroidNotification(
                title=title,
                body=body,
            ),
        ),
        data={
            'progress': str(progress),
        },
        topic='mainz_plant',
    )
    response = messaging.send(message)
    # [END android_message]

def logfile(name, newStamp, code):
    if(int(newStamp[11:13]) > 22 | int(newStamp[11:13]) < 8):
        file = open(name, "w+")
        file.write(newStamp + '\n' + code + '\n' + '1')
        file.close()
        return(False)
    else:
        try:
            file = open(name, "r")
            file_content = file.readline()
            file.close()
            if (file_content == ""):
                print('file empty')
                file = open(name, "w+")
                file.write(newStamp + '\n' + code + '\n' + '1')
                file.close()
                return(True)
            else:
                file = open(name, "r")
                file_content = file.readlines()
                new_interval = str(int(file_content[0][2]) + 1)
                print(file_content[0])
                file.close()
                if (file_content[0][0:10] != newStamp[0:10]):
                    file = open(name, "w+")
                    file.write(newStamp + '\n' + code + '\n' + '1')
                    file.close()
                    return(True)
                else:
                    filehour = int(file_content[0][11:13])
                    stamphour = int(newStamp[11:13])
                    fileminute = int(file_content[0][14:16])
                    stampminute = int(newStamp[14:16])
                    minutedifference = (stamphour - filehour) * 60 + (stampminute - fileminute)
                    if(minutedifference > (20 * int(file_content[2]))):
                        file = open(name, "w+")
                        file.write(newStamp + '\n' + code + '\n' + new_interval)
                        file.close()
                        return(True)
                    else:
                        return(False)

        except Exception as e:
            print(e)
            print('file doesnt exist')
            file = open(name, "a+")
            file.close()
            file = open(name, "w+")
            file.write(newStamp + '\n' + code + '\n' + '1')
            file.close()
            return(True)


def isValueLowerAsMinimum(sensor, isHum):
    sql = "SELECT `data`.`id`, `data`.`value`, `data`.`timestamp`, `plant_object`.`plant_object_name`, `plant_type`.`min_hum`, `plant_type`.`min_temp`, `plant_type`.`max_hum`, `plant_type`.`max_temp` FROM `sensor_device` INNER JOIN `data` ON `data`.`sensor` = `sensor_device`.`id` INNER JOIN `plant_object` ON `sensor_device`.`plant` = `plant_object`.`id` INNER JOIN `plant_type` ON `plant_object`.`type` = `plant_type`.`plant_type_name` WHERE `data`.`sensor` = '%s' ORDER BY `data`.`id` DESC LIMIT 1" % \
          (sensor)
    try:
        curs.execute(sql)
        if(curs.rowcount == 1):
            result = curs.fetchall()
            print(result)
            if(isHum):
                print(str(result[0][4]) + ' < ' + str(result[0][6]))
                if(result[0][4]>result[0][1]):
                    if (logfile("/home/pi/placed/noti_log/" + result[0][3] + ".txt", str(result[0][2]), '0')):
                        send_to_topic(result[0][3],'Wasserstand niedrig: ' + str(result[0][1]) + '%', result[0][1])
                if (result[0][6] < result[0][1]):
                    if (logfile("/home/pi/placed/noti_log/" + result[0][3] + ".txt", str(result[0][2]), '1')):
                        send_to_topic(result[0][3],'Wasserstand zu hoch: ' + str(result[0][1]) + '%', result[0][1])
            else:
                print(str(result[0][5]) + ' < ' + str(result[0][7]))
                if (result[0][5] > result[0][1]):
                    if (logfile("/home/pi/placed/noti_log/" + result[0][3] + ".txt", str(result[0][2]), '0')):
                        send_to_topic(result[0][3], 'Temperatur zu niedrig: ' + str(result[0][1]) + '°C', result[0][1])
                if (result[0][7] < result[0][1]):
                    if(logfile("/home/pi/placed/noti_log/" + result[0][3] + ".txt",str(result[0][2]),'1')):
                        send_to_topic(result[0][3], 'Temperatur zu hoch: ' + str(result[0][1]) + '°C', result[0][1])




    except Exception as e:
        print("Error. Rolling back")
        print(e)
        db.rollback()


cred = credentials.Certificate('/home/pi/placed_service_cred.json')
firebase_admin.initialize_app(cred
                              ,
                              {
                                  'databaseURL': 'https://placed-5b875-default-rtdb.europe-west1.firebasedatabase.app/'
                              }
)

db = MySQLdb.connect("192.168.0.50", "placed_sensor", "sensor_secret_keyphrase", "placed")
curs = db.cursor()

isValueLowerAsMinimum("placed_moist_1", True)
isValueLowerAsMinimum("placed_temp_1", False)

