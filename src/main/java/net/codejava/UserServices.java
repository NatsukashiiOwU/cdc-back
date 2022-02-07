package net.codejava;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class UserServices {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public List<User> listAll() {
		return repo.findAll();
	}
	
	public void register(User user, String siteURL) 
			throws UnsupportedEncodingException, MessagingException {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);
		
		repo.save(user);
		
		sendVerificationEmail(user, siteURL);
	}

	public void registerApi(User user, String siteURL)
			throws UnsupportedEncodingException, MessagingException {
		String randomPassword = RandomString.make(8);
		String encodedPassword = passwordEncoder.encode(randomPassword);
		user.setPassword(encodedPassword);

		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);

		repo.save(user);

		sendVerificationEmailApi(user, siteURL, randomPassword);
	}

	public void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "alpsdi78@gmail.com";
		String senderName = "ThatAnimeKun";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Here is your password: [[password]]<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Your company name.";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", user.getFullName());

		String verifyURL = siteURL + "/verifyUser?code=" + user.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("Email has been sent");
	}

	public void sendVerificationEmailApi(User user, String siteURL, String randomPassword)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "alpsdi78@gmail.com";
		String senderName = "ThatAnimeKun";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Here is your password: [[password]]<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Your company name.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFullName());
		String verifyURL = siteURL + "/verifyUser?code=" + user.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		content = content.replace("[[password]]", randomPassword);

		helper.setText(content, true);

		mailSender.send(message);

		System.out.println("Email has been sent");
	}
	
	public boolean verify(String verificationCode) {
		User user = repo.findByVerificationCode(verificationCode);
		
		if (user == null || user.isEnabled()) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			repo.save(user);
			
			return true;
		}
		
	}
	
}
