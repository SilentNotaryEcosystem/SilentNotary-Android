package com.silentnotary.api.retrofit;

import java.util.List;

import com.silentnotary.api.NetworkingConstantsApi;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by albert on 9/27/17.
 */

public interface FileAPI {

    @Multipart
    @POST(NetworkingConstantsApi.FILE_CREATE)
    Observable<UploadFileResponse> createFile(@Query("sessionId") String sessionId,
                                              @Query("comment") String comment,
                                              @Query("source") String source,
                                              @Part MultipartBody.Part content);

    @Multipart
    @POST(NetworkingConstantsApi.FILE_CREATE)
    Observable<UploadFileResponse> createFiles(@Query("sessionId") String sessionId,
                                               @Query("comment") String comment,
                                               @Query("source") String source,
                                               @Part List<MultipartBody.Part> content);

    @POST(NetworkingConstantsApi.FILE_CONFIRM)
    Observable<ConfirmFileResponse> confirmUploading(@Body ConfirmFileRequest confirmFileRequest);

    @POST(NetworkingConstantsApi.GET_USER_OPERATIONS)
    Observable<List<UserFilesResponse>> getFiles(@Body DatesRequest datesRequest);


    @POST(NetworkingConstantsApi.FILE_DELETE)
    Observable<com.silentnotary.api.retrofit.Response.RestStatusResponse> deleteFile(@Body Request.RestFileRequest request);


    @POST(NetworkingConstantsApi.FILE_VERIFY)
    Observable<RestVerifyFileResponse> verifyFile(@Body Request.RestFileRequest request);

    @POST(NetworkingConstantsApi.FILE_DOWNLOAD)
    Call<ResponseBody> downloadFile(@Body RestDownloadFileRequest restDownloadFileRequest);


    class RestDownloadFileRequest {
        String TokenHex;

        public RestDownloadFileRequest(String tokenHex) {
            TokenHex = tokenHex;
        }

        public String getTokenHex() {
            return TokenHex;
        }

        public void setTokenHex(String tokenHex) {
            TokenHex = tokenHex;
        }
    }

    class RestVerifyFileResponse {
        String TokenHex;

        public String getTokenHex() {
            return TokenHex;
        }

        public void setTokenHex(String tokenHex) {
            TokenHex = tokenHex;
        }
    }

    class UserFilesResponse {
        String DateTime;
        String PayedPeriodEnd;
        int Id;
        int FileSizeBytes;
        boolean IsDeleted;
        String HashHex;
        String NonceHex;
        String Comment;
        String Transaction;
        String Type;
        String RemoteAddress;


        public String getPayedPeriodEnd() {
            return PayedPeriodEnd;
        }

        public void setPayedPeriodEnd(String payedPeriodEnd) {
            PayedPeriodEnd = payedPeriodEnd;
        }

        public String getDateTime() {
            return DateTime;
        }

        public void setDateTime(String dateTime) {
            DateTime = dateTime;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public int getFileSizeBytes() {
            return FileSizeBytes;
        }

        public void setFileSizeBytes(int fileSizeBytes) {
            FileSizeBytes = fileSizeBytes;
        }

        public boolean isDeleted() {
            return IsDeleted;
        }

        public void setDeleted(boolean deleted) {
            IsDeleted = deleted;
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

        public String getComment() {
            return Comment;
        }

        public void setComment(String comment) {
            Comment = comment;
        }

        public String getTransaction() {
            return Transaction;
        }

        public void setTransaction(String transaction) {
            Transaction = transaction;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getRemoteAddress() {
            return RemoteAddress;
        }

        public void setRemoteAddress(String remoteAddress) {
            RemoteAddress = remoteAddress;
        }
    }

    class DatesRequest {
        String StartDate;
        String EndDate;

        public DatesRequest(String startDate, String endDate) {
            StartDate = startDate;
            EndDate = endDate;
        }

        public String getStartDate() {
            return StartDate;
        }

        public void setStartDate(String startDate) {
            StartDate = startDate;
        }

        public String getEndDate() {
            return EndDate;
        }

        public void setEndDate(String endDate) {
            EndDate = endDate;
        }
    }

    class ConfirmFileRequest {
        String ConfirmationTokenHex;
        boolean NotifyAllMembers;

        public ConfirmFileRequest() {
        }

        public ConfirmFileRequest(String confirmationTokenHex, boolean notifyAllMembers) {
            ConfirmationTokenHex = confirmationTokenHex;
            NotifyAllMembers = notifyAllMembers;
        }

        public String getConfirmationTokenHex() {
            return ConfirmationTokenHex;
        }

        public void setConfirmationTokenHex(String confirmationTokenHex) {
            ConfirmationTokenHex = confirmationTokenHex;
        }

        public boolean isNotifyAllMembers() {
            return NotifyAllMembers;
        }

        public void setNotifyAllMembers(boolean notifyAllMembers) {
            NotifyAllMembers = notifyAllMembers;
        }
    }


    class ConfirmFileResponse {
        String Transaction;
        String HashHex;
        String NonceHex;
        String Status;

        public static ConfirmFileResponse createFailInstance() {
            ConfirmFileResponse confirmFileResponse = new ConfirmFileResponse();
            confirmFileResponse.setStatus("failInstance");
            return confirmFileResponse;
        }

        public ConfirmFileResponse() {
        }

        public ConfirmFileResponse(String transaction, String hashHex, String nonceHex, String status) {
            Transaction = transaction;
            HashHex = hashHex;
            NonceHex = nonceHex;
            Status = status;
        }

        public boolean isConfirmationSuccesefull() {
            return Status != null && Status.equals("Success");
        }

        public boolean isFileNotFound() {
            return Status != null && Status.equals("FileNotFound");
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getTransaction() {
            return Transaction;
        }

        public void setTransaction(String transaction) {
            Transaction = transaction;
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


    }

    class UploadFileResponse {
        String Status;
        float Balance;
        String ConfirmationToken;
        float InitialFee;
        float MonthlyFee;

        public boolean isUploadSuccesefully() {
            return Status != null && Status.equals("Success");
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public float getBalance() {
            return Balance;
        }

        public void setBalance(float balance) {
            Balance = balance;
        }

        public String getConfirmationToken() {
            return ConfirmationToken;
        }

        public void setConfirmationToken(String confirmationToken) {
            ConfirmationToken = confirmationToken;
        }

        public float getInitialFee() {
            return InitialFee;
        }

        public void setInitialFee(float initialFee) {
            InitialFee = initialFee;
        }

        public float getMonthlyFee() {
            return MonthlyFee;
        }

        public void setMonthlyFee(float monthlyFee) {
            MonthlyFee = monthlyFee;
        }
    }
}
