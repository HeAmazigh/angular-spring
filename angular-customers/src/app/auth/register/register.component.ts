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

  error: string = "";

  constructor(private authService: AuthService, private router: Router) {

  }

  onSubmit() {
    if (this.registerForm.valid) {
      const customer: RegisterCustomer = this.registerForm.getRawValue();
      // let firstName = this.registerForm.value.firstName;
      // let lastName = this.registerForm.value.lastName;
      // let email = this.registerForm.value.email;
      // let pwd = this.registerForm.value.password;
      // let rpwd = this.registerForm.value.repeatedPassword;


      // this.authService.register(firstName, lastName, email, pwd, rpwd).subscribe({
      this.authService.register(customer).subscribe({
        next: responseData => {
          this.router.navigateByUrl("/login");
        },
        error: err => {
          console.log(err);
        }
      })
    }
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
