import React, { useState } from 'react';
import axios from 'axios';

function FoundItemForm() {
  const [itemName, setItemName] = useState('');
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [dateFound, setDateFound] = useState('');
  const [contactInfo, setContactInfo] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8089/api/items/found', {
        itemName,
        description,
        location,
        dateFound,
        contactInfo
      });
      alert('Item reported successfully!');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to report item');
    }
  };

  return (
    <div className="form-container">
      <h2>Report Found Item</h2>
      {error && <div className="error">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Item Name:</label>
          <input
            type="text"
            value={itemName}
            onChange={(e) => setItemName(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Description:</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Location:</label>
          <input
            type="text"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Date Found:</label>
          <input
            type="date"
            value={dateFound}
            onChange={(e) => setDateFound(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Contact Information:</label>
          <input
            type="text"
            value={contactInfo}
            onChange={(e) => setContactInfo(e.target.value)}
            required
          />
        </div>
        <button type="submit">Submit Found Item</button>
      </form>
    </div>
  );
}

export default FoundItemForm;
