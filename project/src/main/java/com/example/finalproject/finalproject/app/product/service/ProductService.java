package com.example.finalproject.finalproject.app.product.service;

import com.example.finalproject.finalproject.app.cart.entity.CartItem;
import com.example.finalproject.finalproject.app.cart.service.CartService;
import com.example.finalproject.finalproject.app.member.entity.Member;
import com.example.finalproject.finalproject.app.post.entity.Post;
import com.example.finalproject.finalproject.app.postKeyword.entity.PostKeyword;
import com.example.finalproject.finalproject.app.postKeyword.service.PostKeywordService;
import com.example.finalproject.finalproject.app.postTag.entity.PostTag;
import com.example.finalproject.finalproject.app.postTag.service.PostTagService;
import com.example.finalproject.finalproject.app.product.entity.Product;
import com.example.finalproject.finalproject.app.product.repository.ProductRepository;
import com.example.finalproject.finalproject.app.productTag.entity.ProductTag;
import com.example.finalproject.finalproject.app.productTag.service.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final PostKeywordService postKeywordService;
    private final ProductTagService productTagService;
    private final PostTagService postTagService;
    private final CartService cartService;

    @Transactional
    public Product create(Member author, String subject, int price, long postKeywordId, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findById(postKeywordId).get();
        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public Product create(Member author, String subject, int price, String postKeywordContent, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findByContentOrSave(postKeywordContent);
        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public Product create(Member author, String subject, int price, PostKeyword postKeyword, String productTagContents) {
        Product product = Product.builder()
                .author(author)
                .postKeyword(postKeyword)
                .subject(subject)
                .price(price)
                .build();

        productRepository.save(product);
        applyProductTags(product, productTagContents);
        return product;
    }

    @Transactional
    public void applyProductTags(Product product, String productTagContents) {
        productTagService.applyProductTags(product, productTagContents);
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void modify(Product product, String subject, int price, String productTagContents) {
        product.setSubject(subject);
        product.setPrice(price);
        applyProductTags(product, productTagContents);
    }

    public List<Product> findAllByOrderByIdDesc() {
        return productRepository.findAllByOrderByIdDesc();
    }

    public Optional<Product> findForPrintById(long id, Member actor) {
        Optional<Product> opProduct = findById(id);
        if (opProduct.isEmpty()) return opProduct;
        loadForPrintData(opProduct.get(), actor);
        return opProduct;
    }

    private void loadForPrintDataOnProductTagList(List<ProductTag> productTags, Member actor) {
        List<Product> products = productTags
                .stream()
                .map(ProductTag::getProduct)
                .collect(toList());

        loadForPrintData(products, actor);
    }

    private void loadForPrintData(Product product, Member actor) {
        loadForPrintData(List.of(product), actor);
    }

    private void loadForPrintData(List<Product> products, Member actor) {
        long[] ids = products
                .stream()
                .mapToLong(Product::getId)
                .toArray();

        List<ProductTag> productTagsByProductIds = productTagService.getProductTagsByProductIdIn(ids);

        // 현재 로그인 되어 있고
        // 장바구니에 이미 추가되었는지

        if (actor != null) {
            List<CartItem> cartItems = cartService.getCartItemsByBuyerIdProductIdIn(actor.getId(), ids);
            Map<Long, CartItem> cartItemsByProductIdMap = cartItems
                    .stream()
                    .collect(toMap(
                            cartItem -> cartItem.getProduct().getId(),
                            cartItem -> cartItem
                    ));

            products.stream()
                    .filter(product -> cartItemsByProductIdMap.containsKey(product.getId()))
                    .map(product -> cartItemsByProductIdMap.get(product.getId()))
                    .forEach(cartItem -> cartItem.getProduct().getExtra().put("actor_cartItem", cartItem));
        }

        Map<Long, List<ProductTag>> productTagsByProductIdMap = productTagsByProductIds.stream()
                .collect(groupingBy(
                        productTag -> productTag.getProduct().getId(), toList()
                ));

        products.stream().forEach(product -> {
            List<ProductTag> productTags = productTagsByProductIdMap.get(product.getId());

            if (productTags == null || productTags.size() == 0) return;

            product.getExtra().put("productTags", productTags);
        });
    }

    public boolean actorCanModify(Member actor, Product product) {
        if (actor == null) return false;

        return actor.getId().equals(product.getAuthor().getId());
    }

    public List<Post> findPostsByProduct(Product product) {
        Member author = product.getAuthor();
        PostKeyword postKeyword = product.getPostKeyword();
        List<PostTag> postTags = postTagService.getPostTags(author.getId(), postKeyword.getId());

        return postTags
                .stream()
                .map(PostTag::getPost)
                .collect(Collectors.toList());
    }

    public boolean actorCanRemove(Member actor, Product product) {
        return actorCanModify(actor, product);
    }

    @Transactional
    public void remove(Product product) {
        productRepository.delete(product);
    }

    private List<ProductTag> getProductTags(Product product) {
        return productTagService.getProductTags(product);
    }

    public List<Product> findAllForPrintByOrderByIdDesc(Member actor) {
        List<Product> products = findAllByOrderByIdDesc();

        loadForPrintData(products, actor);

        return products;
    }

    public List<ProductTag> getProductTags(String productTagContent, Member actor) {
        List<ProductTag> productTags = productTagService.getProductTags(productTagContent);

        loadForPrintDataOnProductTagList(productTags, actor);

        return productTags;
    }
}
