package api.marca;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Marca;
import service.MarcaService;

@WebServlet("/marca/id")
public class ObterMarcaPorIdServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final MarcaService marcaService;

    public ObterMarcaPorIdServlet() {
        this.objectMapper = new ObjectMapper();
        this.marcaService = new MarcaService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Marca marca = marcaService.obterMarcaPorId(id);
            String json = objectMapper.writeValueAsString(marca);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
