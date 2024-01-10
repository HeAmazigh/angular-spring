import { Customer } from './../shared/interfaces/customer.interface';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) { }

  baseURL: string = 'http://localhost:8080/api';

  public loadAllCustomers() {
    // let options = {
    //   headers: new HttpHeaders().set('Content-Type', "application/json")
    // }
    return this.http.get(`${this.baseURL}/customers`);
  }

  public addCustomer(customer: Customer) {
    // let options = {
    //   headers: new HttpHeaders().set('Content-Type', "application/json")
    // }
    return this.http.post(`${this.baseURL}/customers`, customer);
  }

  public updateCustomer(customer: Customer) {
    // let options = {
    //   headers: new HttpHeaders().set('Content-Type', "application/json")
    // }
    return this.http.put(`${this.baseURL}/customers`, customer);
  }

  public deleteCustomersById(id: number) {
    // let options = {
    //   headers: new HttpHeaders().set('Content-Type', "application/json")
    // }
    return this.http.delete(`${this.baseURL}/customers/${id}`);
  }
}
