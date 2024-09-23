const ImagePostField = ({ postField, index, onChange }) => {
  const handleUpload = (index, e) => {
    const file = e.target.files[0];
    const reader = new FileReader();

    if (file) {
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        onChange(index, e.target.result);
      };
    } else {
      onChange(index, "");
    }
  };

  return (
    <>
      <label htmlFor={postField.dataField.name} className="form-label">
        {(postField.dataField.required ? "*" : "") + postField.dataField.name}{" "}
        {`(Type: ${postField.dataField.dataFieldType.type})`}
      </label>
      <input
        type="file"
        className="form-control"
        id={postField.dataField.name}
        onChange={(e) => handleUpload(index, e)}
      />
    </>
  );
};

export default ImagePostField;
