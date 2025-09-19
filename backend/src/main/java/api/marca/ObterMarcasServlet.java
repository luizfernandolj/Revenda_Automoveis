package api.marca;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Marca;
import model.Marca;
import service.MarcaService;
import service.MarcaService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/marca")
public class ObterMarcasServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final MarcaService marcaService;

    public ObterMarcasServlet() {
        this.objectMapper = new ObjectMapper();
        this.marcaService = new MarcaService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Marca> marcas = marcaService.obterMarcas();
            String json = objectMapper.writeValueAsString(marcas);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
