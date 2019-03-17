package com.domo.zoom.gpsassisntace;

public class User {
    /**
     *  será la clave para identificar a un usuario final.
     */
    public int idUser;
    public String tokenUser;
    public String nombreUser;
    public String apellidoUser;
    public String emailUser;
    public String creadoUser;
    public String celularUser;
    public int tipoUser;
    public int habilitadoUser;
    public int estadoUser;
    public int vehiId;



//    public User(int idUser, String nombreUser, String apellidoUser, String emailUser, String creadoUser, String celularUser) {
//        this.idUser = idUser;
//        this.nombreUser = nombreUser;
//        this.apellidoUser = apellidoUser;
//        this.emailUser = emailUser;
//        this.creadoUser = creadoUser;
//        this.celularUser = celularUser;
//    }

    public User(int idUser, String nombreUser, String apellidoUser, String emailUser,
                String creadoUser, String celularUser, int vehiId) {
        this.idUser = idUser;
        this.nombreUser = nombreUser;
        this.apellidoUser = apellidoUser;
        this.emailUser = emailUser;
        this.creadoUser = creadoUser;
        this.celularUser = celularUser;
        this.vehiId = vehiId;
    }

    public User(int idUser, String tokenUser, String nombreUser, String apellidoUser,
                String emailUser, String creadoUser, String celularUser, int tipoUser,
                int habilitadoUser, int estadoUser, int vehiId) {
        this.idUser = idUser;
        this.tokenUser = tokenUser;
        this.nombreUser = nombreUser;
        this.apellidoUser = apellidoUser;
        this.emailUser = emailUser;
        this.creadoUser = creadoUser;
        this.celularUser = celularUser;
        this.tipoUser = tipoUser;
        this.habilitadoUser = habilitadoUser;
        this.estadoUser = estadoUser;
        this.vehiId = vehiId;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getApellidoUser() {
        return apellidoUser;
    }

    public void setApellidoUser(String apellidoUser) {
        this.apellidoUser = apellidoUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getCreadoUser() {
        return creadoUser;
    }

    public void setCreadoUser(String creadoUser) {
        this.creadoUser = creadoUser;
    }

    public String getCelularUser() {
        return celularUser;
    }

    public void setCelularUser(String celularUser) {
        this.celularUser = celularUser;
    }

    public int getVehiId() {
        return vehiId;
    }

    public void setVehiId(int vehiId) {
        this.vehiId = vehiId;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public int getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(int tipoUser) {
        this.tipoUser = tipoUser;
    }

    public int getHabilitadoUser() {
        return habilitadoUser;
    }

    public void setHabilitadoUser(int habilitadoUser) {
        this.habilitadoUser = habilitadoUser;
    }

    public int getEstadoUser() {
        return estadoUser;
    }

    public void setEstadoUser(int estadoUser) {
        this.estadoUser = estadoUser;
    }

}
