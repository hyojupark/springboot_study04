package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, long itemId);

    @Query("SELECT NEW com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem as ci, ItemImg im " +
            "JOIN ci.item as i " +
            "WHERE ci.cart.id = :cartId " +
            "AND im.item.id = ci.item.id " +
            "AND im.repImgYn = 'Y' " +
            "ORDER BY ci.createDate desc")
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
