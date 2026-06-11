package com.minierp.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_document")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder

public class ProductDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Integer docId;

    @Column(name = "product_no", nullable = false, length = 8)
    private String productNo;

    @Column(name = "doc_type", length = 20)
    private String docType;

    @Column(name = "doc_nm", length = 200)
    private String docNm;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
