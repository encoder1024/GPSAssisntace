package com.domo.zoom.gpsassisntace;

public class Sitio {
    private int idSite;
    private String nombreSite;
    private String direSite;
    private String descSite;
    private String teleSite;
    private int provId;
    private String latSite;
    private String lngSite;
    private String notaSite;
    private String creadoSite;
    private String updateSite;
    private int tipoSite;
    private String emailSite;
    private String webSite;
    private String redSocialSite;
    private String imagenFile;
    private String tokenNotiUno;
    private String tokenNotiDos;
    private String tokenNotiTres;
    private int idContrato;
    private int contratoHabilitado;
    private int idZona;
    private String certificadoSite;
    private String fotoNombre;
    private String ruta;

    //Es el constructor para cuando leo y cargo todos los sitios para el usuario final

    public Sitio(int idSite, String nombreSite, String latSite, String lngSite, int tipoSite, String tokenNotiUno, String tokenNotiDos, String tokenNotiTres, int idContrato, int idZona) {
        this.idSite = idSite;
        this.nombreSite = nombreSite;
        this.latSite = latSite;
        this.lngSite = lngSite;
        this.tipoSite = tipoSite;
        this.tokenNotiUno = tokenNotiUno;
        this.tokenNotiDos = tokenNotiDos;
        this.tokenNotiTres = tokenNotiTres;
        this.idContrato = idContrato; //Join de las tablas talleres y contratos.
        this.idZona = idZona; // Join de las tablas talleres y talleres_zonas.
    }

    //Es el constructor actual de la app funcionando hasta el 04/03/2019.

    public Sitio(int idSite, String nombreSite, String direSite, String descSite, String teleSite,
                 int provId, String latSite, String lngSite, String notaSite, String creadoSite,
                 String updateSite, int tipoSite) {
        this.idSite = idSite;
        this.nombreSite = nombreSite;
        this.direSite = direSite;
        this.descSite = descSite;
        this.teleSite = teleSite;
        this.provId = provId;
        this.latSite = latSite;
        this.lngSite = lngSite;
        this.notaSite = notaSite;
        this.creadoSite = creadoSite;
        this.updateSite = updateSite;
        this.tipoSite = tipoSite;
    }

    //es el constructor completo para cuando leo el detalle del Sitio para mostrarlo en el layout scrolling al usuario final.

    public Sitio(int idSite, String nombreSite, String direSite, String descSite, String teleSite,
                 int provId, String latSite, String lngSite, String notaSite, String creadoSite,
                 String updateSite, int tipoSite, String emailSite, String webSite, String redSocialSite,
                 String imagenFile, String tokenNotiUno, String tokenNotiDos, String tokenNotiTres,
                 int idContrato, int contratoHabilitado, int idZona, String certificadoSite) {
        this.idSite = idSite;
        this.nombreSite = nombreSite;
        this.direSite = direSite;
        this.descSite = descSite;
        this.teleSite = teleSite;
        this.provId = provId;
        this.latSite = latSite;
        this.lngSite = lngSite;
        this.notaSite = notaSite;
        this.creadoSite = creadoSite;
        this.updateSite = updateSite;
        this.tipoSite = tipoSite;
        this.emailSite = emailSite;
        this.webSite = webSite;
        this.redSocialSite = redSocialSite;
        this.imagenFile = imagenFile;
        this.tokenNotiUno = tokenNotiUno;
        this.tokenNotiDos = tokenNotiDos;
        this.tokenNotiTres = tokenNotiTres;
        this.idContrato = idContrato;
        this.contratoHabilitado = contratoHabilitado;
        this.idZona = idZona;
        this.certificadoSite = certificadoSite;
    }

    public Sitio(int idSite, String nombreSite, String direSite, String descSite, String teleSite,
                 int provId, String latSite, String lngSite, String notaSite, String creadoSite,
                 String updateSite, int tipoSite, String emailSite, String webSite, String redSocialSite,
                 String imagenFile, String tokenNotiUno, String tokenNotiDos, String tokenNotiTres,
                 int idContrato, int contratoHabilitado, int idZona, String certificadoSite,
                 String fotoNombre, String ruta) {
        this.idSite = idSite;
        this.nombreSite = nombreSite;
        this.direSite = direSite;
        this.descSite = descSite;
        this.teleSite = teleSite;
        this.provId = provId;
        this.latSite = latSite;
        this.lngSite = lngSite;
        this.notaSite = notaSite;
        this.creadoSite = creadoSite;
        this.updateSite = updateSite;
        this.tipoSite = tipoSite;
        this.emailSite = emailSite;
        this.webSite = webSite;
        this.redSocialSite = redSocialSite;
        this.imagenFile = imagenFile;
        this.tokenNotiUno = tokenNotiUno;
        this.tokenNotiDos = tokenNotiDos;
        this.tokenNotiTres = tokenNotiTres;
        this.idContrato = idContrato;
        this.contratoHabilitado = contratoHabilitado;
        this.idZona = idZona;
        this.certificadoSite = certificadoSite;
        this.fotoNombre = fotoNombre;
        this.ruta = ruta;
    }

    public int getIdSite() {
        return idSite;
    }

    public void setIdSite(int idSite) {
        this.idSite = idSite;
    }

    public String getNombreSite() {
        return nombreSite;
    }

    public void setNombreSite(String nombreSite) {
        this.nombreSite = nombreSite;
    }

    public String getDireSite() {
        return direSite;
    }

    public void setDireSite(String direSite) {
        this.direSite = direSite;
    }

    public String getDescSite() {
        return descSite;
    }

    public void setDescSite(String descSite) {
        this.descSite = descSite;
    }

    public String getTeleSite() {
        return teleSite;
    }

    public void setTeleSite(String teleSite) {
        this.teleSite = teleSite;
    }

    public int getProvId() {
        return provId;
    }

    public void setProvId(int provId) {
        this.provId = provId;
    }

    public String getLatSite() {
        return latSite;
    }

    public void setLatSite(String latSite) {
        this.latSite = latSite;
    }

    public String getLngSite() {
        return lngSite;
    }

    public void setLngSite(String lngSite) {
        this.lngSite = lngSite;
    }

    public String getNotaSite() {
        return notaSite;
    }

    public void setNotaSite(String notaSite) {
        this.notaSite = notaSite;
    }

    public String getCreadoSite() {
        return creadoSite;
    }

    public void setCreadoSite(String creadoSite) {
        this.creadoSite = creadoSite;
    }

    public String getUpdateSite() {
        return updateSite;
    }

    public void setUpdateSite(String updateSite) {
        this.updateSite = updateSite;
    }

    public int getTipoSite() {
        return tipoSite;
    }

    public void setTipoSite(int tipoSite) {
        this.tipoSite = tipoSite;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public int getContratoHabilitado() {
        return contratoHabilitado;
    }

    public void setContratoHabilitado(int contratoHabilitado) {
        this.contratoHabilitado = contratoHabilitado;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getTokenNotiUno() {
        return tokenNotiUno;
    }

    public void setTokenNotiUno(String tokenNotiUno) {
        this.tokenNotiUno = tokenNotiUno;
    }

    public String getImagenFile() {
        return imagenFile;
    }

    public void setImagenFile(String imagenFile) {
        this.imagenFile = imagenFile;
    }

    public String getEmailSite() {
        return emailSite;
    }

    public void setEmailSite(String emailSite) {
        this.emailSite = emailSite;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getRedSocialSite() {
        return redSocialSite;
    }

    public void setRedSocialSite(String redSocialSite) {
        this.redSocialSite = redSocialSite;
    }

    public String getTokenNotiDos() {
        return tokenNotiDos;
    }

    public void setTokenNotiDos(String tokenNotiDos) {
        this.tokenNotiDos = tokenNotiDos;
    }

    public String getTokenNotiTres() {
        return tokenNotiTres;
    }

    public void setTokenNotiTres(String tokenNotiTres) {
        this.tokenNotiTres = tokenNotiTres;
    }

    public String getCertificadoSite() {
        return certificadoSite;
    }

    public void setCertificadoSite(String certificadoSite) {
        this.certificadoSite = certificadoSite;
    }

    public String getFotoNombre() {
        return fotoNombre;
    }

    public void setFotoNombre(String fotoNombre) {
        this.fotoNombre = fotoNombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
