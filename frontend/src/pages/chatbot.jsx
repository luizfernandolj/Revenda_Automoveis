import React, { useState, useRef, useEffect } from "react";
import Navbar from "../components/Navbar";

function Chatbot() {
  const [pergunta, setPergunta] = useState("");
  const [chat, setChat] = useState([]); // Array de mensagens {remetente, texto}
  const [status, setStatus] = useState("");
  const chatContainerRef = useRef(null);

  const fazerPergunta = async () => {
    if (!pergunta.trim()) {
      alert("Digite uma pergunta");
      return;
    }

    adicionarMensagem("Você", pergunta);
    setPergunta("");
    setStatus("Processando...");

    try {
      const res = await fetch(
        `http://localhost:8080/chatbot?mensagem=${encodeURIComponent(pergunta)}`,
        { method: "GET", headers: { "Content-Type": "application/json" } }
      );
      const data = await res.json();
      setStatus("");

      let respostaText = "";

      console.log("Resposta do backend:", data);

      switch (data.intencao) {
        case "LISTAR_MARCAS": {
          const marcasRes = await fetch("http://localhost:8080/marca");
          const marcas = await marcasRes.json();
          respostaText = marcas.map(m => `${m.id} - ${m.nome}`).join("\n");
          break;
        }
        case "CRIAR_MARCA": {
          await fetch("http://localhost:8080/marca/cadastrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data.dados),
          });
          respostaText = "Marca cadastrada com sucesso!";
          break;
        }
        case "LISTAR_MODELOS": {
          const modelosRes = await fetch("http://localhost:8080/modelo");
          const modelos = await modelosRes.json();
          respostaText = modelos
            .map(m => `${m.id} - ${m.nome} - ${m.marca?.nome ?? ""}`)
            .join("\n");
          break;
        }
        case "CRIAR_MODELO": {
          await fetch("http://localhost:8080/modelo/cadastrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data.dados),
          });
          respostaText = "Modelo cadastrado com sucesso!";
          break;
        }
        case "LISTAR_TIPOS_VEICULO": {
          const tiposRes = await fetch("http://localhost:8080/tipo-veiculo");
          const tipos = await tiposRes.json();
          respostaText = tipos.map(t => `${t.id} - ${t.nome}`).join("\n");
          break;
        }
        case "CRIAR_TIPO_VEICULO": {
          await fetch("http://localhost:8080/tipo-veiculo/cadastrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data.dados),
          });
          respostaText = "Tipo de veículo cadastrado com sucesso!";
          break;
        }
        case "LISTAR_CORES": {
          const coresRes = await fetch("http://localhost:8080/cor");
          const cores = await coresRes.json();
          respostaText = cores.map(c => `${c.id} - ${c.nome}`).join("\n");
          break;
        }
        case "CRIAR_COR": {
          await fetch("http://localhost:8080/cor/cadastrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data.dados),
          });
          respostaText = "Cor cadastrada com sucesso!";
          break;
        }
        case "BUSCAR_VEICULO": {
          const veiculoId = data.dados?.id; // ou data.dados?.id, depende do que backend chatbot envia
          if (!veiculoId) {
            respostaText = "ID do veículo não informado.";
            break;
          }
          const veiculosRes = await fetch(`http://localhost:8080/veiculo/id?id=${veiculoId}`);
          if (!veiculosRes.ok) {
            respostaText = `Veículo com ID ${veiculoId} não encontrado.`;
            break;
          }
          const veiculo = await veiculosRes.json();
          respostaText = `${veiculo.id} - ${veiculo.modelo?.marca?.nome ?? ""} - ${veiculo.modelo?.nome ?? ""} - ${veiculo.cor?.nome ?? ""} - ${veiculo.tipoVeiculo?.nome ?? ""}`;
          break;
        }

        case "BUSCAR_MARCA": {
          const marcaId = data.dados?.id;
          if (!marcaId) {
            respostaText = "ID da marca não informado.";
            break;
          }
          const marcaRes = await fetch(`http://localhost:8080/marca/id?id=${marcaId}`);
          if (!marcaRes.ok) {
            respostaText = `Marca com ID ${marcaId} não encontrada.`;
            break;
          }
          const marca = await marcaRes.json();
          respostaText = `${marca.id} - ${marca.nome}`;
          break;
        }

        case "BUSCAR_MODELO": {
          const modeloId = data.dados?.id;
          if (!modeloId) {
            respostaText = "ID do modelo não informado.";
            break;
          }
          const modeloRes = await fetch(`http://localhost:8080/modelo/id?id=${modeloId}`);
          if (!modeloRes.ok) {
            respostaText = `Modelo com ID ${modeloId} não encontrado.`;
            break;
          }
          const modelo = await modeloRes.json();
          respostaText = `${modelo.id} - ${modelo.nome} - ${modelo.marca?.nome ?? ""}`;
          break;
        }

        case "BUSCAR_TIPO_VEICULO": {
          const tipoId = data.dados?.id;
          if (!tipoId) {
            respostaText = "ID do tipo de veículo não informado.";
            break;
          }
          const tipoRes = await fetch(`http://localhost:8080/tipo-veiculo/id?id=${tipoId}`);
          if (!tipoRes.ok) {
            respostaText = `Tipo de veículo com ID ${tipoId} não encontrado.`;
            break;
          }
          const tipo = await tipoRes.json();
          respostaText = `${tipo.id} - ${tipo.nome}`;
          break;
        }

        case "BUSCAR_COR": {
          const corId = data.dados?.id;
          if (!corId) {
            respostaText = "ID da cor não informada.";
            break;
          }
          const corRes = await fetch(`http://localhost:8080/cor/id?id=${corId}`);
          if (!corRes.ok) {
            respostaText = `Cor com ID ${corId} não encontrada.`;
            break;
          }
          const cor = await corRes.json();
          respostaText = `${cor.id} - ${cor.nome}`;
          break;
        }

      
        case "CRIAR_VEICULO": {
          await fetch("http://localhost:8080/veiculo/cadastrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data.dados),
          });
          respostaText = "Veículo cadastrado com sucesso!";
          break;
        }
        default:
          respostaText = data.mensagemPadrao || "Não entendi sua pergunta.";
          break;
      }
      adicionarMensagem("Assistente", respostaText);
    } catch (err) {
      console.error(err);
      adicionarMensagem("Assistente", `Erro ao conectar: ${err.message}`);
      setStatus("");
    }
  };

  const adicionarMensagem = (remetente, mensagem) => {
    setChat(prev => [...prev, { remetente, texto: mensagem }]);
  };

  const limparConversa = () => {
    setChat([]);
    setStatus("");
  };

  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  }, [chat]);

  return (
    <div style={{ fontFamily: "Arial, sans-serif", margin: 20 }}>
      <Navbar />
      <h1>Assistente - Loja de Revenda de Automóveis</h1>

      <div
        id="chat-container"
        ref={chatContainerRef}
        style={{
          border: "1px solid #ccc",
          padding: 10,
          height: 600,
          overflowY: "auto",
          marginBottom: 10,
          backgroundColor: "#2c2c3e",
          color: "#fff",
          borderRadius: 5,
        }}
      >
        {chat.map((msg, i) => {
          const isUser = msg.remetente === "Você";
          return (
            <div
              key={i}
              className="msg"
              style={{
                marginBottom: 8,
                display: "flex",
                justifyContent: isUser ? "flex-end" : "flex-start",
              }}
            >
              <div
                style={{
                  backgroundColor: isUser ? "#4a90e2" : "#44475a",
                  color: isUser ? "#fff" : "#ddd",
                  padding: "8px 12px",
                  borderRadius: 15,
                  maxWidth: "70%",
                  wordBreak: "break-word",
                }}
              >
                <strong
                  style={{
                    color: isUser ? "#cce4ff" : "#ccc",
                    marginRight: 6,
                  }}
                >
                  {msg.remetente}:
                </strong>{" "}
                <span
                  dangerouslySetInnerHTML={{
                    __html: msg.texto.replace(/\n/g, "<br>"),
                  }}
                />
              </div>
            </div>
          );
        })}
      </div>

      <input
        id="pergunta"
        type="text"
        placeholder="Ex: Quais veículos existem?"
        style={{ width: "80%", padding: 5, fontSize: 16 }}
        value={pergunta}
        onChange={(e) => setPergunta(e.target.value)}
        onKeyPress={(e) => {
          if (e.key === "Enter") fazerPergunta();
        }}
      />
      <button
        id="btn"
        style={{ padding: "6px 12px", fontSize: 16, marginLeft: 8 }}
        onClick={fazerPergunta}
      >
        Perguntar
      </button>
      <button
        style={{
          padding: "6px 12px",
          fontSize: 16,
          marginLeft: 8,
          backgroundColor: "#f44336",
          color: "#fff",
          border: "none",
          borderRadius: 4,
          cursor: "pointer",
        }}
        onClick={limparConversa}
      >
        Limpar Conversa
      </button>

      <div id="status" style={{ marginTop: 10, color: "#555" }}>
        {status}
      </div>
    </div>
  );
}

export default Chatbot;
