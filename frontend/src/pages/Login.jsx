import React from 'react';
import Navbar from '../components/Navbar';  // ajuste o caminho conforme sua estrutura
import '../assets/css/styles.css';

function Login() {
  return (
    <div>
      <Navbar />

      <main>
        <div className="login-container">
          <form className="login-box">
            <h2>LOGIN</h2>
            <hr />
            <label htmlFor="username">Nome de usuário:</label>
            <input type="text" id="username" name="username" />
            <label htmlFor="password">Senha:</label>
            <input type="password" id="password" name="password" />
            <button type="submit">acessar</button>
          </form>
        </div>
      </main>
    </div>
  );
}

export default Login;
