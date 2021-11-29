from __future__ import print_function
import time
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db as fb_db
import sensor2
import sensor5

if not firebase_admin._apps:
    cred = credentials.Certificate('/home/pi/placed_service_cred.json')
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://placed-5b875-default-rtdb.europe-west1.firebasedatabase.app/',
        'databaseAuthVariableOverride': {
            'uid': 'sensor_placed_moist_1'
        }
    })

ref = fb_db.reference('/')

def ignore_first_call(fn):
    called = False

    def wrapper(*args, **kwargs):
        nonlocal called
        if called:
            return fn(*args, **kwargs)
        else:
            called = True
            return None

    return wrapper


@ignore_first_call
def listener(event):
    # print(event.event_type)  # can be 'put' or 'patch'
    # print(event.path)  # relative to the reference, it seems
    # print(event.data)  # new data at /reference/event.path. None if deleted

    if len(str(event.path).split('/')) > 3:
        if str(event.path).split('/')[3] == 'requestUpdate':
            sensor = str(event.path).split('/')[2]
            value = event.data
            print(value)
            print('sensor ' + sensor)
            if sensor == '2':
                if value == 1:
                    print('value 1')
                    sensor2.main()
            elif sensor == '5':
                if value == 1:
                    print('value 1')
                    sensor5.main()


fb_db.reference('/').listen(listener)