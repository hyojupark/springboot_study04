package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import ognl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final EntityManager em;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        String queryString = String.format(
                "SELECT i FROM Item as i WHERE " +
                "i.createDate > :startDate " +
                "%s " +
                "%s " +
                "ORDER BY i.id desc",
                searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()));
        TypedQuery<Item> query = em.createQuery(queryString, Item.class);
//        TypedQuery<Item> query = em.createNativeQuery(queryString, Item.class);
        query.setParameter("startDate", createSdtAfter(itemSearchDto.getSearchDateType()));
        if (itemSearchDto.getSearchSellStatus() != null) {
            query.setParameter("itemSellStatus", itemSearchDto.getSearchSellStatus());
        }
        if (itemSearchDto.getSearchBy() != null && !StringUtils.isEmpty(itemSearchDto.getSearchQuery())) {
            query.setParameter("searchQuery", itemSearchDto.getSearchQuery());
        }
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<Item> itemList = query.getResultList();

        return new PageImpl<>(itemList);
    }

    private LocalDateTime createSdtAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return LocalDateTime.of(1900, 1, 1, 0, 0);
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return dateTime;
    }

    private String searchSellStatusEq(ItemSellStatus searchItemStatus) {
        return searchItemStatus == null ? "" : "AND i.itemSellStatus=:itemSellStatus ";
    }

    private String searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchQuery)) {
            return "";
        } else if (StringUtils.equals("itemNm", searchBy)) {
            return "AND i.itemNm LIKE CONCAT('%', :searchQuery, '%') ";
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return "AND i.createdBy LIKE CONCAT('%', :searchQuery, '%') ";
        }

        return "";
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        String queryString = "SELECT NEW com.shop.dto.MainItemDto(i.id, i.itemNm, i.itemDetail, im.imgUrl, i.price) " +
                "FROM ItemImg as im INNER JOIN im.item as i " +
                "WHERE im.repImgYn='Y' " +
                searchByLike("itemNm", itemSearchDto.getSearchQuery()) +
                "ORDER BY i.id desc";
        TypedQuery<MainItemDto> query = em.createQuery(queryString, MainItemDto.class);

        if (!StringUtils.isEmpty(itemSearchDto.getSearchQuery())) {
            query.setParameter("searchQuery", itemSearchDto.getSearchQuery());
        }

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<MainItemDto> itemList = query.getResultList();

        return new PageImpl<>(itemList);
    }
}
