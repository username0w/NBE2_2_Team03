package com.example.nbe2_2_team03.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inquiry")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id", nullable = false, unique = true)
    private Long inquiryId;

    @Column(name = "inquiry_title", length = 255)
    private String inquiryTitle;

    @Column(name = "inquiry_content", length = 2000)
    private String inquiryContent;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}