package api.veiculo;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Veiculo;
import service.VeiculoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/veiculo/cadastrar")
public class CadastrarVeiculoServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final VeiculoService veiculoService = new VeiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Veiculo veiculoForm = objectMapper.readValue(request.getReader(), Veiculo.class);
            Veiculo veiculoCadastrada = veiculoService.cadastraVeiculo(veiculoForm);

            String json = objectMapper.writeValueAsString(veiculoCadastrada);

            response.getWriter().println(json);
        } catch (Exception e) {
            String errorJson =  objectMapper.writeValueAsString(e);
            response.setStatus(500);
            response.getWriter().println(errorJson);
        }
    }
}
