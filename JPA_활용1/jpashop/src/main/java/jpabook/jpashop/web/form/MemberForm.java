package jpabook.jpashop.web.form;

import jpabook.jpashop.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "이름은 필수 값")
    private String name;

    private String city;

    private String street;

    private String zipcode;

    public Address getAddress() {
        return new Address(city, street, zipcode);
    }
}
