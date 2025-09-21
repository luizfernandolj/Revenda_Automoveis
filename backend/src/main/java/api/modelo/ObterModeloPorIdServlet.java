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

@WebServlet("/modelo/id")
public class ObterModeloPorIdServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final ModeloService modeloService;

    public ObterModeloPorIdServlet() {
        this.objectMapper = new ObjectMapper();
        this.modeloService = new ModeloService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Modelo modelo = modeloService.obterModeloPorId(id);
            String json = objectMapper.writeValueAsString(modelo);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
