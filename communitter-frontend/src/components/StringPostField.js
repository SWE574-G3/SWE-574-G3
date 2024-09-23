const StringPostField = ({ postField, index, onChange }) => (
  <>
    <label htmlFor={postField.dataField.name} className="form-label">
      {(postField.dataField.required ? "*" : "") + postField.dataField.name}{" "}
      {`(Type: ${postField.dataField.dataFieldType.type})`}
    </label>
    <input
      type="text"
      className="form-control"
      id={postField.dataField.name}
      value={postField.value}
      onChange={(e) => onChange(index, e.target.value)}
    />
  </>
);

export default StringPostField;
