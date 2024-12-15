const ErrorMessage = ({ message }) => {
  return (
    <div
      className="alert alert-danger fixed-top w-25 mx-auto p-2 d-flex justify-content-center align-items-center"
      role="alert"
      style={{marginTop: "64px"}}
    >
      {message}
    </div>
  );
};

export default ErrorMessage;
