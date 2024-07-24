package org.educa.airline.services.inservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.educa.airline.security.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
@Data
public class SecurityService implements PasswordEncoder {

    /**
     * Clases a inyectar en el constructor.
     */

    private final SecurityUtil securityUtil;

    /**
     * Se inyectan ambas clases para hacer uso de las mismas en esta capa.
     *
     * @param securityUtil SecurityUtil para hacer uso de cifrado, descifrado y hash.
     */
    public SecurityService(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    /**
     * Desencripta los datos de los usuarios que se han guardado en la base de datos cifrados.
     *
     * @param data Dato a desencriptar.
     * @return dato desencriptado.
     */
    public String decrypt(String data) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return this.securityUtil.decrypt(data);
    }

    /**
     * Encripta un dato del usuario que se va a guardar en la base de datos.
     *
     * @param data dato a encriptar del usuario.
     */
    public String crypt(String data) throws NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        return this.securityUtil.crypt(data);
    }

    /**
     * Crea un hash en la contraseña del usuario.
     *
     * @param password contraseña del usuario en hash.
     * @return hash de la contraseña.
     */
    public String hash(String password) throws NoSuchAlgorithmException {
        return this.securityUtil.createHash(password);

    }

    /**
     * Codifica una contraseña en texto plano utilizando un algoritmo de hash.
     *
     * @param rawPassword La contraseña en texto plano a codificar.
     * @return La representación codificada de la contraseña.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        System.out.println("LLega al encode");
        try {
            return hash(rawPassword.toString());
        } catch (Exception e) {
            log.error("Exception encode: ", e);
        }
        return null;
    }

    /**
     * Comprueba si una contraseña en texto plano coincide con una contraseña codificada.
     *
     * @param rawPassword     La contraseña en texto plano a verificar.
     * @param encodedPassword La contraseña codificada a comparar.
     * @return true si la contraseña en texto plano coincide con la contraseña codificada, false en caso contrario.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        System.out.println("LLega a matches");
        boolean find = false;
        try {
            if (encode(rawPassword).equals(encodedPassword)) {
                find = true;
            }
        } catch (Exception e) {
            log.error("Exception encode: ", e);
        }
        return find;
    }

}

