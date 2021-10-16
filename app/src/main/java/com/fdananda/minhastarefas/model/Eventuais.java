package com.fdananda.minhastarefas.model;

public class Eventuais {

    private String nomeTarefa;
    private String tipoTarefa;
    private String statusTarefa;
    private Integer id;

    public Eventuais() {
    }

    public Eventuais(String nomeTarefa, String tipoTarefa, String statusTarefa, Integer id) {
        this.nomeTarefa = nomeTarefa;
        this.tipoTarefa = tipoTarefa;
        this.statusTarefa = statusTarefa;
        this.id = id;
    }

    public String getNomeTarefa() {
        return nomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }

    public String getTipoTarefa() {
        return tipoTarefa;
    }

    public void setTipoTarefa(String tipoTarefa) {
        this.tipoTarefa = tipoTarefa;
    }

    public String getStatusTarefa() {
        return statusTarefa;
    }

    public void setStatusTarefa(String statusTarefa) {
        this.statusTarefa = statusTarefa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
