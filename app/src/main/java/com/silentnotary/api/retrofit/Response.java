package com.silentnotary.api.retrofit;

/**
 * Created by albert on 10/9/17.
 */

public class Response {

    public static class RestStatusResponse{
        String Status = "";
        public boolean isSuccess(){
            return Status!=null && Status.equals("Success");
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }
}
