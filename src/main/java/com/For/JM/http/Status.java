package com.For.JM.http;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Time: 15:50
 */
public enum Status {

        OK(200, "OK"),
        Bad_Request(400, "Bad_Request"),
        Not_Found(404, "Not_Found"),
        Redirect(302,"Temporarily Moved"),
        Method_Not_Allowed(405, "Method_Not_Allowed"),
        Internal_Server_Error(500, "Internal_Server_Error")
        ;

        private int id;
        private String reason;
         Status( int id, String reason){
            this.id = id;
            this.reason = reason;
        }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }
}
