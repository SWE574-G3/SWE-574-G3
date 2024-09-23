import { useState, useEffect } from "react";
const StringSearchField = ({ searchField, index, onChange }) => {
  const [filter, setFilter] = useState({ type: "string", value: "" });
  const handleChange = (index, e) => {
    setFilter((filter) => {
      return { ...filter, value: e.target.value };
    });
    onChange(index, { type: "string", value: e.target.value });
  };

  return (
    <>
      <label htmlFor={searchField.name} className="form-label">
        {searchField.name} {`(Type: ${searchField.dataFieldType.type})`}
      </label>
      <input
        type="text"
        className="form-control"
        id={searchField.id}
        value={filter.value}
        onChange={(e) => handleChange(searchField.id, e)}
      />
    </>
  );
};

export default StringSearchField;
