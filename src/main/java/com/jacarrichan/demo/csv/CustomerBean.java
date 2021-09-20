package com.jacarrichan.demo.csv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBean {

    private String customerNo;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String mailingAddress;
    private Boolean married;
    private Integer numberOfKids;
    private String favouriteQuote;
    private String email;
    private Long loyaltyPoints;

}