export interface RegisterCustomer {
  id?: number,
  firstName: string,
  lastName: string,
  email: string,
  createdAt?: string
  password: string
  repeatedPassword: string
}
