import { useState, useEffect } from "react";
const NumberSearchField = ({ searchField, index, onChange, postFilters }) => {
  const [filter, setFilter] = useState({ type: "number", min: 0, max: 0 });
  const handleMin = (index, e) => {
    console.log(index);
    console.log(postFilters);
    setFilter({ ...filter, min: e.target.value });
    onChange(index, {
      ...postFilters[searchField.id],
      min: e.target.value === "" ? 0 : e.target.value,
    });
  };
  const handleMax = (index, e) => {
    console.log(index);
    console.log(postFilters);
    setFilter({ ...filter, max: e.target.value });
    onChange(index, {
      ...postFilters[searchField.id],
      max: e.target.value === "" ? 0 : e.target.value,
    });
  };

  return (
    <>
      <label htmlFor={searchField.name + "-min"} className="form-label">
        {searchField.name + ": Min Value"}{" "}
        {`(Type: ${searchField.dataFieldType.type})`}
      </label>
      <input
        type="number"
        className="form-control"
        id={searchField.name + "-min"}
        value={filter.min}
        onChange={(e) => handleMin(searchField.id, e)}
      />
      <label htmlFor={searchField.name + "-max"} className="form-label">
        {searchField.name + ": Max Value"}{" "}
        {`(Type: ${searchField.dataFieldType.type})`}
      </label>
      <input
        type="number"
        className="form-control"
        id={searchField.name + "-max"}
        value={filter.max}
        onChange={(e) => handleMax(searchField.id, e)}
      />
    </>
  );
};

export default NumberSearchField;
