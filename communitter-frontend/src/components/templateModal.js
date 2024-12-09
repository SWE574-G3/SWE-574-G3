import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import ModalBody from "react-bootstrap/ModalBody";
import ModalHeader from "react-bootstrap/ModalHeader";
import Modal from "react-bootstrap/Modal";
import ModalFooter from "react-bootstrap/ModalFooter";
import { useDispatch, useSelector } from "react-redux";
import { setErrorMessage } from "../features/errorSlice";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { setVisitedCommunity } from "../features/communitySlice";

export const TemplateModal = ({ setIsOpen, isOpen }) => {
  const [fields, setFields] = useState([]);
  const [templateName, setTemplateName] = useState("");
  const [displayButton, setDisplayButton] = useState(true);
  const community = useSelector((state) => state.community.visitedCommunity);
  const dispatch = useDispatch();
  const [dataFieldTypes, setDataFieldTypes] = useState([
    {
      id: 1,
      type: "string",
    },
    {
      id: 2,
      type: "number",
    },
    {
      id: 3,
      type: "date",
    },
    {
      id: 4,
      type: "image",
    },
    {
      id: 5,
      type: "geolocation",
    },
    {
      id: 6,
      type: "enumeration",
    },
  ]);
  const [chosenFieldType, setChosenFieldType] = useState(
    dataFieldTypes[0].type
  );

  const handleClose = () => {
    setIsOpen(false);
    setDisplayButton(true);
    setFields([]);
  };

  const handleAddfield = () => {
    const chosenTypeObject = dataFieldTypes.find(
        (type) => type.type === chosenFieldType
    );
    setFields([
      ...fields,
      {
        name: "",
        dataFieldType: { type: chosenFieldType, id: chosenTypeObject.id },
        required: true,
        ...(chosenFieldType === "enumeration" ? { enumValues: [] } : {}),
      },
    ]);
  };

  const handleDeleteField = (index) => {
    const updatedFields = [...fields];
    updatedFields.splice(index, 1);
    setFields(updatedFields);
  };
  const handleRequiredChange = (event, index) => {
    const updatedFields = [...fields];
    updatedFields[index].required = event.target.checked;
    setFields(updatedFields);
  };
  const handleFieldChange = (event, index) => {
    console.log("fields", fields);
    const updatedFields = [...fields];
    updatedFields[index] = {
      ...updatedFields[index],
      [event.target.name]: event.target.value,
    };
    setFields(updatedFields);
  };
  const handleFieldEnumValueChange = (event, index) => {
    const updatedFields = [...fields];
    const values = event.target.value.split(",").map((val) => ({ value: val.trim() }));
    updatedFields[index].enumValues = values;
    setFields(updatedFields);
  };

  const handleCreateTemplate = async (e) => {
    e.preventDefault();
    setDisplayButton(false);

    const templateData = { name: templateName, dataFields: fields };
    try {
      const data = await fetchWithOpts(
        `${url}/templates/community/${community.id}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(templateData),
        }
      );
      dispatch(
        setVisitedCommunity({
          ...community,
          templates: [...community.templates, data],
        })
      );
    } catch (err) {
      dispatch(setErrorMessage(err.message));
    }
    handleClose();
  };

  const renderFieldComponents = () => {
    return fields.map((field, index) => (
      <div
        key={index}
        className="mb-3 d-flex justify-content-between align-items-center"
      >
        <div className="d-flex align-items-center me-3">
          <input
            type="checkbox"
            className="form-check-input me-2"
            checked={field.required}
            onChange={(e) => handleRequiredChange(e, index)}
          />
          <label className="form-check-label">Required</label>
        </div>
        <div className="w-50">
          <input
            type="text"
            className="form-control"
            placeholder="Field Name"
            name="name"
            required
            value={field.name}
            onChange={(e) => handleFieldChange(e, index)}
          />
          {field.dataFieldType.type === "enumeration" && (
              <div className="mt-2">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Enter enum values, separated by commas"
                    value={field.enumValues.map((enumObj) => enumObj.value).join(", ")}
                    onChange={(e) => handleFieldEnumValueChange(e, index)}
                />
              </div>
          )}
        </div>
        <div className="w-50">
          <input
            className="form-control"
            value={field.dataFieldType.type}
            disabled
          ></input>
        </div>
        <button
          className="btn btn-sm btn-danger"
          onClick={() => handleDeleteField(index)}
        >
          Delete
        </button>
      </div>
    ));
  };

  return (
    <Modal show={isOpen} onHide={handleClose}>
      <ModalHeader closeButton>
        <h5 className="modal-title">Create Template</h5>
      </ModalHeader>
      <ModalBody>
        <div className="mb-3">
          <label htmlFor="templateName" className="form-label">
            Template Name
          </label>
          <input
            type="text"
            className="form-control"
            id="templateName"
            form="templateForm"
            required
            placeholder="Enter Template Name"
            value={templateName}
            onChange={(e) => setTemplateName(e.target.value)}
          />
        </div>
        <div className="mb-3 d-flex">
          <Button variant="primary" size="sm" onClick={handleAddfield}>
            Add Field
          </Button>
          <select
            className="form-select ms-2"
            value={chosenFieldType}
            onChange={(e) => {
              setChosenFieldType(e.target.value);
            }}
          >
            {dataFieldTypes.map((type, i) => (
              <option key={type.type} value={type.type}>
                {type.type}
              </option>
            ))}
          </select>{" "}
        </div>
        <form id="templateForm" onSubmit={handleCreateTemplate}>
          {renderFieldComponents()}
        </form>
      </ModalBody>
      <ModalFooter>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button
          disabled={!displayButton}
          variant="primary"
          type="submit"
          form="templateForm"
        >
          Create Template
        </Button>
      </ModalFooter>
    </Modal>
  );
};
