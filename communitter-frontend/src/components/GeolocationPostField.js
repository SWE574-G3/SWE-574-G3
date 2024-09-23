import { useState, useEffect } from "react";
import { APIProvider, Map, Marker } from "@vis.gl/react-google-maps";

const libraries = ["places"];
const GeolocationPostField = ({ postField, index, onChange }) => {
  const [coords, setCoords] = useState("41.083556,29.050598");
  const handleMapClick = (event) => {
    const lat = event.detail.latLng.lat;
    const lng = event.detail.latLng.lng;
    const newCoords = `${lat},${lng}`;
    setCoords(newCoords);
    onChange(index, newCoords);
  };
  const mapContainerStyle = {
    width: "100%",
    height: "200px",
  };
  return (
    <>
      <label htmlFor={postField.dataField.name} className="form-label">
        {(postField.dataField.required ? "*" : "") + postField.dataField.name}{" "}
        {`(Type: ${postField.dataField.dataFieldType.type})`}
      </label>
      <APIProvider apiKey={"AIzaSyCMjJifNTIdI562qZqU4NTs5GfepPLWl4A"}>
        <Map
          mapId={"Communitter Geolocation"}
          reuseMaps={true}
          onClick={handleMapClick}
          defaultCenter={{ lat: 41.083556, lng: 29.050598 }}
          defaultZoom={10}
          style={mapContainerStyle}
        >
          {coords && (
            <Marker
              position={{
                lat: parseFloat(coords.split(",")[0].trim()),
                lng: parseFloat(coords.split(",")[1].trim()),
              }}
            />
          )}
        </Map>
      </APIProvider>
      <p className="form-text text-muted">Choose the location on the map</p>
    </>
  );
};

export default GeolocationPostField;
