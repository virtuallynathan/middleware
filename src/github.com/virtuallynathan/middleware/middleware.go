package main

import (
	"database/sql"
	"log"
	"math/rand"
	"net/http"
	"runtime"
	"strconv"
	"time"

	"github.com/ant0ine/go-json-rest"
	_ "github.com/go-sql-driver/mysql"
)

var db *sql.DB
var addDeviceStmt *sql.Stmt
var removeDeviceStmt *sql.Stmt
var deviceIDStmt *sql.Stmt
var deviceLocationStmt *sql.Stmt
var deviceSensorStmt *sql.Stmt
var DeviceBySensorAndLocationStmt *sql.Stmt

func main() {

	runtime.GOMAXPROCS(runtime.NumCPU())
	//Begin database conneciton
	db, err := sql.Open("mysql", "root:compsci123@tcp(173.194.80.185:3306)/middleware")
	if err != nil {
		log.Fatalf("Error opening database: %v", err)
	}
	defer db.Close()

	err = db.Ping()
	if err != nil {
		log.Fatalf("Error connecting to database: %v", err)
	}

	// Prepare statement for inserting data
	addDeviceStmt, err = db.Prepare("INSERT INTO devices VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )") // ? = placeholder
	if err != nil {
		log.Fatalf(err.Error() + "sql insert addDeviceStmt prepare")
	}
	defer addDeviceStmt.Close()

	// Prepare statement for reading data
	deviceIDStmt, err = db.Prepare("SELECT * FROM devices WHERE DeviceID = ?")
	if err != nil {
		log.Fatalf(err.Error() + "sql select deviceIDStmt prepare")
	}
	defer deviceIDStmt.Close()

	deviceLocationStmt, err = db.Prepare("SELECT * FROM devices WHERE Location = ?")
	if err != nil {
		log.Fatalf(err.Error() + "sql select deviceLocationStmt prepare")
	}
	defer deviceLocationStmt.Close()

	deviceSensorStmt, err = db.Prepare("SELECT * FROM devices WHERE Accelerometer = ? AND GPS = ? AND Light = ? AND Temperature = ? AND Orientation = ?")
	if err != nil {
		log.Fatalf(err.Error() + "sql select deviceSensorStmt prepare")
	}
	defer deviceSensorStmt.Close()

	DeviceBySensorAndLocationStmt, err = db.Prepare("SELECT * FROM devices WHERE Accelerometer = ? AND Location = ?")
	if err != nil {
		log.Fatalf(err.Error() + "sql select DeviceBySensorAndLocationStmt prepare")
	}
	defer DeviceBySensorAndLocationStmt.Close()

	removeDeviceStmt, err = db.Prepare("DELETE FROM devices WHERE DeviceID = ?")
	if err != nil {
		log.Fatalf(err.Error() + "sql select removeDeviceStmt prepare")
	}
	defer removeDeviceStmt.Close()

	//Begin HTTP handling
	handler := rest.ResourceHandler{
		EnableRelaxedContentType: true,
	}
	handler.SetRoutes(
		rest.Route{"POST", "/device/add", AddDevice},
		rest.Route{"GET", "/device/:DeviceID", GetDeviceByID},
		rest.Route{"GET", "/device/loc/:Location", GetDeviceByLocation},
		rest.Route{"POST", "/device/sensor", GetDeviceBySensorType},
		rest.Route{"POST", "/device/sensor_location", GetDeviceBySensorAndLocation},
		rest.Route{"DELETE", "/device/remove/:DeviceID", RemoveDevice},
		rest.Route{"GET", "/health/:check", HealthCheck},
	)
	http.ListenAndServe(":8080", &handler)

}

//The struct of type Device stores all the information about a single device.
type Device struct {
	DeviceID        string
	IPAddr          string
	ListenPort      string
	Location        string
	ConnectionLimit string
	Accelerometer   string
	GPS             string
	Light           string
	Temperature     string
	Orientation     string
}

type SensorLocationQuery struct {
	Sensor   string
	Location string
}

type Sensors struct {
	Accelerometer string
	GPS           string
	Light         string
	Temperature   string
	Orientation   string
}

//temporary assignment variables
var (
	ID              string
	DeviceID        string
	IPAddr          string
	ListenPort      string
	Location        string
	ConnectionLimit string
	Accelerometer   string
	GPS             string
	Light           string
	Temperature     string
	Orientation     string
)

//The store is a map containing structs of type Device.
var store = map[string]*Device{}

func HealthCheck(w *rest.ResponseWriter, r *rest.Request) {
	w.WriteJson("OK")
}

//This function searches the store and returns the device matching the ID provided.
func GetDeviceByID(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	devices := make([]*Device, 100) //TODO: fix arbitrary size thing...
	device := Device{}
	rows, err := deviceIDStmt.Query(deviceID)
	if err != nil {
		log.Fatalf("Error running deviceIDStmt %s", err.Error())
	}
	//TODO: put this shit in a function, DRY.
	i := 0
	for rows.Next() {
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Accelerometer, &GPS, &Light, &Temperature, &Orientation)
		if err != nil {
			log.Fatalf("Error scanning rows deviceLocationStmt %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Accelerometer = Accelerometer
		device.GPS = GPS
		device.Light = Light
		device.Temperature = Temperature
		device.Orientation = Orientation
		devices[i] = &device

		i++
	}
	w.WriteJson(&devices)

}

//This function seatches the list of devices and returns the device(s) that have the sensor(s) specified.
func GetDeviceBySensorType(w *rest.ResponseWriter, r *rest.Request) {
	sensors := Sensors{}
	err := r.DecodeJsonPayload(&sensors)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if sensors.Accelerometer != "true" || sensors.Accelerometer != "false" {
		rest.Error(w, "sensors Accelerometer t/f required", 400)
		return
	}
	if sensors.GPS != "true" || sensors.GPS != "false" {
		rest.Error(w, "sensors GPS t/f required", 400)
		return
	}
	if sensors.Light != "true" || sensors.Light != "false" {
		rest.Error(w, "sensors Light t/f required", 400)
		return
	}
	if sensors.Temperature != "true" || sensors.Temperature != "false" {
		rest.Error(w, "sensors Temperature t/f required", 400)
		return
	}
	if sensors.Orientation != "true" || sensors.Orientation != "false" {
		rest.Error(w, "sensors Orientation t/f required", 400)
		return
	}
	devices := make([]*Device, 100) //TODO: fix arbitrary size thing...
	device := Device{}
	rows, err := deviceSensorStmt.Query(sensors.Accelerometer, sensors.GPS, sensors.Light, sensors.Temperature, sensors.Orientation)
	if err != nil {
		log.Fatalf("Error running deviceSensorStmt %s", err.Error())
	}

	//TODO: put this shit in a function, DRY.
	i := 0
	for rows.Next() {
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Accelerometer, &GPS, &Light, &Temperature, &Orientation)
		if err != nil {
			log.Fatalf("Error scanning rows deviceLocationStmt %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Accelerometer = Accelerometer
		device.GPS = GPS
		device.Light = Light
		device.Temperature = Temperature
		device.Orientation = Orientation
		devices[i] = &device

		i++
	}
	w.WriteJson(&devices)
}

//This function adds a device to the store (soon to be moved to Google Cloud Datastore)
func GetDeviceBySensorAndLocation(w *rest.ResponseWriter, r *rest.Request) {
	sensorLocationQuery := SensorLocationQuery{}
	devices := make([]*Device, 100) //TODO: fix arbitrary size thing...
	device := Device{}
	err := r.DecodeJsonPayload(&sensorLocationQuery)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if sensorLocationQuery.Location == "" {
		rest.Error(w, "device query Location required", 400)
		return
	}
	if sensorLocationQuery.Sensor == "" {
		rest.Error(w, "device query Sensor required", 400)
		return
	}
	rows, err := DeviceBySensorAndLocationStmt.Query(sensorLocationQuery.Sensor, sensorLocationQuery.Location)
	if err != nil {
		log.Fatalf("Error running DeviceBySensorAndLocationStmt %s", err.Error())
	}

	//TODO: put this shit in a function, DRY.
	i := 0
	for rows.Next() {
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Accelerometer, &GPS, &Light, &Temperature, &Orientation)
		if err != nil {
			log.Fatalf("Error scanning rows deviceLocationStmt %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Accelerometer = Accelerometer
		device.GPS = GPS
		device.Light = Light
		device.Temperature = Temperature
		device.Orientation = Orientation
		devices[i] = &device

		i++
	}

	w.WriteJson(&devices)

}

//This function seatches the list of devices and returns the device(s) that are in a specific loation.
func GetDeviceByLocation(w *rest.ResponseWriter, r *rest.Request) {
	location := r.PathParam("Location")
	devices := make([]*Device, 100) //TODO: fix arbitrary size thing...
	device := Device{}
	rows, err := deviceLocationStmt.Query(location)
	if err != nil {
		log.Fatalf("Error running deviceLocationStmt %s", err.Error())
	}
	//TODO: put this shit in a function, DRY.
	i := 0
	for rows.Next() {
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Accelerometer, &GPS, &Light, &Temperature, &Orientation)
		if err != nil {
			log.Fatalf("Error scanning rows deviceLocationStmt %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Accelerometer = Accelerometer
		device.GPS = GPS
		device.Light = Light
		device.Temperature = Temperature
		device.Orientation = Orientation
		devices[i] = &device

		i++
	}
	w.WriteJson(&devices)
}

//This function adds a device to the store (soon to be moved to Google Cloud Datastore)
func AddDevice(w *rest.ResponseWriter, r *rest.Request) {
	device := Device{}
	err := r.DecodeJsonPayload(&device)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	rand.Seed(time.Now().Unix())
	device.DeviceID = strconv.Itoa(rand.Intn(100000))
	if device.IPAddr == "" {
		rest.Error(w, "device IPAddr required", 400)
		return
	}
	if device.ListenPort == "" {
		rest.Error(w, "device ListenPort required", 400)
		return
	}
	if device.Location == "" {
		rest.Error(w, "device Location required", 400)
		return
	}
	if device.ConnectionLimit == "" {
		rest.Error(w, "device ConnectionLimit required", 400)
		return
	}
	if device.Accelerometer == "" {
		rest.Error(w, "device Accelerometer t/f required", 400)
		return
	}
	if device.GPS != "true" || device.GPS != "false" {
		rest.Error(w, "device GPS t/f required", 400)
		return
	}
	if device.Light != "true" || device.Light != "false" {
		rest.Error(w, "device Light t/f required", 400)
		return
	}
	if device.Temperature != "true" || device.Temperature != "false" {
		rest.Error(w, "device Temperature t/f required", 400)
		return
	}
	if device.Orientation != "true" || device.Orientation != "false" {
		rest.Error(w, "device Orientation t/f required", 400)
		return
	}
	_, err = addDeviceStmt.Exec(0, device.DeviceID, device.IPAddr, device.ListenPort, device.Location, device.ConnectionLimit, device.Accelerometer, device.GPS, device.Light, device.Temperature, device.Orientation)
	if err != nil {
		log.Fatalf("Error running addDeviceStmt %s", err.Error())
	}

	w.WriteJson(&device)

}

//This function removes a device from the store
func RemoveDevice(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	_, err := removeDeviceStmt.Exec(deviceID)
	if err != nil {
		log.Fatalf("Error running removeDeviceStmt %s", err.Error())
	}
}
