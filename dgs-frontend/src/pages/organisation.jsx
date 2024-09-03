import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';

const Organisation = () => {
  const { id } = useParams();
  const [organisation, setOrganisation] = useState(null);
  const [personnel, setPersonnel] = useState([]);

  useEffect(() => {
    const fetchOrganisation = async () => {
      try {
        const response = await fetch(`http://localhost:8085/dgs-api/v1/organisations/${id}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setOrganisation(data);
      } catch (error) {
        console.error('There was an error fetching the organisation!', error);
      }
    };

    const fetchPersonnel = async () => {
      try {
        const response = await fetch(`http://localhost:8085/dgs-api/v1/personnel/organisation/${id}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setPersonnel(data);
      } catch (error) {
        console.error('There was an error fetching the personnel!', error);
      }
    };

    fetchOrganisation();
    fetchPersonnel();
  }, [id]);

  if (!organisation) {
    return <div>Loading...</div>;
  }

  return (
    <div><h1>{organisation.name}</h1><table border="1" style={{ width: '100%', textAlign: 'left' }}><thead><tr><th>ID</th><th>Name</th><th>Enabled</th><th>Expiry Date</th><th>Registration Date</th></tr></thead><tbody><tr><td>{organisation.id}</td><td>{organisation.name}</td><td>{organisation.enabled ? 'Yes' : 'No'}</td><td>{organisation.expiryDate}</td><td>{organisation.registrationDate}</td></tr></tbody></table><h2>Personnel</h2><table border="1" style={{ width: '100%', textAlign: 'left', marginTop: '20px' }}><thead><tr><th>ID</th><th>First Name</th><th>Last Name</th><th>Username</th><th>Email</th><th>Telephone Number</th><th>Details</th></tr></thead><tbody>
          {personnel.map(({ id, firstName, lastName, username, email, telephoneNumber }) => (
            <tr key={id}><td>{id}</td><td>{firstName}</td><td>{lastName}</td><td>{username}</td><td>{email}</td><td>{telephoneNumber}</td><td><Link to={`/personnel/${id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                  View
                </Link></td></tr>
          ))}
        </tbody></table></div>
  );
};

export default Organisation;