import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBrokerageProduct } from 'app/shared/model/brokerage-product.model';

@Component({
    selector: 'jhi-brokerage-product-detail',
    templateUrl: './brokerage-product-detail.component.html'
})
export class BrokerageProductDetailComponent implements OnInit {
    brokerageProduct: IBrokerageProduct;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerageProduct }) => {
            this.brokerageProduct = brokerageProduct;
        });
    }

    previousState() {
        window.history.back();
    }
}
