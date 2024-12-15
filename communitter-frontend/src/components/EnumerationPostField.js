const EnumerationPostField = ({ postField, index, onChange }) => (
    <>
        <label htmlFor={postField.dataField.name} className="form-label">
            {(postField.dataField.required ? "*" : "") + postField.dataField.name}{" "}
            {`(Type: ${postField.dataField.dataFieldType.type})`}
        </label>
        <select
            id={postField.dataField.name}
            className="form-select"
            value={postField.value}
            onChange={(e) => onChange(index, e.target.value)}
        >
            <option value="">Select an option</option>
            {postField.dataField.enumValues.map((enumObj, idx) => (
                <option key={idx} value={enumObj.value}>
                    {enumObj.value}
                </option>
            ))}
        </select>
    </>
);

export default EnumerationPostField;
