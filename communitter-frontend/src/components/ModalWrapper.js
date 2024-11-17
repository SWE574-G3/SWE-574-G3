export function ModalWrapper({ show, handleClose, children }) {
    console.log("ModalWrapper handleClose: ", handleClose);
  
    return (
      <>
        {show && (
          <>
            {/* Modal Container */}
            <div className="modal d-block" tabIndex="-1" style={{ zIndex: 1050 }}>
              <div
                className="modal-dialog modal-dialog-centered"
                style={{
                  width: "75vw !important",  // Force width to 75% of viewport width
                  height: "75vh !important", // Force height to 75% of viewport height
                  maxWidth: "none", // Remove any maxWidth restrictions
                  zIndex: 1060, // Ensure modal content is above overlay
                }}
              >
                <div className="modal-content" style={{ height: "100%" }}>
                  <div className="modal-header" style={{ zIndex: 1070 }}>
                    <button
                      type="button"
                      className="btn-close"
                      aria-label="Close"
                      onClick={() => {
                        handleClose(false); // Close the modal
                      }}
                    ></button>
                  </div>
                  <div
                    className="modal-body"
                    style={{
                      height: "calc(75vh - 56px)", // Subtract header height (adjust if necessary)
                      overflowY: "auto", // Allow scroll if content exceeds body height
                    }}
                  >
                    {children}
                  </div>
                </div>
              </div>
            </div>
            {/* Modal Overlay */}
            <div
              className="modal-backdrop fade show"
              style={{ zIndex: 1040 }} // Ensure the overlay is behind the modal content
              onClick={() => handleClose(false)}
            ></div>
          </>
        )}
      </>
    );
  }
  