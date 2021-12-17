package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "item")
@Data
public class Item extends BaseTimeEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  // 상품 크도

    @Column(nullable = false, length = 50)
    private String itemNm;  // 상품 명

    @Column(nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int stockNumber;  // 재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.ORDINAL)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태
//    private LocalDateTime regTime;  // 등록 시간
//    private LocalDateTime updateTime;  // 수정 시간
}
