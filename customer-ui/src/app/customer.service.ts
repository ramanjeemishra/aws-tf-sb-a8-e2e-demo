import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Subject, Observable, EMPTY } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from '../environments/environment';

interface Customer {
  'first-name': string;
  'middle-name': string;
  'last-name': string;
  'date-of-birth': string;
  'mobile-number': string;
  gender: string;
  'customer-number': string;
  'country-of-birth': string;
  'country-of-residence': string;
  'customer-segment': string;
  addresses: Address[];
}

interface Address {
  id: number;
  sequence: number;
  type: string;
  'addr-line1': string;
  'addr-line2': string;
  'addr-line3': string;
  'addr-line4': string;
  'country-code': string;
  zipcode: string;
  state: string;
  city: string;
}

export interface CustomerView {
  firstName: string;
  middleName: string;
  lastName: string;
  dateOfBirth: string;
  mobileNumber: string;
  gender: string;
  customerNumber: string;
  countryOfBirth: string;
  countryOfResidence: string;
  customerSegment: string;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService implements OnInit {
  private error: string;
  private customersView$: Observable<CustomerView[]>;

  private mapToView(cust: Customer): CustomerView {
    return {
      firstName: cust['first-name'],
      middleName: cust['middle-name'],
      lastName: cust['last-name'],
      dateOfBirth: cust['date-of-birth'],
      mobileNumber: cust['mobile-number'],
      gender: cust.gender,
      customerNumber: cust['customer-number'],
      countryOfBirth: cust['country-of-birth'],
      countryOfResidence: cust['country-of-residence'],
      customerSegment: cust['customer-segment']
    };
  }


  // tslint:disable-next-line: contextual-lifecycle
  ngOnInit(): void {
    console.log('Init method called...........');
  }

  constructor(private http: HttpClient) {
    console.log('initializing customer service........', environment.apiURL);
    this.customersView$ = this.http.get<Customer[]>(environment.apiURL)
      .pipe(
        map(customers => customers.map(c => this.mapToView(c))),
        catchError(
          err => {
            console.error('[CustomerService] fetching data from http client...', err);
            this.error = `can't get customers... got ${err.status} from ${err.url}`;
            return EMPTY;
          }
        ));
  }

  getCustomers(): Observable<CustomerView[]> {
    console.log(`get customer views ${this.customersView$}......`);
    return this.customersView$;
  }
}
