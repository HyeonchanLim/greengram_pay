package com.green.greengram.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class FeedLikeIds implements Serializable {
    private Long feedId;
    private Long userId;
}
