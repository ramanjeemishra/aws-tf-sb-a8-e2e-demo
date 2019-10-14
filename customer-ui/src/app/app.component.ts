import { Component, ViewChild, OnInit, OnDestroy } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { CustomerService, CustomerView } from './customer.service';
import { Observable } from 'rxjs';
import { MatTableDataSource, MatPaginator } from '@angular/material';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  email = new FormControl('', [Validators.required, Validators.email]);
  title = 'Customer Service-UI-Component';
  private readonly _customers$: Observable<CustomerView[]>;
  dataSource: MatTableDataSource<CustomerView> = new MatTableDataSource<CustomerView>();
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;


  constructor(private customerService: CustomerService) {
    this._customers$ = this.customerService.getCustomers();
    this._customers$.subscribe(
      data => { console.log('data from database....', data); this.dataSource.data = data;},
      err => console.error('[AppComponent] Error retrieving data from database...', err),
      () => console.log('Subscription completed.......')
    );
  }

  ngOnInit(): void {
    console.log('Init Method...');
    this.dataSource.paginator = this.paginator;
  }


  getErrorMessage() {
    return this.email.hasError('required') ? 'You must enter a value' :
      this.email.hasError('email') ? 'Not a valid email' :
        '';
  }

  ngOnDestroy(): void {
    console.log('Destroy...');
  }

  get pageSizeOptions(): number[] {
    return [5, 10, 20, 50, 100];
  }

  get displayedColumns(): string[] {
    return [
      'customerNumber',
      'firstName',
      'middleName',
      'lastName',
      'gender',
      'dateOfBirth',
      'mobileNumber',
      'countryOfBirth'
    ]
  }
}
