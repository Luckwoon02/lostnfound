import React, { useState } from 'react';
import axios from 'axios';

function LostItemForm() {
  const [itemName, setItemName] = useState('');
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [dateLost, setDateLost] = useState('');
  const [contactInfo, setContactInfo] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8089/api/items/lost', {
        itemName,
        description,
        location,
        dateLost,
        contactInfo
      });
      alert('Item reported successfully!');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to report item');
    }
  };

  return (
    <div className="form-container">
      <h2>Report Lost Item</h2>
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
          <label>Date Lost:</label>
          <input
            type="date"
            value={dateLost}
            onChange={(e) => setDateLost(e.target.value)}
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
        <button type="submit">Submit Lost Item</button>
      </form>
    </div>
  );
}

export default LostItemForm;
