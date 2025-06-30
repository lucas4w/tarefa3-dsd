import axios from 'axios';
import type { UserResponse, SensorListResponse, SensorDataResponse } from '../types/api';

const API_BASE_URL = 'https://bug-free-bassoon-5g49gw9qv5v73p457-8000.app.github.dev/api';

export const getUserByEmail = async (email: string): Promise<UserResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/by-email/`, {
      params: { email },
    });
    return response.data;
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      console.error('Erro ao buscar usuário:', {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status,
        config: error.config,
      });
    } else {
      console.error('Erro desconhecido ao buscar usuário:', error);
    }
    throw new Error('Erro ao buscar usuário');
  }
};

export const getUserSensors = async (userId: number): Promise<SensorListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/${userId}/sensors/`);
    return response.data;
  } catch (error) {
    console.error('Erro ao buscar sensores:', error); 
    throw new Error('Erro ao buscar sensores'); 
  }
};

export const getLatestSensorData = async (sensorId: string): Promise<SensorDataResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/sensors/${sensorId}/latest-data/`);
    return response.data;
  } catch (error) {
    console.error('Erro ao buscar dados do sensor:', error); 
    throw new Error('Erro ao buscar dados do sensor');

  }
};