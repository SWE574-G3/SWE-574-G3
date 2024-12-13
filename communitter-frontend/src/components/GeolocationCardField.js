import CardText from "react-bootstrap/CardText";
import { APIProvider, Map, Marker } from "@vis.gl/react-google-maps";
import { Modal, Button, Form } from "react-bootstrap";
import {useEffect, useRef, useState} from "react";
import {type} from "@testing-library/user-event/dist/type";

const GeolocationCardField = ({ value, dataField, isEditable, onChange }) => {

  const [lat, setLat] = useState(parseFloat(value.split(",")[0].trim()));
  const [lng, setLng] = useState(parseFloat(value.split(",")[1].trim()));
  const [markerPosition, setMarkerPosition] = useState({ lat, lng });
  const mapRef = useRef(null)


  useEffect(() => {
    // Update the marker's position if the parent changes the value
    const [newLat, newLng] = value.split(",").map((coord) => parseFloat(coord.trim()));
    setLat(newLat);
    setLng(newLng);
    setMarkerPosition({ lat: newLat, lng: newLng });
  }, [value]);

  console.log("VALUE", value)
  const mapContainerStyle = {
    width: "100%",
    height: "200px",
  };

  const handleMapClick = (event) => {
    console.log("EVENT", event)
    const newLat = event?.detail?.latLng?.lat;
    const newLng = event?.detail?.latLng?.lng;

    setLat(newLat);
    setLng(newLng);
    setMarkerPosition({ lat: newLat, lng: newLng });
    const newValue = `${newLat},${newLng}`;
    if (onChange) {
      onChange(newValue); // Pass the new value to the parent component
    }
  };

  return (
    <>
      <APIProvider apiKey={"AIzaSyCMjJifNTIdI562qZqU4NTs5GfepPLWl4A"}>
        <CardText className="d-flex">
          <div>{dataField.name}:</div>
          {isEditable ?(
              <Map
                  mapId={"Communitter Geolocation"}
                  reuseMaps={true}
                  defaultCenter={markerPosition}
                  defaultZoom={10}
                  style={mapContainerStyle}
                  onClick={ handleMapClick}
                  onLoad={(map) => {
                    mapRef.current = map; // Store the map instance in the ref
                  }}
              >
                {dataField && (
                    <Marker
                        position={markerPosition}
                    />
                )}
              </Map>
          ):(
          <Map
            mapId={"Communitter Geolocation"}
            reuseMaps={true}
            center={markerPosition}
            defaultZoom={10}
            style={mapContainerStyle}
          >
            {dataField && (
              <Marker
                  position={markerPosition}
              />
            )}

          </Map>
        )}

        </CardText>
      </APIProvider>

    </>
  );
};

export default GeolocationCardField;
