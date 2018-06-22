package com.exemplo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.Produces;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Produces("application/json")
public class Dados {

    private String op;
    private double value1;
    private double value2;
    private double total;

    private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    Date data = new Date();


    /*public Dados(String op, double value1, double value2) {
        this.op = op;
        this.value1 = value1;
        this.value2 = value2;
    }*/

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

    public JSONObject calcula() throws JSONException {

        JSONObject jsonOperacao = new JSONObject();

        switch (this.op)
        {
            case "sum": this.total = this.value1 + this.value2;
                break;

            case "avg": this.total = (this.value1 + this.value2) / 2;
                break;

            case "mul": this.total = value1 * value2;
                break;

            case "div": if (this.value2 != 0)
                this.total = this.value1 / this.value2;
            else
                this.total = 0;
                break;

            default: this.total = 0;
                    this.op = "Operação Inválida";

        }

        jsonOperacao.put("op", this.op);
        jsonOperacao.put("value1", this.value1);
        jsonOperacao.put("value2", this.value2);
        jsonOperacao.put("Total", this.total);
        jsonOperacao.put("Data", sdf.format(data));

        return jsonOperacao;
    }

    public JSONObject calcula(String op, double value1, double value2) throws JSONException {  //not used
        JSONObject jsonOperacao = new JSONObject();

        switch (op)
        {
            case "sum": total = value1 + value2;
                break;

            case "avg": total = (value1 + value2) / 2;
                break;

            case "mul": total = value1 * value2;
                break;

            case "div": if (value2 != 0)
                        total = value1 / value2;
                    else
                        total = 0;
                break;

            default: total = 0;

        }

        jsonOperacao.put("op", op);
        jsonOperacao.put("value1", value1);
        jsonOperacao.put("value2", value2);
        jsonOperacao.put("total", total);

        return jsonOperacao;
    }
}
