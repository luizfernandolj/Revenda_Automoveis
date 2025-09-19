package api.veiculo;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Veiculo;
import service.VeiculoService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/veiculo")
public class ObterVeiculosServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final VeiculoService veiculoService;

    public ObterVeiculosServlet() {
        this.objectMapper = new ObjectMapper();
        this.veiculoService = new VeiculoService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Veiculo> veiculos = veiculoService.obterVeiculos();
            String json = objectMapper.writeValueAsString(veiculos);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
