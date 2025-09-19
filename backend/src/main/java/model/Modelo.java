package model;

import java.io.Serializable;

public class Modelo implements Serializable {
    private Long id;
    private String nome;
    private Marca marca;
    private TipoVeiculo tipoVeiculo;

    public Modelo() {
    }

    public Modelo(Long id, String nome, Marca marca, TipoVeiculo tipoVeiculo) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.tipoVeiculo = tipoVeiculo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Marca getMarca() {
        return marca;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
