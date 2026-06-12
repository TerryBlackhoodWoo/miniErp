package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendor")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @Column(name = "vendor_cd", length = 6)
    private String vendorCd;

    @Column(name = "vendor_nm", nullable = false, length = 200)
    private String vendorNm;
}