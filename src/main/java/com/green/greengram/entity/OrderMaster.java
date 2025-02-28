package com.green.greengram.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMaster extends UpdatedAt {
    @Id @Tsid
    private Long orderId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false, length = 2)
    private OrderStatusCode orderStatusCode;

    /*
    DB에서는 외래키 하나로 두 테이블의 연관관계를 관리한다. (양방향)
    하지만 언어에서는 아래처럼 양방향 관계설정을 해주어야 양방향이 된다.
    이때 두 객체 연관관계 중 하나를 정해서 테이블의 외래키를 관리해야 하는데
    이것을 연관관계의 주인(Owner)라고 표현한다. 실제 DB에서 FK가 있는 쪽이 Owner가 된다.
    Owner는 mappedBy속성을 사용하지 않는다. 즉, mappedBy속성이 있으면 Owner가 아니라는 뜻이다.

    cascade = CascadeType.ALL을 설정하여, OrderMaster를 저장할 때 연관된 OrderProduct도 자동으로 저장됩니다.

    orphanRemoval = true를 설정하여, OrderMaster에서 OrderProduct가 제거되면 고아 객체가 되는 OrderProduct도 자동으로 삭제
     */
    @OneToMany(mappedBy = "orderMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>();

    // 양방향 관계를 유지하기 위해 helper 메소드 추가
    public void addOrderProduct(OrderProduct orderProduct) {
        orderProductList.add(orderProduct);
        orderProduct.setOrderMaster(this);
    }
}

