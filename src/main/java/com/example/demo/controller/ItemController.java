package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Controller
public class ItemController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ItemRepository itemRepository;

	@GetMapping("/items")
	public String index(
			@RequestParam(value = "categoryId", defaultValue = "") Integer categoryId,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "maxPrice", defaultValue = "") Integer maxPrice,
			Model model) {

		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		List<Item> itemList = null;

		if (keyword.length() > 0 && maxPrice != null) {
			itemList = itemRepository.findByNameContainingAndPriceLessThan(keyword, maxPrice);
		} else if (keyword.length() > 0) {
			itemList = itemRepository.findByNameContaining(keyword);
		} else if (maxPrice != null) {
			itemList = itemRepository.findByPriceLessThan(maxPrice);
		} else if (categoryId != null) {
			itemList = itemRepository.findByCategoryId(categoryId);
		} else {
			itemList = itemRepository.findAll();
		}
		model.addAttribute("keyword", keyword);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("items", itemList);
		return "items";
	}

	@GetMapping("/items/{id}")
	public String show(
			@PathVariable("id") Integer id,
			Model model) {

		Item item = itemRepository.findById(id).get();
		model.addAttribute("item", item);
		return "itemDetail";
	}

}
