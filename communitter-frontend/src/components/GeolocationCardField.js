import CardText from "react-bootstrap/CardText";
import { APIProvider, Map, Marker } from "@vis.gl/react-google-maps";
const GeolocationCardField = ({ value, dataField }) => {
  const lat = parseFloat(value.split(",")[0].trim());
  const lng = parseFloat(value.split(",")[1].trim());
  const mapContainerStyle = {
    width: "100%",
    height: "200px",
  };
  return (
    <>
      <APIProvider apiKey={"AIzaSyCMjJifNTIdI562qZqU4NTs5GfepPLWl4A"}>
        <CardText className="d-flex">
          <div>{dataField.name}:</div>
          <Map
            mapId={"Communitter Geolocation"}
            reuseMaps={true}
            defaultCenter={{ lat, lng }}
            defaultZoom={10}
            style={mapContainerStyle}
          >
            {dataField && (
              <Marker
                position={{
                  lat,
                  lng,
                }}
              />
            )}
          </Map>
        </CardText>
      </APIProvider>
    </>
  );
};

export default GeolocationCardField;
