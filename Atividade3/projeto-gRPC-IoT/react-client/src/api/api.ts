import axios from 'axios';
import type { UserResponse, SensorListResponse, SensorDataResponse, SensorRegistrationRequest,
   SensorRegistrationResponse, UserRegistrationRequest, UserRegistrationResponse, GenerateDataResponse } from '../types/api';

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

export const registerNewSensor = async (
  sensorData: SensorRegistrationRequest
): Promise<SensorRegistrationResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/sensors/register/`, { 
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(sensorData),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.mensagem || 'Falha ao registrar sensor');
    }

    return await response.json();
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

export const registerUser = async (userData: UserRegistrationRequest): Promise<UserRegistrationResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/users/register/`, { 
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });

    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = `Erro na requisição: ${response.status} ${response.statusText}`;
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.mensagem || errorData.error || errorMessage;
      } catch (error) {
        console.error('Erro ao registrar usuário', error); 
        errorMessage = errorText || errorMessage;
      }
      throw new Error(errorMessage);
    }

    return await response.json();
  } catch (error) {
    console.error('Erro ao registrar usuário', error); 
    throw new Error('Erro ao registrar usuário');
  }
};

export const generateSensorData = async (sensorId: number): Promise<GenerateDataResponse> => { // Ajuste o tipo de retorno conforme sua API
  try {
    const response = await fetch(`${API_BASE_URL}/sensors/${sensorId}/generate-data/`, { // URL da sua API para gerar dados
      method: 'POST', // Geralmente é um POST para acionar uma ação
      headers: {
        'Content-Type': 'application/json',
      },
      // body: JSON.stringify({ /* qualquer dado adicional que sua API precise */ }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = `Erro na requisição: ${response.status} ${response.statusText}`;
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.mensagem || errorData.error || errorMessage;
      } catch (jsonError) {
        errorMessage = errorText || errorMessage;
      }
      throw new Error(errorMessage);
    }
    // Supondo que a API retorne um JSON simples de sucesso/falha
    return await response.json();
  } catch (error: any) {
    console.error('Erro na API ao gerar dados para sensor:', error);
    return { sucesso: false, mensagem: error.message || 'Erro desconhecido' };
  }
};