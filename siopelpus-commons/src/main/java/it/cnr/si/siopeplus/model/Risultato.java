/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.si.siopeplus.model;

import java.util.Date;

public class Risultato {
    private Integer progFlusso;
    private Integer progEsitoApplicativo;
    private Integer progGiornale;
    private Date dataProduzione;
    private Date dataUpload;
    private Boolean download;
    private String location;

    private Exception error;

    public Risultato() {
    }

    public Integer getProgFlusso() {
        return progFlusso;
    }

    public void setProgFlusso(Integer progFlusso) {
        this.progFlusso = progFlusso;
    }

    public Date getDataProduzione() {
        return dataProduzione;
    }

    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    public Boolean getDownload() {
        return download;
    }

    public void setDownload(Boolean download) {
        this.download = download;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

    public Integer getProgEsitoApplicativo() {
        return progEsitoApplicativo;
    }

    public void setProgEsitoApplicativo(Integer progEsitoApplicativo) {
        this.progEsitoApplicativo = progEsitoApplicativo;
    }

    public Integer getProgGiornale() {
        return progGiornale;
    }

    public void setProgGiornale(Integer progGiornale) {
        this.progGiornale = progGiornale;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Risultato{" +
                "progFlusso=" + progFlusso +
                ", progEsitoApplicativo=" + progEsitoApplicativo +
                ", progGiornale=" + progGiornale +
                ", dataProduzione=" + dataProduzione +
                ", dataUpload=" + dataUpload +
                ", download=" + download +
                ", location='" + location + '\'' +
                ", error=" + error +
                '}';
    }
}
