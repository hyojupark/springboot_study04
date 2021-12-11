package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.ItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        ItemEntity item = new ItemEntity();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        ItemEntity savedItem = itemRepository.save(item);
        System.out.println(savedItem);
    }

    private void createItemList() {
        List<ItemEntity> itemDatas = IntStream.range(1, 11)
            .mapToObj(i -> {
                ItemEntity item = new ItemEntity();
                item.setItemNm("테스트 상품" + i);
                item.setPrice(10000 + i);
                item.setItemDetail("테스트 상품 상세 설명" + i);
                item.setItemSellStatus(ItemSellStatus.SELL);
                item.setStockNumber(100);

                return item;
            })
            .collect(Collectors.toList());

        itemRepository.saveAll(itemDatas);
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<ItemEntity> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(ItemEntity item : itemList) {
            System.out.println(item.toString());
        }
    }
}