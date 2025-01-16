package br.com.rplbasicmessages.commons.utils;

import br.com.rplbasicmessages.commons.exceptions.RplMessageInternalServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecretUtils {

    private static final String ALGORITHM = "AES";

    /**
     * Gera uma chave secreta AES de 128 bits.
     * @return String base64 da chave secreta.
     */
    public static String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128); // Tamanho da chave (128 bits)
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RplMessageInternalServiceException(e.getMessage(), e.getCause(), SecretUtils.class);
        }
    }

    /**
     * Criptografa uma senha usando a chave secreta.
     * @param password A senha a ser criptografada.
     * @param secretKeyBase64 Chave secreta em formato Base64.
     * @return Senha criptografada em Base64.
     */
    public static String encrypt(String password, String secretKeyBase64)  {
        try {
            SecretKey secretKey = decodeKey(secretKeyBase64);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RplMessageInternalServiceException(e.getMessage(), e.getCause(), SecretUtils.class);
        }
    }

    /**
     * Descriptografa uma senha usando a chave secreta.
     * @param encryptedPassword Senha criptografada em Base64.
     * @param secretKeyBase64 Chave secreta em formato Base64.
     * @return Senha original.
     */
    public static String decrypt(String encryptedPassword, String secretKeyBase64) {
        try {
            SecretKey secretKey = decodeKey(secretKeyBase64);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RplMessageInternalServiceException(e.getMessage(), e.getCause(), SecretUtils.class);
        }
    }

    /**
     * Descriptografa uma senha usando a chave secreta.
     * @param encryptedPassword Senha criptografada em Base64.
     * @param secretKeyBase64 Chave secreta em formato Base64.
     * @return Senha original.
     */
    public static boolean isPassOk(String password, String encryptedPassword, String secretKeyBase64) {
            String passDecrypt = decrypt(encryptedPassword, secretKeyBase64);
            return passDecrypt.equals(password);
    }

    /**
     * Decodifica uma chave secreta a partir de um Base64.
     * @param secretKeyBase64 Chave secreta em Base64.
     * @return Objeto SecretKey.
     */
    private static SecretKey decodeKey(String secretKeyBase64) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    public static String generateObjectHash(Object object) {
        try {
            // Converte o objeto em um array de bytes
            byte[] objectBytes = convertObjectToBytes(object);

            // Gera o hash SHA-256 a partir do array de bytes
            byte[] hashBytes = generateSHA256Hash(objectBytes);

            // Converte o hash em uma string hexadecimal
            return bytesToHex(hashBytes);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RplMessageInternalServiceException("Erro ao gerar token de login.", e.getCause(), SecretUtils.class);
        }
    }

    private static byte[] convertObjectToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            objectStream.flush();
            return byteStream.toByteArray();
        }
    }

    private static byte[] generateSHA256Hash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
