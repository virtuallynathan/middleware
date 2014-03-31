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
var DeviceHeartBeatStmt *sql.Stmt
var UpdateDeviceLocationStmt *sql.Stmt
var UpdateDeviceConnectionStmt *sql.Stmt
var GetDeviceConnectionStmt *sql.Stmt

func main() {

	//Set the number of threads to use
	runtime.GOMAXPROCS(runtime.NumCPU())

	//Begin database conneciton setup and query setup
	//"open" a connection to the database - does not actually connect at this point.
	db, err := sql.Open("mysql", "root:compsci123@tcp(173.194.80.185:3306)/middleware")
	if err != nil {
		log.Fatalf("Error opening database: %v", err)
	}
	defer db.Close()

	//Ping the database, an attempt to connect to verify the above information
	err = db.Ping()
	if err != nil {
		log.Fatalf("Error connecting to database: %v", err)
	}

	//Prepare statement for inserting data
	addDeviceStmt, err = db.Prepare("INSERT INTO devices VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )") // ? = placeholder
	if err != nil {
		log.Printf(err.Error() + "sql insert addDeviceStmt prepare")
	}
	defer addDeviceStmt.Close()

	//Prepare statement for updating a specific device's HeartBeat time by DeviceID
	DeviceHeartBeatStmt, err = db.Prepare("UPDATE devices SET HeartBeat = ? WHERE DeviceID = ?")
	if err != nil {
		log.Printf(err.Error() + "sql update DeviceHeartBeatStmt prepare")
	}
	defer DeviceHeartBeatStmt.Close()

	//Prepare statement for updating a specific device's Location by DeviceID
	UpdateDeviceLocationStmt, err = db.Prepare("UPDATE devices SET Location = ? WHERE DeviceID = ?")
	if err != nil {
		log.Printf(err.Error() + "sql update UpdateDeviceLocationStmt prepare")
	}
	defer UpdateDeviceLocationStmt.Close()

	//Prepare statement for updating a specific device's Location by DeviceID
	UpdateDeviceConnectionStmt, err = db.Prepare("UPDATE devices SET ConnectionCount = ? WHERE DeviceID = ?")
	if err != nil {
		log.Printf(err.Error() + "sql update UpdateDeviceConnectionStmt prepare")
	}
	defer UpdateDeviceConnectionStmt.Close()

	//Prepare statement for getting a specific device's ConnectionCount by DeviceID
	GetDeviceConnectionStmt, err = db.Prepare("SELECT ConnectionCount, ConnectionLimit FROM devices WHERE DeviceID = ?")
	if err != nil {
		log.Printf(err.Error() + "sql update GetDeviceConnectionStmt prepare")
	}
	defer GetDeviceConnectionStmt.Close()

	//Prepare statement selecting a device based on DeviceID
	deviceIDStmt, err = db.Prepare("SELECT * FROM devices WHERE DeviceID = ?")
	if err != nil {
		log.Printf(err.Error() + "sql select deviceIDStmt prepare")
	}
	defer deviceIDStmt.Close()

	//Prepare statement for selecting devices based on Location
	deviceLocationStmt, err = db.Prepare("SELECT * FROM devices WHERE Location = ?")
	if err != nil {
		log.Printf(err.Error() + "sql select deviceLocationStmt prepare")
	}
	defer deviceLocationStmt.Close()

	//Prepare statement for selecting devices based on Sensor(s)
	deviceSensorStmt, err = db.Prepare("SELECT * FROM devices WHERE Accelerometer = ? AND GPS = ? AND Light = ? AND Temperature = ? AND Orientation = ?")
	if err != nil {
		log.Printf(err.Error() + "sql select deviceSensorStmt prepare")
	}
	defer deviceSensorStmt.Close()

	//Prepare statement for selecting devices based on Sensors(s) and Location
	DeviceBySensorAndLocationStmt, err = db.Prepare("SELECT * FROM devices WHERE Accelerometer = ? AND GPS = ? AND Light = ? AND Temperature = ? AND Orientation = ? AND Location = ?")
	if err != nil {
		log.Printf(err.Error() + "sql select DeviceBySensorAndLocationStmt prepare")
	}
	defer DeviceBySensorAndLocationStmt.Close()

	//Prepare statement for deleteing a device based on DeviceID
	removeDeviceStmt, err = db.Prepare("DELETE FROM devices WHERE DeviceID = ?")
	if err != nil {
		log.Printf(err.Error() + "sql select removeDeviceStmt prepare")
	}
	defer removeDeviceStmt.Close()
	//End database query setup

	//Begin HTTP handling
	handler := rest.ResourceHandler{
		EnableRelaxedContentType: true,
	}
	handler.SetRoutes(
		rest.Route{"POST", "/device/add", AddDevice},
		rest.Route{"GET", "/device/:DeviceID", GetDeviceByID},
		rest.Route{"GET", "/device/connect/:DeviceID", DeviceConnect},
		rest.Route{"GET", "/device/disconnect/:DeviceID", DeviceDisconnect},
		rest.Route{"GET", "/device/loc/:Location", GetDeviceByLocation},
		rest.Route{"POST", "/device/set_loc", SetDeviceLocation},
		rest.Route{"POST", "/device/sensor", GetDeviceBySensorType},
		rest.Route{"POST", "/device/sensor_location", GetDeviceBySensorAndLocation},
		rest.Route{"DELETE", "/device/remove/:DeviceID", RemoveDevice},
		rest.Route{"GET", "/device/heartbeat/:DeviceID", SetDeviceHeatBeat},
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
	ConnectionCount string
	HeartBeat       string
	Accelerometer   string
	GPS             string
	Light           string
	Temperature     string
	Orientation     string
}

type LocationUpdate struct {
	DeviceID string
	Location string
}

//The struct stores the Sensors and location for finding a device based on this information
type SensorLocationQuery struct {
	Accelerometer string
	GPS           string
	Light         string
	Temperature   string
	Orientation   string
	Location      string
}

//The struct stores the sensors for finding a device based on this information
type Sensors struct {
	Accelerometer string
	GPS           string
	Light         string
	Temperature   string
	Orientation   string
}

//temporary assignment variables for adding a device or retrieving a device
//Used to transfer POST variables or MySQL rows to the Device struct
var (
	ID                    string
	DeviceID              string
	IPAddr                string
	ListenPort            string
	Location              string
	ConnectionLimit       string
	ConnectionCount       string
	HeartBeat             string
	Accelerometer         string
	GPS                   string
	Light                 string
	Temperature           string
	Orientation           string
	deviceConnectionCount int
	deviceConnectionLimit int
)

//The store is a map containing structs of type Device.
var store = map[string]*Device{}

//This function is used as health check by the load balancer
func HealthCheck(w *rest.ResponseWriter, r *rest.Request) {
	w.WriteJson("OK")
}

//This function sets the heartbeat for a specific device to the current unix time.
func SetDeviceHeatBeat(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	_, err := DeviceHeartBeatStmt.Exec(time.Now().Unix(), deviceID)
	if err != nil {
		log.Printf("Error running DeviceHeartBeatStmt %s", err.Error())
	}
	w.WriteJson("OK")
}

func DeviceConnect(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	rows, err := GetDeviceConnectionStmt.Query(deviceID)
	if err != nil {
		log.Printf("Error running GetDeviceConnectionStmt %s", err.Error())
	}
	for rows.Next() {
		err := rows.Scan(&deviceConnectionCount, &deviceConnectionLimit)
		if err != nil {
			log.Printf("Error scanning rows %s", err.Error())
		}
	}
	deviceConnectionCount = deviceConnectionCount + 1
	if deviceConnectionCount <= deviceConnectionLimit {
		_, err := UpdateDeviceConnectionStmt.Exec(deviceConnectionCount)
		if err != nil {
			log.Printf("Error running UpdateDeviceConnectionStmt %s", err.Error())
		}
		w.WriteJson("OK")
	} else {
		w.WriteJson("Connetion Limit Exceeded")
	}
}

func DeviceDisconnect(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	rows, err := GetDeviceConnectionStmt.Query(deviceID)
	if err != nil {
		log.Printf("Error running GetDeviceConnectionStmt %s", err.Error())
	}
	for rows.Next() {
		err = rows.Scan(&deviceConnectionCount, &deviceConnectionLimit)
		if err != nil {
			log.Printf("Error scanning rows %s", err.Error())
		}
	}
	deviceConnectionCount = deviceConnectionCount - 1
	_, err = UpdateDeviceConnectionStmt.Exec(deviceConnectionCount - 1)
	if err != nil {
		log.Printf("Error running UpdateDeviceConnectionStmt %s", err.Error())
	}
	w.WriteJson("OK")

}

//This function takes in SQL rows and returns a map of devices that were in that query
func ProcessDeviceQuery(rs *sql.Rows) []*Device {
	device := Device{}
	var devices []*Device //TODO: make this not an arbirary size
	i := 0
	for rs.Next() {
		err := rs.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &ConnectionCount, &HeartBeat, &Accelerometer, &GPS, &Light, &Temperature, &Orientation)
		if err != nil {
			log.Printf("Error scanning rows %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.ConnectionCount = ConnectionCount
		device.HeartBeat = HeartBeat
		device.Accelerometer = Accelerometer
		device.GPS = GPS
		device.Light = Light
		device.Temperature = Temperature
		device.Orientation = Orientation
		devices = append(devices, &device)

		i++
	}
	return devices
}

//This function queries the database returns the device matching the DeviceID provided.
func GetDeviceByID(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	rows, err := deviceIDStmt.Query(deviceID)
	if err != nil {
		log.Printf("Error running deviceIDStmt %s", err.Error())
	}
	devices := ProcessDeviceQuery(rows)
	w.WriteJson(&devices)

}

//This function queries the database and returns the Device(s) that have the Sensor(s) specified.
func GetDeviceBySensorType(w *rest.ResponseWriter, r *rest.Request) {
	sensors := Sensors{}
	err := r.DecodeJsonPayload(&sensors)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if sensors.Accelerometer == "" {
		rest.Error(w, "sensors Accelerometer t/f required", 400)
		return
	}
	if sensors.GPS == "" {
		rest.Error(w, "sensors GPS t/f required", 400)
		return
	}
	if sensors.Light == "" {
		rest.Error(w, "sensors Light t/f required", 400)
		return
	}
	if sensors.Temperature == "" {
		rest.Error(w, "sensors Temperature t/f required", 400)
		return
	}
	if sensors.Orientation == "" {
		rest.Error(w, "sensors Orientation t/f required", 400)
		return
	}
	rows, err := deviceSensorStmt.Query(sensors.Accelerometer, sensors.GPS, sensors.Light, sensors.Temperature, sensors.Orientation)
	if err != nil {
		log.Printf("Error running deviceSensorStmt %s", err.Error())
	}
	devices := ProcessDeviceQuery(rows)
	w.WriteJson(&devices)
}

//This function queries the database and returns the Device(s) that have a specific Sensor in a specific Location
func GetDeviceBySensorAndLocation(w *rest.ResponseWriter, r *rest.Request) {
	sensorLocationQuery := SensorLocationQuery{}
	err := r.DecodeJsonPayload(&sensorLocationQuery)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if sensorLocationQuery.Accelerometer == "" {
		rest.Error(w, "device sensor Accelerometer t/f required", 400)
		return
	}
	if sensorLocationQuery.GPS == "" {
		rest.Error(w, "device sensor GPS t/f required", 400)
		return
	}
	if sensorLocationQuery.Light == "" {
		rest.Error(w, "device sensor Light t/f required", 400)
		return
	}
	if sensorLocationQuery.Temperature == "" {
		rest.Error(w, "device sensor Temperature t/f required", 400)
		return
	}
	if sensorLocationQuery.Orientation == "" {
		rest.Error(w, "device sensor Orientation t/f required", 400)
		return
	}
	if sensorLocationQuery.Location == "" {
		rest.Error(w, "device Location required", 400)
		return
	}
	rows, err := DeviceBySensorAndLocationStmt.Query(sensorLocationQuery.Accelerometer, sensorLocationQuery.GPS, sensorLocationQuery.Light, sensorLocationQuery.Temperature, sensorLocationQuery.Orientation, sensorLocationQuery.Location)
	if err != nil {
		log.Printf("Error running DeviceBySensorAndLocationStmt %s", err.Error())
	}
	devices := ProcessDeviceQuery(rows)
	w.WriteJson(&devices)
}

//This function queries the database and returns the device(s) that are in a specific Location.
func GetDeviceByLocation(w *rest.ResponseWriter, r *rest.Request) {
	location := r.PathParam("Location")
	rows, err := deviceLocationStmt.Query(location)
	if err != nil {
		log.Printf("Error running deviceLocationStmt %s", err.Error())
	}
	devices := ProcessDeviceQuery(rows)
	w.WriteJson(&devices)
}

func SetDeviceLocation(w *rest.ResponseWriter, r *rest.Request) {
	updateLocation := LocationUpdate{}
	err := r.DecodeJsonPayload(&updateLocation)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	_, err = UpdateDeviceLocationStmt.Exec(updateLocation.Location, updateLocation.DeviceID)
	if err != nil {
		log.Printf("Error running UpdateDeviceLocationStmt %s", err.Error())
	}
	w.WriteJson(&updateLocation)
}

//This function adds a device to the database, and generates a DeviceID
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
	if device.GPS == "" {
		rest.Error(w, "device GPS t/f required", 400)
		return
	}
	if device.Light == "" {
		rest.Error(w, "device Light t/f required", 400)
		return
	}
	if device.Temperature == "" {
		rest.Error(w, "device Temperature t/f required", 400)
		return
	}
	if device.Orientation == "" {
		rest.Error(w, "device Orientation t/f required", 400)
		return
	}
	_, err = addDeviceStmt.Exec(0, device.DeviceID, device.IPAddr, device.ListenPort, device.Location, device.ConnectionLimit, device.Accelerometer, device.GPS, device.Light, device.Temperature, device.Orientation, time.Now().Unix(), 0)
	if err != nil {
		log.Printf("Error running addDeviceStmt %s", err.Error())
	}

	w.WriteJson(&device)

}

//This function removes a device from the database based on DeviceID
func RemoveDevice(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	_, err := removeDeviceStmt.Exec(deviceID)
	if err != nil {
		log.Printf("Error running removeDeviceStmt %s", err.Error())
	}
}
