package api.cor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Cor;
import service.CorService;

@WebServlet("/cor/id")
public class ObterCorPorIdServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final CorService corService;

    public ObterCorPorIdServlet() {
        this.objectMapper = new ObjectMapper();
        this.corService = new CorService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            Cor cor = corService.obterCorPorId(id);
            String json = objectMapper.writeValueAsString(cor);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
