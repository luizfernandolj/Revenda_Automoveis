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

@WebServlet("/tipo-veiculo/cadastrar")
public class CadastrarTipoVeiculoServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TipoVeiculoService tipoVeiculoService = new TipoVeiculoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            TipoVeiculo tipoVeiculoForm = objectMapper.readValue(request.getReader(), TipoVeiculo.class);
            TipoVeiculo tipoVeiculoCadastrada = tipoVeiculoService.cadastraTipoVeiculo(tipoVeiculoForm);

            String json = objectMapper.writeValueAsString(tipoVeiculoCadastrada);

            response.getWriter().println(json);
        } catch (Exception e) {
            String errorJson =  objectMapper.writeValueAsString(e);
            response.setStatus(500);
            response.getWriter().println(errorJson);
        }
    }
}
