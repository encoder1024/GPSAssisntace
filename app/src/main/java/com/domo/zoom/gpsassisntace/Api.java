package com.domo.zoom.gpsassisntace;

public class Api {
    private static final String ROOT_URL = "http://www.domozoom.com.ar/gpsas/v1/Api.php?apicall=";
    public static final String ROOT_URL_IMAGES = "http://www.domozoom.com.ar/gpsas/imagenes/";

    public static final String URL_CREATE_HERO = ROOT_URL + "createhero";
    public static final String URL_READ_HEROES = ROOT_URL + "getheroes";
    public static final String URL_UPDATE_HERO = ROOT_URL + "updatehero";
    public static final String URL_DELETE_HERO = ROOT_URL + "deletehero&id=";
    public static final String URL_READ_USER = ROOT_URL + "readUser&email=";
    public static final String URL_READ_USER_TEST = ROOT_URL + "readUser&email=";
    public static final String URL_READ_MARCAS_POR_TIPO_VEHI = ROOT_URL + "readMarcas&tipoVehiculo=";
    public static final String URL_READ_MODELOS_POR_MARCA_VEHI = ROOT_URL + "readModelos&id=";
    public static final String URL_WRITE_ACT_USER = ROOT_URL + "writeDataUser";
    public static final String URL_WRITE_ADD_USER = ROOT_URL + "writeCreateUser";
    public static final String URL_READ_SITIOS = ROOT_URL + "getsitios";
    public static final String URL_READ_SITIOS_FULL = ROOT_URL + "getsitiofull&idSitio=";
    public static final String URL_READ_SITIOS_ESPECIAL = ROOT_URL + "getespecial&idSitio=";
    public static final String URL_READ_SITIO_CERTIF = ROOT_URL + "getcertif&idSitio=";
    public static final String URL_UPDATE_USER_TOKEN_FB = ROOT_URL + "settokenfb&idUser=";
    public static final String URL_WRITE_ADD_PRO = ROOT_URL + "writeCreatePro";
    public static final String URL_WRITE_ACT_PRO = ROOT_URL + "writeDataPro";
    public static final String URL_READ_PRO = ROOT_URL + "readPro&email=";
    public static final String URL_READ_SITIOS_POR_MARCATIPO = ROOT_URL + "readSitioMarca&idMarca=";
}
