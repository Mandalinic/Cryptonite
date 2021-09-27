package fr.isika.al9.userRegistration.appuser;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.isika.al9.userRegistration.registration.token.ConfirmationToken;
import fr.isika.al9.userRegistration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService{
	
	private final static String USER_NOT_FOUND_MSG = "L'utilisateur avec l'email %s est introuvable !";
	private final AppUserRepository appUserRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(
						String.format(USER_NOT_FOUND_MSG, email)));
	}

	public String signUpUser(AppUser appUser) {
		boolean userExists = appUserRepository.
				findByEmail(appUser.getUsername())
				.isPresent();
		
		if (userExists) {
			// verifier que les attributs sont les meme 
			// si enable est false, renvoyer un mail de confirmation
			
			throw new IllegalStateException("L'email est déjà pris");
		}
		
		String encodedPassword = bCryptPasswordEncoder
				.encode(appUser.getPassword());
		
		appUser.setPassword(encodedPassword);
		
		appUserRepository.save(appUser);
		
		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15),
				appUser
				);
		
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		return token;	}
	
	public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
	
}
