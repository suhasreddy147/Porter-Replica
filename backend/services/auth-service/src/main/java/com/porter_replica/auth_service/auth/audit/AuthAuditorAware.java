package com.porter_replica.auth_service.auth.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.porter_replica.auth_service.auth.constants.AuthConstants;
import com.porter_replica.auth_service.auth.security.principal.CustomUserPrincipal;

@Component(AuthConstants.AUTH_AUDITOR_AWARE)
public class AuthAuditorAware implements AuditorAware<Long>{

	@Override
	public Optional<Long> getCurrentAuditor() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}
		
		Object principal = authentication.getPrincipal();
		
		if(principal instanceof CustomUserPrincipal user) {
			return Optional.of(user.getUserId());
		}
		
		return Optional.empty();
	}

}
