package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="vendor")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vendor {
    @Id
    @Column(name="vendor_cd")
    private String vendorCd; // 협력사 코드

    @Column(name = "vendor_nm", nullable=false,unique = true,length = 6)
    private String vendorNm; // 협력사 명

}
