
***

## Como Rodar o Projeto Revenda_Automoveis


***

### 1. Instalação das Ferramentas Necessárias

- **Java JDK:** Baixe e instale a versão 17 ou superior do JDK no site oficial da Oracle, escolhendo a versão correta para seu sistema operacional (Windows, macOS ou Linux):
https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- **Apache Tomcat 9:** Baixe a distribuição binária correspondente ao seu sistema operacional em:
https://tomcat.apache.org/download-90.cgi
Escolha a seção "Binary Distributions" e baixe o arquivo core (como `.zip` ou `.tar.gz`).
- **Node.js e npm:** Baixe a versão LTS 18.x do Node.js em:
https://nodejs.org/en/download/

***

### 2. Configuração das Variáveis de Ambiente

- **Java JDK:** Configure a variável `JAVA_HOME` apontando para a pasta do JDK instalado e adicione a pasta `bin` do Java ao PATH. No Windows, isso é feito via configurações do sistema; no macOS, editando os arquivos de perfil do shell (`.zshrc`, `.bash_profile`).
- **Apache Tomcat:** Opcionalmente configure a variável `CATALINA_HOME` apontando para a pasta raiz onde o Tomcat foi descompactado e adicione o diretório `bin` dele ao PATH para facilitar comandos.
- **Node.js:** Normalmente o instalador do Node já configura automaticamente as variáveis para uso.

***

### 3. Clonar o Repositório

No terminal, rode:

```bash
git clone https://github.com/luizfernandolj/Revenda_Automoveis.git
cd Revenda_Automoveis
```


***

### 4. Gerar o arquivo WAR para o Backend Spring

1. Entre na pasta do backend:
```bash
cd backend
```

2. Gere o arquivo `.war` usando Maven (ou Gradle, se aplicável):
```bash
mvn clean package
```

3. Após o comando, o arquivo WAR será criado na pasta `target` com nome algo como `backend.war`.
4. Renomeie o arquivo `.war` para `ROOT.war` para que seja o aplicativo padrão no Tomcat:
```bash
mv target/backend.war ROOT.war
```


***

### 5. Deploy no Apache Tomcat e Execução

1. Copie o arquivo `ROOT.war` para a pasta `webapps` do Tomcat (local onde foi descompactado/extraiu o Tomcat).
2. Para iniciar o Tomcat, execute o script `startup.bat` (Windows) ou `startup.sh` (macOS/Linux) dentro da pasta `bin` do Tomcat.
3. O Tomcat irá descompactar o `ROOT.war` e sua aplicação backend ficará disponível na URL:
http://localhost:8080/
4. Para parar o servidor use os scripts `shutdown.bat` ou `shutdown.sh`.

***

### 6. Rodar o Frontend React

1. No terminal, navegue para a pasta frontend:
```bash
cd ../frontend
```

2. Instale as dependências:
```bash
npm install
```

3. Execute o projeto React:
```bash
npm start
```

4. O React rodará no navegador na URL padrão:
http://localhost:3000/
5. Para integração com o backend, verifique se no `package.json` do frontend está configurado o proxy para `http://localhost:8080`, para evitar problemas de CORS.

***