package swapc.test.util;

import java.io.File;

public class TestUtilities {

    public static final String SAMPLE_PUBLIC_UNCHAINED_CERT_nl = "MIICsDCCAhmgAwIBAgIJANxvwSnbFJp1MA0GCSqGSIb3DQEBBQUAMEUxCzAJBgNV\nBAYTAkFVMRMwEQYDVQQIEwpTb21lLVN0YXRlMSEwHwYDVQQKExhJbnRlcm5ldCBX\naWRnaXRzIFB0eSBMdGQwHhcNMTIxMTE0MTY1ODQyWhcNMTIxMjE0MTY1ODQyWjBF\nMQswCQYDVQQGEwJBVTETMBEGA1UECBMKU29tZS1TdGF0ZTEhMB8GA1UEChMYSW50\nZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKB\ngQCxrZR/evHyLenGKJEPGFnL3DVXxDSq5BN//8gUlyNiG1I+lFUKQmYw9GzEoWre\nAzwvsmEsOJl6O18b35EYmC8c31oCqvMS/+MI6QQuOe5Jq9hvKro5u3oKEcSEvkgk\nELVP70KlxkZV55SGQLiGaRsZXybkEs9KQUGmYeFuFgYRgwIDAQABo4GnMIGkMB0G\nA1UdDgQWBBSOu1RJmPHRe/fbGBv0hZzoyMk1QDB1BgNVHSMEbjBsgBSOu1RJmPHR\ne/fbGBv0hZzoyMk1QKFJpEcwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgTClNvbWUt\nU3RhdGUxITAfBgNVBAoTGEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZIIJANxvwSnb\nFJp1MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAFpCJ7hF1r9zqnGbQ\np33GQ0dCfuYYEXxGDkfN0aoxY2ubOk2D403Lrv091LvfYc8gd/R510AKIm3psVOC\nR1+/X1IUXNGKfqVMWC/QMqXIOG60SCXirbTZS77Ssye6hm8HENZZ4SQNIR+mHxHr\n8aMNpVcUBnhmehV84MCNZNXRuWU=";
    public static final String SAMPLE_PUBLIC_UNCHAINED_CERT_concat = "MIICsDCCAhmgAwIBAgIJANxvwSnbFJp1MA0GCSqGSIb3DQEBBQUAMEUxCzAJBgNVBAYTAkFVMRMwEQYDVQQIEwpTb21lLVN0YXRlMSEwHwYDVQQKExhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQwHhcNMTIxMTE0MTY1ODQyWhcNMTIxMjE0MTY1ODQyWjBFMQswCQYDVQQGEwJBVTETMBEGA1UECBMKU29tZS1TdGF0ZTEhMB8GA1UEChMYSW50ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxrZR/evHyLenGKJEPGFnL3DVXxDSq5BN//8gUlyNiG1I+lFUKQmYw9GzEoWreAzwvsmEsOJl6O18b35EYmC8c31oCqvMS/+MI6QQuOe5Jq9hvKro5u3oKEcSEvkgkELVP70KlxkZV55SGQLiGaRsZXybkEs9KQUGmYeFuFgYRgwIDAQABo4GnMIGkMB0GA1UdDgQWBBSOu1RJmPHRe/fbGBv0hZzoyMk1QDB1BgNVHSMEbjBsgBSOu1RJmPHRe/fbGBv0hZzoyMk1QKFJpEcwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgTClNvbWUtU3RhdGUxITAfBgNVBAoTGEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZIIJANxvwSnbFJp1MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAFpCJ7hF1r9zqnGbQp33GQ0dCfuYYEXxGDkfN0aoxY2ubOk2D403Lrv091LvfYc8gd/R510AKIm3psVOCR1+/X1IUXNGKfqVMWC/QMqXIOG60SCXirbTZS77Ssye6hm8HENZZ4SQNIR+mHxHr8aMNpVcUBnhmehV84MCNZNXRuWU=";
    public static final String SAMPLE_PUBLIC_UNCHAINED_CERT_spaces = "MIICsDCCAhmgAwIBAgIJANxvwSnbFJp1MA0GCSqGSIb3DQEBBQUAMEUxCzAJBgNV BAYTAkFVMRMwEQYDVQQIEwpTb21lLVN0YXRlMSEwHwYDVQQKExhJbnRlcm5ldCBX aWRnaXRzIFB0eSBMdGQwHhcNMTIxMTE0MTY1ODQyWhcNMTIxMjE0MTY1ODQyWjBF MQswCQYDVQQGEwJBVTETMBEGA1UECBMKU29tZS1TdGF0ZTEhMB8GA1UEChMYSW50 ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKB gQCxrZR/evHyLenGKJEPGFnL3DVXxDSq5BN//8gUlyNiG1I+lFUKQmYw9GzEoWre AzwvsmEsOJl6O18b35EYmC8c31oCqvMS/+MI6QQuOe5Jq9hvKro5u3oKEcSEvkgk ELVP70KlxkZV55SGQLiGaRsZXybkEs9KQUGmYeFuFgYRgwIDAQABo4GnMIGkMB0G A1UdDgQWBBSOu1RJmPHRe/fbGBv0hZzoyMk1QDB1BgNVHSMEbjBsgBSOu1RJmPHR e/fbGBv0hZzoyMk1QKFJpEcwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgTClNvbWUt U3RhdGUxITAfBgNVBAoTGEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZIIJANxvwSnb FJp1MAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAFpCJ7hF1r9zqnGbQ p33GQ0dCfuYYEXxGDkfN0aoxY2ubOk2D403Lrv091LvfYc8gd/R510AKIm3psVOC R1+/X1IUXNGKfqVMWC/QMqXIOG60SCXirbTZS77Ssye6hm8HENZZ4SQNIR+mHxHr 8aMNpVcUBnhmehV84MCNZNXRuWU=";

    public static final String WWW_EXAMPLE_COM_CERT_concat = "MIIC1TCCAb2gAwIBAgIJAOsqzTSf6crZMA0GCSqGSIb3DQEBBQUAMBoxGDAWBgNVBAMTD3d3dy5leGFtcGxlLmNvbTAeFw0yMDA3MDMwODUyNTFaFw0zMDA3MDEwODUyNTFaMBoxGDAWBgNVBAMTD3d3dy5leGFtcGxlLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKcY8hfb6vDCWMFNVZ6yGoc5/BjsowlqGVvU9xW8uIb8rsqyPAxwhQY/gdssKvLSWppmGCapUV1UfNpH7wtW/nG8Z+PtlhyyxAcu6RzKQt6npe/JWs7agRxgnZP5JR7rBiMSRljSL/msEc5Ox0OUCb5ba8Xzk2tMrZhQJyG9SWmgbDNcNrZ6uwSf5nSd88avX40X4gQrqwyPuMmksDc8nDO/2psoD/f5jW8dwezVJ2Ddx5tSxFgRE9SbzgRTpek2QmWAfL3x+UbGhIc3C/x5c+FE1nJstxqhKNT1l2weGFVA+D2bn2MypoldZ0q272XGAfR3ZYrLkT4nn4yycwD1dr0CAwEAAaMeMBwwGgYDVR0RBBMwEYIPd3d3LmV4YW1wbGUuY29tMA0GCSqGSIb3DQEBBQUAA4IBAQBziEmgOAbqi3cxZdYpKHfgszb65fY3VOPjYAh1LeFeW7c5FREGGe+40d9Dg7CXGWlyHfSTSwuHkW2pvV5VKVlj6qA3WHoly7JX/oGLsBuhEvx9oUa8ldBQOciBJrUfejRqFlgDrvCMNFg9blvvasz2ZrfcmRGtHSDNj8kH936Ur2PYuRk14qsxP+rqsm8LqcztdfgXdckgStmJrEw5803dnsS72DBtjFumivIf84sYbfMnNLFoYJDQS0xJxdvi/resrtiiPdIj3zQl9/1QaY3xLBVeQa6xjM7zbGGmS2/LbWBxXEraNducRRhDP69YrNY7q95J7HUA41KoFm39ZmJ8";

    public static File getTestB64File() {
        return new File(TestUtilities.class.getClassLoader().getResource("sample_public_unchained.b64").getFile());
    }

    public static File getTestYamlFile() {
        return new File(TestUtilities.class.getClassLoader().getResource("sample_yaml_with_certs.yaml").getFile());
    }

    public static File getSimpleYamlFile() {
        return new File(TestUtilities.class.getClassLoader().getResource("simple_yaml.yml").getFile());
    }

    public static File getTestDataPath() {
        return new File(TestUtilities.class.getClassLoader().getResource("sample_yaml_with_certs.yaml").getPath()).getParentFile();
    }

    public static File getExampleComCert() {
        String path = TestUtilities.class.getClassLoader().getResource("example_com.cert").getFile();
        return new File(path);
    }

}