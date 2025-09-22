package api.cor;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Cor;
import service.CorService;

@WebServlet("/cor")
public class ObterCoresServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final CorService corService;

    public ObterCoresServlet() {
        this.objectMapper = new ObjectMapper();
        this.corService = new CorService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        try {
            List<Cor> cores = corService.obterCores();
            String json = objectMapper.writeValueAsString(cores);
            resp.getWriter().write(json);
        } catch (Exception e) {
            String error = objectMapper.writeValueAsString(e);
            resp.setStatus(500);
            resp.getWriter().println(error);
        }
    }
}
