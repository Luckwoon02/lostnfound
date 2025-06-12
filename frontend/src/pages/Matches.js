import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Matches() {
  const [matches, setMatches] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchMatches();
  }, []);

  const fetchMatches = async () => {
    try {
      const response = await axios.get('http://localhost:8089/api/matches');
      setMatches(response.data);
      setError('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch matches');
    }
  };

  return (
    <div className="form-container">
      <h2>Matches</h2>
      {error && <div className="error">{error}</div>}
      <div className="matches-list">
        {matches.map((match, index) => (
          <div key={index} className="match-item">
            <h3>Match #{index + 1}</h3>
            <div className="match-details">
              <div>
                <strong>Lost Item:</strong>
                <p>Item: {match.lostItem.itemName}</p>
                <p>Location: {match.lostItem.location}</p>
                <p>Date Lost: {match.lostItem.dateLost}</p>
              </div>
              <div>
                <strong>Found Item:</strong>
                <p>Item: {match.foundItem.itemName}</p>
                <p>Location: {match.foundItem.location}</p>
                <p>Date Found: {match.foundItem.dateFound}</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Matches;
