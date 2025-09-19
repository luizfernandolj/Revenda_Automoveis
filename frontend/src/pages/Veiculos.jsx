import React, { useState, useEffect } from 'react';
import MultiRangeSlider from "multi-range-slider-react";
import Navbar from '../components/Navbar';
import '../assets/css/veiculos.css';
import fiatuno from '../assets/images/fiat_uno.jpg';

function Veiculos() {
  const [veiculos, setVeiculos] = useState([]);
  const [cores, setCores] = useState([]);
  const [modelos, setModelos] = useState([]);
  const [tipos, setTipos] = useState([]);
  const [marcas, setMarcas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterText, setFilterText] = useState('');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [minPreco, setMinPreco] = useState('');
  const [maxPreco, setMaxPreco] = useState('');
  const [minQuilometragem, setMinQuilometragem] = useState('');
  const [maxQuilometragem, setMaxQuilometragem] = useState('');
  const [filterMarca, setFilterMarca] = useState('');
  const [filterCor, setFilterCor] = useState('');
  const [filterDisponivel, setFilterDisponivel] = useState(null); // null: ambos, true: só disponíveis, false: só indisponíveis
  const [precoRange, setPrecoRange] = useState({ min: 0, max: 300000 });
  const [kmRange, setKmRange] = useState({ min: 0, max: 300000 });


  const [newVeiculo, setNewVeiculo] = useState({
    preco: '',
    quilometragem: '',
    disponivel: true,
    cor_id: '',
    modelo_id: '',
    tipo_id: '',
    marca_id: '',
    imagem: ''
  });

  useEffect(() => {
    const fetchAll = async () => {
      setLoading(true);
      try {
        const [resVeiculos, resCores, resModelos, resTipos, resMarcas] = await Promise.all([
          fetch('http://localhost:8080/veiculo'),
          fetch('http://localhost:8080/cor'),
          fetch('http://localhost:8080/modelo'),
          fetch('http://localhost:8080/tipo-veiculo'),
          fetch('http://localhost:8080/marca'),
        ]);
        if (!resVeiculos.ok) throw new Error('Erro ao buscar veículos');
        if (!resCores.ok) throw new Error('Erro ao buscar cores');
        if (!resModelos.ok) throw new Error('Erro ao buscar modelos');
        if (!resTipos.ok) throw new Error('Erro ao buscar tipos de veículos');
        if (!resMarcas.ok) throw new Error('Erro ao buscar marcas');

        const veiculosData = await resVeiculos.json();
        const coresData = await resCores.json();
        const modelosData = await resModelos.json();
        const tiposData = await resTipos.json();
        const marcasData = await resMarcas.json();

        setVeiculos(veiculosData);
        setCores(coresData);
        setModelos(modelosData);
        setTipos(tiposData);
        setMarcas(marcasData);

        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchAll();
  }, []);

  const filteredVeiculos = veiculos.filter(v => {
    const modelo = modelos.find(m => m.id === v.modelo_id);
    const modeloNome = modelo ? modelo.nome : '';

    let matchText =
      String(v.preco).includes(filterText) ||
      modeloNome.toLowerCase().includes(filterText.toLowerCase());

    let matchPreco =
      Number(v.preco) >= precoRange.min && Number(v.preco) <= precoRange.max;

    let matchQuilometragem =
      Number(v.quilometragem) >= kmRange.min && Number(v.quilometragem) <= kmRange.max;

    let matchMarca =
        filterMarca === '' || v.modelo.marca.id === filterMarca;

    let matchCor =
        filterCor === '' || v.cor.id === filterCor;

    let matchDisponivel =
        filterDisponivel === null || v.disponivel === filterDisponivel;

    return matchText && matchPreco && matchQuilometragem && matchMarca && matchCor && matchDisponivel;
  });


  const openAddModal = () => {
    setIsAddModalOpen(true);
    document.body.style.overflow = 'hidden';
  };

  const closeAddModal = () => {
    setIsAddModalOpen(false);
    document.body.style.overflow = '';
    setNewVeiculo({
      preco: '',
      quilometragem: '',
      disponivel: true,
      cor_id: '',
      modelo_id: '',
      tipo_id: '',
      marca_id: '',
      imagem: ''
    });
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked, files } = e.target;
    if (type === 'file') {
      setNewVeiculo(prev => ({ ...prev, [name]: files[0]?.name || '' }));
    } else if ((name === 'preco' || name === 'quilometragem') && value !== '') {
      // Garante valor >= 0
      const num = Number(value);
      if (num < 0) return;
      setNewVeiculo(prev => ({ ...prev, [name]: value }));
    } else if (type === 'checkbox') {
      setNewVeiculo(prev => ({ ...prev, [name]: checked }));
    } else {
      setNewVeiculo(prev => ({ ...prev, [name]: value }));
    }
  };

    const handleAddSubmit = async (e) => {
    e.preventDefault();
    try {
        // Construir os objetos aninhados para envio conforme exemplo do modelo
        const selectedMarca = marcas.find(m => m.id === newVeiculo.marca_id) || { id: newVeiculo.marca_id, nome: '' };
        const selectedModelo = modelos.find(m => m.id === newVeiculo.modelo_id) || { id: newVeiculo.modelo_id, nome: '', marca: selectedMarca };
        const selectedTipo = tipos.find(t => t.id === newVeiculo.tipo_id) || { id: newVeiculo.tipo_id, nome: '' };

        const bodyToSend = {
        preco: newVeiculo.preco,
        quilometragem: newVeiculo.quilometragem,
        disponivel: newVeiculo.disponivel,
        cor: { id: newVeiculo.cor_id },
        modelo: {
            id: selectedModelo.id,
            nome: selectedModelo.nome,
            marca: {
            id: selectedMarca.id,
            nome: selectedMarca.nome,
            },
            tipoVeiculo: {
            id: selectedTipo.id,
            nome: selectedTipo.nome,
            }
        },
        };

        const res = await fetch('http://localhost:8080/veiculo/cadastrar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bodyToSend),
        });

        if (!res.ok) throw new Error('Erro ao cadastrar veículo');
        const created = await res.json();
        setVeiculos(prev => [...prev, created]);
        closeAddModal();
    } catch (err) {
        alert(err.message);
    }
    };

  const handleRemove = async (id) => {
    if (!window.confirm('Confirmar remoção?')) return;
    try {
      const res = await fetch(`http://localhost:8080/veiculo/${id}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('Erro ao remover veículo');
      setVeiculos(prev => prev.filter(v => v.id !== id));
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <>
      <Navbar />
    <div className="main-content flex-layout">
        {loading && <p>Carregando...</p>}
        {error && <p style={{color:'red'}}>{error}</p>}
        <section className="cards-section">
          {filteredVeiculos.length === 0 ? (
            <p>Nenhum veículo encontrado.</p>
          ) : (
            filteredVeiculos.map(v => {
              const modelo = modelos.find(m => m.id === v.modelo_id);
              const cor = cores.find(c => c.id === v.cor_id);
              const tipo = tipos.find(t => t.id === v.tipo_id);
              const marca = marcas.find(m => m.id === v.marca_id);
              return (
                <div key={v.id} className="vehicle-card">
                  <button className="remove-card-btn" onClick={() => handleRemove(v.id)}>&times;</button>
                  <img src={fiatuno} alt="Fiat-Uno" className="vehicle-image-preview" />
                  <div className="card-details">
                    <div>ID: {v.id}</div>
                    <div>Cor: {v.cor.nome}</div>
                    <div>Modelo: {v.modelo.nome}</div>
                    <div>Marca: {v.modelo.marca.nome}</div>
                    <div>Tipo: {v.modelo.tipoVeiculo.nome}</div>
                    <div>Quilometragem: {v.quilometragem}</div>
                    <div>Status: {v.disponivel ? 'Disponível' : 'Indisponível'}</div>
                    <div>Preço: R$ {Number(v.preco).toLocaleString('pt-BR')}</div>
                  </div>
                </div>
              );
            })
          )}
        </section>

        <aside className="sidebar-filters">
          <input
            type="text"
            placeholder="Modelo ou parte do preço"
            value={filterText}
            onChange={e => setFilterText(e.target.value)}
            className="search-bar"
          />
          <div className="filter-group">
            <label>Preço (R$):</label>
            <MultiRangeSlider
              min={0}
              max={300000}
              step={1000}
              minValue={precoRange.min}
              maxValue={precoRange.max}
              onInput={(e) => setPrecoRange({ min: e.minValue, max: e.maxValue })}
              ruler={false}
              label={true}
              className="price-slider"
            />
          </div>
          <div className="filter-group">
            <label>Quilometragem (km):</label>
            <MultiRangeSlider
              min={0}
              max={300000}
              step={500}
              minValue={kmRange.min}
              maxValue={kmRange.max}
              onInput={(e) => setKmRange({ min: e.minValue, max: e.maxValue })}
              ruler={false}
              label={true}
              className="km-slider"
            />
          </div>
          <select value={filterMarca} onChange={e => setFilterMarca(e.target.value)} className="dropdown-filter">
            <option value="">Todas marcas</option>
            {marcas.map(marca => (
              <option key={marca.id} value={marca.id}>{marca.nome}</option>
            ))}
          </select>
          <select value={filterCor} onChange={e => setFilterCor(e.target.value)} className="dropdown-filter">
            <option value="">Todas cores</option>
            {cores.map(cor => (
              <option key={cor.id} value={cor.id}>{cor.nome}</option>
            ))}
          </select>
          <label className="checkbox-group custom-checkbox-label">
            Disponível
            <input
              type="checkbox"
              className="custom-checkbox"
              checked={filterDisponivel === true}
              onChange={e => setFilterDisponivel(prev =>
                prev === true ? null : true
              )}
            />
          </label>
          <label className="checkbox-group custom-checkbox-label">
            Indisponível
            <input
              type="checkbox"
              className="custom-checkbox"
              checked={filterDisponivel === false}
              onChange={e => setFilterDisponivel(prev =>
                prev === false ? null : false
              )}
            />
          </label>
          <button className="add-vehicle-btn" onClick={openAddModal}>
            Adicionar Veículo
          </button>
        </aside>
      </div>

      {isAddModalOpen && (
        <div className="modal-overlay" onClick={closeAddModal}>
          <div className="modal-window" onClick={e => e.stopPropagation()}>
            <button className="close-modal" onClick={closeAddModal}>&times;</button>
            <h2>Adicionar Veículo</h2>
            <form onSubmit={handleAddSubmit} className="vehicle-form">
              <div className="image-upload-group">
                <label htmlFor="imagem">Imagem do Veículo (opcional)</label>
                <input
                  type="file"
                  id="imagem"
                  name="imagem"
                  onChange={handleInputChange}
                />
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="preco">Preço</label>
                  <input
                    id="preco"
                    name="preco"
                    type="number"
                    min="0"
                    value={newVeiculo.preco}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="quilometragem">Quilometragem</label>
                  <input
                    id="quilometragem"
                    name="quilometragem"
                    type="number"
                    min="0"
                    value={newVeiculo.quilometragem}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="form-group checkbox-group">
                  <label htmlFor="disponivel" className="custom-checkbox-label">Disponível</label>
                  <input
                    id="disponivel"
                    name="disponivel"
                    type="checkbox"
                    checked={newVeiculo.disponivel}
                    onChange={handleInputChange}
                    className="custom-checkbox"
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="cor_id">Cor</label>
                  <select
                    id="cor_id"
                    name="cor_id"
                    value={newVeiculo.cor_id}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Selecione uma cor</option>
                    {cores.map(cor => (
                      <option key={cor.id} value={cor.id}>{cor.nome}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="modelo_id">Modelo</label>
                  <select
                    id="modelo_id"
                    name="modelo_id"
                    value={newVeiculo.modelo_id}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Selecione um modelo</option>
                    {modelos.map(modelo => (
                      <option key={modelo.id} value={modelo.id}>{modelo.nome}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="tipo_id">Tipo Veículo</label>
                  <select
                    id="tipo_id"
                    name="tipo_id"
                    value={newVeiculo.tipo_id}
                    onChange={handleInputChange}
                  >
                    <option value="">Selecione um tipo</option>
                    {tipos.map(tipo => (
                      <option key={tipo.id} value={tipo.id}>{tipo.nome}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="marca_id">Marca</label>
                  <select
                    id="marca_id"
                    name="marca_id"
                    value={newVeiculo.marca_id}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Selecione uma marca</option>
                    {marcas.map(marca => (
                      <option key={marca.id} value={marca.id}>{marca.nome}</option>
                    ))}
                  </select>
                </div>
              </div>

              <button type="submit" className="submit-btn">Cadastrar</button>
            </form>
          </div>
        </div>
      )}
    </>
  );
}

export default Veiculos;
