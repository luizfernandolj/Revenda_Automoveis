import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Cores from './pages/cores';
import Marcas from './pages/Marcas';
import Modelo from './pages/Modelo';
import TipoVeiculo from './pages/TipoVeiculo';
import Veiculos from './pages/Veiculos';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/cor" element={<Cores />} />
        <Route path="/marca" element={<Marcas />} />
        <Route path="/modelo" element={<Modelo />} />
        <Route path="/tipo-veiculo" element={<TipoVeiculo />} />
        <Route path="/veiculo" element={<Veiculos />} />
      </Routes>
    </Router>
  );
}

export default App;
