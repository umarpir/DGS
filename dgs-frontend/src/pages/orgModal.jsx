import React from 'react';

const OrgModal = ({ show, handleClose, children }) => {
  const showModal = show ? "modal display-block" : "modal display-none";

  return (
    <div className={showModal}><section className="modal-main">
        {children}
        <button onClick={handleClose}>Close</button></section></div>
  );
};

export default OrgModal;