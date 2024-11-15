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
import StringSearchField from "./StringSearchField";
import NumberSearchField from "./NumberSearchField";
import DateSearchField from "./DateSearchField";
import GeolocationSearchField from "./GeolocationSearchField";
import { FilterPost } from "./Filters";

const AdvancedSearchModal = ({
  posts,
  setPosts,
  templates,
  isOpen,
  setIsOpen,
}) => {
  console.log(posts);
  const [selectedTemplate, setSelectedTemplate] = useState(null);
  const [postFilters, setPostFilters] = useState([]);
  const community = useSelector((state) => state.community.visitedCommunity);
  const [displayButton, setDisplayButton] = useState(true);
  const dispatch = useDispatch();

  const handleClose = () => {
    setSelectedTemplate(null);
    setPostFilters([]);
    setDisplayButton(true);
    setIsOpen(false);
  };

  const handleTemplateChange = (event) => {
    if (!event.target.value) {
      setSelectedTemplate(null);
      setPostFilters([]);
      return;
    }
    const selectedName = event.target.value;
    const template = templates.find((t) => t.name === selectedName);
    setSelectedTemplate(template);
    const initFilters = {};
    template.dataFields.forEach((field) => {
      let filterObject;
      switch (field.dataFieldType.type) {
        case "string":
          filterObject = { type: "string", value: "" };
          break;
        case "number":
          filterObject = { type: "number", min: 0, max: 0 };
          break;
        case "date":
          filterObject = { type: "date", start: 0, end: 0 };
          break;
        case "image":
          filterObject = { type: "image"};
          break;
        case "geolocation":
          filterObject = {
            type: "geolocation",
            coords: "41.083556,29.050598",
            proximity: 0,
          };
          break;
        default:
          console.log(
            `Unknown data postField type: ${field.dataFieldType.type}`
          );
      }
      filterObject.activated=false;
      initFilters[field.id] = filterObject;
    });
    setPostFilters(initFilters);
  };

  const handleFilterChange = (index, value) => {
    setPostFilters((postFilters) => {
      let updatedPostFilters = { ...postFilters };
      updatedPostFilters[index] = {...value,activated:true};
      return updatedPostFilters;
    });
  };

  const handleFiltering = () => {
    const templatePosts = community.posts.filter(
      (post) => post.template.id == selectedTemplate.id
    );
    const filteredPosts = templatePosts.filter((post) => {
      let result = FilterPost(post, postFilters);
      return result;
    });
    setPosts(filteredPosts);
    handleClose();
  };

  const renderSearchFields = () => {
    if (!selectedTemplate?.dataFields.length) return null;

    return selectedTemplate.dataFields.map((searchField, index) => {
      const { id, type } = searchField.dataFieldType;
      let SearchFieldComponent;

      switch (type) {
        case "string":
          SearchFieldComponent = StringSearchField;
          break;
        case "number":
          SearchFieldComponent = NumberSearchField;
          break;
        case "date":
          SearchFieldComponent = DateSearchField;
          break;
        case "image":
          return (
            <p>{`${searchField.name}`}: Filter is not possible for image.</p>
          );
        case "geolocation":
          SearchFieldComponent = GeolocationSearchField;
          break;
        default:
          console.log(`Unknown data postField type: ${type}`);
          return null;
      }

      return (
        <div key={searchField.id} className="mb-3">
          <SearchFieldComponent
            postFilters={postFilters}
            searchField={searchField}
            index={index}
            onChange={handleFilterChange}
          />
        </div>
      );
    });
  };

  return (
    <Modal show={isOpen} onHide={handleClose}>
      <ModalHeader closeButton>
        <h5 className="modal-title">Search In Posts</h5>
      </ModalHeader>
      <ModalBody>
        <p>
          Leave the filters at default values if you want to avoid filtering for
          them
        </p>
        <div className="mb-3">
          <label htmlFor="selectTemplate" className="form-label">
            Select Template
          </label>
          <select
            id="selectTemplate"
            className="form-select"
            value={selectedTemplate?.name || ""}
            onChange={handleTemplateChange}
          >
            <option value="">Choose a Template</option>
            {templates.map((template) => (
              <option key={template.id} value={template.name}>
                {template.name}
              </option>
            ))}
          </select>
        </div>
        {renderSearchFields()}
      </ModalBody>
      <ModalFooter>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button
          disabled={!displayButton}
          variant="primary"
          onClick={handleFiltering}
        >
          Filter
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AdvancedSearchModal;
