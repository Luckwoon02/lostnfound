import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const apiService = {
  // Auth API
  login: async (credentials) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  register: async (userData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/register`, userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  // Items API
  reportLostItem: async (itemData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/items/lost`, itemData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  reportFoundItem: async (itemData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/items/found`, itemData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  getMatches: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/matches`);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },
};
