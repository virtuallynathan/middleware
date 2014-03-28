package main

import (
	"github.com/ant0ine/go-json-rest"

	"net/http"
)

func main() {

	handler := rest.ResourceHandler{
		EnableRelaxedContentType: true,
	}
	handler.SetRoutes(
		rest.Route{"POST", "/device/add", AddDevice},
		rest.Route{"GET", "/device/:DeviceID", GetDeviceById},
		rest.Route{"GET", "/device/loc/:Location", GetDeviceByLocation},
		rest.Route{"GET", "/device/sensor/:Sensor", GetDeviceBySensorType},
		//rest.Route{"DELETE", "/device/:DeviceID", RemoveDevice}
	)
	http.ListenAndServe(":8080", &handler)
}

//The struct of type Device stores all the information about a single device.
type Device struct {
	DeviceID        string
	IpAddr          string
	ListenPort      string
	DeviceType      string
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
	location := r.PathParam("DeviceLocation")
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
	if device.IpAddr == "" {
		rest.Error(w, "device ipAddr required", 400)
		return
	}
	if device.ListenPort == "" {
		rest.Error(w, "device listenPort required", 400)
		return
	}
	if device.DeviceType == "" {
		rest.Error(w, "device type required", 400)
		return
	}
	if device.Location == "" {
		rest.Error(w, "device location required", 400)
		return
	}
	if device.ConnectionLimit == "" {
		rest.Error(w, "device connectionLimit required", 400)
		return
	}
	store[device.DeviceID] = &device
	w.WriteJson(&device)

}

//This function removes a device from the store
func RemoveDevice(w *rest.ResponseWriter, r *rest.Request) {

}

//demo code to figure out what i'm doing
/*
func GetCountry(w *rest.ResponseWriter, r *rest.Request) {
	code := r.PathParam("code")
	country := store[code]
	if country == nil {
		rest.NotFound(w, r)
		return
	}
	w.WriteJson(&country)
}

func GetAllCountries(w *rest.ResponseWriter, r *rest.Request) {
	countries := make([]*Country, len(store))
	i := 0
	for _, country := range store {
		countries[i] = country
		i++
	}
	w.WriteJson(&countries)
}

func PostCountry(w *rest.ResponseWriter, r *rest.Request) {
	country := Country{}
	err := r.DecodeJsonPayload(&country)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if country.Code == "" {
		rest.Error(w, "country code required", 400)
		return
	}
	if country.Name == "" {
		rest.Error(w, "country name required", 400)
		return
	}
	store[country.Code] = &country
	w.WriteJson(&country)
}

func DeleteCountry(w *rest.ResponseWriter, r *rest.Request) {
	code := r.PathParam("code")
	delete(store, code)
}
*/
