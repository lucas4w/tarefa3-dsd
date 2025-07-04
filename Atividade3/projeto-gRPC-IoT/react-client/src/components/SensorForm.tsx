// src/components/SensorForm.tsx
import React, { useState, useEffect } from 'react';
import { registerNewSensor, updateSensor } from '../api/api';
import type { Sensor, SensorRegistrationRequest, SensorRegistrationResponse, SensorUpdateResponse } from '../types/api';

interface SensorFormProps {
  userId: number;
  onSuccess: () => void;
  onClose: () => void;
  sensorData?: Sensor; // sensor_id dentro de Sensor é string
}

const SensorForm: React.FC<SensorFormProps> = ({ userId, onSuccess, onClose, sensorData }) => {
  const [nome, setNome] = useState(sensorData?.nome || '');
  const [descricao, setDescricao] = useState(sensorData?.descricao || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    if (sensorData) {
      setNome(sensorData.nome);
      setDescricao(sensorData.descricao);
    } else {
      setNome('');
      setDescricao('');
    }
    setError('');
    setSuccessMessage('');
  }, [sensorData]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    setLoading(true);
    setError('');
    setSuccessMessage('');

    try {
      if (sensorData) {
        // MODO DE EDIÇÃO
        if (!sensorData.sensor_id) { // sensor_id agora é string
          setError('ID do sensor não encontrado para edição.');
          setLoading(false);
          return;
        }
        const updatedData: Sensor = {
          sensor_id: sensorData.sensor_id, // <--- Mantido como string
          nome,
          descricao,
          usuario_id: userId,
        };
        const response: SensorUpdateResponse = await updateSensor(sensorData.sensor_id, updatedData); // <--- sensor_id passado como string

        if (response.sucesso) {
          setSuccessMessage('Sensor atualizado com sucesso!');
          onSuccess();
        } else {
          setError(response.mensagem || 'Erro ao atualizar sensor. Tente novamente.');
        }
      } else {
        // MODO DE REGISTRO
        const newSensorData: SensorRegistrationRequest = {
          nome,
          descricao,
          usuario_id: userId,
        };
        const response: SensorRegistrationResponse = await registerNewSensor(newSensorData);

        if (response.sucesso) {
          setSuccessMessage('Sensor registrado com sucesso!');
          onSuccess();
        } else {
          setError(response.mensagem || 'Erro ao registrar sensor. Tente novamente.');
        }
      }
    } catch (err: any) {
      setError('Erro de comunicação com o servidor. Tente novamente.');
      console.error('Erro ao processar sensor:', err);
    } finally {
      setLoading(false);
    }
  };

  const submitButtonText = sensorData ?
    (loading ? 'Atualizando...' : 'Salvar Alterações') :
    (loading ? 'Registrando...' : 'Registrar');

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <p className="text-red-500 text-center">{error}</p>}
      {successMessage && <p className="text-green-500 text-center">{successMessage}</p>}

      <div>
        <label htmlFor="nome" className="block text-sm font-medium text-gray-700">
          Nome do Sensor:
        </label>
        <input
          type="text"
          id="nome"
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
          required
          disabled={loading}
        />
      </div>

      <div>
        <label htmlFor="descricao" className="block text-sm font-medium text-gray-700">
          Descrição:
        </label>
        <textarea
          id="descricao"
          rows={3}
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
          required
          disabled={loading}
        ></textarea>
      </div>

      <div className="flex justify-end space-x-3 mt-6">
        <button
          type="button"
          onClick={onClose}
          className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          disabled={loading}
        >
          Cancelar
        </button>
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
          disabled={loading}
        >
          {submitButtonText}
        </button>
      </div>
    </form>
  );
};

export default SensorForm;