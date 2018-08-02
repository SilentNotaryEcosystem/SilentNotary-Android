package com.silentnotary.api;

/**
 * Created by albert on 9/27/17.
 */

public class NetworkingConstantsApi {
    public static final String USER_REGISTRATION_ENDPOINT       = "registerUser";
    public static final String USER_LOGIN_ENDPOINT              = "login";
    public static final String USER_ACTIVATION_ENDPOINT         = "activateUser";
    public static final String USER_RESTORE_PASSWORD            = "restorePassword";
    public static final String USER_CONFIRM_RESTORE_PASSWORD    = "confirmPasswordRestore";
    public static final String GET_USER_BY_CREDENTIALS          = "getUserByCredentials";
    public static final String GET_USER_BY_TOKEN                = "getUserByToken";
    public static final String GET_USER_OPERATIONS              = "getUserOperations";
    public static final String GET_USER_DATA                    = "getUserData";
    public static final String ACTIVATE_PROMOCODE               = "activatePromocode";
    public static final String EDIT_PROFILE                     = "editProfile";
    public static final String REPLENISH_BALANCE                = "replenishBalance";
    public static final String GET_FILE_BY_TOKEN                = "getFileInfoByToken";

    //Facebook
    public static final String FACEBOOK_ADD_CONVERSATION        = "addFacebookConversation";
    public static final String FACEBOOK_FIX_CONVERSATION        = "fixFacebookConversation";
    public static final String FACEBOOK_GET_CHATBOT_CONVERSATIONS = "getChatBotConversations";
    public static final String FACEBOOK_RESOLVE_FACEBOOKID      = "resolveFacebookId";
    public static final String FACEBOOK_REGISTER_FACEBOOKID     = "registerFacebookId";
    public static final String AUTH_EXTERNAL                    = "authExternal";
    public static final String REQUEST_AUTH_TOKEN               = "requestAuthenticationToken";

    //Bank
    public static final String BANK_CARD_BIND                   = "bindBankCard";
    public static final String BANK_CARD_GETALL                 = "getBankCards";
    public static final String GET_USER_ACCOUNT_OPERATIONS      = "getUserAccountOperations";

    //FILE
    public static final String FILE_CREATE                      = "createFileExternal";
    public static final String FILE_CONFIRM                     = "confirmFile";
    public static final String FILE_VERIFY                      = "verifyFile";
    public static final String FILE_DOWNLOAD                    = "downloadFile";
    public static final String FILE_DELETE                      = "deleteFile";

}
