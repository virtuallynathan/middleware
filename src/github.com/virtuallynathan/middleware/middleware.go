package main

import (
	"database/sql"
	"fmt"
	"net/http"

	"github.com/ant0ine/go-json-rest"
	_ "github.com/go-sql-driver/mysql"
)

func main() {

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
	)
	http.ListenAndServe(":8080", &handler)
	//go ConnectToDatabase()

}

func ConnectToDatabase() {
	//Begin database conneciton setup
	db, err := sql.Open("mysql", "root:compmgmt123@tcp(127.0.0.1:3306)/middleware")
	if err != nil {
		fmt.Printf("error, could not open sql connection")
	}
	defer db.Close()

	err = db.Ping()
	if err != nil {
		fmt.Printf("error, could not connect to database.")
	}
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

//The store is a map containing structs of type Device.
var store = map[string]*Device{}

//This function searches the store and returns the device matching the ID provided.
func GetDeviceById(w *rest.ResponseWriter, r *rest.Request) {
	DeviceID := r.PathParam("DeviceID")
	device := store[DeviceID]
	if device == nil {
		rest.NotFound(w, r)
		return
	}
	w.WriteJson(&device)
}

//This function seatches the list of devices and returns the device(s) that have the sensor(s) specified.
func GetDeviceBySensorType(w *rest.ResponseWriter, r *rest.Request) {
	sensor := r.PathParam("Sensor")
	devices := make([]*Device, len(store))
	i := 0
	for _, device := range store {
		if device.Sensor == sensor {
			devices[i] = device
			i++
		}

	}
	w.WriteJson(&devices)
}

//This function seatches the list of devices and returns the device(s) that are in a specific loation.
func GetDeviceByLocation(w *rest.ResponseWriter, r *rest.Request) {
	location := r.PathParam("Location")
	devices := make([]*Device, len(store))
	i := 0
	for _, device := range store {
		if device.Location == location {
			devices[i] = device
			i++
		}

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
	store[device.DeviceID] = &device
	w.WriteJson(&device)

}

//This function removes a device from the store
func RemoveDevice(w *rest.ResponseWriter, r *rest.Request) {
	deviceID := r.PathParam("DeviceID")
	delete(store, deviceID)
}
