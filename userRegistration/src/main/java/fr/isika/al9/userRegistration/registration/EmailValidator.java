package fr.isika.al9.userRegistration.registration;

import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{

	@Override
	public boolean test(String s) {
		
		// regex pour valider l email
		
		
		return true;
	}

}
