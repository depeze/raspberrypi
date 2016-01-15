raspberrypi
===========

Applications for the Raspberry PI

## raspberrypi-dhtled
Application written in Java runs on the Raspberry PI and it is collecting temperature and humidity values. It stores the data values in separate CSV files. 

The project also contains an index.html file which can show you these values with the help of the Highstock JS library. The PHP files are for retrieving the data values from the Raspberry PI. The web files should be hosted by a web server like Apache or Nginx with PHP module loaded.

## DHTViewer
Android application will show you the latest temperature and humidity values which are being collected by your Raspberry PI. It accesses the get-last-values.php file which returns those values in a simple JSON format.

Screenshot:

![Android application screenshot](https://github.com/depeze/raspberrypi/blob/master/DHTViewer/RaspberryPI-DHTViewer-Screenshot.jpg)

