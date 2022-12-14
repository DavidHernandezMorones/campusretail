package com.campusretail.orderservice.controller;

import com.campusretail.orderservice.domain.Cart;
import com.campusretail.orderservice.domain.Item;
import com.campusretail.orderservice.exception.CartNotFoundException;
import com.campusretail.orderservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controller which manage
 * all the actions for the 
 * cart of the API 
 */
@RestController
public class CartController {

	private final CartService cartService;
	
	@Autowired
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	/**
	 * Endpoint which returns the current cart 
	 * in the API (All the items in the car as a
	 * collection)
	 * @param cartId the id of the cart in the API
	 * @return a collection of all the items of the cart
	 */
	@GetMapping(value = "/cart")
	public ResponseEntity<List<Item>> getCart(@RequestHeader(value = "Cookie") String cartId) throws InterruptedException, ExecutionException{
		List<Item> cart = cartService.getCart(cartId).get();
		if (!cart.isEmpty()) {
			return new ResponseEntity<>(cart, HttpStatus.OK);
		}
		//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		throw new CartNotFoundException("Cart with id: "+cartId+" was not found");
	}


	/**
	 * Endpoint which adds the wanted item(s) to
	 * the cart
	 * @param productId the id of the wanted product to add
	 * @param quantity the total number of the products
	 *                 wanted to add to the cart   
	 * @param cartId the id of the cart to add the items
	 * @return a new list with the added products
	 */
	@PostMapping(value = "/cart", params = {"productId", "quantity"})
	public ResponseEntity<List<Item>> addItemToCart(@RequestParam Long productId, @RequestParam Integer quantity, @RequestHeader(value = "Cookie") String cartId) throws InterruptedException, ExecutionException {
		List<Item> cart = cartService.getCart(cartId).get();
		if (cart != null) {
			if (cart.isEmpty()) {
				cartService.addItemToCart(cartId, productId, quantity);
			} else {
				if (cartService.checkIfItemExists(cartId, productId)) {
					cartService.changeItemQuantity(cartId, productId, quantity);
				} else {
					cartService.addItemToCart(cartId, productId, quantity);
				}
			}
			return new ResponseEntity<>(cart, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Endpoint which delete/remove a specific item from
	 * the list of the cart
	 * @param productId the id of the wanted to delete product
	 * @param cartId the id of the cart will remove the product
	 * @return a new list without the removed products
	 */
	@DeleteMapping(value = "/cart", params = "productId")
	public ResponseEntity<List<Item>> removeItemFromCart(@RequestParam Long productId, @RequestHeader(value = "Cookie") String cartId) throws InterruptedException, ExecutionException {
		List<Item> cart = cartService.getCart(cartId).get();
		if (cart != null) {
			cartService.deleteItemFromCart(cartId, productId);
			cart = cartService.getCart(cartId).get();
			return new ResponseEntity<>(cart, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
