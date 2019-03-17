package com.domo.zoom.gpsassisntace;

public class Pro extends User {
    public int password;

    public Pro(int idUser, String nombreUser, String apellidoUser, String emailUser, String creadoUser, String celularUser, int vehiId, int password) {
        super(idUser, nombreUser, apellidoUser, emailUser, creadoUser, celularUser, vehiId);
        this.password = password;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
