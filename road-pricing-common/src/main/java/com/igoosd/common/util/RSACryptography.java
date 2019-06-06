package com.igoosd.common.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 2018/1/19.
 * <p>
 * RSA 加密算法
 */
public class RSACryptography {


    private static final String KEY_ALGORITHM = "RSA";

    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     *
     * 公司钥  base64 编码 处理
     *
     * @return
     */
    public static final String[] genPublicAndPrivateKeys(int len) {
        KeyPair pair = genKeyPair(len);
        String publicKey = encodeBase64ToString(pair.getPublic().getEncoded());
        String privateKey = encodeBase64ToString(pair.getPrivate().getEncoded());
        String[] keys = new String[2];
        keys[0] = publicKey;
        keys[1] = privateKey;
        return keys;
    }

    /**
     * 生成秘钥对
     *
     * @return
     * @Param 长度 不超过 1024 建议RSA 加密策略 明文长度和密文长度有关
     * 如 密文长度为1024位 则 密文长度为：1024/8 =128个字节。减去  -11个字节  最多只允许存储117 个字节
     * 加解密过程中 如果是json 序列化需考虑额外长度相关问题
     */
    private static final KeyPair genKeyPair(int len) {
        //Assert.isTrue(len > 0 && len <= 1024, "RSA 加密长度");
        try {
            return KeyPairGenerator.getInstance(KEY_ALGORITHM).genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param publicKeyStr
     * @param data
     * @return
     */
    public static final String encryptBypublicKey(String publicKeyStr, String data) {
        Cipher cipher;//java默认"RSA"="RSA/ECB/PKCS1Padding"
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyStr, KEY_ALGORITHM));
            byte[] entryDataBytes = data.getBytes();
            return encodeBase64ToString(cipher.doFinal(entryDataBytes));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param privateKeyStr
     * @param base64Data
     * @return
     */
    public static final String decryptByprivateKey(String privateKeyStr, String base64Data) {
        Cipher cipher = null;//java默认"RSA"="RSA/ECB/PKCS1Padding"
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyStr, KEY_ALGORITHM));
            return new String(cipher.doFinal(decodeBase64(base64Data)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static PublicKey getPublicKey(String publicKeyStr, String keyAlgorithm) throws Exception {
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(decodeBase64(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        PublicKey publicKey = keyFactory.generatePublic(pubX509);

        return publicKey;

    }

    private static PrivateKey getPrivateKey(String privateKeyStr, String keyAlgorithm) throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decodeBase64(privateKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
        return privateKey;
    }

    public static byte[] decodeBase64(String str) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(str);
    }

    public static String encodeBase64ToString(byte[] bytes) {

        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJtEENiejaSJZZMgxwFgrODy/PciiS6c/gSXcBIKlMJIrFh5ah3Uu/UBR5L7avMjDE3gz81wFMxpFzsmGsL4DA0JOiH1GmFBDWQf9VCC5uVljuWvLSzorGAyq49UfKb+4Vt2sqkyIbF0i0scq3i6ci+vgESe2NBib8BeM4GTZdtRAgMBAAECgYBjH0Yd9ML6atjU39UMVRqpFZdXcs3fW0xzw/ZBHk4v5lNYvFJ5ruk7ldCKzkVN6W8efMpNHiO5B9JT6ZSmKdMOsiQ7SaCfmJm2LHu7xRUQE6yVUSa/Zyaft8gY6iNzEqYlsIZCbTL/pxsABRZnZhbUMqzipKpD7zORXoQuz+cUkQJBAOcGCAE2A3o0aNvxcKzp3ljffVrqf+tLAJdSEo37YdI8usbb8H5VyBQUWWNvQE4jhZ6w6z7TgShvXmqWletlcP0CQQCsDU/2mNkY/spQoFcMKhD2pEDShoESBlDdSrZuL34aI4iOh2YoieEtWFZ3olaiWJ+w/zBxmz6QALWkgEIxQb3lAkA3polJGWTPDE7yJH/69z87c2K0Aucbm+6hhQC5OQQAa+amPpb5J9rRk3FQ9Zys9fubFY0ljjd/sQwyauHkWYRVAkAeyq8h8g8AkzEAJwINMkYg7hqP0vSslibIxANMSwGlifB5ma/l129OXz3yYUJiAbxqv/Eak+8pdIcNrsqp6BslAkBOyMWRwfezggtinWtSw25JtJyg/UYE57cTWn57UMurjozeHCrgFAawv20DV/KJ52kxhGOf8oYJMmFmkG4/irEk";
        String miwen = "YhlcdaZwEBXmgB1l8r67ZHiqyJDMOO+7rjKTucy+ip22mKzMqARIpNOqbrYiNWiDN+d0tHDv1QV627lJmFCL2gkt2jHZ6SlJTNQRRLF2GaGE1VxHMMs4OBBHOs7NIY9+UQ2y8hKXXkgr6XuVpSRjGwf0cJeKi24hNDzRg5NFzWI=";
        String mingwen = decryptByprivateKey(privateKey,miwen);
        System.out.println(mingwen);
    }
}
