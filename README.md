
***

# Como Rodar o Projeto Revenda_Automoveis

***

## 1. Instalação das Ferramentas Necessárias

- **Java JDK:** Baixe e instale a versão 17 ou superior do JDK no site oficial da Oracle, escolhendo a versão correta para seu sistema operacional (Windows, macOS ou Linux):
[https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Apache Tomcat 9:** Baixe a distribuição binária correspondente ao seu sistema operacional em:
[https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi)
Na seção "Binary Distributions", baixe o arquivo core — por exemplo, `.zip` no Windows ou `.tar.gz` no macOS/Linux.
- **Node.js e npm:** Baixe a versão LTS 18.x do Node.js em:
[https://nodejs.org/en/download/](https://nodejs.org/en/download/)

***

## 2. Configuração das Variáveis de Ambiente

- **Java JDK:** Após instalar, configure a variável `JAVA_HOME` apontando para a pasta do JDK no seu sistema:
    - No Windows, faça isso nas configurações do sistema, área de variáveis de ambiente.
    - No macOS, edite o arquivo de perfil do shell (`.zshrc`, `.bash_profile`) para definir `JAVA_HOME` apontando para o diretório de instalação do Java e adicione seu `bin` no PATH.
- **Apache Tomcat:** Opcionalmente, defina a variável `CATALINA_HOME` apontando para a pasta onde o Tomcat foi descompactado:
    - No Windows, nas variáveis de ambiente.
    - No macOS, no arquivo de perfil do shell.
    - Também pode adicionar o diretório `bin` do Tomcat ao PATH para facilitar o uso de comandos.
- **Node.js:** Normalmente o instalador já faz essa configuração, mas caso necessário, adicione a pasta do Node.js ao PATH no mesmo modo descrito acima para seu sistema.

***

## 3. Clonar o Repositório

Abra o terminal e execute:

```bash
git clone https://github.com/luizfernandolj/Revenda_Automoveis.git
cd Revenda_Automoveis
```


***

## 4. Gerar o arquivo WAR para o Backend Spring

1. Entre na pasta do backend:
```bash
cd backend
```

2. Execute o comando para gerar o arquivo `.war`:
```bash
mvn clean package
```

3. O arquivo WAR será criado em `target` com nome parecido com `revenda-veiculos-1.0-SNAPSHOT.war`.
4. Renomeie o arquivo para `ROOT.war` para que o Tomcat o reconheça como aplicação padrão.

**No Windows:**

```bash
ren target\revenda-veiculos-1.0-SNAPSHOT.war ROOT.war
```

**No macOS/Linux:**

```bash
mv target/revenda-veiculos-1.0-SNAPSHOT.war ROOT.war
```


***

## 5. Deploy no Apache Tomcat e Execução

1. Copie o arquivo `ROOT.war` para a pasta `webapps` do Tomcat (local onde o Tomcat foi descompactado).
2. Para iniciar o Tomcat:

- No Windows, execute `startup.bat` localizado na pasta `bin` do Tomcat.
- No macOS/Linux, execute o script `startup.sh` na pasta `bin` com permissão de execução.

3. O Tomcat vai descompactar o `ROOT.war` automaticamente e o backend estará disponível em:
http://localhost:8080/
4. Para parar o servidor, use os scripts `shutdown.bat` (Windows) ou `shutdown.sh` (macOS/Linux).

***

## 6. Rodar o Frontend React

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

## 7. Criar e Popular o Banco de Dados e Ajustar o arquivo de Conexão

### Criar e Popular o Banco de Dados PostgreSQL

1. Crie o banco de dados com o nome desejado (por exemplo, `revenda_veiculos`).
2. Para criar as tabelas e a estrutura do banco, copie e execute o conteúdo do arquivo **query1.txt** no seu cliente PostgreSQL.
3. Para inserir os dados iniciais, copie e execute o conteúdo do arquivo **query2.txt** no seu cliente PostgreSQL.

***

### Modificar o arquivo de conexão `ConexaoBD.java`

- O arquivo está localizado em `backend/src/main/java/bd/ConexaoBD.java`.
- Originalmente está configurado para MySQL com algo similar a:

```java
String url = "jdbc:mysql://localhost:3306/revenda_veiculos";
String usuario = "root";
String senha = "root";
String driver = "com.mysql.cj.jdbc.Driver";
```

- Para usar PostgreSQL, deve ajustar para:

```java
String url = "jdbc:postgresql://localhost:5432/revenda_veiculos";
String usuario = "postgres"; // ou seu usuário do Postgres
String senha = "sua_senha"; // sua senha do Postgres
String driver = "org.postgresql.Driver";
```

- Pode implementar uma forma de escolher o banco (MySQL ou PostgreSQL) alterando os valores na classe conforme desejado.
- Certifique-se de que o `pom.xml` do backend contenha a dependência do driver JDBC do banco que usar (MySQL e/ou PostgreSQL).

***