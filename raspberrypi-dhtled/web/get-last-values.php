<?php
header("Content-type: application/json");

function readCSVLastLine($fileName) {
    $line = `tail -n 1 $fileName`;
    $array = explode(",", $line);
    $timestamp = round(floatval($array[0]), 0);
    $value = round(floatval($array[1]), 1);

    $ret = array();
    $ret["timestamp"] = $timestamp;
    $ret["value"] = $value;
    return $ret;
}

$values = array();

$temperature = readCSVLastLine("temperature.csv");
$values["temperature"] = $temperature;
$humidity = readCSVLastLine("humidity.csv");
$values["humidity"] = $humidity;

echo json_encode($values);
?>
