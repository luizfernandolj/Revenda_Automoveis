package api.modelo;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Modelo;
import service.ModeloService;

@WebServlet("/modelo")
public class ObterModelosServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final ModeloService modeloService;

    public ObterModelosServlet() {
        this.objectMapper = new ObjectMapper();
        this.modeloService = new ModeloService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Modelo> modelos = modeloService.obterModelos();
            String json = objectMapper.writeValueAsString(modelos);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
