import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import '../assets/css/modelos.css';

function Modelos() {
  const [modelos, setModelos] = useState([]);
  const [marcas, setMarcas] = useState([]);
  const [tiposVeiculo, setTiposVeiculo] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterText, setFilterText] = useState('');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingModelo, setEditingModelo] = useState(null);
  const [hamburgerActive, setHamburgerActive] = useState(false);
  const [newModeloName, setNewModeloName] = useState('');
  const [selectedMarcaId, setSelectedMarcaId] = useState('');
  const [selectedTipoId, setSelectedTipoId] = useState('');
  const [editModeloName, setEditModeloName] = useState('');
  const [editMarcaId, setEditMarcaId] = useState('');
  const [editTipoId, setEditTipoId] = useState('');

  useEffect(() => {
    fetchModelos();
    fetch('http://localhost:8080/marca')
      .then(res => res.ok ? res.json() : [])
      .then(data => setMarcas(data))
      .catch(() => setMarcas([]));
    fetch('http://localhost:8080/tipo-veiculo')
      .then(res => res.ok ? res.json() : [])
      .then(data => setTiposVeiculo(data))
      .catch(() => setTiposVeiculo([]));
  }, []);

  const fetchModelos = () => {
    setLoading(true);
    fetch('http://localhost:8080/modelo')
      .then(res => {
        if (!res.ok) throw new Error('Erro ao buscar modelos da API');
        return res.json();
      })
      .then(data => {
        setModelos(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  };

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    if (!filterText.trim()) {
      fetchModelos();
      return;
    }
    const idBusca = Number(filterText);
    if (isNaN(idBusca)) {
      setError('Filtro deve ser um número válido de ID');
      setModelos([]);
      return;
    }
    setLoading(true);
    setError(null);
    fetch(`http://localhost:8080/modelo/id?id=${idBusca}`)
      .then((res) => {
        if (!res.ok) {
          if (res.status === 404) return null;
          throw new Error('Erro ao buscar modelo pelo ID');
        }
        return res.json();
      })
      .then((data) => {
        if (data) {
          setModelos([data]);
        } else {
          setModelos([]);
          setError('Nenhum modelo encontrado com este ID');
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setModelos([]);
        setLoading(false);
      });
  };

  const toggleHamburger = () => {
    setHamburgerActive(!hamburgerActive);
  };

  // Modais abrir/fechar
  const openAddModal = () => {
    setNewModeloName('');
    setSelectedMarcaId('');
    setSelectedTipoId('');
    setIsAddModalOpen(true);
  };
  const closeAddModal = () => {
    setIsAddModalOpen(false);
  };
  const openEditModal = (modelo) => {
    setEditingModelo(modelo);
    setEditModeloName(modelo.nome);
    setEditMarcaId(modelo.marcaId || '');
    setEditTipoId(modelo.tipoVeiculoId || '');
    setIsEditModalOpen(true);
  };
  const closeEditModal = () => {
    setIsEditModalOpen(false);
    setEditingModelo(null);
  };

  // Adicionar modelo
  const handleAddSubmit = (e) => {
    const selectedMarca = marcas.find(m => m.id === Number(selectedMarcaId));
    const selectedTipo = tiposVeiculo.find(t => t.id === Number(selectedTipoId));
    e.preventDefault();
    if (!newModeloName.trim() || !selectedMarca || !selectedTipo) return;
    fetch('http://localhost:8080/modelo/cadastrar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        nome: newModeloName,
        marca: selectedMarca,
        tipoVeiculo: selectedTipo,
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Erro ao cadastrar novo modelo');
        return res.json();
      })
      .then(newModelo => {
        setModelos(prev => [...prev, newModelo]);
        closeAddModal();
      })
      .catch(err => setError(err.message));
  };

  // Editar modelo
  const handleEditSubmit = (e) => {
    e.preventDefault();
    if (!editModeloName.trim() || !editingModelo || !editMarcaId || !editTipoId) return;
    fetch(`http://localhost:8080/modelo/${editingModelo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        nome: newModeloName,
        marca: {
            id: selectedMarcaId.id,
            nome: selectedMarcaId.nome
        },
        tipoVeiculo: {
            id: selectedTipoId.id,
            nome: selectedTipoId.nome
        },
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Erro ao atualizar modelo');
        return res.json();
      })
      .then(updatedModelo => {
        setModelos(modelos.map(m => (m.id === updatedModelo.id ? updatedModelo : m)));
        closeEditModal();
      })
      .catch(err => setError(err.message));
  };

  // Remover modelo localmente (adapte para DELETE API se houver)
  const handleRemoveModelo = (id) => {
    setModelos(modelos.filter(modelo => modelo.id !== id));
  };

  const handleModalBgClick = (e, modalSetter) => {
    if (e.target === e.currentTarget) {
      modalSetter(false);
    }
  };

  if (loading) return <div>Carregando...</div>;
  if (error) return <div style={{ color: 'red' }}>Erro: {error}</div>;

  return (
    <>
      <Navbar hamburgerActive={hamburgerActive} toggleHamburger={toggleHamburger} />

      <div className="container">
        <main className="main-content">
          <h1>Modelos</h1>
          <section className="maintenance-table-section">
            <table className="maintenance-table">
              <thead>
                <tr>
                  <th>Id</th>
                  <th>Modelo</th>
                  <th>Marca</th>
                  <th>Tipo Veículo</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {modelos.length > 0 ? (
                  modelos.map(({ id, nome, marca, tipoVeiculo }) => {
                    return (
                      <tr key={id}>
                        <td>{id}</td>
                        <td>{nome}</td>
                        <td>{marca.nome}</td>
                        <td>{tipoVeiculo.nome}</td>
                        <td>
                          <button
                            className="btn-remove"
                            title="Remover modelo"
                            onClick={() => handleRemoveModelo(id)}
                          >
                            &#128465;
                          </button>{' '}
                          <a
                            href="#"
                            className="btn-details"
                            onClick={e => {
                              e.preventDefault();
                              openEditModal({ id, nome, marca, tipoVeiculo });
                            }}
                          >
                            Editar
                          </a>
                        </td>
                      </tr>
                    );
                  })
                ) : (
                  <tr>
                    <td colSpan="5" style={{ textAlign: 'center' }}>
                      Nenhum modelo encontrado
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </section>
        </main>

        <aside className="sidebar">
          <h2>Filtrar</h2>
          <form className="filter-form" onSubmit={handleFilterSubmit}>
            <label>
              Modelo
              <input
                type="text"
                placeholder="ID do Modelo"
                name="modelo"
                value={filterText}
                onChange={e => setFilterText(e.target.value)}
              />
            </label>
            <button type="submit" className="btn btn-primary">
              Filtrar
            </button>
          </form>
          <hr />
          <button id="openAddModal" className="btn btn-success" onClick={openAddModal}>
            + Novo Modelo
          </button>
        </aside>
      </div>

      {/* Modal Adicionar Modelo */}
      {isAddModalOpen && (
        <div
          className="modal-bg"
          onClick={e => handleModalBgClick(e, setIsAddModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Novo Modelo</h2>
              <button className="modal-close" onClick={closeAddModal} aria-label="Fechar modal">
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleAddSubmit}>
              <div className="form-group">
                <label htmlFor="addModelo">Nome do Modelo</label>
                <input
                  type="text"
                  name="modelo"
                  id="addModelo"
                  required
                  value={newModeloName}
                  onChange={e => setNewModeloName(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label htmlFor="addMarca">Marca</label>
                <select
                  name="marca"
                  id="addMarca"
                  required
                  value={selectedMarcaId}
                  onChange={e => setSelectedMarcaId(e.target.value)}
                >
                  <option value="">Selecione a marca</option>
                  {marcas.map(marca => (
                    <option key={marca.id} value={marca.id}>
                      {marca.nome}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="addTipoVeiculo">Tipo Veículo</label>
                <select
                  name="tipoVeiculo"
                  id="addTipoVeiculo"
                  required
                  value={selectedTipoId}
                  onChange={e => setSelectedTipoId(e.target.value)}
                >
                  <option value="">Selecione o tipo de veículo</option>
                  {tiposVeiculo.map(tipo => (
                    <option key={tipo.id} value={tipo.id}>
                      {tipo.nome}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-actions">
                <button type="submit" className="btn btn-success" id="add-btn">
                  Adicionar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Editar Modelo */}
      {isEditModalOpen && (
        <div
          className="modal-bg"
          onClick={e => handleModalBgClick(e, setIsEditModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Editar Modelo</h2>
              <button className="modal-close" onClick={closeEditModal} aria-label="Fechar modal">
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleEditSubmit}>
              <div className="form-group">
                <label htmlFor="editModelo">Nome do Modelo</label>
                <input
                  type="text"
                  name="modelo"
                  id="editModelo"
                  required
                  value={editModeloName}
                  onChange={e => setEditModeloName(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label htmlFor="editMarca">Marca</label>
                <select
                  name="marca"
                  id="editMarca"
                  required
                  value={editMarcaId}
                  onChange={e => setEditMarcaId(e.target.value)}
                >
                  <option value="">Selecione a marca</option>
                  {marcas.map(marca => (
                    <option key={marca.id} value={marca.id}>
                      {marca.nome}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="editTipoVeiculo">Tipo Veículo</label>
                <select
                  name="tipoVeiculo"
                  id="editTipoVeiculo"
                  required
                  value={editTipoId}
                  onChange={e => setEditTipoId(e.target.value)}
                >
                  <option value="">Selecione o tipo de veículo</option>
                  {tiposVeiculo.map(tipo => (
                    <option key={tipo.id} value={tipo.id}>
                      {tipo.nome}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-actions">
                <button type="submit" className="btn btn-success" id="save-btn">
                  Salvar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}

export default Modelos;
