package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
//@Table(name = "feed_table") //테이블명이 클래스명이 아닌 다른 이름으로 만들고 싶다면
public class Feed extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @ManyToOne
    @JoinColumn(name = "writer_user_id", nullable = false)
    private User writerUser;

    @Column(length = 1_000)
    private String contents;

    @Column(length = 30)
    private String location;
}
