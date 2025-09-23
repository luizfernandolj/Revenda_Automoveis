import React, { useState } from 'react';
import { Link } from 'react-router-dom'; // importe Link
import logo from '../assets/images/logo.png';
import '../assets/css/navbar.css';

function Navbar() {
  const [navActive, setNavActive] = useState(false);
  const [dropdownStates, setDropdownStates] = useState({});

  const toggleHamburger = () => {
    setNavActive(!navActive);
  };

  const toggleDropdown = (index) => {
    if (window.innerWidth <= 900) {
      setDropdownStates(prev => ({
        ...prev,
        [index]: !prev[index],
      }));
    }
  };

return (
    <nav className="navbar">
        <img src={logo} alt="Logo" className="logo" />
        <div className="hamburger" onClick={toggleHamburger}>
            <div></div>
            <div></div>
            <div></div>
        </div>
        <ul className={`nav-links ${navActive ? 'active' : ''}`}>
          <li><Link to="/chatbot">Chatbot</Link></li>
            <li className={`dropdown ${dropdownStates[0] ? 'active' : ''}`}>
                <a href="#!" onClick={() => toggleDropdown(0)}>Cadastros</a>
                <ul className="dropdown-menu">
                    <li><Link to="/veiculo">Veículos</Link></li>
                    <li><Link to="/produtos">Produtos</Link></li>
                    <li><Link to="/marca">Marcas</Link></li>
                    <li><Link to="/tipo-veiculo">Tipos de Veículos</Link></li>
                    <li><Link to="/modelo">Modelos</Link></li>
                    <li><Link to="/tipo-produto">Tipos de Produtos</Link></li>
                    <li><Link to="/cor">Cores</Link></li>
                </ul>
            </li>
            <li className={`dropdown ${dropdownStates[1] ? 'active' : ''}`}>
                <a href="#!" onClick={() => toggleDropdown(1)}>Operações</a>
                <ul className="dropdown-menu">
                    <li><Link to="/vendas">Vendas</Link></li>
                    <li><Link to="/manutencoes">Manutenções</Link></li>
                </ul>
            </li>
            <li className={`dropdown ${dropdownStates[2] ? 'active' : ''}`}>
                <a href="#!" onClick={() => toggleDropdown(2)}>Pessoas</a>
                <ul className="dropdown-menu">
                    <li><Link to="/vendedores">Vendedores</Link></li>
                    <li><Link to="/clientes">Clientes</Link></li>
                </ul>
            </li>
        </ul>
    </nav>
);
}

export default Navbar;
