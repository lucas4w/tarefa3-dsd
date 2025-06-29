import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getUserSensors, getLatestSensorData } from '../api/api';
import type { Sensor, SensorDataResponse } from '../types/api';

interface SensorWithData extends Sensor {
  latestData?: SensorDataResponse;
}

const SensorList: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();
  const [sensors, setSensors] = useState<SensorWithData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchSensorsAndData = async () => {
      try {
        const sensorResponse = await getUserSensors(Number(userId));
        if (sensorResponse.sucesso) {
          const sensorsWithData: SensorWithData[] = sensorResponse.sensores;

          // Buscar os dados mais recentes para cada sensor
          const dataPromises = sensorResponse.sensores.map((sensor) =>
            getLatestSensorData(sensor.sensor_id)
          );
          const dataResponses = await Promise.all(dataPromises);

          const updatedSensors = sensorsWithData.map((sensor, index) => ({
            ...sensor,
            latestData: dataResponses[index],
          }));

          setSensors(updatedSensors);
        } else {
          setError('Nenhum sensor encontrado');
        }
      } catch (err) {
        setError('Erro ao carregar sensores');
        console.error('Erro ao buscar sensores:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchSensorsAndData();
  }, [userId]);

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center">Carregando...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h1 className="text-2xl font-bold mb-6 text-center">Sensores do Usuário</h1>
      {error && <p className="text-red-500 text-center mb-4">{error}</p>}
      {sensors.length === 0 && !error && (
        <p className="text-center text-gray-600">Nenhum sensor registrado.</p>
      )}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {sensors.map((sensor) => (
          <div
            key={sensor.sensor_id}
            className="bg-white p-6 rounded-lg shadow-md"
          >
            <h2 className="text-xl font-semibold mb-2">{sensor.nome}</h2>
            <p className="text-gray-600 mb-4">{sensor.descricao}</p>
            {sensor.latestData?.sucesso ? (
              <div>
                <p><strong>Temperatura:</strong> {sensor.latestData.temperatura_encontrada} °C</p>
                <p><strong>Umidade:</strong> {sensor.latestData.umidade_encontrada}%</p>
                <p><strong>Data:</strong> {sensor.latestData.timestamp_encontrado || 'N/A'}</p>
              </div>
            ) : (
              <p className="text-red-500">Sem dados recentes</p>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default SensorList;