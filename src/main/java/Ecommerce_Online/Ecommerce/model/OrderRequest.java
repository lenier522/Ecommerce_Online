/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ecommerce_Online.Ecommerce.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;

	private String address;

	private String city;

	private String state;

	private String pincode;
	
	private String paymentType;
       
}
