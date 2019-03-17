package com.domo.zoom.gpsassisntace;

public class Vehiculo {
    int idVehiculo;
    int idUser;
    int idTipo;
    String created_at;
    String updated_at;

    public Vehiculo(int idVehiculo, int idUser, int idTipo, String created_at, String updated_at) {
        this.idVehiculo = idVehiculo;
        this.idUser = idUser;
        this.idTipo = idTipo;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public static class Marca {
        int idMarca;
        String nombre;
        String imagen;
        String created_at;
        String updated_at;

        public Marca(int idMarca, String nombre, String imagen, String created_at, String updated_at) {
            this.idMarca = idMarca;
            this.nombre = nombre;
            this.imagen = imagen;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        public int getIdMarca() {
            return idMarca;
        }

        public void setIdMarca(int idMarca) {
            this.idMarca = idMarca;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class Modelo {
        int idModelo;
        String nombre;
        String slug;
        int idMarca;
        int idVehiTipo;
        String imagen;
        String created_at;
        String updated_at;

        public Modelo(int idModelo, String nombre, String slug, int idMarca, int idVehiTipo, String imagen, String created_at, String updated_at) {
            this.idModelo = idModelo;
            this.nombre = nombre;
            this.slug = slug;
            this.idMarca = idMarca;
            this.idVehiTipo = idVehiTipo;
            this.imagen = imagen;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        public int getIdModelo() {
            return idModelo;
        }

        public void setIdModelo(int idModelo) {
            this.idModelo = idModelo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public int getIdMarca() {
            return idMarca;
        }

        public void setIdMarca(int idMarca) {
            this.idMarca = idMarca;
        }

        public int getIdVehiTipo() {
            return idVehiTipo;
        }

        public void setIdVehiTipo(int idVehiTipo) {
            this.idVehiTipo = idVehiTipo;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
