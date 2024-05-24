package com.credibanco.bankinc.dfsv.utilitary;

/**
 * @author Daniel felipe saraza velez
 * @since Mayo 22 2024
 */
public class Utilitario {


    /**
     * @author Daniel felipe saraza velez
     * @since Mayo 22 2024
     */
    public static String obtenerNumeroAleatorio(int n) {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length()* Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

}
