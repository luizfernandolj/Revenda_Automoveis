import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import '../assets/css/marcas.css';

function Marcas() {
  const [marcas, setMarcas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterText, setFilterText] = useState('');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingMarca, setEditingMarca] = useState(null);
  const [hamburgerActive, setHamburgerActive] = useState(false);
  const [newMarcaName, setNewMarcaName] = useState('');
  const [editMarcaName, setEditMarcaName] = useState('');

  // Busca marcas da API ao montar
  useEffect(() => {
    fetchMarcas();
  }, []);

  const fetchMarcas = () => {
    setLoading(true);
    fetch('http://localhost:8080/marca')
      .then(res => {
        if (!res.ok) {
          throw new Error('Erro ao buscar marcas da API');
        }
        return res.json();
      })
      .then(data => {
        setMarcas(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  };

  // Buscar por nome via filtro
  const handleFilterSubmit = (e) => {
    e.preventDefault();

    if (!filterText.trim()) {
      fetchMarcas();
      return;
    }
        // Tentamos converter o filtro para número para usar como id
    const idBusca = Number(filterText);
    if (isNaN(idBusca)) {
      setError('Filtro deve ser um número válido de ID');
      setMarcas([]);
      return;
    }

    setLoading(true);
    setError(null);
    fetch(`http://localhost:8080/marca/id?id=${idBusca}`)
      .then((res) => {
        if (!res.ok) {
          if (res.status === 404) {
            return null; // Não encontrado, retornamos null
          }
          throw new Error('Erro ao buscar marca pelo ID');
        }
        return res.json();
      })
      .then((data) => {
        if (data) {
          setMarcas([data]);
        } else {
          setMarcas([]);
          setError('Nenhuma marca encontrada com este ID');
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setMarcas([]);
        setLoading(false);
      });
  };

  const toggleHamburger = () => {
    setHamburgerActive(!hamburgerActive);
  };

  // Modais abrir/fechar
  const openAddModal = () => {
    setNewMarcaName('');
    setIsAddModalOpen(true);
  };
  const closeAddModal = () => {
    setIsAddModalOpen(false);
  };
  const openEditModal = (marca) => {
    setEditingMarca(marca);
    setEditMarcaName(marca.nome);
    setIsEditModalOpen(true);
  };
  const closeEditModal = () => {
    setIsEditModalOpen(false);
    setEditingMarca(null);
  };

  // Adicionar marca
  const handleAddSubmit = (e) => {
    e.preventDefault();
    if (!newMarcaName.trim()) return;

    fetch('http://localhost:8080/marca/cadastrar', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome: newMarcaName }),
    })
      .then(res => {
        if (!res.ok) {
          throw new Error('Erro ao cadastrar nova marca');
        }
        return res.json();
      })
      .then(newMarca => {
        setMarcas(prev => [...prev, newMarca]);
        closeAddModal();
      })
      .catch(err => setError(err.message));
  };

  // Editar marca
  const handleEditSubmit = (e) => {
    e.preventDefault();
    if (!editMarcaName.trim() || !editingMarca) return;

    fetch(`http://localhost:8080/marca/${editingMarca.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome: editMarcaName }),
    })
      .then(res => {
        if (!res.ok) {
          throw new Error('Erro ao atualizar marca');
        }
        return res.json();
      })
      .then(updatedMarca => {
        setMarcas(marcas.map(m => (m.id === updatedMarca.id ? updatedMarca : m)));
        closeEditModal();
      })
      .catch(err => setError(err.message));
  };

  // Remover marca localmente (adapte para DELETE API se houver)
  const handleRemoveMarca = (id) => {
    setMarcas(marcas.filter(marca => marca.id !== id));
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
          <h1>Marcas</h1>
          <section className="maintenance-table-section">
            <table className="maintenance-table">
              <thead>
                <tr>
                  <th>Id</th>
                  <th>Marca</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {marcas.length > 0 ? (
                  marcas.map(({ id, nome }) => (
                    <tr key={id}>
                      <td>{id}</td>
                      <td>{nome}</td>
                      <td>
                        <button
                          className="btn-remove"
                          title="Remover marca"
                          onClick={() => handleRemoveMarca(id)}
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
                      Nenhuma marca encontrada
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
              Marca
              <input
                type="text"
                placeholder="Nome da Marca"
                name="marca"
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
            + Nova Marca
          </button>
        </aside>
      </div>

      {/* Modal Adicionar Marca */}
      {isAddModalOpen && (
        <div
          className="modal-bg"
          onClick={e => handleModalBgClick(e, setIsAddModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Nova Marca</h2>
              <button className="modal-close" onClick={closeAddModal} aria-label="Fechar modal">
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleAddSubmit}>
              <div className="form-group">
                <label htmlFor="addMarca">Nome da Marca</label>
                <input
                  type="text"
                  name="marca"
                  id="addMarca"
                  required
                  value={newMarcaName}
                  onChange={e => setNewMarcaName(e.target.value)}
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

      {/* Modal Editar Marca */}
      {isEditModalOpen && (
        <div
          className="modal-bg"
          onClick={e => handleModalBgClick(e, setIsEditModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Editar Marca</h2>
              <button className="modal-close" onClick={closeEditModal} aria-label="Fechar modal">
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleEditSubmit}>
              <div className="form-group">
                <label htmlFor="editMarca">Nome da Marca</label>
                <input
                  type="text"
                  name="marca"
                  id="editMarca"
                  required
                  value={editMarcaName}
                  onChange={e => setEditMarcaName(e.target.value)}
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

export default Marcas;
