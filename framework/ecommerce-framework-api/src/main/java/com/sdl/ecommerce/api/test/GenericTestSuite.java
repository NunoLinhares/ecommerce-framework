package com.sdl.ecommerce.api.test;

import com.sdl.ecommerce.api.*;
import com.sdl.ecommerce.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Generic Test Suite for the E-Commerce API
 *
 * @author nic
 */
public abstract class GenericTestSuite {

    private static final Logger LOG = LoggerFactory.getLogger(GenericTestSuite.class);

    @Autowired(required = false)
    protected ProductCategoryService productCategoryService;

    @Autowired(required = false)
    protected ProductCategoryService categoryService;

    @Autowired(required = false)
    protected ProductQueryService queryService;

    @Autowired(required = false)
    protected ProductDetailService detailService;

    @Autowired(required = false)
    protected CartFactory cartFactory;

    // TODO: Add some generic asserts here

    protected void testGetCategoryById(String categoryId) throws Exception {

        Category category = this.categoryService.getCategoryById(categoryId);
        LOG.info("Category ID: " + category.getId() + ", Name: " + category.getName());
        printCategories(category.getCategories());
    }

    protected void testGetCategoryByPath(String categoryPath) throws Exception {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        LOG.info("Category ID: " + category.getId() + ", Name: " + category.getName());
        printCategories(category.getCategories());
    }

    protected void testGetProductsInCategory(String categoryPath) throws Exception {

        Category category = this.categoryService.getCategoryByPath(categoryPath);
        LOG.info("Category ID: " + category.getId() + ", Name: " + category.getName());

        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        category(category).
                        viewSize(10));
        this.printProducts(result);
        LOG.info("Next set of products =>");
        result = result.next();
        this.printProducts(result);
        LOG.info("Previous set of products =>");
        result = result.previous();
        this.printProducts(result);
    }

    protected void testNavigateCategoryTree(String categoryPath) throws Exception {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        LOG.info("Navigate through subcategories:");
        for( Category subCategory : category.getCategories() ) {
            LOG.info("Category:" + subCategory.getName());
            QueryResult result = this.queryService.query(
                    this.queryService.newQuery().category(subCategory));
            this.printProducts(result);
        }
    }

    protected void testPromotions(String categoryPath) throws Exception {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        category(category).
                        viewSize(10));
        this.printPromotions(result.getPromotions());
    }

    protected void testFacets(String categoryPath) throws Exception {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        category(category).
                        viewSize(10));
        this.printFacets(result.getFacetGroups(null));
    }

    protected void testBreadcrumbs(String categoryPath) throws Exception {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        category(category).
                        viewSize(10));
        this.printBreadcrumbs(result.getBreadcrumbs("/products", "Products"));
    }

    protected void testSearch(String searchPhrase) throws Exception {
        QueryResult result = this.queryService.query(this.queryService.newQuery().searchPhrase(searchPhrase).viewSize(100));
        LOG.info("Total count: " + result.getTotalCount());
        this.printProducts(result);
        this.printFacets(result.getFacetGroups(null));
    }

    protected void testSearchWithFacets(String searchPhase, List<FacetParameter> facets) throws Exception {
        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        searchPhrase(searchPhase).
                        viewSize(10).
                        facets(facets));
        LOG.info("Total count: " + result.getTotalCount());
        this.printProducts(result);
        this.printFacets(result.getFacetGroups(null));
    }

    protected void testQueryFlyout(String categoryPath) throws Exception {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        category(category).
                        viewSize(10).
                        viewType(ViewType.FLYOUT));

        this.printFacets(result.getFacetGroups(null));
        this.printPromotions(result.getPromotions());
    }

    protected void testQueryWithFilterAttributes(String categoryPath, QueryFilterAttribute filterAttribute) {
        Category category = this.categoryService.getCategoryByPath(categoryPath);
        QueryResult result = this.queryService.query(
                this.queryService.newQuery().
                        category(category).
                        viewSize(10).
                        filterAttribute(filterAttribute));
        this.printFacets(result.getFacetGroups(null));
        this.printPromotions(result.getPromotions());
    }

    protected void testGetProductDetail(String productId) throws Exception {
        LOG.info("Getting detail for product...");
        ProductDetailResult result = this.detailService.getDetail(productId);
        Product product = result.getProductDetail();
        LOG.info("Product ID: " + product.getId());
        LOG.info("Product Name: " + product.getName());
        LOG.info("Product Description: " + product.getDescription());
        LOG.info("Price: " + product.getPrice().getPrice());
        LOG.info("Detail Page URL:" + product.getDetailPageUrl());
        LOG.info("Primary Image URL: " + product.getPrimaryImageUrl());
        LOG.info("Categories: ");
        for ( Category category : product.getCategories() ) {
            LOG.info("ID: " + category.getId() + " Name: " + category.getName() + " Parent: " + category.getParent().getName());
        }
        LOG.info("Breadcrumbs: ");
        this.printBreadcrumbs(result.getBreadcrumbs("/products", "Products"));
        this.printPromotions(result.getPromotions());
        LOG.info("Facets: ");
        for ( FacetParameter facet : product.getFacets() ) {
            LOG.info(facet.getName() + " : " + facet.getValues());
        }
        LOG.info("Attributes: ");
        for ( String attrName : product.getAttributes().keySet() ) {
            LOG.info("Name: " + attrName + " Value: " + product.getAttributes().get(attrName));
        }
    }

    protected void testCart(String... productIds) throws Exception {
        LOG.info("Test cart...");

        Cart cart = this.cartFactory.createCart();

        for ( String productId : productIds ) {
            LOG.info("Adding product with ID: " + productId + " to cart...");
            cart.addProduct(productId);
            this.printCartItems(cart);
        }
    }

    /******** SUPPORT PRINTOUT FUNCTIONS ***********/

    protected void printCategories(List<Category> categories) {
        LOG.info("------ Categories: ---------");
        for ( Category subCategory : categories ) {
            LOG.info("Category ID: " + subCategory.getId() + ", Name: " + subCategory.getName());
        }
    }

    protected void printProducts(QueryResult resultSet) {
        LOG.info("------ Products: ---------");
        for ( Product product : resultSet.getProducts() ) {
            LOG.info("  Product ID: " + product.getId() + " Name: " + product.getName());
        }
    }

    protected void printPromotions(List<Promotion> promotions) {
        LOG.info("------ Promotions: -------");
        for ( Promotion promotion : promotions ) {
            LOG.info("Promo ID: " + promotion.getId() + " Name: "  + promotion.getName() + " Title: " + promotion.getTitle());
        }
    }

    protected void printFacets(List<FacetGroup> facetGroups) {
        LOG.info("------- Facets: ----------");
        for ( FacetGroup facetGroup : facetGroups ) {
            LOG.info("Facet group title: " + facetGroup.getTitle() + ", type:" + facetGroup.getType());
            LOG.info("--------------------------------------------");
            for ( Facet facet : facetGroup.getFacets() ) {
                LOG.info(facet.getTitle() + " (" + facet.getCount() + ")");
            }
            LOG.info("");
        }
    }

    protected void printBreadcrumbs(List<Breadcrumb> breadcrumbs) {
        LOG.info("------- Breadcrumbs: ---------");
        for ( Breadcrumb breadcrumb : breadcrumbs ) {
            LOG.info("\"" + breadcrumb.getTitle() + "\"" + " URL: " + breadcrumb.getUrl() + " category: " + breadcrumb.isCategory());
        }
    }

    protected void printCartItems(Cart cart) {
        LOG.info("Cart items:");
        for ( CartItem cartItem : cart.getItems() ) {
            LOG.info("Product: " + cartItem.getProduct().getName() + " Price: " + cartItem.getPrice().getFormattedPrice() + " Quantity: " + cartItem.getQuantity());
        }
        LOG.info("Total items: " + cart.count());
        LOG.info("Total price: " + cart.getTotalPrice().getFormattedPrice());
    }

}
