import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const Personnel = () => {
  const { id } = useParams();
  const [person, setPerson] = useState(null);

  useEffect(() => {
    const fetchPerson = async () => {
      try {
        const response = await fetch(`http://localhost:8085/dgs-api/v1/personnel/${id}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setPerson(data);
      } catch (error) {
        console.error('There was an error fetching the personnel!', error);
      }
    };

    fetchPerson();
  }, [id]);

  if (!person) {
    return <div>Loading...</div>;
  }

  return (
    <div><h1>{person.firstName} {person.lastName}</h1><table border="1" style={{ width: '100%', textAlign: 'left' }}><thead><tr><th>ID</th><th>First Name</th><th>Last Name</th><th>Username</th><th>Email</th><th>Telephone Number</th></tr></thead><tbody><tr><td>{person.id}</td><td>{person.firstName}</td><td>{person.lastName}</td><td>{person.username}</td><td>{person.email}</td><td>{person.telephoneNumber}</td></tr></tbody></table></div>
  );
};

export default Personnel;