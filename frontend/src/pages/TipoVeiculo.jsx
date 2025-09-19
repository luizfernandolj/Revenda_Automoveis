import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import '../assets/css/marcas.css';

function TipoVeiculo() {
  const [tipos, setTipos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterText, setFilterText] = useState('');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingTipo, setEditingTipo] = useState(null);
  const [hamburgerActive, setHamburgerActive] = useState(false);
  const [newTipoNome, setNewTipoNome] = useState('');
  const [editTipoNome, setEditTipoNome] = useState('');

  // Buscar tipos de veículo ao montar
  useEffect(() => {
    fetchTipos();
  }, []);

  const fetchTipos = () => {
    setLoading(true);
    fetch('http://localhost:8080/tipo-veiculo')
      .then(res => {
        if (!res.ok) throw new Error('Erro ao buscar tipos de veículo da API');
        return res.json();
      })
      .then(data => {
        setTipos(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  };

  // Buscar tipo por ID via filtro
  const handleFilterSubmit = (e) => {
    e.preventDefault();
    if (!filterText.trim()) {
      fetchTipos();
      return;
    }
    const idBusca = Number(filterText);
    if (isNaN(idBusca)) {
      setError('Filtro deve ser um número válido de ID');
      setTipos([]);
      return;
    }
    setLoading(true);
    setError(null);
    fetch(`http://localhost:8080/tipo-veiculo/id?id=${idBusca}`)
      .then((res) => {
        if (!res.ok) {
          if (res.status === 404) return null;
          throw new Error('Erro ao buscar tipo-veículo pelo ID');
        }
        return res.json();
      })
      .then((data) => {
        if (data) {
          setTipos([data]);
        } else {
          setTipos([]);
          setError('Nenhum tipo-veículo encontrado com este ID');
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setTipos([]);
        setLoading(false);
      });
  };

  const toggleHamburger = () => {
    setHamburgerActive(!hamburgerActive);
  };

  // Modais
  const openAddModal = () => {
    setNewTipoNome('');
    setIsAddModalOpen(true);
  };
  const closeAddModal = () => {
    setIsAddModalOpen(false);
  };
  const openEditModal = (tipo) => {
    setEditingTipo(tipo);
    setEditTipoNome(tipo.nome);
    setIsEditModalOpen(true);
  };
  const closeEditModal = () => {
    setIsEditModalOpen(false);
    setEditingTipo(null);
  };

  // Adicionar tipo-veículo
  const handleAddSubmit = (e) => {
    e.preventDefault();
    if (!newTipoNome.trim()) return;
    fetch('http://localhost:8080/tipo-veiculo/cadastrar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome: newTipoNome }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Erro ao cadastrar novo tipo-veículo');
        return res.json();
      })
      .then(newTipo => {
        setTipos(prev => [...prev, newTipo]);
        closeAddModal();
      })
      .catch(err => setError(err.message));
  };

  // Editar tipo-veículo
  const handleEditSubmit = (e) => {
    e.preventDefault();
    if (!editTipoNome.trim() || !editingTipo) return;
    fetch(`http://localhost:8080/tipo-veiculo/${editingTipo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome: editTipoNome }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Erro ao atualizar tipo-veículo');
        return res.json();
      })
      .then(updatedTipo => {
        setTipos(tipos.map(t => (t.id === updatedTipo.id ? updatedTipo : t)));
        closeEditModal();
      })
      .catch(err => setError(err.message));
  };

  // Remover localmente (adapte para DELETE API se necessário)
  const handleRemoveTipo = (id) => {
    setTipos(tipos.filter(tipo => tipo.id !== id));
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
          <h1>Tipos de Veículo</h1>
          <section className="maintenance-table-section">
            <table className="maintenance-table">
              <thead>
                <tr>
                  <th>Id</th>
                  <th>Tipo</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {tipos.length > 0 ? (
                  tipos.map(({ id, nome }) => (
                    <tr key={id}>
                      <td>{id}</td>
                      <td>{nome}</td>
                      <td>
                        <button
                          className="btn-remove"
                          title="Remover tipo-veículo"
                          onClick={() => handleRemoveTipo(id)}
                        >
                          &#128465;
                        </button>{' '}
                        <a
                          href="#"
                          className="btn-details"
                          onClick={e => {
                            e.preventDefault();
                            openEditModal({ id, nome });
                          }}
                        >
                          Editar
                        </a>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="3" style={{ textAlign: 'center' }}>
                      Nenhum tipo-veículo encontrado
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
              TipoVeiculo
              <input
                type="text"
                placeholder="ID do TipoVeiculo"
                name="tipoVeiculo"
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
            + Novo TipoVeiculo
          </button>
        </aside>
      </div>

      {/* Modal Adicionar TipoVeiculo */}
      {isAddModalOpen && (
        <div
          className="modal-bg"
          onClick={e => handleModalBgClick(e, setIsAddModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Novo Tipo de Veículo</h2>
              <button className="modal-close" onClick={closeAddModal} aria-label="Fechar modal">
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleAddSubmit}>
              <div className="form-group">
                <label htmlFor="addTipoVeiculo">Nome do Tipo de Veículo</label>
                <input
                  type="text"
                  name="tipoVeiculo"
                  id="addTipoVeiculo"
                  required
                  value={newTipoNome}
                  onChange={e => setNewTipoNome(e.target.value)}
                />
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

      {/* Modal Editar TipoVeiculo */}
      {isEditModalOpen && (
        <div
          className="modal-bg"
          onClick={e => handleModalBgClick(e, setIsEditModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Editar Tipo de Veículo</h2>
              <button className="modal-close" onClick={closeEditModal} aria-label="Fechar modal">
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleEditSubmit}>
              <div className="form-group">
                <label htmlFor="editTipoVeiculo">Nome do Tipo de Veículo</label>
                <input
                  type="text"
                  name="tipoVeiculo"
                  id="editTipoVeiculo"
                  required
                  value={editTipoNome}
                  onChange={e => setEditTipoNome(e.target.value)}
                />
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

export default TipoVeiculo;
