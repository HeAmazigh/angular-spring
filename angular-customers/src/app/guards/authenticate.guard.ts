import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authenticateGuard: CanActivateFn = (route, state) => {
  const authServise = inject(AuthService)
  const router = inject(Router);

  if (!authServise.isAuthenticated) {
    router.navigateByUrl('/login');
    return false;
  } else {
    return true;
  }
};
