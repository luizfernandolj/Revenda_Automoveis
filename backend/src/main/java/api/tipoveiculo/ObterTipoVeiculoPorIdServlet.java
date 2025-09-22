package api.tipoveiculo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.TipoVeiculo;
import service.TipoVeiculoService;

@WebServlet("/tipo-veiculo/id")
public class ObterTipoVeiculoPorIdServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final TipoVeiculoService tipoVeiculoService;

    public ObterTipoVeiculoPorIdServlet() {
        this.objectMapper = new ObjectMapper();
        this.tipoVeiculoService = new TipoVeiculoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            TipoVeiculo tipoVeiculo = tipoVeiculoService.obterTipoVeiculoPorId(id);
            String json = objectMapper.writeValueAsString(tipoVeiculo);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
