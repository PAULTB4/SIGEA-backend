package com.zentry.sigea.module_usuarios.presentation.models.responseDTO;

import lombok.Getter;

@Getter
public class GeneralResponseDTO<T> {
    private boolean status;
    private String message;
    private T extraData;

    public GeneralResponseDTO(
        boolean status , 
        String message , 
        T extraData
    ){
        this.status = status;
        this.message = message;
        this.extraData = extraData;
    }
}
