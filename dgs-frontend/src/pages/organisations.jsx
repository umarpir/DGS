import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./OrgModal.css"; // Import the CSS for the Modal
import OrgModal from "./orgModal";

const Organisations = () => {
  const [organisations, setOrganisations] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({});

  useEffect(() => {
    const fetchOrganisations = async () => {
      try {
        const response = await fetch(
          "http://localhost:8085/dgs-api/v1/organisations"
        );
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setOrganisations(data);
      } catch (error) {
        console.error("There was an error fetching the organisations!", error);
      }
    };

    fetchOrganisations();
  }, []);

  const handleAdd = () => {
    setFormData({ name: "", enabled: true, expiryDate: "" });
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
    const url = "http://localhost:8085/dgs-api/v1/organisations/";
    try {
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      setOrganisations([...organisations, data]);
      setShowModal(false);
    } catch (error) {
      console.error("There was an error adding the organisation!", error);
    }
  };

  const getRowStyle = (enabled, expiryDate) => {
    const oneWeekFromNow = new Date();
    oneWeekFromNow.setDate(oneWeekFromNow.getDate() + 7);
    const expiry = new Date(expiryDate);

    if (!enabled) {
      return { color: "red" };
    }
    if (expiry < oneWeekFromNow) {
      return { color: "orange" };
    }
    return {};
  };

  return (
    <div>
      <h1>Organisations</h1>
      <button onClick={handleAdd}>Add Organisation</button>
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
          {organisations.map(
            ({ id, name, enabled, expiryDate, registrationDate }) => (
              <tr key={id} style={getRowStyle(enabled, expiryDate)}>
                <td>{id}</td>
                <td>
                  <Link
                    to={`/${id}`}
                    style={{ textDecoration: "none", color: "inherit" }}
                  >
                    {name}
                  </Link>
                </td>
                <td>{enabled ? "Yes" : "No"}</td>
                <td>{expiryDate}</td>
                <td>{registrationDate}</td>
              </tr>
            )
          )}
        </tbody>
      </table>
      <OrgModal show={showModal} handleClose={handleClose}>
        <form onSubmit={handleSubmit}>
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
          />
          <br />
          <label>Enabled:</label>
          <input
            type="checkbox"
            name="enabled"
            checked={formData.enabled}
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
            value={formData.expiryDate}
            onChange={handleChange}
          />
          <br />
          <button type="submit">Save</button>
        </form>
      </OrgModal>
    </div>
  );
};

export default Organisations;
