package com.silentnotary.api.retrofit;

/**
 * Created by albert on 10/9/17.
 */

public class Request {

    public static class RestFileRequest{
        String HashHex;
        String NonceHex;
        String RemoteAddress = "";

        public RestFileRequest(String hashHex, String nonceHex) {
            HashHex = hashHex;
            NonceHex = nonceHex;
        }

        public String getHashHex() {
            return HashHex;
        }

        public void setHashHex(String hashHex) {
            HashHex = hashHex;
        }

        public String getNonceHex() {
            return NonceHex;
        }

        public void setNonceHex(String nonceHex) {
            NonceHex = nonceHex;
        }

        public String getRemoteAddress() {
            return RemoteAddress;
        }

        public void setRemoteAddress(String remoteAddress) {
            RemoteAddress = remoteAddress;
        }
    }
}
