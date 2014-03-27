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
		//rest.Route{"GET", "/device/:id", GetDeviceById},
		//rest.Route{"POST", "/device/sensor", GetDeviceBySensorType},
		///rest.Route{"GET", "/device/:location", GetDeviceByLocation},
		//rest.Route{"DELETE", "/device/:id", RemoveDevice}
	)
	http.ListenAndServe(":8080", &handler)
}

type Device struct {
	DeviceId        string
	IpAddr          string
	ListenPort      string
	DeviceType      string
	Location        string
	ConnectionLimit string
	//sensors         []string
}

var store = map[string]*Device{}

func GetDeviceById(w *rest.ResponseWriter, r *rest.Request) {

}

func GetDeviceBySensorType(w *rest.ResponseWriter, r *rest.Request) {

}

func GetDeviceByLocation(w *rest.ResponseWriter, r *rest.Request) {

}

func AddDevice(w *rest.ResponseWriter, r *rest.Request) {
	device := Device{}
	err := r.DecodeJsonPayload(&device)
	if err != nil {
		rest.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if device.DeviceId == "" {
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
	store[device.DeviceId] = &device
	w.WriteJson(&device)

}

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
