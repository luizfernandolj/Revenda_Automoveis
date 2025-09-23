package service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.Intencao;
import dto.RespostaIA;
import model.Cor;
import model.Marca;
import model.Modelo;
import model.TipoVeiculo;
import model.Veiculo;

public class ChatbotService {
    private final String geminiApiUrl;
    private final String geminiApiKey;

    private final String instrucaoInicial;
    private final RespostaIA<Void> respostaPadrao;

    private final ObjectMapper objectMapper;

    public ChatbotService() {
        geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
        geminiApiKey = "AIzaSyAaScJURSqg6E7Q-D8VOG9a0wZTatXmmSU";

        instrucaoInicial = """
            Você é um assistente virtual para um sistema de gerenciamento de uma revenda de automóveis.
            Sua especialidade é ajudar a gerenciar o inventário de veículos, incluindo suas marcas, modelos, cores e tipos. Responda apenas a perguntas relacionadas a essas operações e sempre em português.
            
            As ações que você pode executar são:
            - Para Marcas: criar uma nova marca, listar todas as marcas, buscar por id.
            - Para Modelos: criar um novo modelo, listar todos os modelos, buscar por id.
            - Para Cores: criar uma nova cor, listar todas as cores, buscar por id.
            - Para Tipos de Veículo: criar um novo tipo, listar todos os tipos, buscar por id.
            - Para Veículos: criar um novo veículo, listar todos os veículos em estoque, buscar por id.

            Exemplos:
                - "Busque o veículo com id 1" -> executar BUSCAR_VEICULO com {"idVeiculo": 1}
                - "Quais veículos existem?" -> executar LISTAR_VEICULOS
                - "Adicione a marca Fiat" -> executar CRIAR_MARCA com {"nomeMarca": "Fiat"}
            
            Quando lhe pedirem algo fora deste escopo, peça desculpas e explique que sua função se limita a gerenciar o inventário de veículos.
            """;

        respostaPadrao = new RespostaIA<>(
                null,
                null,
                "Sinto muito, não entendi o que você deseja saber sobre o sistema de revenda de automóveis"
        );

        objectMapper = new ObjectMapper();
    }

    public RespostaIA<?> obterResposta(String mensagem) throws Exception {
        return processarRespostaIA(enviarRequisicao(mensagem));
    }

    private String enviarRequisicao(String mensagem) throws IOException, InterruptedException {
        Map<String, Object> conteudoRequisicao = criarConteudoRequisicao(mensagem);

        ObjectMapper objectMapper = new ObjectMapper();
        String conteudoRequisicaoJSON = objectMapper.writeValueAsString(conteudoRequisicao);

        try(HttpClient clienteHttp = HttpClient.newHttpClient()) {
            HttpRequest requisicao = HttpRequest.newBuilder()
                    .uri(URI.create(geminiApiUrl))
                    .header("Content-Type", "application/json")
                    .header("X-goog-api-key", geminiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(conteudoRequisicaoJSON))
                    .build();

            HttpResponse<String> resposta = clienteHttp.send(requisicao, HttpResponse.BodyHandlers.ofString());

            return resposta.body();
        }
    }


    private Map<String, Object> criarConteudoRequisicao(String mensagem){
        // --- Funções para MARCA ---
        Map<String, Object> listarMarcas = Map.of(
                "name", Intencao.LISTAR_MARCAS.toString(),
                "description", "Lista todas as marcas de veículos cadastradas no sistema. Use quando o usuário pedir 'quais as marcas?', 'mostre todas as marcas', etc."
        );

        Map<String, Object> criarMarca = Map.of(
                "name", Intencao.CRIAR_MARCA.toString(),
                "description", "Cria/cadastra uma marca no sistema. Use quando o usuário pedir para 'adicionar marca Fiat'.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of("nomeMarca", Map.of("type", "string", "description", "O nome da marca a ser criado.")),
                        "required", List.of("nomeMarca")
                )
        );

        // --- Funções para MODELO ---
        Map<String, Object> listarModelos = Map.of(
                "name", Intencao.LISTAR_MODELOS.toString(),
                "description", "Lista todos os modelos de veículos cadastrados."
        );

        Map<String, Object> criarModelo = Map.of(
                "name", Intencao.CRIAR_MODELO.toString(),
                "description", "Cria/cadastra um modelo no sistema. Use quando o usuário pedir para 'adicionar modelo Uno'.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of("nomeModelo", Map.of("type", "string", "description", "O nome do modelo a ser criado."),
                                "idMarca", Map.of("type", "integer", "description", "Id da marca do modelo a ser criado.")),
                        "required", List.of("nomeModelo", "idMarca")
                )
        );

        // --- Funções para TIPO DE VEÍCULO ---
        Map<String, Object> listarTiposVeiculo = Map.of(
                "name", Intencao.LISTAR_TIPOS_VEICULO.toString(),
                "description", "Lista todos os tipos de veículos cadastrados (Sedan, Hatch, etc.)."
        );

        Map<String, Object> criarTipoVeiculo = Map.of(
                "name", Intencao.CRIAR_TIPO_VEICULO.toString(),
                "description", "Cria/cadastra uma novo tipo de veiculo no sistema. Use quando o usuário pedir para 'adicionar a tipo de veiculo sedan'.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of("nomeTipoVeiculo", Map.of("type", "string", "description", "O nome da tipo de veiculo a ser criado.")),
                        "required", List.of("nomeTipoVeiculo")
                )
        );

        // --- Funções para COR ---

        Map<String, Object> listarCores = Map.of(
                "name", Intencao.LISTAR_CORES.toString(),
                "description", "Lista todas as cores disponíveis no sistema."
        );

        Map<String, Object> criarCor = Map.of(
                "name", Intencao.CRIAR_COR.toString(),
                "description", "Cria/cadastra uma nova cor no sistema. Use quando o usuário pedir para 'adicionar a cor Azul Marinho'.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of("nomeCor", Map.of("type", "string", "description", "O nome da nova cor a ser criada.")),
                        "required", List.of("nomeCor")
                )
        );

        // --- Funções para VEÍCULO ---

        Map<String, Object> listarVeiculos = Map.of(
                "name", Intencao.LISTAR_VEICULOS.toString(),
                "description", "Lista todos os veículos cadastrados no sistema."
        );

        Map<String, Object> criarVeiculo = Map.of(
                "name", Intencao.CRIAR_VEICULO.toString(),
                "description", "Cria/cadastra um veiculo no sistema. Use quando o usuário pedir para 'adicionar veiculo com as seguintes informações'.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of("preco", Map.of("type", "number", "description", "Preço do veiculo a ser criado."),
                                "idModelo", Map.of("type", "integer", "description", "Id do modelo do veiculo a ser criado."),
                                "idTipoVeiculo", Map.of("type", "integer", "description", "Id do tipo de veiculo do veiculo a ser criado."),
                                "idCor", Map.of("type", "integer", "description", "Id da cor do veiculo a ser criado."),
                                "quilometragem", Map.of("type", "integer", "description", "Quilometragem do veiculo a ser criado."),
                                "ano", Map.of("type", "integer", "description", "Ano do veiculo a ser criado."),
                                "disponivel", Map.of("type", "integer", "description", "Se veiculo a ser criado está disponível.")),
                        "required", List.of("preco", "idModelo", "idTipoVeiculo", "idCor", "quilometragem", "disponivel", "ano")
                )
        );


        Map<String, Object> buscarVeiculo = Map.of(
            "name", Intencao.BUSCAR_VEICULO.toString(),
            "description", "Busca um veículo pelo seu ID no sistema.",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of(
                    "idVeiculo", Map.of(
                        "type", "integer",
                        "description", "ID do veículo a ser buscado"
                    )
                ),
                "required", List.of("idVeiculo")
            )
        );


        Map<String, Object> buscarMarca = Map.of(
            "name", Intencao.BUSCAR_MARCA.toString(),
            "description", "Busca uma marca pelo seu ID no sistema.",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of("idMarca", Map.of("type", "integer", "description", "ID da marca a ser buscada")),
                "required", List.of("idMarca")
            )
        );

        Map<String, Object> buscarModelo = Map.of(
            "name", Intencao.BUSCAR_MODELO.toString(),
            "description", "Busca um modelo pelo seu ID no sistema.",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of("idModelo", Map.of("type", "integer", "description", "ID do modelo a ser buscado")),
                "required", List.of("idModelo")
            )
        );

        Map<String, Object> buscarTipoVeiculo = Map.of(
            "name", Intencao.BUSCAR_TIPO_VEICULO.toString(),
            "description", "Busca um tipo de veículo pelo seu ID no sistema.",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of("idTipoVeiculo", Map.of("type", "integer", "description", "ID do tipo de veículo a ser buscado")),
                "required", List.of("idTipoVeiculo")
            )
        );

        Map<String, Object> buscarCor = Map.of(
            "name", Intencao.BUSCAR_COR.toString(),
            "description", "Busca uma cor pelo seu ID no sistema.",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of("idCor", Map.of("type", "integer", "description", "ID da cor a ser buscada")),
                "required", List.of("idCor")
            )
        );




        return Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", instrucaoInicial), // Sua instrução inicial sobre o sistema
                                        Map.of("text", mensagem)
                                )
                        )
                ),
                "tools", Map.of(
                        "function_declarations", List.of(
                                listarMarcas,
                                criarMarca,
                                listarModelos,
                                criarModelo,
                                listarTiposVeiculo,
                                criarTipoVeiculo,
                                listarCores,
                                criarCor,
                                listarVeiculos,
                                criarVeiculo,
                                buscarVeiculo,
                                buscarMarca,
                                buscarModelo,
                                buscarTipoVeiculo,
                                buscarCor
                        )
                )
        );
    }

    private RespostaIA<?> processarRespostaIA(String resposta) throws Exception {
        JsonNode noRaiz = objectMapper.readTree(resposta);

        if (noRaiz.has("candidates")) {
            return lidarRespostasCandidatas(noRaiz.path("candidates"));
        }

        return new RespostaIA<>(null, null, resposta);
    }

    private RespostaIA<?> lidarRespostasCandidatas(JsonNode nosCandidatos) throws Exception{
        if(nosCandidatos.isArray() && !nosCandidatos.isEmpty()){
            JsonNode noConteudo = nosCandidatos.get(0).path("content");

            if(noConteudo.has("parts") && noConteudo.path("parts").isArray()){
                return lidarPartesResposta(noConteudo.path("parts"));
            }
        }

        return respostaPadrao;
    }

    private RespostaIA<?> lidarPartesResposta(JsonNode partesResposta) throws Exception {
        if(!partesResposta.isEmpty()){
            JsonNode primeiraParte = partesResposta.get(0);

            if (primeiraParte.has("functionCall")) {
                JsonNode functionCallNode = primeiraParte.path("functionCall");
                return realizarChamadaServico(functionCallNode);
            } else if (primeiraParte.has("text")) {
                String resposta = primeiraParte.path("text").asText();

                return new RespostaIA<>(null, null, resposta);
            }
        }

        return respostaPadrao;
    }

    private RespostaIA<?> realizarChamadaServico(JsonNode noMetodoServico) throws Exception {
        String servico = noMetodoServico.path("name").asText();
        JsonNode argumentos = noMetodoServico.path("args");

        return switch (Intencao.valueOf(servico)) {
            case LISTAR_MARCAS -> criarRespostaParaListarMarcas();
            case CRIAR_MARCA -> criarRespostaParaCriarMarca(argumentos);
            case LISTAR_MODELOS -> criarRespostaParaListarModelos();
            case CRIAR_MODELO -> criarRespostaParaCriarModelo(argumentos);
            case LISTAR_TIPOS_VEICULO -> criarRespostaParaListarTiposVeiculo();
            case CRIAR_TIPO_VEICULO -> criarRespostaParaCriarTipoVeiculo(argumentos);
            case LISTAR_CORES -> criarRespostaParaListarCores();
            case CRIAR_COR -> criarRespostaParaCriarCor(argumentos);
            case LISTAR_VEICULOS -> criarRespostaParaListarVeiculos();
            case CRIAR_VEICULO -> criarRespostaParaCriarVeiculo(argumentos);
            case BUSCAR_VEICULO -> criarRespostaParaBuscarVeiculo(argumentos);
            case BUSCAR_MARCA -> criarRespostaParaBuscarMarca(argumentos);
            case BUSCAR_MODELO -> criarRespostaParaBuscarModelo(argumentos);
            case BUSCAR_TIPO_VEICULO -> criarRespostaParaBuscarTipoVeiculo(argumentos);
            case BUSCAR_COR -> criarRespostaParaBuscarCor(argumentos);
            default -> respostaPadrao;
        };
    }

    // --- Métodos para Marca ---
    private RespostaIA<Void> criarRespostaParaListarMarcas() {
        return new RespostaIA<>(Intencao.LISTAR_MARCAS, null, null);
    }

    private RespostaIA<Marca> criarRespostaParaCriarMarca(JsonNode argumentos) {
        String nomeMarca = argumentos.path("nomeMarca").asText();
        return new RespostaIA<>(Intencao.CRIAR_MARCA, new Marca(null, nomeMarca), null);
    }

    // --- Métodos para Modelo ---
    private RespostaIA<Void> criarRespostaParaListarModelos() {
        return new RespostaIA<>(Intencao.LISTAR_MODELOS, null, null);
    }

    private RespostaIA<Modelo> criarRespostaParaCriarModelo(JsonNode argumentos) {
        String nomeModelo = argumentos.path("nomeModelo").asText();
        Long idMarca = argumentos.path("idMarca").asLong();
        return new RespostaIA<>(Intencao.CRIAR_MODELO, new Modelo(null, nomeModelo, new Marca(idMarca, null)), null);
    }

    // --- Métodos para Tipo de Veículo ---
    private RespostaIA<Void> criarRespostaParaListarTiposVeiculo() {
        return new RespostaIA<>(Intencao.LISTAR_TIPOS_VEICULO, null, null);
    }

    private RespostaIA<TipoVeiculo> criarRespostaParaCriarTipoVeiculo(JsonNode argumentos) {
        String nomeTipoVeiculo = argumentos.path("nomeTipoVeiculo").asText();
        return new RespostaIA<>(Intencao.CRIAR_TIPO_VEICULO, new TipoVeiculo(null, nomeTipoVeiculo), null);
    }

    // --- Métodos para Cor ---
    private RespostaIA<Void> criarRespostaParaListarCores() {
        return new RespostaIA<>(Intencao.LISTAR_CORES, null, null);
    }

    private RespostaIA<Cor> criarRespostaParaCriarCor(JsonNode argumentos) {
        String nomeCor = argumentos.path("nomeCor").asText();
        return new RespostaIA<>(Intencao.CRIAR_COR, new Cor(null, nomeCor), null);
    }

    // --- Métodos para Veículo ---
    private RespostaIA<Void> criarRespostaParaListarVeiculos() {
        return new RespostaIA<>(Intencao.LISTAR_VEICULOS, null, null);
    }
    private RespostaIA<Veiculo> criarRespostaParaCriarVeiculo(JsonNode argumentos) {
        Double preco = argumentos.path("preco").asDouble();
        Long quilometragem = argumentos.path("quilometragem").asLong();
        boolean disponivel = argumentos.path("disponivel").asBoolean();
        Long idModelo = argumentos.path("idModelo").asLong();
        Long idCor = argumentos.path("idCor").asLong();
        Long idTipoVeiculo = argumentos.path("idTipoVeiculo").asLong();
        Long ano = argumentos.path("ano").asLong();
        return new RespostaIA<>(Intencao.CRIAR_VEICULO, new Veiculo(null, preco, quilometragem, disponivel, ano, new Cor(idCor, null), new Modelo(idModelo, null, null), new TipoVeiculo(idTipoVeiculo, null)), null);
    }


    private RespostaIA<Veiculo> criarRespostaParaBuscarVeiculo(JsonNode argumentos) {
        Long idVeiculo = argumentos.path("idVeiculo").asLong();
        // Retorna um objeto Veiculo apenas com o id (a busca real ficaria no serviço de backend)
        return new RespostaIA<>(Intencao.BUSCAR_VEICULO, new Veiculo(idVeiculo, null, null, false, null, null, null, null), null);
    }


    private RespostaIA<Marca> criarRespostaParaBuscarMarca(JsonNode argumentos) {
    Long idMarca = argumentos.path("idMarca").asLong();
    return new RespostaIA<>(Intencao.BUSCAR_MARCA, new Marca(idMarca, null), null);
    }

    private RespostaIA<Modelo> criarRespostaParaBuscarModelo(JsonNode argumentos) {
        Long idModelo = argumentos.path("idModelo").asLong();
        return new RespostaIA<>(Intencao.BUSCAR_MODELO, new Modelo(idModelo, null, null), null);
    }

    private RespostaIA<TipoVeiculo> criarRespostaParaBuscarTipoVeiculo(JsonNode argumentos) {
        Long idTipoVeiculo = argumentos.path("idTipoVeiculo").asLong();
        return new RespostaIA<>(Intencao.BUSCAR_TIPO_VEICULO, new TipoVeiculo(idTipoVeiculo, null), null);
    }

    private RespostaIA<Cor> criarRespostaParaBuscarCor(JsonNode argumentos) {
        Long idCor = argumentos.path("idCor").asLong();
        return new RespostaIA<>(Intencao.BUSCAR_COR, new Cor(idCor, null), null);
    }
}   