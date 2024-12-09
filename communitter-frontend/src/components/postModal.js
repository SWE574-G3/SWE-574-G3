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
import StringPostField from "./StringPostField";
import NumberPostField from "./NumberPostField";
import DatePostField from "./DatePostField";
import ImagePostField from "./ImagePostField";
import GeolocationPostField from "./GeolocationPostField";
import EnumerationPostField from "./EnumerationPostField";

const MakePostModal = ({ templates, isOpen, setIsOpen }) => {
  const [selectedTemplate, setSelectedTemplate] = useState(null);
  const [postFields, setPostFields] = useState([]);
  const community = useSelector((state) => state.community.visitedCommunity);
  const [displayButton, setDisplayButton] = useState(true);
  const dispatch = useDispatch();

  const handleClose = () => {
    setSelectedTemplate(null);
    setPostFields([]);
    setDisplayButton(true);
    setIsOpen(false);
  };

  const handleTemplateChange = (event) => {
    if (!event.target.value) {
      setSelectedTemplate(null);
      setPostFields([]);
      return;
    }
    const selectedName = event.target.value;
    const template = templates.find((t) => t.name === selectedName);
    setSelectedTemplate(template);
    const initFields = template.dataFields.map((field) => {
      return { value: "", dataField: { ...field } };
    });
    setPostFields(initFields);
  };

  const handlePostChange = (index, value) => {
    let updatedPostFields = [...postFields];
    updatedPostFields[index].value = value;
    setPostFields(updatedPostFields);
  };

  const handleCreatePost = async () => {
    setDisplayButton(false);
    const postData = {
      date: new Date(),
      template: { id: selectedTemplate?.id, name: selectedTemplate?.name },
      postFields: postFields.filter((postField) => postField.value !== ""),
    };
    try {
      if(!postData.template.id || !postData.template.name) throw new Error("Please choose a template first")
      const data = await fetchWithOpts(
        `${url}/posts/community/${community.id}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(postData),
        }
      );
      dispatch(
        setVisitedCommunity({
          ...community,
          posts: [...community.posts, data],
        })
      );
    } catch (err) {
      dispatch(setErrorMessage(err.message));
    }
    handleClose();
  };

  const renderPostFields = () => {
    if (!postFields.length) return null;

    return postFields.map((postField, index) => {
      const { id, type } = postField.dataField.dataFieldType;
      let PostFieldComponent;

      switch (type) {
        case "string":
          PostFieldComponent = StringPostField;
          break;
        case "number":
          PostFieldComponent = NumberPostField;
          break;
        case "date":
          PostFieldComponent = DatePostField;
          break;
        case "image":
          PostFieldComponent = ImagePostField;
          break;
        case "geolocation":
          PostFieldComponent = GeolocationPostField;
          break;
        case "enumeration":
          PostFieldComponent = EnumerationPostField;
          break;
        default:
          console.error(`Unknown data postField type: ${type}`);
          return null;
      }

      return (
        <div key={postField.dataField.id} className="mb-3">
          <PostFieldComponent
            postField={postField}
            index={index}
            onChange={handlePostChange}
          />
        </div>
      );
    });
  };

  return (
    <Modal show={isOpen} onHide={handleClose}>
      <ModalHeader closeButton>
        <h5 className="modal-title">Make a Post</h5>
      </ModalHeader>
      <ModalBody>
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
        {renderPostFields()}
      </ModalBody>
      <ModalFooter>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button
          disabled={!displayButton}
          variant="primary"
          onClick={handleCreatePost}
        >
          Make a Post
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default MakePostModal;
