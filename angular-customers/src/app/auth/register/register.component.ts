import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

import { PasswordValidatorService } from '../../core/password-validator.service'
import { Customer } from 'src/app/shared/interfaces/customer.interface';
import { RegisterCustomer } from 'src/app/shared/interfaces/register-customer.interface';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup = new FormGroup({
    firstName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    repeatedPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
  },
    [PasswordValidatorService.match('password', 'repeatedPassword')]
  );

  isSubmission: boolean = false;
  error: string = "";

  constructor(private authService: AuthService, private router: Router) {

  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.isSubmission = true;
      const customer: RegisterCustomer = this.registerForm.getRawValue();
      this.authService.register(customer).subscribe({
        next: responseData => {
          this.router.navigateByUrl("/login");
        },
        error: err => {
          console.log(err);
          this.isSubmission = false;
        }
      })
    }
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
