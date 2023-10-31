import datetime
import paho.mqtt.client as mqtt 
import time
import json

broker_hostname = "localhost"
port = 1883
topic = "idc/fc56339"

def on_connect(client, userdata, flags, return_code):
    if return_code == 0:
        print("connected")
    else:
        print("could not connect, return code:", return_code)

client = mqtt.Client("Client1")
#  client.username_pw_set(username="user_name", password="password") # uncomment if you use password auth
client.on_connect=on_connect

client.connect(broker_hostname, port)
client.loop_start()


msg = []

# Read data
datafile = open("client/online.data", "r")

lines = datafile.readlines()


# Get current date and time
curr = datetime.datetime.now()

curr_date = curr.strftime("%d/%m/%y")
curr_time = curr.strftime("%H:%M:%S:%f")


# for each line, separate by ';' and put all lines in msg
for line in lines[1:]:
    fields = line.split(';')
    msg.append({
        "date":curr_date,
        "time":curr_time,
        "acceleration_x":fields[0],
        "acceleration_y":fields[1],
        "acceleration_z":fields[2],
        "gyro_x":fields[3],
        "gyro_y":fields[4],
        "gyro_z":fields[5]
    })


# Send data to Broker every 1 seconds (send a line)
msg_count = 0
try:
    while msg_count < len(msg):
        time.sleep(1)
        payload = json.dumps(msg[msg_count])
        result = client.publish(topic, payload)
        status = result[0]
        if status == 0:
            print("Message "+ str(payload) + " is published to topic " + topic)
        else:
            print("Failed to send message to topic " + topic)
        msg_count += 1
finally:
    client.loop_stop()

