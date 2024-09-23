export const UserProfile = ({ shownUser }) => {
  return (
    <div className="container mt-5">
      <div className="row">
        <div className="col-md-2">
          <img
            src="https://beforeigosolutions.com/wp-content/uploads/2021/12/dummy-profile-pic-300x300-1.png"
            alt="Profile"
            className="img-thumbnail rounded-circle d-block img-fluid mx-auto img-fluid"
          />
        </div>
        <div className="col-md-8">
          <div className="card">
            <div className="card-header">
              <h2>{shownUser.username}</h2>
            </div>
            <ul className="list-group list-group-flush">
              <li className="list-group-item">Email: {shownUser.email}</li>
              <li className="list-group-item">Header: {shownUser.header}</li>
              <li className="list-group-item">About: {shownUser.about}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};
