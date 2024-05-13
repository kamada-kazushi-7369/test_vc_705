package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Account;

@Aspect
@Component
public class CheckLoginAspect {

	@Autowired
	Account account;

	@Before("execution(* com.example.demo.controller.*Controller.*(..))")
	public void writeLog(JoinPoint jp) {
		if (account == null || account.getName() == null || account.getName().length() == 0) {
			System.out.println("ゲスト：");
		} else {
			System.out.println(account.getName() + ":");
		}
		System.out.println(jp.getSignature());
	}

	@Around("execution(* com.example.demo.controller.ItemController.*(..))||"
			+ "execution(* com.example.demo.controller.CartController.*(..))||"
			+ "execution(* com.example.demo.controller.OrderController.*(..))")
	public Object checkLogin(ProceedingJoinPoint jp) throws Throwable {
		if (account == null || account.getName() == null || account.getName().length() == 0) {
			System.err.println("ログインしていません！");
			return "redirect:/login?error=notLoggedIn";
		}
		return jp.proceed();
	}

}
