package com.zentry.sigea.module_pago.presentation.models.responseDTO;

import java.util.Date;

import lombok.Data;

@Data
public class WebHookResponse {
    private String id;
    private Boolean liveMode;
    private String type;
    private Date createdAt;
    private DataObj data;



    @Data
    public static class DataObj {
        private String id;
    }
}