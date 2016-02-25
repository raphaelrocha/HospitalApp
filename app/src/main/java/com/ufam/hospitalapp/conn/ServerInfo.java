package com.ufam.hospitalapp.conn;

/**
 * Created by rli on 15/06/2015.
 */
public class ServerInfo {

    public static final String EXTENSION_IMAGE_FILE = "jpg";
    //public static final String SERVER_ADDR = "http://200.222.36.115"; //servidor aws
    //public static final String SERVER_FOLDER = "/wscsm"; //servidor aws

    //public static final String LOGIN = SERVER_ADDR + SERVER_FOLDER + "/login.php"; //servidor aws
    //public static final String LIST_HOSPITAIS = SERVER_ADDR + SERVER_FOLDER + "/hospitais.php"; //servidor aws
    //public static final String GET_HOSPITAL = SERVER_ADDR + SERVER_FOLDER + "/hospital.php"; //servidor aws

    //public static final String IMAGE_FOLDER = SERVER_ADDR+"/historyapp/web/images/__w-200-400-600-800-1000__/";
    public static final String IMAGE_FOLDER = "http://ec2-54-233-85-160.sa-east-1.compute.amazonaws.com/wscsm/images/__w-200-400-600-800-1000__/";

    //public static final String SERVER_ADDR_JAVA = "http://200.222.36.115:8080/WSCSM/ws"; //servidor aws
    public static final String SERVER_ADDR_JAVA = "http://ec2-54-233-85-160.sa-east-1.compute.amazonaws.com/wscsm"; //servidor aws
    public static final String LIST_HOSPITAIS = SERVER_ADDR_JAVA + "/server.php?f=listarHospitais"; //servidor aws
    public static final String LOGIN = SERVER_ADDR_JAVA + "/server.php?f=login"; //servidor aws
    public static final String LISTA_COMENTARIOS_HOSPITAL = SERVER_ADDR_JAVA + "/server.php?f=listaRComentariosHospital"; //servidor aws
    public static final String ENVIA_COMENTARIO_HOSPITAL = SERVER_ADDR_JAVA + "/server.php?f=enviarComentario"; //servidor aws
    public static final String REMOVE_COMENTARIO_HOSPITAL = SERVER_ADDR_JAVA + "/server.php?f=removerComentario"; //servidor aws
    public static final String BUSCA_HOSPITAIS = SERVER_ADDR_JAVA + "/server.php?f=buscarHospitais"; //servidor aws
    public static final String VERIFICA_AVALIACAO = SERVER_ADDR_JAVA + "/server.php?f=verificaAvaliacao"; //servidor aws
    public static final String AVALIA = SERVER_ADDR_JAVA + "/server.php?f=avaliar"; //servidor aws
    public static final String EFETIVA_AVALIACAO = SERVER_ADDR_JAVA + "/server.php?f=efetivarAvaliacao"; //servidor aws
    public static final String LISTA_AVALIACOES = SERVER_ADDR_JAVA + "/server.php?f=listarAvaliacoes"; //servidor aws
    public static final String CRIA_USUARIO = SERVER_ADDR_JAVA + "/server.php?f=criarUsuario"; //servidor aws


}
