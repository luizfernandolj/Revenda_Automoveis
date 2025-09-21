package model;

import java.io.Serializable;

public class Veiculo implements Serializable {
    private Long id;
    private Double preco;
    private Long quilometragem;
    private boolean disponivel;
    private Long ano;
    private Cor cor;
    private Modelo modelo;
    private TipoVeiculo tipoVeiculo;

    public Veiculo() {
    }

    public Veiculo(Long id, Double preco, Long quilometragem, boolean disponivel, Long ano, Cor cor, Modelo modelo, TipoVeiculo tipoVeiculo) {
        this.id = id;
        this.preco = preco;
        this.quilometragem = quilometragem;
        this.disponivel = disponivel;
        this.cor = cor;
        this.modelo = modelo;
        this.tipoVeiculo = tipoVeiculo;
        this.ano = ano;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Long quilometragem) {
        this.quilometragem = quilometragem;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public Long getAno() {
        return ano;
    }

    public void setAno(Long ano) {
        this.ano = ano;
    }
}
