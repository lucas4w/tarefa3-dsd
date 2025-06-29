import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUserByEmail } from '../api/api';

const EmailForm: React.FC = () => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      const response = await getUserByEmail(email);
      if (response.sucesso) {
        navigate(`/sensors/${response.usuario_id_encontrado}`);
      } else {
        setError('Usuário não encontrado');
      }
    } catch (err) {
      setError('Erro ao buscar usuário. Tente novamente.');
      console.error('Erro ao buscar usuário:', err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h1 className="text-2xl font-bold mb-6 text-center">Consultar Sensores</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="mt-1 p-2 w-full border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Digite seu email"
              required
            />
          </div>
          {error && <p className="text-red-500 text-sm">{error}</p>}
          <button
            type="submit"
            className="w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600 transition"
          >
            Buscar Sensores
          </button>
        </form>
      </div>
    </div>
  );
};

export default EmailForm;