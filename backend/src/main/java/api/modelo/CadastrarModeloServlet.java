package api.modelo;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Modelo;
import service.ModeloService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/modelo/cadastrar")
public class CadastrarModeloServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModeloService modeloService = new ModeloService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Modelo modeloForm = objectMapper.readValue(request.getReader(), Modelo.class);
            Modelo modeloCadastrado = modeloService.cadastraModelo(modeloForm);

            String json = objectMapper.writeValueAsString(modeloCadastrado);

            response.getWriter().println(json);
        } catch (Exception e) {
            String errorJson =  objectMapper.writeValueAsString(e);
            response.setStatus(500);
            response.getWriter().println(errorJson);
        }
    }
}
