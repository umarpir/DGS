import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import Modal from "./modal.jsx";
import "./Modal.css";

const Organisation = () => {
  const { id } = useParams();
  const [organisation, setOrganisation] = useState(null);
  const [personnel, setPersonnel] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editType, setEditType] = useState("");
  const [formData, setFormData] = useState({});

  useEffect(() => {
    const fetchOrganisation = async () => {
      try {
        const response = await fetch(
          `http://localhost:8085/dgs-api/v1/organisations/${id}`
        );
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setOrganisation(data);
      } catch (error) {
        console.error("There was an error fetching the organisation!", error);
      }
    };

    const fetchPersonnel = async () => {
      try {
        const response = await fetch(
          `http://localhost:8085/dgs-api/v1/personnel/organisation/${id}`
        );
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setPersonnel(data);
      } catch (error) {
        console.error("There was an error fetching the personnel!", error);
      }
    };

    fetchOrganisation();
    fetchPersonnel();
  }, [id]);

  const handleEdit = (type, data) => {
    setEditType(type);
    setFormData(data);
    setShowModal(true);
  };

  const handleAdd = () => {
    setEditType("add_personnel");
    setFormData({
      firstName: "",
      lastName: "",
      username: "",
      password: "",
      email: "",
      telephoneNumber: "",
    });
    setShowModal(true);
  };

  const handleClose = () => {
    setShowModal(false);
    setFormData({});
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    let url = "";
    let method = "";

    if (editType === "organisation") {
      url = `http://localhost:8085/dgs-api/v1/organisations/${formData.id}`;
      method = "PUT";
    } else if (editType === "personnel") {
      url = `http://localhost:8085/dgs-api/v1/personnel/${formData.id}`;
      method = "PUT";
    } else if (editType === "add_personnel") {
      url = `http://localhost:8085/dgs-api/v1/personnel/${id}`;
      method = "POST";
    }

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      if (editType === "organisation") {
        setOrganisation(data);
      } else if (editType === "personnel") {
        setPersonnel(personnel.map((p) => (p.id === data.id ? data : p)));
      } else if (editType === "add_personnel") {
        setPersonnel([...personnel, data]);
      }
      setShowModal(false);
    } catch (error) {
      console.error("There was an error updating the data!", error);
    }
  };

  if (!organisation) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>{organisation.name}</h1>
      <button onClick={() => handleEdit("organisation", organisation)}>
        Edit Organisation
      </button>
      <table border="1" style={{ width: "100%", textAlign: "left" }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Enabled</th>
            <th>Expiry Date</th>
            <th>Registration Date</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{organisation.id}</td>
            <td>{organisation.name}</td>
            <td>{organisation.enabled ? "Yes" : "No"}</td>
            <td>{organisation.expiryDate}</td>
            <td>{organisation.registrationDate}</td>
          </tr>
        </tbody>
      </table>
      <h2>Personnel</h2>
      <button onClick={handleAdd}>Add Personnel</button>
      <table
        border="1"
        style={{ width: "100%", textAlign: "left", marginTop: "20px" }}
      >
        <thead>
          <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Telephone Number</th>
            <th>Details</th>
            <th>Edit</th>
          </tr>
        </thead>
        <tbody>
          {personnel.map(
            ({ id, firstName, lastName, username, email, telephoneNumber }) => (
              <tr key={id}>
                <td>{id}</td>
                <td>{firstName}</td>
                <td>{lastName}</td>
                <td>{username}</td>
                <td>{email}</td>
                <td>{telephoneNumber}</td>
                <td>
                  <Link
                    to={`/personnel/${id}`}
                    style={{ textDecoration: "none", color: "inherit" }}
                  >
                    View
                  </Link>
                </td>
                <td>
                  <button
                    onClick={() =>
                      handleEdit("personnel", {
                        id,
                        firstName,
                        lastName,
                        username,
                        email,
                        telephoneNumber,
                      })
                    }
                  >
                    Edit
                  </button>
                </td>
              </tr>
            )
          )}
        </tbody>
      </table>
      <Modal show={showModal} handleClose={handleClose}>
        <form onSubmit={handleSubmit}>
          {editType === "organisation" ? (
            <>
              <label>ID: {formData.id}</label>
              <br />
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name || ""}
                onChange={handleChange}
              />
              <br />
              <label>Enabled:</label>
              <input
                type="checkbox"
                name="enabled"
                checked={formData.enabled || false}
                onChange={(e) =>
                  handleChange({
                    target: { name: "enabled", value: e.target.checked },
                  })
                }
              />
              <br />
              <label>Expiry Date:</label>
              <input
                type="date"
                name="expiryDate"
                value={formData.expiryDate || ""}
                onChange={handleChange}
              />
              <br />
              <label>Registration Date:</label>
              <input
                type="date"
                name="registrationDate"
                value={formData.registrationDate || ""}
                onChange={handleChange}
              />
              <br />
            </>
          ) : (
            <>
              <label>ID: {formData.id}</label>
              <br />
              <label>First Name:</label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName || ""}
                onChange={handleChange}
              />
              <br />
              <label>Last Name:</label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName || ""}
                onChange={handleChange}
              />
              <br />
              <label>Username:</label>
              <input
                type="text"
                name="username"
                value={formData.username || ""}
                onChange={handleChange}
              />
              <br />
              <label>Password:</label>
              <input
                type="password"
                name="password"
                value={formData.password || ""}
                onChange={handleChange}
              />
              <br />
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={formData.email || ""}
                onChange={handleChange}
              />
              <br />
              <label>Telephone Number:</label>
              <input
                type="text"
                name="telephoneNumber"
                value={formData.telephoneNumber || ""}
                onChange={handleChange}
              />
              <br />
            </>
          )}
          <button type="submit">Save</button>
        </form>
      </Modal>
    </div>
  );
};

export default Organisation;
