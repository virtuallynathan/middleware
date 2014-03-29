package main

import (
	"database/sql"
	"log"
	"net/http"
	"runtime"

	"github.com/ant0ine/go-json-rest"
	_ "github.com/go-sql-driver/mysql"
)

var db *sql.DB
var addDeviceStmt *sql.Stmt
var removeDeviceStmt *sql.Stmt
var deviceIDStmt *sql.Stmt
var deviceLocationStmt *sql.Stmt
var deviceSensorStmt *sql.Stmt

func main() {

	runtime.GOMAXPROCS(runtime.NumCPU())
	//Begin database conneciton
	db, err := sql.Open("mysql", "virtuallynathan@cloudsql(component-tech-middleware:db1)/middleware")
	if err != nil {
		log.Fatalf("Error opening database: %v", err)
	}
	defer db.Close()

	err = db.Ping()
	if err != nil {
		log.Fatalf("Error connecting to database: %v", err)
	}

	// Prepare statement for inserting data
	addDeviceStmt, err = db.Prepare("INSERT INTO devices VALUES( ?, ?, ?, ?, ?, ?, ? )") // ? = placeholder
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

	deviceSensorStmt, err = db.Prepare("SELECT * FROM devices WHERE Sensor = ?")
	if err != nil {
		log.Fatalf(err.Error() + "sql select deviceSensorStmt prepare")
	}
	defer deviceSensorStmt.Close()

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
		rest.Route{"GET", "/device/:DeviceID", GetDeviceById},
		rest.Route{"GET", "/device/loc/:Location", GetDeviceByLocation},
		rest.Route{"GET", "/device/sensor/:Sensor", GetDeviceBySensorType},
		rest.Route{"DELETE", "/device/remove/:DeviceID", RemoveDevice},
		rest.Route("GET", "/health/:check", HealthCheck)
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
	Sensor          string
}

//temporary assignment variables
var (
	ID              string
	DeviceID        string
	IPAddr          string
	ListenPort      string
	Location        string
	ConnectionLimit string
	Sensor          string
)

//The store is a map containing structs of type Device.
var store = map[string]*Device{}

func HealthCheck(w *rest.ResponseWriter, r *rest.Request) {
	w.WriteJson("OK")
}

//This function searches the store and returns the device matching the ID provided.
func GetDeviceById(w *rest.ResponseWriter, r *rest.Request) {
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
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Sensor)
		if err != nil {
			log.Fatalf("Error scanning rows deviceIDStmt %s", err.Error())
		}
		device.DeviceID = deviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Sensor = Sensor
		devices[i] = &device

		i++
	}
	w.WriteJson(&devices)

}

//This function seatches the list of devices and returns the device(s) that have the sensor(s) specified.
func GetDeviceBySensorType(w *rest.ResponseWriter, r *rest.Request) {
	sensor := r.PathParam("Sensor")
	devices := make([]*Device, 100) //TODO: fix arbitrary size thing...
	device := Device{}
	rows, err := deviceSensorStmt.Query(sensor)
	if err != nil {
		log.Fatalf("Error running deviceSensorStmt %s", err.Error())
	}

	//TODO: put this shit in a function, DRY.
	i := 0
	for rows.Next() {
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Sensor)
		if err != nil {
			log.Fatalf("Error scanning rows deviceSensorStmt %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Sensor = Sensor
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
		err := rows.Scan(&ID, &DeviceID, &IPAddr, &ListenPort, &Location, &ConnectionLimit, &Sensor)
		if err != nil {
			log.Fatalf("Error scanning rows deviceLocationStmt %s", err.Error())
		}
		device.DeviceID = DeviceID
		device.IPAddr = IPAddr
		device.ListenPort = ListenPort
		device.Location = Location
		device.ConnectionLimit = ConnectionLimit
		device.Sensor = Sensor
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
	if device.DeviceID == "" {
		rest.Error(w, "device id required", 400)
		return
	}
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
	if device.Sensor == "" {
		rest.Error(w, "device Sensor required", 400)
		return
	}
	_, err = addDeviceStmt.Exec(0, device.DeviceID, device.IPAddr, device.ListenPort, device.Location, device.ConnectionLimit, device.Sensor)
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
