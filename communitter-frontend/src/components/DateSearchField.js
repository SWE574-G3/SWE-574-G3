import { useState, useEffect } from "react";
const DateSearchField = ({ searchField, index, onChange, postFilters }) => {
  const [filter, setFilter] = useState({ type: "date", start: 0, end: 0 });
  const handleStart = (index, e) => {
    setFilter({ ...filter, start: e.target.value });
    onChange(index, { ...postFilters[searchField.id], start: e.target.value });
  };
  const handleEnd = (index, e) => {
    setFilter({ ...filter, end: e.target.value });
    onChange(index, { ...postFilters[searchField.id], end: e.target.value });
  };

  return (
    <>
      <label htmlFor={searchField.name + "-start"} className="form-label">
        {searchField.name + ": Start Date"}{" "}
        {`(Type: ${searchField.dataFieldType.type})`}
      </label>
      <input
        type="text"
        className="form-control"
        id={searchField.name + "-start"}
        value={filter.start}
        onChange={(e) => handleStart(searchField.id, e)}
      />
      <p className="form-text text-muted">Date format: YYYY-MM-DD</p>
      <label htmlFor={searchField.name + "-end"} className="form-label">
        {searchField.name + ": End Date"}{" "}
        {`(Type: ${searchField.dataFieldType.type})`}
      </label>
      <input
        type="text"
        className="form-control"
        id={searchField.name + "-end"}
        value={filter.end}
        onChange={(e) => handleEnd(searchField.id, e)}
      />
      <p className="form-text text-muted">Date format: YYYY-MM-DD</p>
    </>
  );
};

export default DateSearchField;
