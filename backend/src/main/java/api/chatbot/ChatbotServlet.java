package api.chatbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.RespostaIA;
import service.ChatbotService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chatbot")
public class ChatbotServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final ChatbotService chatbotServicos;

    public ChatbotServlet(){
        objectMapper = new ObjectMapper();
        chatbotServicos = new ChatbotService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try{
            String mensagem = request.getParameter("mensagem");
            RespostaIA<?> resposta = chatbotServicos.obterResposta(mensagem);

            String respostaJSON = objectMapper.writeValueAsString(resposta);

            response.getWriter().write(respostaJSON);
        }
        catch (Exception e){
            String errorJSON = objectMapper.writeValueAsString(e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(errorJSON);
        }
    }
}
