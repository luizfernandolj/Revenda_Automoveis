import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import '../assets/css/cores.css';

function Cores() {
  const [cores, setCores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterText, setFilterText] = useState('');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingCor, setEditingCor] = useState(null);
  const [hamburgerActive, setHamburgerActive] = useState(false);
  const [newCorName, setNewCorName] = useState('');
  const [editCorName, setEditCorName] = useState('');

  // Busca todas cores da API quando o componente monta
  useEffect(() => {
    fetchCores();
  }, []);

  const fetchCores = () => {
    setLoading(true);
    fetch('http://localhost:8080/cor')
      .then((res) => {
        if (!res.ok) {
          throw new Error('Erro ao buscar cores da API');
        }
        return res.json();
      })
      .then((data) => {
        setCores(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  };

  // Busca por ID via filtro
  const handleFilterSubmit = (e) => {
    e.preventDefault();

    if (!filterText.trim()) {
      // Se não informou filtro, busca a lista completa
      fetchCores();
      return;
    }

    // Tentamos converter o filtro para número para usar como id
    const idBusca = Number(filterText);
    if (isNaN(idBusca)) {
      setError('Filtro deve ser um número válido de ID');
      setCores([]);
      return;
    }

    setLoading(true);
    setError(null);
    fetch(`http://localhost:8080/cor/id?id=${idBusca}`)
      .then((res) => {
        if (!res.ok) {
          if (res.status === 404) {
            return null; // Não encontrado, retornamos null
          }
          throw new Error('Erro ao buscar cor pelo ID');
        }
        return res.json();
      })
      .then((data) => {
        if (data) {
          setCores([data]);
        } else {
          setCores([]);
          setError('Nenhuma cor encontrada com este ID');
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setCores([]);
        setLoading(false);
      });
  };

  // Ações do menu hambúrguer
  const toggleHamburger = () => {
    setHamburgerActive(!hamburgerActive);
  };

  // Abrir/fechar modais
  const openAddModal = () => {
    setNewCorName('');
    setIsAddModalOpen(true);
  };
  const closeAddModal = () => {
    setIsAddModalOpen(false);
  };
  const openEditModal = (cor) => {
    setEditingCor(cor);
    setEditCorName(cor.nome);
    setIsEditModalOpen(true);
  };
  const closeEditModal = () => {
    setIsEditModalOpen(false);
    setEditingCor(null);
  };

  // Submit adicionar cor chamando cor/cadastrar
  const handleAddSubmit = (e) => {
    e.preventDefault();
    if (!newCorName.trim()) return;

    fetch('http://localhost:8080/cor/cadastrar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ nome: newCorName }),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Erro ao cadastrar nova cor');
        }
        return res.json();
      })
      .then((newCor) => {
        // Após cadastrar, atualiza lista completa (ou adiciona localmente)
        setCores((prev) => [...prev, newCor]);
        closeAddModal();
      })
      .catch((err) => {
        setError(err.message);
      });
  };

  // Submit editar cor (sem mudança de endpoint, pode ajustar se precisar)
  const handleEditSubmit = (e) => {
    e.preventDefault();
    if (!editCorName.trim() || !editingCor) return;

    // Exemplo: pode haver endpoint para update, ajustar conforme API
    fetch(`http://localhost:8080/cor/${editingCor.id}`, {
      method: 'PUT', // ou PATCH, conforme sua API
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ nome: editCorName }),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Erro ao atualizar cor');
        }
        return res.json();
      })
      .then((updatedCor) => {
        setCores(
          cores.map((c) => (c.id === updatedCor.id ? updatedCor : c))
        );
        closeEditModal();
      })
      .catch((err) => {
        setError(err.message);
      });
  };

  // Remover cor localmente (ajustar chamada API se tiver)
  const handleRemoveCor = (id) => {
    // Se sua API tiver endpoint para remover, faça chamada DELETE aqui
    setCores(cores.filter((cor) => cor.id !== id));
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
      <Navbar
        hamburgerActive={hamburgerActive}
        toggleHamburger={toggleHamburger}
      />

      <div className="container">
        <main className="main-content">
          <h1>Cores</h1>
          <section className="maintenance-table-section">
            <table className="maintenance-table">
              <thead>
                <tr>
                  <th>Id</th>
                  <th>Cores</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {cores.length > 0 ? (
                  cores.map(({ id, nome }) => (
                    <tr key={id}>
                      <td>{id}</td>
                      <td>{nome}</td>
                      <td>
                        <button
                          className="btn-remove"
                          title="Remover cor"
                          onClick={() => handleRemoveCor(id)}
                        >
                          &#128465;
                        </button>{' '}
                        <a
                          className="btn-details"
                          href="#"
                          onClick={(e) => {
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
                      Nenhuma cor encontrada
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
              Cor
              <input
                type="text"
                placeholder="Digite o ID para buscar"
                name="cliente"
                value={filterText}
                onChange={(e) => setFilterText(e.target.value)}
              />
            </label>
            <button type="submit" className="btn btn-primary">
              Filtrar
            </button>
          </form>
          <hr />
          <button
            id="openAddModal"
            className="btn btn-success"
            onClick={openAddModal}
          >
            + Nova Cor
          </button>
        </aside>
      </div>

      {isAddModalOpen && (
        <div
          className="modal-bg"
          onClick={(e) => handleModalBgClick(e, setIsAddModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Nova Cor</h2>
              <button
                className="modal-close"
                onClick={closeAddModal}
                aria-label="Fechar modal"
              >
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleAddSubmit}>
              <div className="form-group">
                <label htmlFor="addCor">Cor</label>
                <input
                  type="text"
                  name="cor"
                  id="addCor"
                  required
                  value={newCorName}
                  onChange={(e) => setNewCorName(e.target.value)}
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

      {isEditModalOpen && (
        <div
          className="modal-bg"
          onClick={(e) => handleModalBgClick(e, setIsEditModalOpen)}
          style={{ display: 'flex' }}
        >
          <div className="modal wide-modal">
            <div className="modal-header">
              <h2>Editar Cor</h2>
              <button
                className="modal-close"
                onClick={closeEditModal}
                aria-label="Fechar modal"
              >
                &times;
              </button>
            </div>
            <form className="add-form grid-form" onSubmit={handleEditSubmit}>
              <div className="form-group">
                <label htmlFor="editCor">Cor</label>
                <input
                  type="text"
                  name="cor"
                  id="editCor"
                  required
                  value={editCorName}
                  onChange={(e) => setEditCorName(e.target.value)}
                />
              </div>
              <div className="form-actions">
                <button
                  type="submit"
                  className="btn btn-success"
                  id="save-btn"
                >
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

export default Cores;
