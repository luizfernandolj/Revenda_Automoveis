package api.cor;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Cor;
import service.CorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cor/cadastrar")
public class CadastrarCorServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CorService corService = new CorService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Cor corForm = objectMapper.readValue(request.getReader(), Cor.class);
            Cor corCadastrada = corService.cadastraCor(corForm);

            String json = objectMapper.writeValueAsString(corCadastrada);

            response.getWriter().println(json);
        } catch (Exception e) {
            String errorJson =  objectMapper.writeValueAsString(e);
            response.setStatus(500);
            response.getWriter().println(errorJson);
        }
    }
}
