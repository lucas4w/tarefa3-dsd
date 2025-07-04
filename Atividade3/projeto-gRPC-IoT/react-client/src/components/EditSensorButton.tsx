// src/components/EditSensorButton.tsx
import React, { useState } from 'react';
import SensorForm from './SensorForm';
import type { Sensor } from '../types/api';

interface EditSensorButtonProps {
  userId: number;
  sensor: Sensor; // sensor.sensor_id é string aqui
  onSensorUpdated: () => void;
}

const EditSensorButton: React.FC<EditSensorButtonProps> = ({ userId, sensor, onSensorUpdated }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);

  const handleSensorFormSuccess = () => {
    handleCloseModal();
    onSensorUpdated();
  };

  return (
    <div>
      <button
        className="mt-4 mr-2 bg-yellow-500 hover:bg-yellow-600 text-white font-bold py-1 px-3 rounded text-sm disabled:opacity-50"
        onClick={handleOpenModal}
      >
        Editar
      </button>

      {isModalOpen && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-xl w-full max-w-md mx-auto">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Editar Sensor: {sensor.nome}</h2>
              <button
                className="text-gray-500 hover:text-gray-800 text-2xl"
                onClick={handleCloseModal}
              >
                &times;
              </button>
            </div>
            <SensorForm
              userId={userId}
              sensorData={sensor} // sensor.sensor_id é string aqui
              onSuccess={handleSensorFormSuccess}
              onClose={handleCloseModal}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default EditSensorButton;