<?php 
header("Content-type: application/json");

function readCSV($fileName) {
	$file = fopen($fileName, "r") or die();
	$ret = [];
	while (!feof($file)) {
		$line = fgets($file, 32);
		if ($line != null) {
			$array = explode(",", $line);
			$time = round(floatval($array[0]), 0);
			$value = round(floatval($array[1]), 1);
			$tmp = array($time, $value);
			array_push($ret, $tmp);
		}
	}
	return $ret;
}

$csvFile1 = "humidity.csv";
$ret = readCSV($csvFile1);
echo json_encode($ret);
?>
