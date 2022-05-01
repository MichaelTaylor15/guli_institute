package com.atguigu.servicebase.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor /*生成全参构造方法*/
@NoArgsConstructor /*生成无参构造方法*/
public class MyException extends RuntimeException{

    /*状态码*/
    private Integer code;

    private String message;


}
