import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {
  MatButtonModule,
  MatTableModule,
  MatCardModule,
  MatInputModule,
  MatSliderModule,
  MatPaginatorModule,
  MatSelectModule
} from '@angular/material';

import { HttpClientModule, HttpClientJsonpModule } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CustomerService } from './customer.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { from } from 'rxjs';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    HttpClientJsonpModule,
    MatSliderModule,
    MatTableModule,
    MatPaginatorModule,
    MatSliderModule,
    MatInputModule,
    MatCardModule,
    MatSelectModule,
    MatButtonModule,
    FlexLayoutModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
