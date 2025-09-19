package api.tipoveiculo;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.TipoVeiculo;
import service.TipoVeiculoService;

@WebServlet("/tipo-veiculo")
public class ObterTiposVeiculoServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final TipoVeiculoService tipoVeiculoService;

    public ObterTiposVeiculoServlet() {
        this.objectMapper = new ObjectMapper();
        this.tipoVeiculoService = new TipoVeiculoService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        try {
            List<TipoVeiculo> tipoVeiculos = tipoVeiculoService.obterTipoVeiculos();
            String json = objectMapper.writeValueAsString(tipoVeiculos);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
