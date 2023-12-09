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


# Read data
datafile = open("online.data", "r")

# Get lines
lines = datafile.readlines()

# Send data to Broker every 1 seconds (send a line)
try:
    while True:
        
        for line in lines[1:]:

            # Get current date and time
            curr = datetime.datetime.now()

            # Format date and time
            curr_date = curr.strftime("%d/%m/%y")
            curr_time = curr.strftime("%H-%M-%S-%f")

            fields = line.strip().split(';')

            msg = {
                    "date":curr_date,
                    "time":curr_time,
                    "acceleration_x":fields[0],
                    "acceleration_y":fields[1],
                    "acceleration_z":fields[2],
                    "gyro_x":fields[3],
                    "gyro_y":fields[4],
                    "gyro_z":fields[5]
                }


            time.sleep(1)
            payload = json.dumps(msg)
            result = client.publish(topic, payload)
            status = result[0]
            if status == 0:
                print("Message "+ str(payload) + " is published to topic " + topic)
            else:
                print("Failed to send message to topic " + topic)


finally:
    client.loop_stop()

