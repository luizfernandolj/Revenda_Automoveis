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

@WebServlet("/marca/cadastrar")
public class CadastrarMarcaServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MarcaService marcaService = new MarcaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        try {
            Marca marcaForm = objectMapper.readValue(request.getReader(), Marca.class);
            Marca marcaCadastrada = marcaService.cadastraMarca(marcaForm);

            String json = objectMapper.writeValueAsString(marcaCadastrada);

            response.getWriter().println(json);
        } catch (Exception e) {
            String errorJson =  objectMapper.writeValueAsString(e);
            response.setStatus(500);
            response.getWriter().println(errorJson);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
