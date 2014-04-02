middleware
==========

To run a producer example: call

java -jar Producer.jar

when within the middleware directory, this sends a registration then a heartbeat, the heartbeat is more regular than every 2 minutes for demonstration purposes.

To run a consumer example: call

java -jar Consumer.jar

when within the middleware directory, this sends a request then a connect and a disconnect to a device.