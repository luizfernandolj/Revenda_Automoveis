package api.veiculo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Veiculo;
import service.VeiculoService;

@WebServlet("/veiculo/id")
public class ObterVeiculoPorIdServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final VeiculoService veiculoService;

    public ObterVeiculoPorIdServlet() {
        this.objectMapper = new ObjectMapper();
        this.veiculoService = new VeiculoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Veiculo veiculo = veiculoService.obterVeiculoPorId(id);
            String json = objectMapper.writeValueAsString(veiculo);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
