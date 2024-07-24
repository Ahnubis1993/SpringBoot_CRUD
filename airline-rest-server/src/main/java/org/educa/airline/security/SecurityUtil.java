package org.educa.airline.security;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SecurityUtil {

    /**
     * Formatos de cifrado y hashing.
     */
    private static final String SEED = "CLAVE_DE_CIFRADO_DE_32_BITS_A256";
    private static final String CRYPT = "AES";

    // Hash
    private static final String CRYPT_ONE = "SHA-512";

    /**
     * Cifra un dato para que la información es confidencial.
     *
     * @param message dato (String) a cifrar.
     * @return dato cifrado.
     */
    public String crypt(String message) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec skeySpec = new SecretKeySpec(SEED.getBytes(), CRYPT);
        System.out.println("Obteniendo objeto Cipher con cifrado AES");
        Cipher aesCipher = Cipher.getInstance(CRYPT);
        System.out.println("Configurando Cipher para encriptar");
        aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        System.out.println("Preparando mensaje");
        System.out.println("Mensaje original: " + message);
        System.out.println("Cifrando mensaje");
        byte[] mensajeByteCifrado = aesCipher.doFinal(message.getBytes());
        byte[] mensajeByteCifradoBase64 = Base64.getEncoder().encode(mensajeByteCifrado);
        String mensajeCifrado = new String(mensajeByteCifradoBase64);
        System.out.println("Mensaje cifrado: " + mensajeCifrado);
        return mensajeCifrado;

    }

    /**
     * Descifra un dato que previamente ha sido cifrado y poder ver su contenido.
     *
     * @param mensajeCifrado dato cifrado para descifrar.
     * @return dato descifrado (dato simple).
     */
    public String decrypt(String mensajeCifrado) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalArgumentException {
        SecretKeySpec skeySpec = new SecretKeySpec(SEED.getBytes(), CRYPT);
        System.out.println("Obteniendo objeto Cipher con cifrado AES");
        Cipher aesCipher = Cipher.getInstance(CRYPT);
        System.out.println("Configurando Cipher para desencriptar");
        aesCipher.init(Cipher.DECRYPT_MODE, skeySpec);

        System.out.println("Descifrando mensaje");
        byte[] mensajeByte = Base64.getDecoder().decode(mensajeCifrado.getBytes());
        String mensajeDescifrado = new String(aesCipher.doFinal(mensajeByte));
        System.out.println("Mensaje descifrado: " + mensajeDescifrado);
        return mensajeDescifrado;
    }

    /**
     * Crea un hash (clave única) para un dato concreto.
     *
     * @param message dato hash.
     * @return dato hash.
     */
    public String createHash(String message) throws NoSuchAlgorithmException {
        MessageDigest messagedigest;
        messagedigest = MessageDigest.getInstance(CRYPT_ONE);
        messagedigest.update(message.getBytes());
        return new String(Base64.getUrlEncoder().encode(messagedigest.digest()), StandardCharsets.UTF_8);
    }
}
