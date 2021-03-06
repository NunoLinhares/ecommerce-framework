<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dxa" uri="http://www.sdl.com/tridion-dxa" %>
<%@ taglib prefix="xpm" uri="http://www.sdl.com/tridion-xpm" %>
<jsp:useBean id="entity" type="com.sdl.ecommerce.dxa.model.CartWidget" scope="request"/>
<jsp:useBean id="markup" type="com.sdl.webapp.common.markup.Markup" scope="request"/>

<div class="utility-nav cart-nav" style="margin-right: 15px;margin-top: 4px;">
    <a href="${entity.cartPageLink}" class="btn btn-default cart-btn">
        <div class="cart-text">
            <span id="cart-amount">${entity.cartCount}</span>&nbsp;<i class="fa fa-shopping-cart"></i>
        </div>
    </a>
</div>
