import { useState, useEffect } from "react";
import { APIProvider, Map, Marker } from "@vis.gl/react-google-maps";

const GeolocationSearchField = ({
  searchField,
  index,
  onChange,
  postFilters,
}) => {
  const [filter, setFilter] = useState({
    type: "geolocation",
    coords: "41.083556,29.050598",
    proximity: 0,
  });
  const handleMapClick = (event) => {
    const lat = event.detail.latLng.lat;
    const lng = event.detail.latLng.lng;
    const newCoords = `${lat},${lng}`;
    setFilter({ ...filter, coords: newCoords });
    onChange(searchField.id, {
      ...postFilters[searchField.id],
      coords: newCoords,
    });
  };

  const handleProximity = (e) => {
    setFilter({ ...filter, proximity: e.target.value });
    onChange(searchField.id, {
      ...postFilters[searchField.id],
      proximity: e.target.value,
    });
  };
  const mapContainerStyle = {
    width: "100%",
    height: "200px",
  };

  return (
    <>
      <label htmlFor={searchField.name} className="form-label">
        {searchField.name} {`(Type: ${searchField.dataFieldType.type})`}
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
          {filter.coords && (
            <Marker
              position={{
                lat: parseFloat(filter.coords.split(",")[0].trim()),
                lng: parseFloat(filter.coords.split(",")[1].trim()),
              }}
            />
          )}
        </Map>
      </APIProvider>
      <p className="form-text text-muted">Choose the location on the map</p>
      <label htmlFor={searchField.name + "-proximity"} className="form-label">
        {searchField.name + ": Proximity in kms"}{" "}
        {`(Type: ${searchField.dataFieldType.type})`}
      </label>
      <input
        type="number"
        className="form-control"
        id={searchField.name + "-proximity"}
        value={filter.max}
        onChange={(e) => handleProximity(e)}
      />
      <p className="form-text text-muted">
        Define how close the target should be to the chosen location above
      </p>
    </>
  );
};

export default GeolocationSearchField;
