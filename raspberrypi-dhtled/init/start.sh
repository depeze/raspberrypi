#!/bin/bash
DHTLED_HOME=/root/dhtled
nohup java -jar $DHTLED_HOME/app/dhtled-0.0.1-SNAPSHOT.one-jar.jar -dhtCommand "$DHTLED_HOME/adatfruit/Adafruit-Raspberry-Pi-Python-Code/Adafruit_DHT_Driver/Adafruit_DHT 22 4" -dataDirectory /usr/share/nginx/www/dht -highHumidityLevel 65 -sampleFrequencySeconds 10 -allowedErrorPercent 70 > /dev/null &