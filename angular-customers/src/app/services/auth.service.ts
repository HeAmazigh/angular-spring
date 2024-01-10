import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RegisterCustomer } from '../shared/interfaces/register-customer.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated: boolean = false;
  username!: any;
  jwtToken!: string;

  constructor(private http: HttpClient, private router: Router) { }

  public login(email: string, password: string) {
    let options = {
      headers: new HttpHeaders().set('Content-Type', "application/json")
    }
    return this.http.post("http://localhost:8080/api/auth/authenticate", {email, password}, options);
  }

  public register(customer: RegisterCustomer) {
    let options = {
      headers: new HttpHeaders().set('Content-Type', "application/json")
    }
    return this.http.post("http://localhost:8080/api/auth/register", customer, options);
  }

  public validateToken() {
    const token = window.localStorage.getItem('token');
    if (token) {
      let options = {
        headers: new HttpHeaders().set('Content-Type', "application/json")
      }
      let params = new HttpParams().set('jwt', token);
      return this.http.get("http://localhost:8080/api/auth/validate", { params, ...options });
    }
    return new Observable();
  }

  public loadProfil(userToken: any) {
    this.isAuthenticated = true;
    this.jwtToken = userToken.data.token;

    let decoderJwt = jwtDecode(this.jwtToken)
    this.username = decoderJwt?.sub;

    window.localStorage.setItem('token', userToken.data.token)
  }

  public logout() {
    this.isAuthenticated = false;
    this.jwtToken = '';
    this.username = null;
    window.localStorage.removeItem('token');
    this.router.navigateByUrl('/login');
  }

  loadToken() {
    const token = window.localStorage.getItem('token');
    if (token == null) {
      this.router.navigateByUrl('/login');
    } else {
      const userToken = {
        data: {
          token: token
        }
      }
      this.loadProfil(userToken);
    }
  }
}

