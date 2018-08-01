package br.danieltiburciosf.rankingfutebol;

import java.io.Serializable;

public class Resultado implements Serializable {
    static final long serialVersionUID = -9359378245L;

    private final String clube;
    private final String campos;

    public Resultado(String clube, String campos)
    {
        this.clube = clube;
        this.campos = campos;
    }

    public String getClube() {
        return this.clube;
    }

    public String getCampos()
    {
        return this.campos;
    }
}