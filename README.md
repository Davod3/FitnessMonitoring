## Fitness Monitoring

Welcome to Fitness Monitoring IoT!
This project is built using Docker, which simplifies the setup process for you.

## Prerequisites

Before running this project, ensure that you have Docker installed on your computer.
You can download and install Docker from [Docker's official website]
(https://www.docker.com/get-started/).
Also you need to have Docker Compose installed. You can install it from here
[Docker Compose Installation](https://docs.docker.com/compose/install/)

## Getting Started:

To run this project, you just need to run the 'run.sh'
script in a command prompt as follows:

 `./run.sh`

This script will handle the DOCKER setup and execution of the project for you.
Once the script completes, you should be able to access the project from your browser at

`http://localhost:1880/ui`

To stop the project execution just run the 'stop.sh' script 
in a command prompt as follows:

`./stop.sh`

Depending on your docker installation these commands may need to be run with sudo privileges.

## Additional Notes

If you encounter any issues while running the 'run.sh' script,
ensure that Docker is installed and running properly on your system.

If doing `./<script>.sh` does not work, try doing `bash <script>.sh` instead.

To provide some sample data to the system you can run the provided client.
To do so, please execute the following commands starting in the project root 
directory:

`cd client`

`pip install -r requirements.txt`

`python client1.py`
