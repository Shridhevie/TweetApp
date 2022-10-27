package com.tweetapp.model;


import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.UniqueElements;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Table(name="user_details")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class UserData {


	
    @Column
    //@Schema(description="FirstName of the user",required=true,example="Sri")
    //@NotBlank(message = "FirstName should not be empty")
    private String firstName;

    @Column
    //@Schema(description="LastName of the User",required=true,example="devi")
    //@NotBlank(message="LastName should not be empty")
    private String lastName;


    @Email
    @Column(unique = true,nullable = false)
    @NotBlank(message = "Email should not be empty")
    //@Schema(description = "Email of the User",required = true,example = "sridevi@gmail.com")
    private String email;

    @Id
    @NotBlank(message="userName should not be empty")
    @Pattern( regexp = "[a-zA-Z0-9@.]*$",message = "userName should contain only Alphabets and Numbers")
   // @Schema(description="UserName of the User",required=true,example="Sri")
    private String userName;

    @Column
    @NotBlank(message = "Password should not be empty")
    @Size(min=8, message = "minimum 8 Characters required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  //  @Schema(description="Password of the User",required=true,example="Qwerty123")
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d]{4,}$",message = "password must alteast 8 characters,one Lowercase letter,one Uppercase letter,one numeric value,one special character")
    private String password;

    @Transient
    //@Schema(description="Confirm Password of the User and should match with password",required=true,example="Qwerty123")
    private String confirmPassword;



    @Column
   // @Schema(description="Contact Number of the User",required=true,example="9999999999")
    private long contactNo;

   
}

