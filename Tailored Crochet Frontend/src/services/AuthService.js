import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const AuthService = {
    login: async (email, password) => {
        try {
            const response = await axios.post(`${API_URL}/login`, {
                email,
                password
            });

            // Store user data and token in localStorage
            if (response.data.accessToken) {
                localStorage.setItem('user', JSON.stringify({
                    accessToken: response.data.accessToken,
                    email: email
                }));
            }
            return response.data;

        } catch (error) {
            throw error.response?.data || error;
        }
    },

    register: async (username, email, password) => {
        try {
            const response = await axios.post(`${API_URL}/register`, {
                username,
                email,
                password
            });

            // Automatically log in user after registration
            if (response.data.accessToken) {
                localStorage.setItem('user', JSON.stringify({
                    accessToken: response.data.accessToken,
                    email: email
                }));
            }
            return response.data;

        } catch (error) {
            throw error.response?.data || error;
        }
    },

    logout: () => {
        localStorage.removeItem('user');
    },

    getCurrentUser: () => {
        return JSON.parse(localStorage.getItem('user'));
    }
};

export default AuthService;