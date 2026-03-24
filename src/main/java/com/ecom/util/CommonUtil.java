package com.ecom.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;

	
	  @Autowired private UserService userService;
	 

	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("thecodertalkies@gmail.com", "Shoping Cart");
		helper.setTo(reciepentEmail);

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
				+ "\">Change my password</a></p>";
		helper.setSubject("Password Reset");
		helper.setText(content, true);
		mailSender.send(message);

		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		// http://localhost:8080/forgot-password
		String siteUrl = request.getRequestURL().toString();

		return siteUrl.replace(request.getServletPath(), "");

	}

	String msg = null;

	public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {

		msg = "<div style='font-family: Arial, sans-serif; padding:20px;'>"
				+ "<h2 style='color:#4CAF50;'>Order Status Update</h2>"

				+ "<p>Hello <b>[[name]]</b>,</p>"

				+ "<p>Thank you for shopping with us. Your order status has been updated.</p>"

				+ "<p><b>Status :</b> " + "<span style='color:#ff6600; font-weight:bold;'>[[orderStatus]]</span></p>"

				+ "<hr>"

				+ "<h3 style='color:#333;'>Product Details</h3>"

				+ "<table style='border-collapse: collapse; width: 100%;'>" + "<tr>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>Product Name</td>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>[[productName]]</td>" + "</tr>"

				+ "<tr>" + "<td style='padding:8px; border:1px solid #ddd;'>Category</td>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>[[category]]</td>" + "</tr>"

				+ "<tr>" + "<td style='padding:8px; border:1px solid #ddd;'>Quantity</td>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>[[quantity]]</td>" + "</tr>"

				+ "<tr>" + "<td style='padding:8px; border:1px solid #ddd;'>Price</td>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>₹ [[price]]</td>" + "</tr>"

				+ "<tr>" + "<td style='padding:8px; border:1px solid #ddd;'>Total Order Price</td>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>₹ [[totalPrice]]</td>" + "</tr>"

				+ "<tr>" + "<td style='padding:8px; border:1px solid #ddd;'>Payment Type</td>"
				+ "<td style='padding:8px; border:1px solid #ddd;'>[[paymentType]]</td>" + "</tr>"

				+ "</table>"

				+ "<br>"

				+ "<p>If you have any questions, feel free to contact our support team.</p>"

				+ "<p>Thank you for choosing us.</p>"

				+ "<p><b>Shooping Cart Team</b></p>"

				+ "<hr>"

				+ "<p style='font-size:12px;color:gray;'>This is an automated message. Please do not reply.</p>"

				+ "</div>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("thecodertalkies@gmail.com", "Shooping Cart");
		helper.setTo(order.getOrderAddress().getEmail());
		
		Double totalPrice = order.getPrice() * order.getQuantity();

		msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
		msg = msg.replace("[[orderStatus]]", status);
		msg = msg.replace("[[productName]]", order.getProduct().getTitle());
		msg = msg.replace("[[category]]", order.getProduct().getCategory());
		msg = msg.replace("[[quantity]]", order.getQuantity().toString());
		msg = msg.replace("[[price]]", order.getPrice().toString());
		msg = msg.replace("[[totalPrice]]", totalPrice.toString());
		msg = msg.replace("[[paymentType]]", order.getPaymentType());

		helper.setSubject("Your Order Status Has Been Updated");
		helper.setText(msg, true);

		mailSender.send(message);

		return true;
	}
	
	public UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}

}
