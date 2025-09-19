
***

## Como Rodar o Projeto Revenda_Automoveis

***

### 1. Instalação das Ferramentas Necessárias

- **Java JDK:** Baixe e instale a versão 17 ou superior do JDK no site oficial da Oracle, escolhendo a versão correta para seu sistema operacional (Windows, macOS ou Linux):
[https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Apache Tomcat 9:** Baixe a distribuição binária correspondente ao seu sistema operacional em:
[https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi)
Escolha a seção "Binary Distributions" e baixe o arquivo core (como `.zip` ou `.tar.gz`).
- **Node.js e npm:** Baixe a versão LTS 18.x do Node.js em:
[https://nodejs.org/en/download/](https://nodejs.org/en/download/)

***

### 2. Configuração das Variáveis de Ambiente

- **Java JDK:** Configure a variável `JAVA_HOME` apontando para a pasta do JDK instalado e adicione a pasta `bin` do Java ao PATH. No Windows, isso é feito via configurações do sistema; no macOS, editando os arquivos de perfil do shell (`.zshrc`, `.bash_profile`).
- **Apache Tomcat:** Opcionalmente configure a variável `CATALINA_HOME` apontando para a pasta raiz onde o Tomcat foi descompactado e adicione o diretório `bin` dele ao PATH para facilitar comandos.
- **Node.js:** Normalmente o instalador do Node já configura automaticamente as variáveis para uso.

***

### 3. Clonar o Repositório

No terminal, rode:

```bash
git clone [https://github.com/luizfernandolj/Revenda_Automoveis.git](https://github.com/luizfernandolj/Revenda_Automoveis.git)
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
mv target/revenda-veiculos-1.0-SNAPSHOT.war ROOT.war
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

adicione também se tiver diferenças para mac

Segue o relatório completo com o passo a passo para rodar o projeto Revenda_Automoveis, incluindo as diferenças para macOS quando aplicável.

***

## Como Rodar o Projeto Revenda_Automoveis


***

### 1. Instalação das Ferramentas Necessárias

- **Java JDK:** Baixe e instale a versão 17 ou superior do JDK no site oficial da Oracle, escolhendo a versão correta para seu sistema operacional (Windows, macOS ou Linux):
https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- **Apache Tomcat 9:** Baixe a distribuição binária correspondente ao seu sistema operacional em:
https://tomcat.apache.org/download-90.cgi
Na seção "Binary Distributions", baixe o arquivo core — por exemplo, `.zip` no Windows ou `.tar.gz` no macOS/Linux.
- **Node.js e npm:** Baixe a versão LTS 18.x do Node.js em:
https://nodejs.org/en/download/

***

### 2. Configuração das Variáveis de Ambiente

- **Java JDK:** Após instalar, configure a variável `JAVA_HOME` apontando para a pasta do JDK no seu sistema:
    - No Windows, faça isso nas configurações do sistema, área de variáveis de ambiente.
    - No macOS, edite o arquivo de perfil do shell (`.zshrc`, `.bash_profile`) para definir `JAVA_HOME` apontando para o diretório de instalação do Java e adicione seu `bin` no PATH.
- **Apache Tomcat:** Opcionalmente, defina a variável `CATALINA_HOME` apontando para a pasta onde o Tomcat foi descompactado:
    - No Windows, nas variáveis de ambiente.
    - No macOS, no arquivo de perfil do shell.
    - Também pode adicionar o diretório `bin` do Tomcat ao PATH para facilitar o uso de comandos.
- **Node.js:** Normalmente o instalador já faz essa configuração, mas caso necessário, adicione a pasta do Node.js ao PATH no mesmo modo descrito acima para seu sistema.

***

### 3. Clonar o Repositório

Abra o terminal e execute:

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

2. Execute o comando para gerar o arquivo `.war`:
```bash
mvn clean package
```

3. O arquivo WAR será criado em `target` com nome parecido com `backend.war`.
4. Renomeie o arquivo para `ROOT.war` para que o Tomcat o reconheça como aplicação padrão.

**No Windows:**

```bash
ren target\backend.war ROOT.war
```

**No macOS/Linux:**

```bash
mv target/backend.war ROOT.war
```


***

### 5. Deploy no Apache Tomcat e Execução

1. Copie o arquivo `ROOT.war` para a pasta `webapps` do Tomcat (local onde o Tomcat foi descompactado).
2. Para iniciar o Tomcat:

- No Windows, execute `startup.bat` localizado na pasta `bin` do Tomcat.
- No macOS/Linux, execute o script `startup.sh` na pasta `bin` com permissão de execução.

3. O Tomcat vai descompactar o `ROOT.war` automaticamente e o backend estará disponível em:
http://localhost:8080/
4. Para parar o servidor, use os scripts `shutdown.bat` (Windows) ou `shutdown.sh` (macOS/Linux).

***

### 6. Rodar o Frontend React

1. No terminal, navegue para a pasta frontend:
```bash
cd ../frontend
```

2. Instale as dependências com:
```bash
npm install
```

3. Inicie o frontend React:
```bash
npm start
```

4. O React estará disponível no navegador em:
http://localhost:3000/
5. Para que o React comunique com o backend sem problemas de CORS, verifique se o arquivo `package.json` na pasta frontend tem configurado o proxy para `http://localhost:8080`.

***
