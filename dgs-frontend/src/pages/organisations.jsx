import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const Organisations = () => {
  const [organisations, setOrganisations] = useState([]);

  useEffect(() => {
    const fetchOrganisations = async () => {
      try {
        const response = await fetch('http://localhost:8085/dgs-api/v1/organisations');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setOrganisations(data);
      } catch (error) {
        console.error('There was an error fetching the organisations!', error);
      }
    };

    fetchOrganisations();
  }, []);

  const getRowStyle = (enabled, expiryDate) => {
    const oneWeekFromNow = new Date();
    oneWeekFromNow.setDate(oneWeekFromNow.getDate() + 7);
    const expiry = new Date(expiryDate);

    if (!enabled) {
      return { color: 'red' };
    }
    if (expiry < oneWeekFromNow) {
      return { color: 'orange' };
    }
    return {};
  };

  return (
    <div><h1>Organisations</h1><table border="1" style={{ width: '100%', textAlign: 'left' }}><thead><tr><th>ID</th><th>Name</th><th>Enabled</th><th>Expiry Date</th><th>Registration Date</th></tr></thead><tbody>
          {organisations.map(({ id, name, enabled, expiryDate, registrationDate }) => (
            <tr key={id} style={getRowStyle(enabled, expiryDate)}><td>{id}</td><td><Link to={`/${id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                  {name}
                </Link></td><td>{enabled ? 'Yes' : 'No'}</td><td>{expiryDate}</td><td>{registrationDate}</td></tr>
          ))}
        </tbody></table></div>
  );
};

export default Organisations;