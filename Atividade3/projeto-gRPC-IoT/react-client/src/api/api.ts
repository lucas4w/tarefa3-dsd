import axios from 'axios';
import type { UserResponse, SensorListResponse, SensorDataResponse, SensorRegistrationRequest,
   SensorRegistrationResponse, UserRegistrationRequest, UserRegistrationResponse, GenerateDataResponse, Sensor } from '../types/api';

const API_BASE_URL = 'https://upgraded-doodle-v67wjvj6w9q39p4-8000.app.github.dev/api';

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

export const generateSensorData = async (sensorId: string): Promise<GenerateDataResponse> => { 
  try {
    const response = await fetch(`${API_BASE_URL}/sensors/${sensorId}/generate-data/`, { 
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
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
    return await response.json();
  } catch (error: any) {
    console.error('Erro na API ao gerar dados para sensor:', error);
    return { sucesso: false, mensagem: error.message || 'Erro desconhecido' };
  }
};

export const updateSensor = async (sensorId: string, updatedSensorData: Sensor): Promise<SensorUpdateResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/sensors/${sensorId}/`, { // Ajuste a URL da sua API para atualização
      method: 'PUT', // Ou 'PATCH', dependendo da sua API
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedSensorData),
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
    return await response.json();
  } catch (error: any) {
    console.error('Erro na API ao atualizar sensor:', error);
    return { sucesso: false, mensagem: error.message || 'Erro desconhecido' };
  }
};