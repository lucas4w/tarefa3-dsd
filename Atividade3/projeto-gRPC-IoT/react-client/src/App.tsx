import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import EmailForm from './components/EmailForm';
import SensorList from './components/SensorList';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<EmailForm />} />
        <Route path="/sensors/:userId" element={<SensorList />} />
      </Routes>
    </Router>
  );
};

export default App;