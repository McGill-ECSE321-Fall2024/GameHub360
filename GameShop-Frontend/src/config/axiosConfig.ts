import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const apiService = axios.create({
  baseURL: API_BASE_URL,
});

export default apiService;
