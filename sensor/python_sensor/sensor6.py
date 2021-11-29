from __future__ import print_function
import time
import board
import busio
import adafruit_ads1x15.ads1015 as ADS
from adafruit_ads1x15.analog_in import AnalogIn
import Adafruit_DHT
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db as fb_db
from datetime import datetime
import json
import requests
import MySQLdb
import mysql.connector

def main():
    now = datetime.now()

    # settings for temperature sensor:
    local_sensor_type = Adafruit_DHT.DHT22
    pin = 12

    # only adjust cred file with data for sensor
    with open('/home/pi/placed/sensor_creds/local_db_cred_sensor6.json', 'r') as file:
        connection_dictionary = json.load(file)

    # load data from creds file
    for line in connection_dictionary:
        host = line['host']
        user = line['user']
        password = line['password']
        sensor = line['sensor']
        is_generic = line['is_generic']

    # open connection to local SQL-Database
    localDB = mysql.connector.connect(
      host=host,
      user=user,
      password=password,
      database="placed"
    )

    # cursor = element which reads/writes to/from database. Only onw is needed, for readability two
    writeCursor = localDB.cursor()
    readCursor = localDB.cursor()

    # firebase-realitme-database initialization with special credits and uid-override
    if not firebase_admin._apps:
        cred = credentials.Certificate('/home/pi/placed_service_cred.json')
        firebase_admin.initialize_app(cred, {
            'databaseURL': 'https://placed-5b875-default-rtdb.europe-west1.firebasedatabase.app/',
            'databaseAuthVariableOverride': {
                'uid': 'sensor_placed_moist_1'
            }
        })

    ref = fb_db.reference('/')

    # connection to the Sensor. Uncomment right sensor type and adjust the connection parameters

    # --- temperature and air-humidity sensor ---
    Adafruit_DHT.read_retry(local_sensor_type, pin)
    humidity, value = Adafruit_DHT.read_retry(local_sensor_type, pin)
    if humidity is not None and value is not None:
        print("Temp={0:0.1f}*C Humidity={1:0.1f}%"
              .format(value, humidity))
    else:
        print("Failed to get reading. Try again!")

    # --- humidity-sensor ---
    # i2c = busio.I2C(board.SCL, board.SDA)
    # ads = ADS.ADS1015(i2c)
    # chan = AnalogIn(ads, ADS.P0)
    # value = 100 - (((float(chan.value) - 6000) / 12000) * 100)

    print('value: ' + str(value))

    # sql command for saving data to local server
    sql = "INSERT INTO `data`(`sensor`, `is_generic`, `value`, `data_type`, `year`, `month`, `day`, `hour`, `minute`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')" % \
    	(sensor, is_generic, value, 'single', now.year, now.month, now.day, now.hour, now.minute)
    # execution of sql command with fail-safe
    try:
        writeCursor.execute(sql)
        localDB.commit()
        print("data from sensor " + sensor + " inserted into " + host)
    except Exception as e:
        print("Error. Rolling back")
        print(e)

    # sql-command for getting the last entered id
    sql = "SELECT id FROM data WHERE sensor = %s ORDER BY id DESC LIMIT 1"
    inp = (sensor,)
    readCursor.execute(sql, inp)

    readResult = readCursor.fetchone()

    # try to save the data to the firebase database with fail-safe
    try:
        data_ref = ref.child('data_by_sensors').child(sensor)
        data_ref.update({
            'id':readResult[0],
            'sensor': int(sensor),
            'is_generic': is_generic,
            'value':value,
            'data_type': 'single',
            'year': now.year,
            'month': now.month,
            'day': now.day,
            'hour': now.hour,
            'minute': now.minute,
            'requestUpdate': 0
        })
        print("data from sensor " + sensor + " uploaded to firebase")
    except Exception as e:
        print("Error. Rolling back")
        print(e)


if __name__ == "__main__":
    main()