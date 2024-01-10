import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('',[Validators.required, Validators.minLength(8)]),
  });

  error: string = "";

  ngOnInit(): void {
    this.authService.validateToken()?.subscribe({
      next: (result) => {
        if(result) this.router.navigateByUrl("/customers/list");
      },
      error: (err) => {console.log(err)}
    });
  }

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.loginForm.valid) {
      let email = this.loginForm.value.email;
      let pwd = this.loginForm.value.password;

      this.authService.login(email, pwd).subscribe({
        next: responseData => {
          this.authService.loadProfil(responseData);
          this.router.navigateByUrl("/customers/list");
        },
        error: err => console.log(err)

      })
    }
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }
}
