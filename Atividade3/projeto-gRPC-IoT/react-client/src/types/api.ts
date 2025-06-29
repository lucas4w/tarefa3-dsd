export interface UserResponse {
  sucesso: boolean;
  usuario_id_encontrado: number;
  error?: string;
}

export interface Sensor {
  sensor_id: string;
  nome: string;
  descricao: string;
}

export interface SensorListResponse {
  mensagem: string;
  sucesso: boolean;
  sensores: Sensor[];
  error?: string;
}

export interface SensorDataResponse {
  mensagem: string;
  sucesso: boolean;
  sensor_id_encontrado: string;
  temperatura_encontrada: number;
  umidade_encontrada: number;
  timestamp_encontrado: string | null;
  error?: string;
}